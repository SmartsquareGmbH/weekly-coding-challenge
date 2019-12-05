(ns advent-of-code-2019.day02)
(require '[clojure.string :as str])

(def ops {1 + 2 *})

(defn step [is pc]
  (let [op (ops (nth is pc)) 
        p1 (nth is (+ pc 1)) 
        p2 (nth is (+ pc 2)) 
        p3 (nth is (+ pc 3)) 
        r (apply op (list (nth is p1) (nth is p2)))]
    (assoc is p3 r)))

(defn run [iis]
  (loop [pc 0
         is iis]
    (if
      (= 99 (nth is pc)) (first is)
      (recur (+ pc 4) (step is pc)))))

(defn find-numbers [is]
  (let [coords (for [a (range 100) b (range 100)] [a b])]
    (loop [[[a b] & cs] coords]
      (let [r (run (assoc (assoc is 2 b) 1 a))]
        (if 
          (= 19690720 r) [a b]
          (recur cs))))))

