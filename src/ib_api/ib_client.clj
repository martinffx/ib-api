(ns ib-api.ib-client
    (:require [taoensso.timbre :as log])
  (:import (com.ib.client EWrapper EJavaSignal EClientSocket)))

(defn build-ib-wrapper
  "Creates an instance of the EWrapper interface to handle callbacks
  from the IB client."
  [connected]

  (reify EWrapper
    (^void error [this ^Exception e] (log/error e))
    (^void error [this ^String errorMsg] (log/error errorMsg))
    (^void error [this ^int id, ^int errorCode, ^String errorMsg] (log/error errorMsg))
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
    (log/debug (str "Establish connection to TWS: " host ":" port))
    (.eConnect client-socket host port 0)

    (log/debug "Wait for it to become healthy")
    (while (and (not connected) (>= connection-attempt-count 5))
      (log/info "Waiting for connection...")
      (Thread/sleep (10 * connection-attempt-count)))

    ;; handle healthy connection or throw error
    (if connected
      (do
        (log/info "Connection established")
        this)
      (throw (Exception. "Failed to establish a connection."))))

  (disconnect
    [this]

    (.eDisconnect client-socket)
    this))

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
