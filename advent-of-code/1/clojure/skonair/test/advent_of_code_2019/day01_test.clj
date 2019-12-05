(ns advent-of-code-2019.day01-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day01 :refer :all]))

(deftest fuel-by-mass-test-part1
  (testing "the corrext amount of fuel for a given mass"
    (is (= 2 (required-fuel 12)))
    (is (= 2 (required-fuel 14)))
    (is (= 654 (required-fuel 1969)))
    (is (= 33583 (required-fuel 100756) )))
  (testing "the correct amount of fuel for several masses"
    (is (= 34241 (fuel [12 14 1969 100756])))))

(deftest fuel-by-mass-test-part2
  (testing "the correct amount of fuel-fuel-fuel... for a given mass"
    (is (= 2 (required-fuel-fuel 14)))
    (is (= 966 (required-fuel-fuel 1969)))
    (is (= 50346 (required-fuel-fuel 100756) )))
  (testing "the correct amount of fuel for several masses"
    (is (= 51314 (fuel-fuel [14 1969 100756])))))

;
; the results
; 
(def input
    (map #(Long/parseLong %) (str/split (slurp "resources/day01/input.txt") #"\n")))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 3150224 (fuel input)))
    (is (= 4722484 (fuel-fuel input)))))

