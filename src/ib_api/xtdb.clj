(ns ib-api.xtdb
  (:require [clojure.java.io :as io]
            [xtdb.api :as xt]
            [taoensso.timbre :as log]
            [integrant.core :as ig]))

(defn start-xtdb! [tx-log doc-store index-store]
  (letfn [(kv-store [dir]
            {:kv-store
             {:xtdb/module 'xtdb.rocksdb/->kv-store
              :db-dir (io/file dir)}})]
    (log/debug (kv-store tx-log))
    (xt/start-node
     {:xtdb/tx-log (kv-store tx-log)
      :xtdb/document-store (kv-store doc-store)
      :xtdb/index-store (kv-store index-store)})))

(defmethod ig/init-key :core/xtdb [_ {:keys [config] :as opts}]
  (let [xtdb-config (:xtdb config)]
    (log/debug xtdb-config)
    (start-xtdb! (:tx-log xtdb-config) (:doc-store xtdb-config) (:index-store xtdb-config))))

(defmethod ig/halt-key! :core/xtdb [_ xtdb-node]
  (.close xtdb-node))
