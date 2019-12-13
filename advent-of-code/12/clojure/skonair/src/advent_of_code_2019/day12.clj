(ns advent-of-code-2019.day12
  (:require [advent-of-code-2019.utils :as utils]))

(defn- vector-diff [v1 v2]
  (map utils/signum (utils/v- v1 v2)))

(defn- new-velocity [mp mps]
    (utils/v+ (:vel mp)
      (apply utils/v+  (map #(vector-diff % (:pos mp)) (map :pos mps)))))

(defn- position [state]
  (for [s state] 
    (let [v (new-velocity s state)
          p (utils/v+ (:pos s) v)]
      {:pos p :vel v})))

(defn- potential-energy [m]
  (apply + (map utils/abs (:pos m))))

(defn- kinetic-energy [m]
  (apply + (map utils/abs (:vel m))))

(defn- energy [m]
  (* (potential-energy m) (kinetic-energy m)))
    
(defn total-energy [initial-state n]
  (loop [state initial-state
         all-states #{}
         cnt 0]
    (if (>= cnt n) 
      (reduce + (map energy state))
      (recur (position state) (conj all-states state) (inc cnt)))))

(defn- repetition [reduced-state]
  (loop [state reduced-state
         all-states #{}]
    (if (contains? all-states state)
      (count all-states)
      (recur (position state) (conj all-states state)))))

 (defn- run-reduced [initial-state p]
   (repetition 
     (map #(hash-map :pos [(nth (:pos %) p)] :vel [(nth (:vel %) p)] ) initial-state)))

(defn first-repetition [initial-state]
  (reduce utils/lcm
    (for [p (range 0 3)] (run-reduced initial-state p))))
    
