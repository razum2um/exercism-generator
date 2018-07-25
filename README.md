# Exercism Generator

Take [exercism test specifications](https://github.com/exercism/problem-specifications) ad
generate project's test file automatically

## Usage

```clojure
(def input   "../../problem-specifications/exercises/series/canonical-data.json")
(def output  "../../clojure-track/exercises/series/test/series_test.clj")
(def example "../../clojure-track/exercises/series/src/example.clj")

(require '[exercism-generator.core :refer :all])

(->> input test-cases-forms (take 2) (apply print-test-forms) println)
;;=> (ns series-test
;;=>   (:require [clojure.test :refer [deftest is testing]]
;;=>             [series :refer [slices]]))
;;=>
;;=> (deftest slices-of-one-from-one
;;=>   (testing "slices of one from one"
;;=>     (is (= ["1"] (slices "1" 1)))))

(run-tests-with-example input example)
;;=> Ran 10 tests in 0.038 seconds
;;=> 10 assertions, 0 failures, 0 errors.
;;=> {:test 10, :pass 10, :fail 0, :error 0, :type :summary, :duration 38.478862}

(write-tests-to-output input output)
(-> output slurp println)
;;=> (ns series-test
;;=>   (:require [clojure.test :refer [deftest is testing]]
;;=>             [series :refer [slices]]))
;;=> 
;;=> (deftest slices-of-one-from-one
;;=>   (testing "slices of one from one"
;;=>     (is (= ["1"] (slices "1" 1)))))
```

## License

Copyright © 2018 Vlad Bokov

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
