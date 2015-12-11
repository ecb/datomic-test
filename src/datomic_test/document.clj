(ns datomic-test.document)

(use '[datomic.api :only [q db] :as d])
(use 'datomic-test.schema)
(use 'clojure.pprint)

(defn populate-db [conn]
  (d/transact conn [{:db/id #db/id[:db.part/user -1000001],
                                     :entry/name "bar"
                                     :entry/value (.getBytes "baz")}
                                    {:db/id #db/id[:db.part/user -1000002],
                                     :entry/name "bat"
                                     :entry/value (.getBytes "bah")}
                                    {:db/id #db/id[:db.part/user],
                                     :document/id "foo",
                                     :document/version 1
                                     :document/entries [#db/id[:db.part/user -1000001]
                                                        #db/id[:db.part/user -1000002]]}

                                    {:db/id #db/id[:db.part/user -1000003],
                                     :entry/name "bar"
                                     :entry/value (.getBytes "baz")}
                                    {:db/id #db/id[:db.part/user -1000004],
                                     :entry/name "bat"
                                     :entry/value (.getBytes "bah")}
                                    {:db/id #db/id[:db.part/user],
                                     :document/id "bar",
                                     :document/version 1
                                     :document/entries [#db/id[:db.part/user -1000003]
                                                        #db/id[:db.part/user -1000004]]}
                                    ]))

(defn create-db [uri]
  (d/create-database uri)
  (let [conn (d/connect uri)]
    (d/transact conn datomic-test.schema/schema)
    (populate-db conn)
    conn))

(defn get [id conn]
  (let [result (q '[:find (pull ?doc [*]) :in $ ?id :where [?doc :document/id ?id]] (db conn) id)
        nr (count result)]
    (if (not= 1 nr)
      (throw (Exception. (format "unexpected result count: %d", nr))))
    (ffirst result)))

(defn print [doc]
  (printf "document \"%s\" version %d with %d entries: \n"
          (:document/id doc)
          (:document/version doc)
          (count (:document/entries doc)))
  (dorun
   (map #(printf "\t%s=%s" (:entry/name %)
                 (with-out-str (pprint (:entry/value %))))
        (:document/entries doc))))