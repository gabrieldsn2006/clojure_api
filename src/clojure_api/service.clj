(ns clojure-api.service
  (:require [cheshire.core        :as json]
            [clojure-api.db       :as db]
            [clojure-api.external :as external])
  (:import [java.time LocalDate]
           [java.time.format DateTimeFormatter])
)


(defn get-user-data []
  (json/generate-string @db/user-data)
)


(defn post-user-data [req] (let
  [payload (json/parse-string (slurp (:body req)) true)]
  (swap! db/user-data assoc
         :user_name (:user_name payload)
         :age       (:age       payload)
         :height    (:height    payload)
         :weight    (:weight    payload)
         :sex       (:sex       payload)
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
   kcal  (- (external/calories-burned name value))]
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


(defn transactions-between [start-date end-date]
  (vec (filter
    #(let
      [formatter (DateTimeFormatter/ofPattern "dd/MM/yyyy")
       date      (LocalDate/parse (:date %) formatter)]
      (and
        (not (.isBefore date start-date))
        (not (.isAfter  date end-date))
      )
    )
    @db/transactions
  ))
)


(defn transactions-by-date [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   formatter  (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   start-date (LocalDate/parse (:start_date payload) formatter)
   end-date   (LocalDate/parse (:end_date payload) formatter)]
  (json/generate-string (transactions-between start-date end-date))
))


(defn balance []
  (json/generate-string (str (reduce + (map :kcal @db/transactions)) " kcal"))
)


(defn balance-between [start-date end-date]
  (reduce + (map :kcal (transactions-between start-date end-date)))
)


(defn balance-by-date [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   formatter  (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   start-date (LocalDate/parse (:start_date payload) formatter)
   end-date   (LocalDate/parse (:end_date payload) formatter)]
  (json/generate-string (str (balance-between start-date end-date) " kcal"))
))
