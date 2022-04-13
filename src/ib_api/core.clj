(ns ib-api.core
  (:require [taoensso.timbre :as log]
            [integrant.core :as ig]
            [clojure.java.io :as io]
            [ib-api.ib-client]))

(defn run-system []
  (log/info "Starting system...")
  (let [system (ig/read-string (slurp (io/resource "system.edn")))]
    (.addShutdownHook
     (ig/halt! system))))

(defn -main []
  ((try
     (run-system)
     (read-line)
     (catch Exception ex
       (log/error ex)))))
