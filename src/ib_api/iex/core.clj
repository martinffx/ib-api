(ns ib-api.iex.core
  (:require [taoensso.timbre :as log]
            [ib-api.iex.client]
            [ib-api.iex.repo]
            [integrant.core :as ig]
            [manifold.deferred :as d]))

(defprotocol IEXProtocol
  (update-exchanges [this]))

(defrecord IEX
    [client repo]
  IEXProtocol
  (update-exchanges [_this]
    (d/let-flow' [])))

(defmethod ig/init-key :iex/core [_ {:keys [client repo]}]
  (IEX. client repo))
