(ns user
  (:require [clojure.pprint :refer [pp]]
             [clojure.tools.namespace.repl :as tn]
             [mount.core :as mount]
             [ib-api.core]))

(defn start []
  (mount/start #'ib-api.core/server))

(defn stop []
  (mount/stop))

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
  (tn/refresh :after 'dev/go))

(mount/in-clj-mode)
