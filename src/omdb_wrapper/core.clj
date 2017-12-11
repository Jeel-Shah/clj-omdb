(ns omdb-wrapper.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

(defonce base-map {:query-params {}})
(defonce omdb-url "http://www.omdbapi.com/")
(defonce search-options {"title" :t "id" :id "term" :s})

(defn construct-param-map [kw by-term api-key options-map]
  (if-not (empty? options-map)
    (assoc-in base-map [:query-params] (assoc options-map kw by-term :apikey api-key))
    (assoc-in base-map [:query-params] {kw by-term :apikey api-key})))

(defn search-options-id [search-by]
  (get search-options search-by))

(defn search [search-by search-term api-key options-map]
  "Returns a map containing the results. Search by can be: title, id or term."
    (json/read-str
     (:body (client/get omdb-url (construct-param-map (search-options-id search-by) search-term api-key options-map)))
     :key-fn keyword))
