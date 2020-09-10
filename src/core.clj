(ns core
  (:require [clojure.pprint :refer [pprint pp]]
            [clojure.data.json :as json]
            [clojure.edn :as edn]))

(def tweets (json/read-str (slurp "src/tweets-full.js") :key-fn keyword))

(defn -main [& args]
  (println "Begin Tweet Scan")
  (->>
   (filter #(when (= (get-in % [:tweet :entities :hashtags 0 :text]) "侘寂") %) tweets)
   (sort-by #(get-in % [:tweet :favorite_count]))
   (spit "target/tweets-filtered.edn")))

(defn get-stats [stat coll]
  (read-string (get-in coll stat)))

(defn get-top-aphorisms []
  (let [tweets (edn/read-string (slurp "target/tweets-filtered.edn"))
        favorites (partial get-stats [:tweet :favorite_count])
        retweets (partial get-stats [:tweet :retweet_count])]
    (->>
     (filter #(when (> (+ (favorites %) (retweets %)) 2) %) tweets)
     (map #(get-in % [:tweet :full_text]))
     (reverse)
     (pprint))))


(comment

  (count (edn/read-string (slurp "target/tweets-filtered.edn"))) ;; 210

  )
