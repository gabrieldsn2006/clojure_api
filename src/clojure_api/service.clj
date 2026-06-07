(ns clojure-api.service
  (:require [cheshire.core        :as json]
            [clojure-api.db       :as db]
            [clojure-api.external :as external])
  (:import [java.time LocalDate]
           [java.time.format DateTimeFormatter])
)


(defn get_user_data []
  (str @db/user_data)
)


(defn post_user_data [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   user_name (:user_name payload)
   age       (:age payload)
   height    (:height payload)
   weight    (:weight payload)
   sex       (:sex payload)]
  (swap! db/user_data assoc
    :user_name user_name
    :age       age
    :height    height
    :weight    weight
    :sex       sex
  )
  (str "API: " (str @db/user_data))
))


(defn food [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   food_name  (:food_name payload)
   food_value (:food_value payload)
   formatter  (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   date       (LocalDate/parse (:date payload) formatter)]
  (swap! db/transactions conj payload)
  (str "API: " payload)
))


(defn activity [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   activity_name  (:activity_name payload)
   activity_value (:activity_value payload)
   formatter      (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   date           (LocalDate/parse (:date payload) formatter)]
  (swap! db/transactions conj payload)
  (str "API: " payload)
))


(defn statement []
  (str @db/transactions)
)


(defn statement_by_date [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   formatter  (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   start_date (LocalDate/parse (:start_date payload) formatter)
   end_date   (LocalDate/parse (:end_date payload) formatter)]
  (str "API: " payload)
))


(defn balance []
  "API: Exibir Saldo"
)

(defn balance_by_date [req] (let
  [payload (json/parse-string (slurp (:body req)) true)
   formatter  (DateTimeFormatter/ofPattern "dd/MM/yyyy")
   start_date (LocalDate/parse (:start_date payload) formatter)
   end_date   (LocalDate/parse (:end_date payload) formatter)]
  (str "API: " payload)
))
