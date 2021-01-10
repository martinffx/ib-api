(ns ib-api.ib-client
    (:require [taoensso.timbre :as log]
              [mount.core :refer [defstate]]
              [ib-api.config :refer [config]])
  (:import (com.ib.client EWrapper EJavaSignal EClientSocket)))

(defn build-ib-wrapper
  "Creates an instance of the EWrapper interface to handle callbacks
  from the IB client."
  [connected]
  
  (reify EWrapper
    (^void error [this ^Exception e] (println e))
    (^void error [this ^String errorMsg] (println errorMsg))
    (^void error [this ^int id, ^int errorCode, ^String errorMsg] (println errorMsg))
    (^void connectAck [this]
     (log/info "Connection Accepted")
     (swap! connected #(true)))
    (^void nextValidId [this ^int orderId]
     (log/info "Next Valid Order ID: " orderId))))


(defprotocol Client
  (connect [this host port])
  (disconnect [this]))

(defrecord IBClient
    [connected connection-attempt-count reader-signal ib-wrapper client-socket]
  Client
  (connect
    [this host port]    
    ;; Establish connection to TWS
    (log/info client-socket)
    (log/info host)
    (log/info port)
    (.eConnect client-socket host port 0)

    ;; Wait for it to become healthy
    (while (and (not connected) (< connection-attempt-count 10))
      (log/info "Waiting for connection...")
      (Thread/sleep (10 * connection-attempt-count)))

    ;; handle healthy connection or throw error
    (if connected
      this ;; TODO: Handle reader signals
      (throw (Exception. "Failed to establish a connection."))))
  
  (disconnect
    [this]
    
    (.eDisconnect client-socket)))

(defn build-ib-client
  "Builds a healthy IBClient with a healthy connection to TWS"
  [host port]
  
  (let [connected (atom false)
        connection-attempt-count (atom 0)
        reader-signal (new EJavaSignal)
        ib-wrapper (build-ib-wrapper connected)
        client-socket (new EClientSocket ib-wrapper reader-signal)
        ib-client (IBClient. connected connection-attempt-count reader-signal ib-wrapper client-socket)]
    (.connect ib-client host port)))


(defstate client
  :start (build-ib-client (:tws-host config) (:tws-port config))
  :stop (.disconnect client))
