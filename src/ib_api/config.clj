(ns ib-api.config
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]
            [taoensso.timbre :as log]
            [aero.core :refer (read-config)]))


(defmethod ig/init-key :core/config [_ {:keys [config-file]}]
  (let [c (io/resource config-file)]
    (log/info c)
    (read-config c)))
