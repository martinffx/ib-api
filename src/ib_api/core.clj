(ns ib-api.core
  (:require [taoensso.timbre :as log]
            [mount.core :as mount]
            [ib-api.ib-client :as client]))


(defn -main []
  (mount/start #'client/client)
  (.addShutdownHook
   (mount/stop))
  (read-line))
