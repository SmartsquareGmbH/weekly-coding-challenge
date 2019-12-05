(ns advent-of-code-2019.day05-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day05 :refer :all]))

;
; the results
; 
(def input
    (into [] (map #(Long/parseLong %) (str/split (slurp "resources/day05/input.txt") #","))))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 14522484 (last (run input 1))))
    (is (= 4655956 (last (run input 5))))))

