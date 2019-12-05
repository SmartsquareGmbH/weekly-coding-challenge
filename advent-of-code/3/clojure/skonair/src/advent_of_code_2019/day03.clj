(ns advent-of-code-2019.day03)
(require '[clojure.string :as str]
         '[clojure.set :as set]
         '[advent-of-code-2019.utils :as utils])

(defn- new-coord [c d]
  (let [[x y] c] 
    (case d 
      "R" [(inc x) y]
      "L" [(dec x) y]
      "U" [x (inc y)]
      "D" [x (dec y)])))

(defn- path-from [sc v]
  (let [[full d ns] (re-find #"([RLUD])(\d+)" v)
        n (Integer. ns)]
    (loop [c sc
           i n
           akk []]
      (if 
        (zero? i) akk
        (let [nc (new-coord c d)]
          (recur nc (dec i) (conj akk nc)))))))

(defn- follow-path [path]
  (loop [c [0 0]
         [p & ps] path
         akk []]
    (if
      (nil? p) akk
      (let [pf (path-from c p)
            nc (last pf)]
        (recur nc ps (concat akk pf))))))

(defn- shortest-path [fp coord]
  (inc (.indexOf fp coord)))

(defn- shortest [is fp1 fp2]
  (apply min (map #(+ (shortest-path fp1 %) (shortest-path fp2 %)) is)))

(defn- nearest [is fp1 fp2]
  (first (sort (map utils/manhatten-distance is))))

(defn- intersection [v1 v2]
  (let [fp1 (follow-path v1)
        fp2 (follow-path v2)
        p1 (into #{} fp1)
        p2 (into #{} fp2)
        is (set/intersection p1 p2)]
    [is fp1 fp2]))

(defn shortest-intersection [v1 v2]
  (apply shortest (intersection v1 v2)))

(defn nearest-intersection [v1 v2]
  (apply nearest (intersection v1 v2)))

