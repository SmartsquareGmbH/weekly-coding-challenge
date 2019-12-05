(ns advent-of-code-2019.day05)

(defn- exp [x n]
    (reduce * (repeat n x)))

(defn- get-param-mods [opcode i]
  (mod (quot opcode (* 10 (exp 10 i))) 10))

(defn- get-param [is pc i]
  (case (get-param-mods (nth is pc) i)
    1 (nth is (+ pc i))
    0 (nth is (nth is (+ pc i)))))

(defn- handle-plus [is pc]
   (let [p1 (get-param is pc 1)
         p2 (get-param is pc 2)
         p3 (nth is (+ pc 3))]
     [(assoc is p3 (+ p1 p2)) (+ pc 4)]))

(defn- handle-mult [is pc]
   (let [p1 (get-param is pc 1)
         p2 (get-param is pc 2)
         p3 (nth is (+ pc 3))]
     [(assoc is p3 (* p1 p2)) (+ pc 4)]))

(defn- handle-input [is pc in]
   (let [p1 (nth is (+ pc 1))]
     [(assoc is p1 in) (+ pc 2)]))

(defn- handle-output [is pc]
   (let [p1 (get-param is pc 1)]
     [is (+ pc 2) p1]))

(defn- handle-jmp-if-true [is pc]
   (let [p1 (get-param is pc 1)
         p2 (get-param is pc 2)]
     [is (if (zero? p1) (+ pc 3) p2)]))

(defn- handle-jmp-if-false [is pc]
   (let [p1 (get-param is pc 1)
         p2 (get-param is pc 2)]
     [is (if (zero? p1) p2 (+ pc 3))]))

(defn- handle-less-than [is pc]
   (let [p1 (get-param is pc 1)
         p2 (get-param is pc 2)
         p3 (nth is (+ pc 3))]
     [(assoc is p3 (if (< p1 p2) 1 0)) (+ pc 4)]))

(defn- handle-equals [is pc]
   (let [p1 (get-param is pc 1)
         p2 (get-param is pc 2)
         p3 (nth is (+ pc 3))]
     [(assoc is p3 (if (= p1 p2) 1 0)) (+ pc 4)]))

(defn step [is pc in]
  (case (mod (nth is pc) 10)
    1 (handle-plus is pc)
    2 (handle-mult is pc)
    3 (handle-input is pc in)
    4 (handle-output is pc)
    5 (handle-jmp-if-true is pc)
    6 (handle-jmp-if-false is pc)
    7 (handle-less-than is pc)
    8 (handle-equals is pc)
    ))

(defn run [iis in]
  (loop [is iis
         pc 0
         akk []]
    (if
      (= 99 (nth is pc)) akk
      (let [[nis npc & out] (step is pc in)]
        (recur nis npc (if (nil? out) akk (concat akk out))))))) 

