(ns clojure-api.service
  (:require [cheshire.core        :as json]
            [clojure-api.db       :as db]
            [clojure-api.external :as external])
)


(defn get_user_data []
  "API: Dados Pessoais"
)


(defn post_user_data [req] (let
  [payload (slurp (:body req))]
  (str "API: " (json/parse-string payload true))
))


(defn food [req] (let
  [payload (slurp (:body req))]
  (str "API: " (json/parse-string payload true))
))


(defn activity [req] (let
  [payload (slurp (:body req))]
  (str "API: " (json/parse-string payload true))
))


(defn statement []
  "API: Exibir Extrato"
)


(defn balance []
  "API: Exibir Saldo"
)
