(ns exercism-generator.core
  (:require [camel-snake-kebab.core :refer [->camelCaseString
                                            ->kebab-case-keyword
                                            ->kebab-case-symbol]]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [eftest.runner :refer [run-tests]]
            [jsonista.core :as j]
            [zprint.core :as zp]))

(def mapper
  (j/object-mapper
   {:decode-key-fn ->kebab-case-keyword,
    :encode-key-fn ->camelCaseString}))


(defn gen-test [{:keys [description property expected input]}]
  (let [fname (symbol property)
        test-name (->kebab-case-symbol description)
        args (->> input (sort-by key) (map val))]
    (list 'deftest test-name
          (list 'testing description
                (list 'is
                      (if (and (map? expected) (contains? expected :error))
                        (list 'thrown? 'Throwable (cons fname args))
                        (list '= expected (cons fname args))))))))

(defn wrap-ns [test-ns-name ns-name fnames & tests]
  (cons (list 'ns test-ns-name
              (list :require
                    ['clojure.test :refer ['deftest 'is 'testing]]
                    [ns-name :refer (vec fnames)]))
        tests))

(defn print-test-forms [& forms]
  (->>
   forms
   (map #(zp/zprint-str % {:fn-map {"testing" :arg1-force-nl}}))
   (string/join "\n\n")))

(defn anayze-input [input-file]
  (let [{test-cases :cases exercise-name :exercise} (j/read-value (slurp input-file) mapper)]
    (when-let [fnames (->> test-cases
                           (map (comp symbol :property))
                           set
                           not-empty)]
      (let [ns-name (symbol exercise-name)
            test-ns-name (symbol (str exercise-name "-test"))]
        {:ns-name ns-name
         :test-ns-name test-ns-name
         :fnames fnames
         :test-cases test-cases}))))

(defn test-cases-forms [input-file]
  (let [{:keys [ns-name test-ns-name fnames test-cases]} (anayze-input input-file)]
    (apply wrap-ns test-ns-name ns-name fnames
           (map gen-test test-cases))))

(defn format-test-cases [input-file]
  (apply print-test-forms (test-cases-forms input-file)))


(defn eval-tests [input]
  (filter var?
          (map eval (concat (test-cases-forms input)
                            (list (list 'in-ns (list 'quote (ns-name *ns*))))))))

(defn run-tests-with-example [input example]
  (load-file example)
  (run-tests
    (eval-tests (io/file input))
    {:report eftest.report.pretty/report}))

(defn write-tests-to-output [input output]
  (spit (io/file output) (-> input io/file format-test-cases)))
