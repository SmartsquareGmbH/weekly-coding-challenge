(ns advent-of-code-2019.day08-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day08 :refer :all]))

;
; the results
; 
(def input (partition 150 (slurp "resources/day08/input.txt")))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 2356 (image-checksum input)))
    (is (= '(nil nil nil nil nil nil) (show-image input 25))))) ; output is PZEKB

