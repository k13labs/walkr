(ns walkr.main
  (:require [walkr.core :as w])
  (:gen-class))

(defn -main
  "this is a test to compile into native code, to test with graalvm"
  [& args]
  (println "hello world"))
