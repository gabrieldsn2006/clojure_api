(ns clojure-api.service
  (:require [cheshire.core        :as json]
            [clojure-api.db       :as db]
            [clojure-api.external :as external])
  (:import [java.time LocalDate]
           [java.time.format DateTimeFormatter])
)


(defn get_user_data []
  (json/generate-string @db/user_data)
)


(defn post_user_data [req] (let
  [payload (json/parse-string (slurp (:body req)) true)]
  (swap! db/user_data assoc
    :user_name (:user_name payload)
    :age       (:age payload)
    :height    (:height payload)
    :weight    (:weight payload)
    :sex       (:sex payload)
  )
  (json/generate-string {:res "Dados atualizados!"})
))


(defn food [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   name    (:food_name payload)
   value   (:food_value payload)
   kcal    (external/calories name value)]
  (swap! db/transactions conj
         {:name  name
          :value value
          :date  (:date payload)
          :kcal  kcal})
  (json/generate-string {:res (str "Alimento registrado! +" kcal " kcal")})
))


(defn activity [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   name  (:activity_name payload)
   value (:activity_value payload)
   kcal  (- (external/caloriesburned name value))]
  (swap! db/transactions conj
         {:name  name
          :value value
          :date  (:date payload)
          :kcal  kcal})
  (json/generate-string {:res (str "Atividade resgistrada! " kcal " kcal")})
))


(defn transactions []
  (json/generate-string @db/transactions)
)


(defn transactions_between [start_date end_date]
  (vec (filter
    #(let
      [formatter (DateTimeFormatter/ofPattern "dd/MM/yyyy")
       date      (LocalDate/parse (:date %) formatter)]
      (and
        (not (.isBefore date start_date))
        (not (.isAfter  date end_date))
      )
    )
    @db/transactions
  ))
)


(defn transactions_by_date [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   formatter  (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   start_date (LocalDate/parse (:start_date payload) formatter)
   end_date   (LocalDate/parse (:end_date payload) formatter)]
  (json/generate-string (transactions_between start_date end_date))
))


(defn balance []
  (json/generate-string (str (reduce + (map :kcal @db/transactions)) " kcal"))
)


(defn balance_between [start_date end_date]
  (reduce + (map :kcal (transactions_between start_date end_date)))
)


(defn balance_by_date [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   formatter  (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   start_date (LocalDate/parse (:start_date payload) formatter)
   end_date   (LocalDate/parse (:end_date payload) formatter)]
  (json/generate-string (str (balance_between start_date end_date) " kcal"))
))
