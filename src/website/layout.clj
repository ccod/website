(ns website.layout
  (:require [hiccup.core :as h]
            [hiccup.page :as p]))

(defn head [options]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   ;; option title, posts and the like
   [:link {:rel "stylesheet" :href "//fonts.googleapis.com/css?family=Roboto:300,300italic,700,700italic"}]
   [:link {:rel "stylesheet" :href "//fonts.googleapis.com/css?family=Roboto+Slab:300,300italic,700,700italic"}]
   [:link {:rel "stylesheet" :href "//cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css"}]
   [:link {:rel "stylesheet" :href "//cdnjs.cloudflare.com/ajax/libs/normalize/5.0.0/normalize.css"}]
   [:link {:rel "stylesheet" :href "/css/milligram.min.css"}]
   [:link {:rel "stylesheet" :href "/css/prism.css"}]
   [:link {:rel "stylesheet" :href "/css/main.css"}]])

(def drawer
  [:aside.drawer__container 
   [:a {:href "/index.html" } [:h4 [:b "Flailing Forward"]]]

   [:ul.drawer__list
    [:li [:a {:href "/about.html"} "About"]]
    [:li [:a {:href "/projects.html"} "Projects"]]]
   
   [:a {:href "mailto:chcodrington@gmail.com"}
    [:i.fas.fa-envelope-square.fa-2x]]
   [:a {:href "https://github.com/ccod"}
    [:i.fab.fa-github-square.fa-2x.icon--margin]]
   [:a {:href "https://www.linkedin.com/in/christopher-codrington/"}
    [:i.fab.fa-linkedin.fa-2x.icon--margin]]
   [:a {:href "https://www.instagram.com/ccod_paints/"}
    [:i.fab.fa-instagram-square.fa-2x.icon--margin]]])

(defn default-layout [content]
  (p/html5
   {:lang "en"}
   (head {})
   [:body

    [:div.container
     [:div.row
      [:input#drawer--toggle {:type "checkbox" :name "drawer--toggle"}]

      drawer

      [:div.column.content--transition
       [:label#drawer__bars {:for "drawer--toggle"}
        [:i.fa.fa-bars]]

       [:div.content content]]

      [:label#drawer--overlay {:for "drawer--toggle"}]]]
    [:script {:src "/js/prism.js"}]]))


(defn post-content [{:keys [metadata html]}]
  (list
   html
   [:div
    (:Author metadata)
    (:Date metadata)]))

(defn post-page [x]
  (-> x
      post-content
      default-layout))

(defn item-description [metadata]
  [:div
   [:a {:href (:uri metadata)}
    [:h4 (:Title metadata)]]
   [:div (:Author metadata)]
   [:div (:Date metadata)]])

(defn paginated-page [{:keys [item pagination]}]
  (list
   [:div
    (map item-description item)]

   [:hr]
   [:div.row
    [:div.column.column-25
     (when (:previous pagination)
       [:a.float-right {:href (:previous pagination)}
        [:b "previous" [:i.fas.fa-long-arrow-alt-left.fa-lg.p-2]]])]
    [:div.column.column-25.column-offset-50
     (when (:next pagination)
       [:a.float-left {:href (:next pagination)}
        [:i.fas.fa-long-arrow-alt-right.fa-lg.p-2] [:b  "next"]])]]))

(defn post-index [x]
  (-> x
      paginated-page
      default-layout))

