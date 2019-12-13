(ns advent-of-code-2019.day09
  (:require 
    [advent-of-code-2019.intcode :as intcode]
    [advent-of-code-2019.utils :as utils]))

(defn run [iis in]
  (first ((intcode/run iis in) :params)))

