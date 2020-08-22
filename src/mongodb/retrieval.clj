(ns mongodb.retrieval
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.query :as mq]
            [monger.operators :refer :all])
  (:import [org.bson.types ObjectId]))

;; Monger provides two ways of finding documents:
;; Using finder functions in the monger.collection namespace
;; Using query DSL in the monger.query namespace
;; The former is designed to cover simple cases better while the latter gives you access to full power of MongoDB querying
;; capabilities and extra features like pagination.

(def conn (mg/connect))
(def db-name "mydb")
(def db (mg/get-db conn db-name))
(def coll-name "students")

(mc/remove db coll-name)

;; via monger.collection

(mc/insert-batch db coll-name [{:_id (ObjectId.)
                                :name "Seerat"
                                :age 20
                                :gender "f"}
                               {:_id (ObjectId.)
                                :name "Basit"
                                :age 24
                                :gender "m"}
                               {:_id (ObjectId.)
                                :name "Hammad"
                                :age 21
                                :gender "m"}])
;; => #object[com.mongodb.WriteResult 0x6e67480b "WriteResult{n=0, updateOfExisting=false, upsertedId=null}"]

;; Finder functions in Monger return either Clojure maps (commonly used) or Java driver's objects like DBObject and DBCursor.
;; monger.collection/find-maps is similar to monger.collection/find but converts DBObject instances to Clojure maps:
;; For example, monger.collection/find returns a DBCursor:

(mc/find db coll-name {:gender "m"})
;; => #object[com.mongodb.DBCursor 0x583a137a "DBCursor{collection=DBCollection{database=DB{name='mydb'}, name='students'}, find=com.mongodb.client.model.DBCollectionFindOptions@4e37db4b}"]

(mc/find-maps db coll-name {:gender "m"})
;; => ({:_id #object[org.bson.types.ObjectId 0x7eed3729 "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 24, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0xc36a8ad "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"})

(mc/find-maps db coll-name {:gender "m"} [:name])
;; => ({:_id #object[org.bson.types.ObjectId 0x4e73ff69 "5f413bc0799d900ab9cde9a7"], :name "Basit"}
;;     {:_id #object[org.bson.types.ObjectId 0x3039659a "5f413bc0799d900ab9cde9a8"], :name "Hammad"}
;;     {:_id #object[org.bson.types.ObjectId 0x30b328c9 "5f41422e799d900ab9cde9aa"], :name "Danish"}
;;     {:_id #object[org.bson.types.ObjectId 0xccd39ed "5f414254799d900ab9cde9ac"], :name "Danish"})

;; keywordize not working
(mc/find-maps db coll-name {:gender "m"} [:name])
;; => ({:_id #object[org.bson.types.ObjectId 0x23c371dc "5f413bc0799d900ab9cde9a7"], :name "Basit"}
;;     {:_id #object[org.bson.types.ObjectId 0x2ee5cd0a "5f413bc0799d900ab9cde9a8"], :name "Hammad"}
;;     {:_id #object[org.bson.types.ObjectId 0x638629ec "5f41508e799d900ab9cde9ae"], :name "Danish"})

(mc/find-maps db coll-name {:gender "m"} [:name] false)
;; => ({"_id" #object[org.bson.types.ObjectId 0x5419e89a "5f413bc0799d900ab9cde9a7"], "name" "Basit"}
;;     {"_id" #object[org.bson.types.ObjectId 0x4b3cecca "5f413bc0799d900ab9cde9a8"], "name" "Hammad"}
;;     {"_id" #object[org.bson.types.ObjectId 0x327f79af "5f41508e799d900ab9cde9ae"], "name" "Danish"})


(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x5c0e50a5 "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}
;;     {:_id #object[org.bson.types.ObjectId 0x24e8b2c9 "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 24, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x47a07fd9 "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"})


(mc/find-one db coll-name {:_id (ObjectId. "5f413bc0799d900ab9cde9a8")})
;; => {"_id" #object[org.bson.types.ObjectId 0x3a81e845 "5f413bc0799d900ab9cde9a8"], "name" "Hammad", "age" 21, "gender" "m"}

(mc/find-one db coll-name {:_id (ObjectId. "5f401d86799d900ab9cde98a")})
;; => nil

(mc/find-one db coll-name {:_id (ObjectId. "123")})
;; => Execution error (IllegalArgumentException) at org.bson.types.ObjectId/parseHexString (ObjectId.java:550).
;; => invalid hexadecimal representation of an ObjectId: [123]

(mc/find-one-as-map db coll-name {:name "Seerat"})
;; => {:_id #object[org.bson.types.ObjectId 0x2be4d524 "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}

(mc/find-one-as-map db coll-name {:name "Saqib"})
;; => nil

(mc/find-one-as-map db coll-name {:_id (ObjectId. "5f413bc0799d900ab9cde9a6")})
;; => {:_id #object[org.bson.types.ObjectId 0x189eaf8a "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}

;; ***
;; A more convenient way of finding a document by id as Clojure map is  monger.collection/find-map-by-id
(mc/find-map-by-id db coll-name (ObjectId. "5f413bc0799d900ab9cde9a6"))
;; => {:_id #object[org.bson.types.ObjectId 0x5959c281 "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}

(let [conn (mg/connect)
      db-name "mydb"
      db (mg/get-db conn db-name)
      coll-name "students"
      oid (ObjectId.)
      doc {:name "Danish" :age 20 :gender "m"}]
  (mc/insert db coll-name (merge doc
                                 {:_id oid}))
  (mc/find-map-by-id db coll-name oid))
;; => {:_id #object[org.bson.types.ObjectId 0x1762f9ee "5f41508e799d900ab9cde9ae"], :name "Danish", :age 20, :gender "m"}

(mc/find-maps db coll-name {:age 20})
;; => ({:_id #object[org.bson.types.ObjectId 0x3ca0bf2c "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}
;;     {:_id #object[org.bson.types.ObjectId 0x13ca3aac "5f41508e799d900ab9cde9ae"], :name "Danish", :age 20, :gender "m"})


(mc/find-maps db coll-name {:age {"$gt" 20}})
;; => ({:_id #object[org.bson.types.ObjectId 0x56e1a4c "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 24, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x669d992c "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"})

(mc/find-maps db coll-name {:age {"$gt" 20 "$lt" 24}})
;; => ({:_id #object[org.bson.types.ObjectId 0x42d8b110 "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"})

;; mongo operators as symbols
(mc/find-maps db coll-name {:age {$gt 20}})
;; => ({:_id #object[org.bson.types.ObjectId 0x38f46e68 "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 24, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x5fafe758 "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"})

(mc/find-maps db coll-name {:age {$gt 20 $lt 24}})
;; => ({:_id #object[org.bson.types.ObjectId 0x1479fb33 "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"})

(mc/count db coll-name)
;; => 4

(mc/empty? db coll-name)
;; => false

(mc/any? db coll-name)
;; => true



;; monger.query - pending
;; Query DSL Overview
;; For cases when it is necessary to combine sorting, limiting or offseting results, pagination and even more advanced features
;; like cursor snapshotting or manual index hinting, Monger provides a very powerful query DSL
