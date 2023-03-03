(ns ib-api.iex.repo
  (:require [taoensso.timbre :as log]
            [integrant.core :as ig]))

(defprotocol IEXRepoProtocol
  (put-exchange [this exchange]))

(defrecord IEXRepo
    [xtdb]
  IEXRepoProtocol
  (put-exchange [_this exchange]
    (log/info exchange)))

(defmethod ig/init-key :iex/repo [_ {:keys [xtdb]}]
  (IEXRepo. xtdb))
