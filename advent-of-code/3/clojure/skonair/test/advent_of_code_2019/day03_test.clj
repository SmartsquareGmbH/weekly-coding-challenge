(ns advent-of-code-2019.day03-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day03 :refer :all]))

(deftest man-distance-test
  (testing "the correct function of the closest intersetion"
    (is (= 159 (nearest-intersection ["R75","D30","R83","U83","L12","D49","R71","U7","L72"] ["U62","R66","U55","R34","D71","R55","D58","R83"])))
    (is (= 135 (nearest-intersection ["R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51"] ["U98","R91","D20","R16","D67","R40","U7","R15","U6","R7"])))))

(deftest shortest-distance-test
  (testing "the correct function of the shortest intersetion"
    (is (= 610 (shortest-intersection ["R75","D30","R83","U83","L12","D49","R71","U7","L72"] ["U62","R66","U55","R34","D71","R55","D58","R83"])))
    (is (= 410 (shortest-intersection ["R98","U47","R26","D63","R33","U87","L62","D20","R33","U53","R51"] ["U98","R91","D20","R16","D67","R40","U7","R15","U6","R7"])))))

;
; the results
; 
(def lines (str/split (slurp "resources/day03/input.txt") #"\n"))
(def input (map #(str/split % #",") lines))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 1225 (nearest-intersection (first input) (second input))))
    (is (= 107036 (shortest-intersection (first input) (second input))))))

