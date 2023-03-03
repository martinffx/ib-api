(ns ib-api.iex.client
  (:require [ib-api.utils.http :as http]
            [integrant.core :as ig]
            [taoensso.timbre :as log]))

(defprotocol IEXClientProtocol
  (list-exchanges [this])
  (list-securities [this exchange])
  (get-prices [this symbol date]))

(defrecord IEXClient
    [base-url api-token]
  IEXClientProtocol
  (list-exchanges
    "Returns a deferred seq of exchanges."
    [_]
    (let [url (str base-url "/ref-data/exchanges?token=" api-token)]
      (http/get url)))
  (list-securities
    "Takes an exchange code and returns a deferred seq of the securities available on that exchange."
    [_ exchange]
    (let [url (format "%s/ref-data/exchange/%s/symbols?token=%s" base-url exchange api-token)]
      (http/get url)))
  (get-prices
    "Takes a security symbol and date, returns a deferred seq of prices for that symbol from the given date."
    [_ symbol date]
    (let [url (format "%s/stock/%s/chart/date/%s?token=%s" base-url symbol api-token)]
      (http/get url))))


(defmethod ig/init-key :iex/client [_ {:keys [host token] :as opts}]
  (IEXClient. host token))
