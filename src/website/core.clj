(ns website.core
  (:require [stasis.core :as stasis]
            [website.loader :as loader]
            [website.layout :as layout]
            [website.style :refer [gen-css]]

            [clj-time.format :as f]
            [clj-time.core :as t]
            [markdown.core :as md]
            [clojure.string :as s]
            ))

(defonce static (loader/get-static-assets))

(defn get-posts []
  (loader/resource-dir-files :posts))

(defn parse-date [s]
  (f/parse (f/formatter "yyyy-MM-dd") s))

(defn parse-post [file]
  (let [path-name (->> (.getName file)
             (drop-last 2)
             (apply str)
             (#(str "/post/" % "html")))
        post-data (md/md-to-html-string-with-meta (slurp file))]
    (assoc-in post-data [:metadata :uri] path-name)))

(defn add-post-page [post]
  (-> post
      (assoc :page (layout/post-page post))
      (dissoc :html)))

(defn exportable-post [post]
  {(get-in post [:metadata :uri]) (:page post)})

(defn paginate-by [p r]
  (loop [dest [] coll r]
    (if (< (count coll) p)
      (if (= (count coll) 0)
        dest
        (conj dest coll))
      (recur (conj dest (vec (take p coll))) (drop p coll)))))

(defn paginator [count idx item]
  (let [previous (cond (= idx 0) nil
                       (= idx 1) :special
                       :else idx)
        next     (cond (= idx (dec count)) nil
                       :else (+ idx 2))]
    {:item item
     :pagination {:previous previous
                  :current (if (= idx 0) :special (inc idx))
                  :next next}}))

(defn post-pagination [x]
  (cond (nil? x) nil
        (= :special x) "/index.html"
        :else (str "/posts/page/" x ".html")))

(defn configure-links [f coll]
  (let [updated-pagination
        (-> coll
            (update-in [:pagination :next] f)
            (update-in [:pagination :previous] f))
        current (get-in coll [:pagination :current])]
    [(f current) updated-pagination]))

(defn gen-post-pagination [coll]
  (let [n (count coll)]
    (->> coll
         (map-indexed (partial paginator n))
         (map (partial configure-links post-pagination)))))

(defn generate-post-index-page [[k v]]
  {k (layout/post-index v)})

(defn date-sort [meta-coll]
  (->> meta-coll
       (map #(update % :Date parse-date))
       (sort-by #(:Date %)
                #(compare %1 %2))))

(defn post-collection []
  (let [post-data   (->> (get-posts)
                         (map parse-post)
                         (map add-post-page)) 

        pagination-pages (->> post-data
                              (map :metadata)
                              (date-sort)
                              (paginate-by 1)
                              (gen-post-pagination)
                              (map generate-post-index-page))]

    (apply merge
           (concat
            pagination-pages
            (map exportable-post post-data)))))

(defn convert-default-page [k]
  (let [[path page-data] (loader/retrieve-md k)]
    {path (layout/default-layout (:html page-data))}))

(defn get-pages []
  (let [about (convert-default-page :about)
        projects (convert-default-page :projects)]
    (merge (post-collection)
           about
           projects
           {"/css/main.css" (gen-css)}
           static)))

(def app (stasis/serve-pages get-pages))

(defn export []
  (stasis/export-pages
   (merge (post-collection)
          {"/css/main.css" (gen-css)}
          static) "public"))
