(ns advent-of-code-2019.day01)

(defn required-fuel [m]
  (- (quot m 3) 2))

(defn fuel [ms]
  (apply + (map required-fuel ms)))

(defn required-fuel-fuel [m]
  (loop [r m
         f 0]
    (if (zero? r)
      f
      (let [rf (max 0 (required-fuel r))]
        (recur rf (+ f rf))))))

(defn fuel-fuel [ms]
  (apply + (map required-fuel-fuel ms)))

