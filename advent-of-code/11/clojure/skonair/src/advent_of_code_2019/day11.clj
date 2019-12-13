(ns advent-of-code-2019.day11
  (:require [advent-of-code-2019.intcode :as intcode]))

(defn- compute [initial-state color]
  (loop [state (assoc initial-state :params [color])]
    (if (state :halt?)
      state
      (let [next-state (intcode/step state)]
        (if (= 2 (count (next-state :params)))
          next-state
          (recur next-state))))))

(defn- rotate [rotation n]
  (case rotation
    :UP (if (zero? n) :LEFT :RIGHT)
    :RIGHT (if (zero? n) :UP :DOWN)
    :DOWN (if (zero? n) :RIGHT :LEFT)
    :LEFT (if (zero? n) :DOWN :UP)))
    
(defn- new-pos [[x y] rotation]
  (case rotation
    :UP [x (inc y)]
    :RIGHT [(inc x) y]
    :DOWN [x (dec y)]
    :LEFT [(dec x) y]))

(defn paint-robot [iis initial-world]
  (loop [state (intcode/create-state (into [] (concat iis (repeat 500 0))) [])
        world initial-world
        position [0 0]
        rotation :UP]
  (let [new-state (compute state (get world position 0))]
    (if (new-state :halt?)
      world 
      (let [[direction color] (new-state :params)
            new-rotation (rotate rotation direction)]
        (recur new-state (assoc world position color) (new-pos position new-rotation) new-rotation))))))

(defn part1 [iis]
  (count (paint-robot iis {})))
  
(defn- paint-line [world y minx maxx]
   (apply str
     (for [x (range minx (inc maxx))]
       (let [color (get world [x y] 0)]
         (if (zero? color) " " "#")))))

(defn- paint-world [world]
  (let [mfw (map first (keys world))
        msw (map second (keys world))]
    (for [y (range (apply max msw) (dec (apply min msw)) -1)]
      (println 
        (paint-line world y (apply min mfw) (apply max mfw))))))

(defn part2 [iis]
  (paint-world (paint-robot iis {[0 0] 1})))
  
