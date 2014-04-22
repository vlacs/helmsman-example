(defproject helmsman-example "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.vlacs/helmsman "0.2.4"]
                 [ring "1.2.2"]]
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]]
                   :source-paths ["dev"]}})
