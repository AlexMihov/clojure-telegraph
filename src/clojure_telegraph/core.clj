(ns clojure-telegraph.core
  (:require [dynne.sampled-sound :refer :all]
            [clojure.data.json :as json])
  (:gen-class))

(defn dit
  "Adds a short beep (.) to the sequence"
  [previousSequence]
  (append previousSequence (sinusoid 0.1 440)) )

(defn dat
  "Adds a long beep (-) to the sequence"
  [previousSequence]
  (append previousSequence (sinusoid 0.3 440)) )

(defn short-pause
  "Adds a short pause to the sequence, which is used between the dits and dats, not between letters."
  [previousSequence]
  (append previousSequence (sinusoid 0.1 0)) )

(defn medium-pause
  "Adds a medium pause to the sequence, which is used between letters."
  [previousSequence]
  (append previousSequence (sinusoid 0.2 0)) )

(defn long-pause
  "Adds a long pause to the sequence, which is used between words."
  [previousSequence]
  (append previousSequence (sinusoid 0.3 0)) )

(defn string-keys-to-symbols [map]
    (reduce #(assoc %1 (-> (key %2) keyword) (val %2)) {} map))

(defn translate-char-to-morse
  "Takes a character and returns the morse code representation in dots (.) and dashes (-)"
  [char]
  (def morse (-> "morse-code-dictionary.json"
                 (slurp)
                 (json/read-str)
                 (string-keys-to-symbols)
  ))
  (morse (keyword (str char)))
  )
  
(defn -main
  "A small programm that allows to play around with morse code"
  [& args]
  (defn startTone[] (sinusoid 0 0))
  (println "Hello, World!"))

(comment 
  (play (-> (startTone)
            (dit)
            (short-pause)
            (dit)
            (short-pause)
            (dit)
            (medium-pause)
            (dat)
            (short-pause)
            (dat)
            (short-pause)
            (dat)
            (medium-pause)
            (dit)
            (short-pause)
            (dit)
            (short-pause)
            (dit)
            ))

  (translate-char-to-morse \a)

  
  (map translate-char-to-morse "alex mihov")

)
