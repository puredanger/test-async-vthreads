(ns build
  (:require [clojure.tools.build.api :as b]))

(def lib 'my/lib1)
(def class-dir "target/classes")

;; delay to defer side effects (artifact downloads)
(def basis (delay (b/create-basis {:project "deps.edn"})))

(defn co [{:keys [vthreads]}]
  (b/delete {:path "target"})
  (let [vopts (if vthreads [(str "-Dclojure.core.async.vthreads=" vthreads)] [])]
    (b/compile-clj {:basis @basis :ns-compile '[main] :class-dir class-dir
                    :java-opts vopts})))
