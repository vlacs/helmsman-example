(ns user
  "Tools for interactive development with the REPL. This file should
  not be included in a production build of the application."
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer (javadoc)]
   [clojure.pprint :refer (pprint)]
   [clojure.reflect :refer (reflect)]
   [clojure.repl :refer (apropos dir doc find-doc pst source)]
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.test :as test]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [ring.adapter.jetty :refer (run-jetty)]
   [helmsman-example]))

(def system
  "A Var containing an object representing the application under
  development."
  nil)

(defn init
  "Creates and initializes the system under development in the Var
  #'system."
  []
  (alter-var-root #'system (constantly {:initialized true
                                      :application helmsman-example/application
                                      :meta-data helmsman-example/meta-data}))
  :initialized)

(defn start
  "Starts the system running, updates the Var #'system."
  []
  (alter-var-root #'system assoc :http {:server :jetty
                                      :instance (run-jetty
                                                  (:application system)
                                                  {:port 8080
                                                   :join? false})})
  :started)

(defn stop
  "Stops the system if it is currently running, updates the Var
  #'system."
  []
  (when (not (nil? system))
    (when (:http system nil)
      (.stop (get-in system [:http :instance]))
      (alter-var-root #'system dissoc :http)))
  :stopped)

(defn go
  "Initializes and starts the system running."
  []
  (init)
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop)
  (refresh :after 'user/go))
