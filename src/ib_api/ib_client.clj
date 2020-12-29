(ns ib-api.ib-client
    (:require [taoensso.timbre :as log]
              [mount.core :refer [defstate]])
  (:import (com.ib.client EWrapper EJavaSignal EClientSocket)))

(def connected (atom false))
(def connection-attempt-count (atom 0))

(defn build-ib-wrapper []
  "Creates an instance of the EWrapper interface to handle callbacks
  from the IB client."
  (reify EWrapper
    (^void error [this ^Exception e] (println e))
    (^void error [this ^String errorMsg] (println errorMsg))
    (^void error [this ^int id, ^int errorCode, ^String errorMsg] (println errorMsg))
    (^void connectAck [this]
     (log/info "Connection Accepted")
     (swap! connected #(true)))
    (^void nextValidId [this ^int orderId]
     (log/info "Next Valid Order ID: " orderId))))

(def readerSignal (new EJavaSignal))
(def clientSocket (new EClientSocket ibWrapper readerSignal))

(defn start [host port]
  (log/info "Attempting to connect to TWS on: " host ":" port)
  (.eConnect clientSocket host port 0)
  (while (and (not connected) (< connection-attempt-count 10))
    (log/info "Waiting for connection...")
    (Thread/sleep 100))
  (when (not connected)
    (throw (Exception. "Failed to establish a connection.")))
  (log/info "Connection to TWS established."))


(defstate)
