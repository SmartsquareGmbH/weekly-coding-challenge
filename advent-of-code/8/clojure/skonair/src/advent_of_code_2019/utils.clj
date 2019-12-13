(ns advent-of-code-2019.utils)

(defn abs [n] 
  (max n (- n)))

(defn exp [x n]
    (reduce * (repeat n x)))

(defn manhatten-distance [c]
  (let [[x y & zs] c]
    (+ (abs x) (abs y))))

(defn positions
  [pred coll]
  (keep-indexed (fn [idx x]
                  (when (pred x)
                    idx))
                coll))

(defn permutations [s]
  (lazy-seq
     (if (seq (rest s))
        (apply concat (for [x s]
           (map #(cons x %) (permutations (remove #{x} s)))))
        [s])))

(defn signum [e]
  (cond
    (< e 0) -1
    (> e 0) 1
    (zero? e) 0))

(def v+ (partial mapv +))
(def v- (partial mapv -))

(defn gcd [a b]
  (if (zero? b)
    a
    (recur b, (mod a b))))
 
(defn lcm [a b]
  (/ (* a b) (gcd a b)))
