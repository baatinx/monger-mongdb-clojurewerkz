(ns mongodb.updation
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.operators :refer [$inc]]
            [mongodb.retrieval :refer [conn db-name db coll-name]])
  (:import [org.bson.types ObjectId]))

(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x304b5dff "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}
;;     {:_id #object[org.bson.types.ObjectId 0x38ead783 "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 24, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x5a4196e9 "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x65226c53 "5f41508e799d900ab9cde9ae"], :name "Danish", :age 20, :gender "m"})

(mc/update db coll-name {:name "Basit"} {$inc {:age 1}})
;; => #object[com.mongodb.WriteResult 0x5a1aca29 "WriteResult{n=1, updateOfExisting=true, upsertedId=null}"]

(mc/update db coll-name {:name "Basit"} {$inc {:age 2}})
;; => #object[com.mongodb.WriteResult 0x53941e55 "WriteResult{n=1, updateOfExisting=true, upsertedId=null}"]


(mc/find-maps db coll-name {:name "Basit"})
;; => ({:_id #object[org.bson.types.ObjectId 0x4978181 "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 27, :gender "m"})

(mc/update db coll-name {:name "Basit"} {$inc {:age -3}})
;; => #object[com.mongodb.WriteResult 0x5a8b584c "WriteResult{n=1, updateOfExisting=true, upsertedId=null}"]

(mc/find-maps db coll-name {:name "Basit"})
;; => ({:_id #object[org.bson.types.ObjectId 0x28155087 "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 24, :gender "m"})


(let [{:keys [_id]} (mc/find-one-as-map db coll-name {:name "Basit"})]
  (mc/update-by-id db coll-name _id {:name "Mustafa Basit"}))
;; => #object[com.mongodb.WriteResult 0x63d635b5 "WriteResult{n=1, updateOfExisting=true, upsertedId=null}"]

(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x1d27b695 "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}
;;     {:_id #object[org.bson.types.ObjectId 0x5b1b4d83 "5f413bc0799d900ab9cde9a7"], :name "Mustafa Basit"}
;;     {:_id #object[org.bson.types.ObjectId 0x14f97fff "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x5a5e2b4c "5f41508e799d900ab9cde9ae"], :name "Danish", :age 20, :gender "m"})

(let [{:keys [_id]} (mc/find-one-as-map db coll-name {:name "Mustafa Basit"})]
  (mc/update-by-id db coll-name _id {:name "Basit" :age 23 :gender "m" :married false}))
;; => #object[com.mongodb.WriteResult 0x595bca7c "WriteResult{n=1, updateOfExisting=true, upsertedId=null}"]

(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x649097f9 "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}
;;     {:_id #object[org.bson.types.ObjectId 0x4eadf0ac "5f413bc0799d900ab9cde9a7"],
;;      :name "Basit",
;;      :age 23,
;;      :gender "m",
;;      :married false}
;;     {:_id #object[org.bson.types.ObjectId 0x24bcec2b "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0xdda3473 "5f41508e799d900ab9cde9ae"], :name "Danish", :age 20, :gender "m"})

(mc/update-by-id db coll-name nil {:name "bla"})
;; => Execution error (IllegalArgumentException) at monger.collection/update-by-id (collection.clj:311).
;; => id must not be nil

;; trying to update via object id that does not exist
(mc/update-by-id db coll-name (ObjectId. "11ff3bc0799d900ab9cde888") {:name "bla"})
;; => #object[com.mongodb.WriteResult 0x1dbb626 "WriteResult{n=0, updateOfExisting=false, upsertedId=null}"]


;; ***
;; upserts -  "update or insert"

;; To do an upsert with Monger, use monger.collection/update function with :upsert option set to true:

(mc/update db coll-name {:name "Faizan Naveed"} {:name "Faizan Naveed" :age 20 :gender "m"})
;; => #object[com.mongodb.WriteResult 0x2345d292 "WriteResult{n=0, updateOfExisting=false, upsertedId=null}"]

(mc/update db coll-name {:name "Faizan Naveed"} {:name "Faizan Naveed" :age 20 :gender "m"} {:upsert true})
;; => #object[com.mongodb.WriteResult 0x2bff6225 "WriteResult{n=1, updateOfExisting=false, upsertedId=5f41758ae08a958ca71b5929}"]

(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x112eb22b "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}
;;     {:_id #object[org.bson.types.ObjectId 0x38c11f3b "5f413bc0799d900ab9cde9a7"],
;;      :name "Basit",
;;      :age 23,
;;      :gender "m",
;;      :married false}
;;     {:_id #object[org.bson.types.ObjectId 0x4a771c52 "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x1425cd5e "5f41508e799d900ab9cde9ae"], :name "Danish", :age 20, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x28f2b4bd "5f41758ae08a958ca71b5929"],
;;      :name "Faizan Naveed",
;;      :age 20,
;;      :gender "m"})

;;Atomic Modifiers
;; Modifier operations are highly-efficient and useful when updating existing values; for instance, they're great
;; for incrementing counters, setting individual fields, updating fields that are arrays and so on.
;; MongoDB supports modifiers via update operation and Monger API works the same way: you pass a document with modifiers
;; to monger.collection/update.
(mc/update db coll-name {:name "Faizan Naveed"} {$inc {:age -1}})
;; => #object[com.mongodb.WriteResult 0x4f5e9c6d "WriteResult{n=1, updateOfExisting=true, upsertedId=null}"]

(mc/find-one-as-map db coll-name {:name "Faizan Naveed"})
;; => {:_id #object[org.bson.types.ObjectId 0x42d38f9e "5f41758ae08a958ca71b5929"],
;;     :name "Faizan Naveed",
;;     :age 19,
;;     :gender "m"}


