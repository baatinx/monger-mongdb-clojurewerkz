(ns mongodb.updation
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [mongodb.retrieval :refer [conn db-name db coll-name]]))

(mc/find-maps db coll-name)
;; => ({:_id #object[org.bson.types.ObjectId 0x248f1dce "5f413bc0799d900ab9cde9a6"], :name "Seerat", :age 20, :gender "f"}
;;     {:_id #object[org.bson.types.ObjectId 0x357ae1d1 "5f413bc0799d900ab9cde9a7"], :name "Basit", :age 24, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x2e7c4d04 "5f413bc0799d900ab9cde9a8"], :name "Hammad", :age 21, :gender "m"}
;;     {:_id #object[org.bson.types.ObjectId 0x45a0c00e "5f41508e799d900ab9cde9ae"], :name "Danish", :age 20, :gender "m"})
