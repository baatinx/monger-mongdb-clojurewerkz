(ns mongodb.removal
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mongodb.retrieval :refer [conn db-name db]])
  (:import [org.bson.types ObjectId]))

(def coll-name "mongo-test")

(mc/insert-batch db coll-name [{:_id (ObjectId.)
                                :name "Danish"}
                               {:_id (ObjectId.)
                                :name "Zahid"}
                               {:_id (ObjectId.)
                                :name "Zakir"}
                               {:_id (ObjectId.)
                                :name "Usmaan"}])
(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x5f6d72a6 "5f41fafb799d900ab9cde9b3"], :name "Danish"}
;;     {:_id #object[org.bson.types.ObjectId 0x6d0aec64 "5f41fafb799d900ab9cde9b4"], :name "Zahid"}
;;     {:_id #object[org.bson.types.ObjectId 0x479fb453 "5f41fafb799d900ab9cde9b5"], :name "Zakir"}
;;     {:_id #object[org.bson.types.ObjectId 0x1903f5f "5f41fafb799d900ab9cde9b6"], :name "Usmaan"})

(mc/remove db coll-name {:name "Zahid"})
;; => #object[com.mongodb.WriteResult 0x570d8d77 "WriteResult{n=1, updateOfExisting=false, upsertedId=null}"]

(mc/remove-by-id db coll-name (ObjectId. "5f41fafb799d900ab9cde9b5"))
;; => #object[com.mongodb.WriteResult 0x6281c781 "WriteResult{n=1, updateOfExisting=false, upsertedId=null}"]

(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x91ea3d3 "5f41fafb799d900ab9cde9b3"], :name "Danish"}
;;     {:_id #object[org.bson.types.ObjectId 0x76fae1e4 "5f41fafb799d900ab9cde9b6"], :name "Usmaan"})

(mc/remove-by-id db coll-name (ObjectId. "5f41fafb799d900ab9cde9b5"))
;; => #object[com.mongodb.WriteResult 0xe96f3fa "WriteResult{n=0, updateOfExisting=false, upsertedId=null}"]

(type (mc/remove-by-id db coll-name (ObjectId. "5f41fafb799d900ab9cde9b5")))
(class (mc/remove-by-id db coll-name (ObjectId. "5f41fafb799d900ab9cde9b5")))
;; => com.mongodb.WriteResult

(mc/rename db coll-name "mongo-test2")
;; => #object[com.mongodb.DBCollection 0x1cee5e53 "DBCollection{database=DB{name='mydb'}, name='mongo-test2'}"]


(mc/find-maps db coll-name)
;; => ()

(mc/find-maps db "mongo-test2")
;; => ({:_id #object[org.bson.types.ObjectId 0x2d17a623 "5f41fafb799d900ab9cde9b3"], :name "Danish"}
;;     {:_id #object[org.bson.types.ObjectId 0x707da5c9 "5f41fafb799d900ab9cde9b6"], :name "Usmaan"})

(mc/exists? db coll-name)
;; => false

(mc/exists? db "mongo-test2")
;; => true

;; remove all
(mc/remove db "mongo-test2")
;; => #object[com.mongodb.WriteResult 0x36dd4b4c "WriteResult{n=2, updateOfExisting=false, upsertedId=null}"]

(mc/find-maps db "mongo-test2")
;; => ()

(mc/exists? db "mongo-test2")
;; => true
