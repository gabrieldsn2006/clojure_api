(ns clojure-api.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure-api.service :as service]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]])
)

(defroutes app-routes
  (GET "/"            [] "clojure_api")
  (GET "/user_data"   [] (service/user_data))
  (GET "/food"        [] (service/food))
  (GET "/activity"    [] (service/food))
  (GET "/statement"   [] (service/statement))
  (GET "/balance"     [] (service/balance))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults)
)
