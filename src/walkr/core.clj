(ns walkr.core
  (:import [clojure.lang IMapEntry MapEntry IRecord]))

(defn- reduce-form
  [build-fn inner outer acc form]
  (let [[acc vec-form] (reduce (fn walk
                                 [[acc-in form-in] item-in]
                                 (let [[acc-out item-out] (inner acc-in item-in)
                                       form-out (conj form-in item-out)]
                                   [acc-out form-out]))
                               [acc []]
                               form)]
    (outer acc (with-meta
                 (build-fn vec-form)
                 (meta form)))))

(defn walk-reduce
  "Traverses form, an arbitrary data structure.  inner and outer are
  2 arity functions, returning a vector of [acc form] . 
  Applies inner to acc and each element of form, building up a data
  structure of the same type, then applies outer to acc and the result.
  Recognizes all Clojure data structures. Consumes seqs as with doall."
  [inner outer acc form]
  (cond
    (list? form)
    (reduce-form (partial apply list) inner outer acc form)

    (instance? IMapEntry form)
    (let [[acc key] (inner acc (key form))
          [acc val] (inner acc (val form))]
      (outer acc (MapEntry/create key val)))

    (seq? form)
    (reduce-form seq inner outer acc (doall form))

    (instance? IRecord form)
    (reduce-form (partial into form) inner outer acc form)

    (coll? form)
    (reduce-form (partial into (empty form)) inner outer acc form)

    :else
    (outer acc form)))

(defn postwalk-reduce
  "Performs a depth-first, post-order traversal of form.
  Calls f with acc on each sub-form, f should return a vector of [new-acc new-form].
  Uses f's return acc and form in place of the original with each call.
  Recognizes all Clojure data structures. Consumes seqs as with doall."
  [f acc form]
  (walk-reduce (partial postwalk-reduce f) f acc form))

(defn prewalk-reduce
  "Like postwalk-reduce, but does pre-order traversal."
  [f acc form]
  (let [[acc form] (f acc form)]
    (walk-reduce (partial prewalk-reduce f) vector acc form)))
