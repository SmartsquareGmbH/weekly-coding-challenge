(ns advent-of-code-2019.day13
  (:require [advent-of-code-2019.utils :as utils])
  (:require [advent-of-code-2019.intcode :as intcode]))

(defn- compute [initial-state]
  (loop [state (assoc initial-state :params [])]
      (let [next-state (intcode/step state)]
        (if (= 3 (count (next-state :params)))
          next-state
          (recur next-state)))))

; 239
(defn part1 [iis]
  (loop [state (intcode/create-state (into [] (concat iis (repeat 500 0))) [])
         block-tiles #{}]
    (if (state :halt?)
      (count (filter #(= 2 (nth % 2)) (partition 3 (:params state))))
      (let [new-state (intcode/step state)
            [x y t] (:params new-state)
            new-block-tiles (if (= 2 t) (conj block-tiles [x y]) block-tiles)]
        (recur new-state new-block-tiles)))))
  
(defn part2 [iis]
  (loop [state (intcode/create-state (into [] (concat (assoc iis 0 2) (repeat 500 0))) [])
         block-tiles #{}]
    (if (state :halt?)
      state
      (let [new-state (intcode/step state)
            [x y t] (:params new-state)
            new-block-tiles (if (= 2 t) (conj block-tiles [x y]) block-tiles)]
          (if (and (= -1 x) (zero? y)) (println "Score: " t))
        (recur new-state new-block-tiles)))))
  

(def input (into [] (map #(Long/parseLong %) (str/split (slurp "resources/day13/input.txt") #","))))

(def istate (intcode/create-state (into [] (concat input (repeat 500 0))) []))
