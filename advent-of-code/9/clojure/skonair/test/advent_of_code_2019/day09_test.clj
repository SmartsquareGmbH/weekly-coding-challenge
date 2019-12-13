(ns advent-of-code-2019.day09-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day09 :refer :all]))

;
; the results
; 
(def input (into [] (map #(bigint (Long/parseLong %)) (str/split (slurp "resources/day09/input.txt") #","))))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 2465411646N (run input [1])))
    (is (= 69781N (run input [2])))))

