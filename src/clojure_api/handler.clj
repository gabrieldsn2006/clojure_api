(ns clojure-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure-api.service :as service]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]])
)


(defroutes app-routes
  (GET  "/"             _   "clojure_api")
  (GET  "/user_data"    _ (service/get-user-data))
  (POST "/user_data"    req (service/post-user-data req))
  (POST "/food"         req (service/food req))
  (POST "/activity"     req (service/activity req))
  (GET  "/transactions" _   (service/transactions))
  (POST "/transactions" req (service/transactions-by-date req))
  (GET  "/balance"      _   (service/balance))
  (POST "/balance"      req (service/balance-by-date req))
  (route/not-found "Not Found")
)


(def app (wrap-defaults app-routes api-defaults))
