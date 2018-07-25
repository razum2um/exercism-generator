(defproject exercism-generator "0.1.0-SNAPSHOT"
  :description "Generate projects from https://github.com/exercism/problem-specifications"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [metosin/jsonista "0.2.1"]
                 [camel-snake-kebab "0.4.0"]
                 [zprint "0.4.9"] [eftest "0.5.2"]]
  :repl-options {:init-ns exercism-generator.core})
