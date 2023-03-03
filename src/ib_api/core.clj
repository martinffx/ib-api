(ns ib-api.core
  (:require [taoensso.timbre :as log]
<<<<<<< HEAD
            [ib-api.system :refer [run-system stop-system]]))
=======
            [integrant.core :as ig]
            [clojure.java.io :as io]
            [ib-api.ib-client]))
>>>>>>> c1a023e84f5198088ed37d71ba61586bbd53daf0

(defn run-system []
  (log/info "Starting system...")
  (let [system (ig/read-string (slurp (io/resource "system.edn")))]
    (.addShutdownHook
     (ig/halt! system))))

(defn -main []
<<<<<<< HEAD
  (log/info "System starting...")
  (let [system (run-system)]
    (.addShutdownHook
      (fn [] (stop-system system))))
  (log/info "System started.")
  (read-line))
=======
  ((try
     (run-system)
     (read-line)
     (catch Exception ex
       (log/error ex)))))
>>>>>>> c1a023e84f5198088ed37d71ba61586bbd53daf0
