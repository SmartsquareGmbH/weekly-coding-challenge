(ns advent-of-code-2019.day10-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day10 :refer :all]))

(defn- parse-line [l y]
  (map #(vector (first %) y) (filter #(= \# (second %)) (map-indexed (fn [i e] [i e]) l))))

(defn- parse-lines [lines]
  (apply concat
    (map-indexed (fn [i e] (parse-line e i)) lines)))

(deftest maximum-asteroids-test
  (testing "the max-asteroids function"
    (is (= [[3 4] 8] (max-asteroids (parse-lines (str/split (slurp "resources/day10/example0.txt") #"\n")))))
    (is (= [[5 8] 33] (max-asteroids (parse-lines (str/split (slurp "resources/day10/example1.txt") #"\n")))))
    (is (= [[1 2] 35] (max-asteroids (parse-lines (str/split (slurp "resources/day10/example2.txt") #"\n")))))
    (is (= [[6 3] 41] (max-asteroids (parse-lines (str/split (slurp "resources/day10/example3.txt") #"\n")))))
    (is (= [[11 13] 210] (max-asteroids (parse-lines (str/split (slurp "resources/day10/example4.txt") #"\n"))))))
  (testing "the correct vaporized asteroid"
    (is (= 802 (nth-vaporized 200 [11 13] (parse-lines (str/split (slurp "resources/day10/example4.txt") #"\n")))))))

;
; the results
; 
(def input
   (str/split (slurp "resources/day10/input.txt") #"\n"))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= [[22 19] 282] (max-asteroids (parse-lines input))))
    (is (= 1008 (nth-vaporized 200 [22 19] (parse-lines input))))))

