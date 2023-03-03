(ns ib-api.system
  (:require [clojure.java.io :as io]
            [taoensso.timbre :as log]
            [integrant.core :as ig]
            [ib-api.config]
            [ib-api.xtdb]
            [ib-api.iex.core]))


(defn run-system
  "Starts the Integrant System described in system.edn and returns the system."
  ([]
   (let [config (ig/read-string (slurp(io/resource "system.edn")))]
     (log/info "Running System from: system.edn")
     (log/debug "System config: " config)
     (ig/init config))))

(defn stop-system
  "Stops the Integrant System passed into the function"
  ([system]
   (log/info "Stopping System...")
   (ig/halt! system)
   (log/info "System stopped.")))
