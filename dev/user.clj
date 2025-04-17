(ns user
  (:require [walkr.core :as w]
            [criterium.core :refer [report-result
                                    quick-benchmark] :as crit]))

(comment
  (add-tap #'println)
  (remove-tap #'println)
  (tap> "foobar"))
