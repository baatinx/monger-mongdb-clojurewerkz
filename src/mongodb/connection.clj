(ns mongodb.connection
  (:require [monger.core :as mg]
            [monger.credentials :as mcr]
            [monger.collection :as coll]))

(let [conn (mg/connect {:host "localhost"
                        :port 27017})
      db (mg/get-db conn "mydb")]
  [conn db])
;; => [#object[com.mongodb.MongoClient 0x114da6cc "Mongo{options=MongoClientOptions{description='null', applicationName='null', compressors='[]', readPreference=primary, writeConcern=WriteConcern{w=null, wTimeout=null ms, fsync=null, journal=null, retryWrites=false, readConcern=com.mongodb.ReadConcern@0, codecRegistry=org.bson.codecs.configuration.ProvidersCodecRegistry@1df70da9, serverSelector=null, clusterListeners=[], commandListeners=[], minConnectionsPerHost=0, maxConnectionsPerHost=100, threadsAllowedToBlockForConnectionMultiplier=5, serverSelectionTimeout=30000, maxWaitTime=120000, maxConnectionIdleTime=0, maxConnectionLifeTime=0, connectTimeout=10000, socketTimeout=0, socketKeepAlive=true, sslEnabled=false, sslInvalidHostNamesAllowed=false, sslContext=null, alwaysUseMBeans=false, heartbeatFrequency=10000, minHeartbeatFrequency=500, heartbeatConnectTimeout=20000, heartbeatSocketTimeout=20000, localThreshold=15, requiredReplicaSetName='null', dbDecoderFactory=com.mongodb.DefaultDBDecoder$1@57aff6cd, dbEncoderFactory=com.mongodb.DefaultDBEncoder$1@4693486e, socketFactory=null, cursorFinalizerEnabled=true, connectionPoolSettings=ConnectionPoolSettings{maxSize=100, minSize=0, maxWaitQueueSize=500, maxWaitTimeMS=120000, maxConnectionLifeTimeMS=0, maxConnectionIdleTimeMS=0, maintenanceInitialDelayMS=0, maintenanceFrequencyMS=60000, connectionPoolListeners=[]}, socketSettings=SocketSettings{connectTimeoutMS=10000, readTimeoutMS=0, keepAlive=true, receiveBufferSize=0, sendBufferSize=0}, serverSettings=ServerSettings{heartbeatFrequencyMS=10000, minHeartbeatFrequencyMS=500, serverListeners='[]', serverMonitorListeners='[]'}, heartbeatSocketSettings=SocketSettings{connectTimeoutMS=20000, readTimeoutMS=20000, keepAlive=true, receiveBufferSize=0, sendBufferSize=0}}}"]
;;     #object[com.mongodb.DB 0x51acf042 "DB{name='mydb'}"]]

;;connection using uri - without authentication
(let [uri "mongodb://localhost:27017/mydb"
      {:keys [conn db]} (mg/connect-via-uri uri)]
  [conn db])
;; => [#object[com.mongodb.MongoClient 0x38f0f97b "Mongo{options=MongoClientOptions{description='null', applicationName='null', compressors='[]', readPreference=primary, writeConcern=WriteConcern{w=null, wTimeout=null ms, fsync=null, journal=null, retryWrites=false, readConcern=com.mongodb.ReadConcern@0, codecRegistry=org.bson.codecs.configuration.ProvidersCodecRegistry@1df70da9, serverSelector=null, clusterListeners=[], commandListeners=[], minConnectionsPerHost=0, maxConnectionsPerHost=100, threadsAllowedToBlockForConnectionMultiplier=5, serverSelectionTimeout=30000, maxWaitTime=120000, maxConnectionIdleTime=0, maxConnectionLifeTime=0, connectTimeout=10000, socketTimeout=0, socketKeepAlive=true, sslEnabled=false, sslInvalidHostNamesAllowed=false, sslContext=null, alwaysUseMBeans=false, heartbeatFrequency=10000, minHeartbeatFrequency=500, heartbeatConnectTimeout=20000, heartbeatSocketTimeout=20000, localThreshold=15, requiredReplicaSetName='null', dbDecoderFactory=com.mongodb.DefaultDBDecoder$1@57aff6cd, dbEncoderFactory=com.mongodb.DefaultDBEncoder$1@4693486e, socketFactory=null, cursorFinalizerEnabled=true, connectionPoolSettings=ConnectionPoolSettings{maxSize=100, minSize=0, maxWaitQueueSize=500, maxWaitTimeMS=120000, maxConnectionLifeTimeMS=0, maxConnectionIdleTimeMS=0, maintenanceInitialDelayMS=0, maintenanceFrequencyMS=60000, connectionPoolListeners=[]}, socketSettings=SocketSettings{connectTimeoutMS=10000, readTimeoutMS=0, keepAlive=true, receiveBufferSize=0, sendBufferSize=0}, serverSettings=ServerSettings{heartbeatFrequencyMS=10000, minHeartbeatFrequencyMS=500, serverListeners='[]', serverMonitorListeners='[]'}, heartbeatSocketSettings=SocketSettings{connectTimeoutMS=20000, readTimeoutMS=20000, keepAlive=true, receiveBufferSize=0, sendBufferSize=0}}}"]
;;     #object[com.mongodb.DB 0x5f00400c "DB{name='mydb'}"]]

;;connection using uri - with authentication - pending


;;Authentication
(let [admin-db "admin"
      u "mustafa-basit"
      p (.toCharArray "root")
      cred (mcr/create u admin-db p)
      host "localhost"]
  (mg/connect-with-credentials host cred))
;; => #object[com.mongodb.MongoClient 0x494958ee "Mongo{options=MongoClientOptions{description='null', applicationName='null', compressors='[]', readPreference=primary, writeConcern=WriteConcern{w=null, wTimeout=null ms, fsync=null, journal=null, retryWrites=false, readConcern=com.mongodb.ReadConcern@0, codecRegistry=org.bson.codecs.configuration.ProvidersCodecRegistry@1df70da9, serverSelector=null, clusterListeners=[], commandListeners=[], minConnectionsPerHost=0, maxConnectionsPerHost=100, threadsAllowedToBlockForConnectionMultiplier=5, serverSelectionTimeout=30000, maxWaitTime=120000, maxConnectionIdleTime=0, maxConnectionLifeTime=0, connectTimeout=10000, socketTimeout=0, socketKeepAlive=true, sslEnabled=false, sslInvalidHostNamesAllowed=false, sslContext=null, alwaysUseMBeans=false, heartbeatFrequency=10000, minHeartbeatFrequency=500, heartbeatConnectTimeout=20000, heartbeatSocketTimeout=20000, localThreshold=15, requiredReplicaSetName='null', dbDecoderFactory=com.mongodb.DefaultDBDecoder$1@57aff6cd, dbEncoderFactory=com.mongodb.DefaultDBEncoder$1@4693486e, socketFactory=null, cursorFinalizerEnabled=true, connectionPoolSettings=ConnectionPoolSettings{maxSize=100, minSize=0, maxWaitQueueSize=500, maxWaitTimeMS=120000, maxConnectionLifeTimeMS=0, maxConnectionIdleTimeMS=0, maintenanceInitialDelayMS=0, maintenanceFrequencyMS=60000, connectionPoolListeners=[]}, socketSettings=SocketSettings{connectTimeoutMS=10000, readTimeoutMS=0, keepAlive=true, receiveBufferSize=0, sendBufferSize=0}, serverSettings=ServerSettings{heartbeatFrequencyMS=10000, minHeartbeatFrequencyMS=500, serverListeners='[]', serverMonitorListeners='[]'}, heartbeatSocketSettings=SocketSettings{connectTimeoutMS=20000, readTimeoutMS=20000, keepAlive=true, receiveBufferSize=0, sendBufferSize=0}}}"]

(let [conn (mg/connect)]
  (mg/disconnect conn))
;; => nil