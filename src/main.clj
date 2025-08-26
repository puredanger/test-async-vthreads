(ns main
  (:gen-class)
  (:require [clojure.core.async :as a]))

(defn thread-type []
  (.getSimpleName (class (Thread/currentThread))))

(defn log [context parking]
  (println (str context ": " (thread-type) ", parking " 
                (cond (nil? parking) "nil"
                      (instance? Throwable parking) "throws"
                      :else "works"))))

(defn -main [& args]
  (println "testing\n")
  (let [c (a/chan 10)]
    (a/>!! c 1)
    (a/thread (log "thread" (try (a/<! c) (catch Throwable _ :throws))))
    (Thread/sleep 5)

    (a/>!! c 2)
    (a/io-thread (log "io-thread" (try (a/<! c) (catch Throwable _ :throws))))
    (Thread/sleep 5)

    (a/>!! c :works)
    (a/go (log "go" (try (a/<! c) (catch Throwable _ :throws))))

    (a/close! c))
  
  (Thread/sleep 1000))
