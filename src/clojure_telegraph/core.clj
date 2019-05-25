(ns clojure-telegraph.core
  (:require [dynne.sampled-sound :refer :all]
            [clojure.data.json :as json])
  (:gen-class))

(defn dit
  "Short beep (.)"
  []
  (sinusoid 0.1 440))

(defn dat
  "Long beep (-)"
  []
  (sinusoid 0.3 440))

(defn short-pause
  "Short pause, which is used between the dits and dats, not between letters."
  []
  (sinusoid 0.1 0))

(defn medium-pause
  "Medium pause, which is used between letters."
  []
  (sinusoid 0.2 0))

(defn long-pause
  "Long pause, which is used between words."
  []
  (sinusoid 0.3 0))

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

(defn decode-dit-dat
  "Checks if the character is a dash or dot and returns the correct tone for it"
  [char]
  (printf (str char))
  (case (str char)
    "-" (dat)
    "." (dit)
    )
  )

  

(defn morse-char-to-sound
  "Takes a morse code sequence and creates a sound sequence"
  [morse-char]
  (map decode-dit-dat morse-char)
)
  
(defn -main
  "A small programm that allows to play around with morse code"
  [& args]
  (defn startTone[] (sinusoid 0 0))
  (println "Hello, World!"))

(comment 

  (translate-char-to-morse \a)

  ;; The whole idea is to transform a string into morse-code characters (-.. etc.)
  (def alex-morse (vec (map translate-char-to-morse "alex")))

  ;; Once the chars are converted, I use the morse-char-to-sound function to make tones with different lengths according to the - and .
  (def alex-morse-tones (vec (map morse-char-to-sound alex-morse)))

  ;; When I try to connect the tones into a whole sequence, that I would like to use in (play),
  ;; I get an error about lazy sequences
  ;; In the examples below, you can see all the things I tried with (and without) vec, doall, take etc to realize the sequence
  (def melody (reduce append (vec alex-morse-tones)))

  (def melod2y (doall (reduce append alex-morse-tones)))

  ;; Even this is not working with the same error
  (append (first alex-morse-tones) (second alex-morse-tones))
    
  (def one-note (take 1 alex-morse-tones))
  
  ;; Here you can see, that the tones are appended and even played, if I run them explicitelly by making a sequence by hand
  (play (reduce append [(dit) (short-pause) (dat)]))

  ;; Doing the same with map, reduce and append is not working
  (play (take 3 (reduce append (vec (map morse-char-to-sound alex-morse)))))

  (play (vec (take 3 (reduce append (vec (map morse-char-to-sound alex-morse))))))
 
)
