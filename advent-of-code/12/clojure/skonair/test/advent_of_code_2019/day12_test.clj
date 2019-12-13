(ns advent-of-code-2019.day12-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day12 :refer :all]))

(def test-input1
    [{:pos [-1 0 2] :vel [0 0 0]} {:pos [2 -10 -7] :vel [0 0 0]} {:pos [4 -8 8] :vel [0 0 0]} {:pos [3 5 -1] :vel [0 0 0]}])
(def test-input2
   [{:pos [-8 -10 0] :vel [0 0 0]} {:pos [5 5 10] :vel [0 0 0]} {:pos [2 -7 3] :vel [0 0 0]} {:pos [9 -8 -3] :vel [0 0 0]}])

(deftest total-energy-test
  (testing "the correct total energy"
    (is (= 179 (total-energy test-input1 10)))
    (is (= 1940 (total-energy test-input2 100) ))))

(deftest repetitions-test
  (testing "the correct steps after a first repetition occured"
    (is (= 2772 (first-repetition test-input1)))
    (is (= 4686774924 (first-repetition test-input2)))))

;
; the results
; 
(def input
  [{:pos [17 5 1] :vel [0 0 0]} {:pos [-2 -8 8] :vel [0 0 0]} {:pos [7 -6 14] :vel [0 0 0]} {:pos [1 -10 4] :vel [0 0 0]}])

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 9876 (total-energy input 1000)))
    (is (= 307043147758488 (first-repetition input)))))

