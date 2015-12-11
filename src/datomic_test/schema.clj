(ns datomic-test.schema)

(def schema
  [{:db/id #db/id[:db.part/db]
    :db/ident :community/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/fulltext true
    :db/doc "A community's name"
    :db.install/_attribute :db.part/db}
   {:db/id #db/id[:db.part/db]
    :db/ident :community/url
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db/doc "A community's url"
    :db.install/_attribute :db.part/db}
   ]
  )
