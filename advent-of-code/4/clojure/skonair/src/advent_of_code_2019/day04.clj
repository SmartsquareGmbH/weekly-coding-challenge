(ns advent-of-code-2019.day04)

(defn gen-passwords [l h cnd]
  (for [a (range 1 10)
        b (range a 10)
        c (range b 10)
        d (range c 10)
        e (range d 10)
        f (range e 10)
        :let [n (+ (* 100000 a) (* 10000 b) (* 1000 c) (* 100 d) (* 10 e) f)]
        :when (and (<= n h) (>= n l) (or (= a b) (= b c) (= c d) (= d e) (= e f)) (cnd n))]
    n))

(defn matching-two-group [n]
  (some #(= (count %) 2) (partition-by identity (str n))))

