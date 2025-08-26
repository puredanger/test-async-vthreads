(ns main
  (:gen-class)
  (:require [clojure.core.async :as a]))

(defn thread-type []
  (.getName (class (Thread/currentThread))))

(defn -main [& args]
  (println "running main")
  (let [c (a/chan 10)]
    (a/>!! c 1)
    (println (str "thread "
      (try
        (a/<! c)
        "parking op worked"
        (catch Throwable _ "parking op throws")) "\n"))

    (a/>!! c 2)
    (a/io-thread (println (str "io-thread " (thread-type) " " 
                               (try (a/<! c) "parking op worked" (catch Throwable _ "parking op throws")) "\n")))

    (a/>!! c :works)
    (a/go (println (str "go " (thread-type) " parking op: " (a/<! c) "\n")))

    (a/close! c))
  
  (Thread/sleep 1000))
