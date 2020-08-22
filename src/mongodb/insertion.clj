(ns mongodb.insertion
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]
           [com.mongodb WriteConcern]))

(def conn (mg/connect))
(def  db-name "mydb")
(def db (mg/get-db conn db-name))
(def coll-name "students")

(mc/insert-and-return db coll-name {:name "Jhonney"
                                    :msg "success!"})
;; => {:_id #object[org.bson.types.ObjectId 0x78eca866 "5f3ff9da799d900ab9cde96a"], :name "Jhonney", :msg "success!"}

;; with explicit document id (recommended)
(mc/insert db coll-name {:_id (ObjectId.)
                         :name "Danish"
                         :msg "success!"})
;; => #object[com.mongodb.WriteResult 0x3240dd22 "WriteResult{n=0, updateOfExisting=false, upsertedId=null}"]

(mc/insert-batch db coll-name [{:_id (ObjectId.)
                                :name "Zakir"
                                :msg "abc"}
                               {:_id (ObjectId.)
                                :name "Nasir"
                                :msg "xyz"}])
;; => #object[com.mongodb.WriteResult 0x20fedbae "WriteResult{n=0, updateOfExisting=false, upsertedId=null}"]

;; without document id (when you don't need to use it after storing the document)
(mc/insert db coll-name {:name "Shoaib"
                         :msg "success!"})
;; => #object[com.mongodb.WriteResult 0x6581debb "WriteResult{n=0, updateOfExisting=false, upsertedId=null}"]

(mc/insert db coll-name {:_id (ObjectId.)
                         :name "Zahid"
                         :msg "success!"}
           WriteConcern/JOURNAL_SAFE)


;; monger.collection/insert returns write result that monger. result/acknowledged? and other monger.result functions can operate on.
;; If you insert a document without the :_id key, MongoDB Java driver that Monger uses under the hood will generate one for you. Unfortunately,
;; it does so by mutating the document you pass it. With Clojure's immutable data structures, that won't work the way MongoDB Java driver authors
;; expected.
;; So it is highly recommended to always store documents with the :_id key set. If you need a generated object id. You do so by instantiating org.
;; bson.types.ObjectId without arguments

;; example of monger.result/acknowledged? - pending

;;Write Failures, Default WriteConcern - pending