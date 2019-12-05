(ns advent-of-code-2019.day04-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day04 :refer :all]))

;
; the results
; 

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 1653 (count (gen-passwords 206938 679128 (fn [n] true)))))
    (is (= 1133 (count (gen-passwords 206938 679128 matching-two-group))))))

