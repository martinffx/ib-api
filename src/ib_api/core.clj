(ns ib-api.core
  (:require [taoensso.timbre :as log]
            [ib-api.system :refer [run-system stop-system]]))


(defn -main []
  (log/info "System starting...")
  (let [system (run-system)]
    (.addShutdownHook
      (fn [] (stop-system system))))
  (log/info "System started.")
  (read-line))
