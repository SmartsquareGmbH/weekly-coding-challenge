(ns advent-of-code-2019.day07
  (:require 
    [advent-of-code-2019.intcode :as intcode]
    [advent-of-code-2019.utils :as utils]))

(defn- run [iis in]
  (loop [state (intcode/create-state iis in)]
    (if (state :halt?) 
      state
      (recur (intcode/step state)))))

(defn- amp [iis phase-inputs]
  (loop [[phase-input & is] phase-inputs
         in2 0]
    (if (nil? phase-input)
      in2
      (let [next-run (run iis [phase-input in2])]
        (recur is (last (next-run :params)))))))

(defn highest-amp [iis]
    (apply max
      (map #(amp iis %) (utils/permutations (range 5)))))

(defn- run-loop [start-state in]
  (loop [state (assoc start-state :params (conj (start-state :params) in))
         signal? false]
    (if (or signal? (state :halt?))
      state
      (let [next-state (intcode/step state)]
        (recur next-state (> (count (next-state :params)) (count (state :params))))))))

(defn- amp-loop [iis phase-inputs]
  (loop [amps (into [] (map #(intcode/create-state iis [%]) phase-inputs))
         a 0
         in2 0]
    (let [next-amp (run-loop (nth amps a) in2)]
      (if (next-amp :halt?)
        (first (next-amp :params))
        (recur (assoc amps a next-amp) (mod (inc a) 5) (first (next-amp :params)))))))

(defn highest-amp-loop [iis]
    (apply max
      (map #(amp-loop iis %) (utils/permutations (range 5 10)))))

