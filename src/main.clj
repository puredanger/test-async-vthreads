(ns main
  (:gen-class)
  (:require [clojure.core.async :as a]))

(defn thread-type []
  (.getName (class (Thread/currentThread))))

(defn -main [& args]
  (println "running main")

  (a/go (println (str "go " (thread-type) "\n")))

  (a/io-thread (println (str "io-thread " (thread-type) "\n")))
  
  (Thread/sleep 1000))
