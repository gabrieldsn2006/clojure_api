(ns clojure-api.db)

(def user_data (atom
  {:user_name nil
   :age       nil
   :height    nil
   :weight    nil
   :sex       nil}
))

(def transactions (atom (vector)))

(def calories (atom 0.0))