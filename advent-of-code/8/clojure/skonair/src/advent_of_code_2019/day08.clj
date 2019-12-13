(ns advent-of-code-2019.day08
  (:require [clojure.string :as str]))

(defn- count-chars [cs c]
  (count (filter #(= c %) cs)))

(defn- fewest-zeros [cs]
 (first (sort-by #(count-chars % \0) cs)))

(defn image-checksum [in]
  (let [layer (fewest-zeros in)]
    (* (count-chars layer \1) (count-chars layer \2))))

(defn- get-pixel [cs]
  (loop [[a & as] cs]
    (case a
      nil (throw (RuntimeException. "No colored color found."))
      \0 " "
      \1 "*"
      \2 (recur as))))

(defn show-image [in width]
  (let [image-layers (map get-pixel (apply map vector in))]
    (for [pil (partition width image-layers)] (println pil))))

