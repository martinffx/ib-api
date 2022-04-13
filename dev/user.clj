(ns user
  (:require [clojure.tools.namespace.repl :as tn]
            [integrant.core :as ig]
            [clojure.java.io :as io]
            [ib-api.core]))

(def system (atom nil))

(defn start []
  (let [config (ig/read-string (slurp (io/resource "system.edn")))]
    (swap! system (fn [_] (ig/init config)))))

(defn stop []
  (ig/halt! @system))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh-all []
  (stop)
  (tn/refresh-all))

(defn go
  "starts all states defined by defstate"
  []
  (start)
  :ready)

(defn reset
  "stops all states defined by defstate, reloads modified source files, and restarts the states"
  []
  (stop)
  (tn/refresh :after 'user/go))
