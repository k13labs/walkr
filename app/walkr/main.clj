(ns walkr.main
  (:require [walkr.core :as w]
            [clojure.string :as str])
  (:gen-class))

(defn -main
  "this is a test to compile into native code, to test with graalvm"
  [& args]
  (let [input [{:foo {:bar {:value #{"cOlD"}}
                      :other "hOt"}}
               {:foo {:bar {:value "ColDer"}
                      :other "hoTteR"}}]
        handler (fn [acc item]
                  (if (string? item)
                    [(conj acc (str/lower-case item)) (str/upper-case item)]
                    [acc item]))]
    (println "prewalk-reduce:" (w/prewalk-reduce handler #{} input))
    (println "postwalk-reduce:" (w/postwalk-reduce handler #{} input)))
  (println "hello world"))
