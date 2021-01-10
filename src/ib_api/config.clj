(ns ib-api.config
  (:require [mount.core :refer [defstate]]
            [aero.core :refer (read-config)]))


(defstate config :start (read-config (clojure.java.io/resource "config.edn")))
