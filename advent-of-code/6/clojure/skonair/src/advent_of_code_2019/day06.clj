(ns advent-of-code-2019.day06)

(defn all-orbits-for [os s]
  (loop [cs s
      akk []]
    (if (= "COM" cs)
      akk
      (recur (os cs) (conj akk (os cs))))))

(defn all-orbits [os]
  (let [ks (into #{} (keys os))]
    (apply concat (map #(all-orbits-for os %) ks))))

(defn orbit-map [l]
  (loop [[a & as] l
         akk {}]
    (if (nil? a)
      akk
      (recur as (assoc akk (second a) (first a))))))

(defn- intersect-list [c1 c2] 
  (filter #(some #{%} c2) c1))

(defn minimum-orbit-hops [os start end]
  (let [aoy (all-orbits-for os start)
        aos (all-orbits-for os end)
        max-intersect (first (intersect-list aoy aos))
        hops-y (.indexOf aoy max-intersect)
        hops-s (.indexOf aos max-intersect)]
    (+ hops-y hops-s)))

