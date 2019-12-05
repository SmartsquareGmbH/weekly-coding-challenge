(ns advent-of-code-2019.utils)

(defn- abs [n] 
  (max n (- n)))

(defn manhatten-distance [c]
  (let [[x y & zs] c]
    (+ (abs x) (abs y))))

(defn positions
  [pred coll]
  (keep-indexed (fn [idx x]
                  (when (pred x)
                    idx))
                coll))

