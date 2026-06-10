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


(defn get_food_id [food]
  (:fdcId (first (:foods (json/parse-string
    (:body (client/get (str "https://api.nal.usda.gov/fdc/v1/foods/search?query=" food "&pageSize=1&api_key=6vNUGJt6b4uTtkct1hOFZUFE0W4mjKIEOfsHuQLq")))
    true
  ))))
)


(defn get_food_details [id]
  (json/parse-string
    (:body (client/get (str "https://api.nal.usda.gov/fdc/v1/food/" id "?api_key=6vNUGJt6b4uTtkct1hOFZUFE0W4mjKIEOfsHuQLq")))
    true
  )
)


(defn calories [food g]
  (let [id      (get_food_id food)
        details (get_food_details id)
        kcal    (get-in details [:labelNutrients :calories :value])
        serving (:servingSize details)]
    (* kcal (/ g serving))
  )
)