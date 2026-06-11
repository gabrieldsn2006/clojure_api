(ns clojure-api.db)

(def user-data (atom
  {:user_name nil
   :age       nil
   :height    nil
   :weight    nil
   :sex       nil}
))

(def transactions (atom (vector)))

