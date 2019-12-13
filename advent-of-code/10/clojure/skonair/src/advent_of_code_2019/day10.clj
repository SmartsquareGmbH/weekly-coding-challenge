(ns advent-of-code-2019.day10)

(defn- steigung [a1 a2]
  (let [[x1 y1] a1
        [x2 y2] a2]
    (/ (- y2 y1) (- x2 x1))))
  
(defn- points-between [a1 a2]
  (let [[[x1 y1] [x2 y2]] (sort-by first [a1 a2])]
    (cond
      (= x1 x2) (for [n (range (inc (min y1 y2)) (max y1 y2))] [x1 n])
      (= y1 y2) (for [n (range (inc (min x1 x2)) (max x1 x2))] [n y1])
      :otherwise (let [a (steigung a1 a2)]
        (for [nx (range (inc x1) x2)
              :let [ny (+ y1 (* a (- nx x1)))]
              :when (and (integer? nx) (integer? ny))] 
          [nx ny])))))

(defn- asteroid? [p as]
  (not (nil? (some #{p} as))))

(defn- visible? [a1 a2 as]
  (if (= a1 a2) 
    false
    (zero? (count (filter #(asteroid? % as) (points-between a1 a2))))))
     
(defn- visible-asteroids [a as]
 (count (filter #(visible? a % as) as)))

(defn max-asteroids [as]
  (last (sort-by second (map #(vector % (visible-asteroids % as)) as))))

(defn- angle [a b]
  (let [[x1 y1] a
        [x2 y2] b
        dx (- x2 x1)
        dy (- y2 y1)]
    (Math/atan2 dx dy)))


(defn nth-vaporized [n a as]
  (let [[angle [x y]] 
        (first
          (nth 
            (reverse 
              (partition-by first 
                      (sort-by first 
                               (map #(vector (angle a %) %) as)))) (dec n)))]
    (+ (* 100 x) y)))
  
