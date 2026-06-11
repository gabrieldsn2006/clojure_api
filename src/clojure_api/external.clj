(ns clojure-api.external
  (:require [clj-http.client :as client]
            [cheshire.core   :as json]
            [clojure-api.db  :as db])
)


(defn kilograms-to-pounds [kilograms]
  (* kilograms 2.20462262185)
)


(defn calories-burned [activity duration]
  (let [response (json/parse-string
      (:body (client/get
        (str "https://api.api-ninjas.com/v1/caloriesburned"
             "?activity=" activity
             "&weight="   (kilograms-to-pounds (:weight @db/user-data))
             "&duration=" (str duration))
        {:headers {"x-api-key" "lPUDugv7d9lJ3I39WuD715dgUQX0XDNfzQsyzPYT"}}))
    true)]
    (:total_calories (first response))
  )
)


(defn get-food-id [food]
  (:fdcId (first (:foods (json/parse-string
    (:body (client/get (str "https://api.nal.usda.gov/fdc/v1/foods/search?query=" food "&pageSize=1&api_key=6vNUGJt6b4uTtkct1hOFZUFE0W4mjKIEOfsHuQLq")))
    true
  ))))
)


(defn get-food-details [id]
  (json/parse-string
    (:body (client/get (str "https://api.nal.usda.gov/fdc/v1/food/" id "?api_key=6vNUGJt6b4uTtkct1hOFZUFE0W4mjKIEOfsHuQLq")))
    true
  )
)


(defn convert-kcal [kcal g serving]
  (* kcal (/ g serving))
)


(defn calories [food g]
  (let [id      (get-food-id food)
        details (get-food-details id)
        kcal    (get-in details [:labelNutrients :calories :value])
        serving (:servingSize details)]
    (convert-kcal kcal g serving)
  )
)