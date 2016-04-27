(ns wacnet.js-util
  (:require [goog.Timer :as timer]            
            [clojure.string :as s]))



(defn evt-add-class [event class]
  (.add (.-classList (.-target event)) class))

(defn evt-remove-class [event class]
  (.remove (.-classList (.-target event)) class))

(defn evt-set-attr [event attr attr-value]
  (.setAttribute (.-target event) attr attr-value))


(defn debounce-factory
  "Return a function that will always store a future call into the
  same atom. If recalled before the time is elapsed, the call is
  replaced without being executed." []
  (let [f (atom nil)]
    (fn [func ttime]
      (when @f
        (timer/clear @f))
      (reset! f (timer/callOnce func ttime)))))


(defn- where*
  [criteria not-found-result]
    (fn [m]
      (every? (fn [[k v]]
                (let [tested-value (get m k :not-found)]
                  (cond
                   (= tested-value :not-found) not-found-result
;;                    (and (fn? v) tested-value) (try (v tested-value)
;;                                                    (catch Exception e))
                   ;; catch exception if there's an error on the provided testing function
                   ;; (for example, if we use '>' on a string)
                   (number? tested-value) (== tested-value v)
                   (and (regexp? v)
                        (string? tested-value)) (re-find v tested-value)
                   (= tested-value v) :pass))) criteria)))

(defn where
  "Will test with criteria map as a predicate. If the value of a
  key-val pair is a function, use it as a predicate. If the tested map
  value is not found, fail.

  For example:
  Criteria map:  {:a #(> % 10) :b \"foo\"}
  Tested-maps  {:a 20 :b \"foo\"}  success
               {:b \"foo\"}        fail
               {:a nil :b \"foo\"} fail"
  [criteria]
  (where* criteria false))

(defn make-fuzzy-regex [s]
  (->> (for [splitted (s/split s #"\.")]
         (->> (map #(str ".*" %) splitted)
              (apply str)))
       (s/join "\\.")
       (#(str "(?i)" % ".*"))))


(defn is-embed?
  "Check if we are currently in an iframe." []
  (not= js/window js/window.parent))
