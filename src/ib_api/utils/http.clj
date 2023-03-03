(ns ib-api.utils.http
  (:require [org.httpkit.client :as http]
            [manifold.deferred :as d]
            [jsonista.core :as j]
            [taoensso.timbre :as log]
            [clojure.core.match :refer [match]]))

(def default-options {:as :stream})
(defn- handle-rs
    [{:keys [status headers body error opts]}]
  (log/debug (str "status: " status ", headers: " headers ", body: " body ", error: " error ", opts: " opts))
  (if error
    (do
      (log/error "HTTP Error: " error)
      {:status 500 :error error})
    (match [status]
         [(_ :guard #(and (>= % 200) (< % 300)))] {:status status :data (j/read-value body)}
         :else {:status status :data (slurp body)}))
  )

(defn get [url options]
  (d/chain
       (http/get url (conj options default-options))
       #(handle-rs %)))

(defn post [url options]
  (d/chain
       (http/get url (conj options default-options))
       #(handle-rs %)))

(defn patch [url options]
  (d/chain
       (http/get url (conj options default-options))
       #(handle-rs %)))

(defn delete [url options]
  (d/chain
       (http/get url (conj options default-options))
       #(handle-rs %)))
