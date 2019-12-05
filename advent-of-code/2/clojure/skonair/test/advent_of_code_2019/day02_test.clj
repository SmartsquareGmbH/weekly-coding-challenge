(ns advent-of-code-2019.day02-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day02 :refer :all]))

(deftest int-computer-test-part1
  (testing "the correct function of the int computer"
    (is (= 2 (run [1, 0, 0, 0, 99])))
    (is (= 2 (run [2,3,0,3,99])))
    (is (= 2 (run [2,4,4,5,99,0])))
    (is (= 30 (run [1,1,1,4,99,5,6,0,99])))))

;
; the results
; 
(def input
    (into [] (map #(Long/parseLong %) (str/split (slurp "resources/day02/input.txt") #","))))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 6627023 (run (assoc (assoc input 2 2) 1 12)))))
    (is (= [40 19] (find-numbers input))))

