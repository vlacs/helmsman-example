(ns helmsman-example
  (:require [helmsman :as h]
            [helmsman.navigation :as nav]
            [helmsman.uri :as uri]))

(defn basic-html-doc
  [body]
  (str "<!doctype html><html><body>"
       body "</body></html>"))

(defn home-page-body
  [request]
  (let [add-uri (nav/id->uri request ::add
                             :one 4 :two 8)
        sub-uri (nav/id->uri request ::subtract
                             :one 3 :two 13)
        mult-uri (nav/id->uri request ::multiply
                              :one 23 :two 7)]
    (str "<h1>Welcome to the Helmsman Example!</h1>
         <ul>
         <li><a href=\"" add-uri "\">4 + 8</a></li>
         <li><a href=\"" sub-uri "\">3 - 13</a></li>
         <li><a href=\"" mult-uri "\">23 * 7</a></li>
         </ul>")))

(defn home-page
  [request]
  (basic-html-doc "<h1>Hello world!</h1>"))

(h/defhandler home-page
  [:as request]
  (home-page-body
    request))

(defn punt [n] (Integer/parseInt n))

(defn return-home
  [request]
  (str "<a href=\""
       (nav/id->uri request ::home)
       "\">Go Home</a>"))

(h/defhandler add-page
  [one two :as request]
  (str "Result: " (+ (punt one) (punt two))
       "<br />"
       (home-page-body request)
       (return-home request)))

(h/defhandler subtract-page
  [one two :as request]
  (str "Result: " (- (punt one) (punt two))
       "<br />"
       (home-page-body request)
  (return-home request)))
  
(h/defhandler multiply-page
  [one two :as request]
  (str "Result: " (* (punt one) (punt two))
       "<br />"
       (home-page-body request)
       (return-home request)))

(def our-routes
  [^{:id ::home}
   [:get "/" home-page]
   [:context "math"
    ^{:id ::add}
    [:get "add/:one/:two" add-page]
    ^{:id ::subtract}
    [:get "subtract/:one/:two" subtract-page]
    ^{:id ::multiply}
    [:get "multiply/:one/:two" multiply-page]]])

(def application (h/compile-routes our-routes))
(def meta-data (h/compile-meta our-routes))
