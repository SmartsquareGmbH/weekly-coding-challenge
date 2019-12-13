(ns advent-of-code-2019.intcode
  (:require [advent-of-code-2019.utils :as utils]))

(defn- get-param-mods [opcode i]
  (mod (quot opcode (* 10 (utils/exp 10 i))) 10))

(defn- get-param-immediate [state i]
  (+ (state :pc) i))

(defn- get-param-position [state i]
  (nth (state :is) (get-param-immediate state i)))

(defn- get-param-relative [state i]
  (+ (state :rbase) (get-param-position state i)))

(defn- get-param [state i]
  (case (get-param-mods (get-param-position state 0) i)
    0 (get-param-position state i)
    1 (get-param-immediate state i)
    2 (get-param-relative state i)))

(defn- eval-param [state i]
  (nth (state :is) (get-param state i)))

(defn- handle-plus [state]
   (let [p1 (eval-param state 1)
         p2 (eval-param state 2)
         p3 (get-param state 3)]
     (assoc state :is (assoc (state :is) p3 (+ p1 p2)) :pc (+ (state :pc) 4))))

(defn- handle-mult [state]
   (let [p1 (eval-param state 1)
         p2 (eval-param state 2)
         p3 (get-param state 3)]
     (assoc state :is (assoc (state :is) p3 (* p1 p2)) :pc (+ (state :pc) 4))))

(defn- handle-input [state]
   (let [p1 (get-param state 1)
         [i & is] (state :params)]
     (assoc state :is (assoc (state :is) p1 i) :pc (+ (state :pc) 2) :params is)))

(defn- handle-output [state]
   (let [p1 (eval-param state 1)]
     (assoc state :pc (+ (state :pc) 2) :params (conj (state :params) p1))))

(defn- handle-jmp-if-true [state]
   (let [p1 (eval-param state 1)
         p2 (eval-param state 2)
         npc (if (zero? p1) (+ (state :pc) 3) p2)]
     (assoc state :pc (if (zero? p1) (+ (state :pc) 3) p2))))

(defn- handle-jmp-if-false [state]
   (let [p1 (eval-param state 1)
         p2 (eval-param state 2)]
     (assoc state :pc (if (zero? p1) p2 (+ (state :pc) 3)))))

(defn- handle-less-than [state]
   (let [p1 (eval-param state 1)
         p2 (eval-param state 2)
         p3 (get-param state 3)]
     (assoc state :is (assoc (state :is) p3 (if (< p1 p2) 1 0)) :pc (+ (state :pc) 4))))

(defn- handle-equals [state]
   (let [p1 (eval-param state 1)
         p2 (eval-param state 2)
         p3 (get-param state 3)]
     (assoc state :is (assoc (state :is) p3 (if (= p1 p2) 1 0)) :pc (+ (state :pc) 4))))

(defn- handle-adjust-rbase [state]
  (let [p1 (eval-param state 1)]
    (assoc state :rbase (+ p1 (state :rbase)) :pc (+ (state :pc) 2))))

(defn- handle-halt [state]
  (assoc state :halt? true))

(defn step [state]
  (case (mod (nth (state :is) (state :pc)) 100)
    1 (handle-plus state)
    2 (handle-mult state)
    3 (handle-input state)
    4 (handle-output state)
    5 (handle-jmp-if-true state)
    6 (handle-jmp-if-false state)
    7 (handle-less-than state)
    8 (handle-equals state)
    9 (handle-adjust-rbase state)
    99 (handle-halt state)))

(defn create-state [iis params]
  {:is (into [] (concat iis (repeat 500 0))) :pc 0 :params params :halt? false :rbase 0})
 
(defn run [iis in]
  (loop [state (create-state iis in)]
    (if (state :halt?)
      state
      (recur (step state)))))
