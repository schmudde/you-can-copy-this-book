(ns core
  (:require [clojure.pprint :refer [pprint pp]]
            [clojure.data.json :as json]))

(def tweets (json/read-str (slurp "src/tweets.js") :key-fn keyword))

(defn -main [& args]
  (println "Begin Tweet Scan")
  (->>
   (filter #(when (= (get-in % [:tweet :entities :hashtags 0 :text]) "侘寂") %) tweets)
   (sort-by #(get-in % [:tweet :favorite_count]))))

#_(filter #(when (= (get-in % [:tweet :entities :hashtags 0 :text]) "侘寂") %) tweets)
