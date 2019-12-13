(ns advent-of-code-2019.day06-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [advent-of-code-2019.day06 :refer :all]))

(def test-orbits (orbit-map '(["COM" "B"] ["B" "C"] ["C" "D"] ["D" "E"] ["E" "F"] ["B" "G"] ["G" "H"] ["D" "I"] ["E" "J"] ["J" "K"] ["K" "L"])))

(deftest number-of-orbits
  (testing "the correct number of direct and indirect orbits"
    (is (= 3 (count (all-orbits-for test-orbits "D"))))
    (is (= 7 (count (all-orbits-for test-orbits "L"))))
    (is (= 0 (count (all-orbits-for test-orbits "COM")))))
  (testing "the correct number of all orbits"
    (is (= 42 (count (all-orbits test-orbits))))))
  
(def test-orbits-2 (orbit-map '(["COM" "B"] ["B" "C"] ["C" "D"] ["D" "E"] ["E" "F"] ["B" "G"] ["G" "H"] ["D" "I"] ["E" "J"] ["J" "K"] ["K" "L"] ["K" "YOU"] ["I" "SAN"])))

(deftest orbit-hops-test
  (testing "the amount of orbits hops between YOU and SAN"
    (is (= 4 (minimum-orbit-hops test-orbits-2 "YOU" "SAN")))))


;
; the results
; 
(def input
  (orbit-map
    (map #(str/split % #"\)") (str/split (slurp "resources/day06/input.txt") #"\n"))))

(deftest result
  (testing "the correct answer for the given input for documentation purposes"
    (is (= 160040 (count (all-orbits input))))
    (is (= 373 (minimum-orbit-hops input "YOU" "SAN")))))

