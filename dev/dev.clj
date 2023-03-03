(ns dev
  (:require [clojure.pprint :refer [pp]]
            [clojure.tools.namespace.repl :as tn]
            [ib-api.system :as sys]
            [taoensso.timbre :as log]))

(def system (atom nil))

(defn start []
  (log/info "user/start")
  (let [s (sys/run-system)]
    (swap! system (fn [_] s))))

(defn stop []
  (log/info "user/stop")
  (sys/stop-system @system)
  (swap! system (fn [_] nil)))

(defn go []
  (start)
  :ready)

(defn reset []
  (stop)
  (tn/refresh :after 'dev/go))
