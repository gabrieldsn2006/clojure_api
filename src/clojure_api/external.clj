(ns clojure-api.external
  (:require [clj-http.client :as client]
            [cheshire.core   :as json])
)


(defn caloriesburned [activity duration]
  (let [response (json/parse-string
      (:body (client/get
        (str "https://api.api-ninjas.com/v1/caloriesburned?activity=" activity "&duration=" (str duration))
        {:headers {"x-api-key" "lPUDugv7d9lJ3I39WuD715dgUQX0XDNfzQsyzPYT"}}))
    true)]
    (:total_calories (first response))
  )
)