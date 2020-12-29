(ns ib-api.core
  (:require [taoensso.timbre :as log]))

(defn start []
  (log/info "Start..."))

(defn stop []
  (log/info "Stop..."))

(defn -main []
  (start)
  (.addShutdownHook
   (Runtime/getRuntime)
   (Thread. ^Runnable stop))
  (read-line))
