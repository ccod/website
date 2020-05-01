(ns website.loader
  (:require [clojure.java.io :as io]
            [clojure.walk :as w]
            [clojure.string :as s]
            [markdown.core :as md]))

(defn file? [f] (and (= java.io.File (type f)) (.isFile f)))

(defn directory? [f] (and (= java.io.File (type f)) (.isDirectory f)))

(defn get-children [dir-path]
  (->> (io/file dir-path)
       (.list)
       (vec)
       (map #(str dir-path "/" %))
       (map io/file)))

(defn get-dir-children [dir]
  (let [root-path (.getPath dir)]
    (->> (.list dir)
         (vec)
         (map #(str root-path "/" %))
         (mapv io/file))))

(defn file-walker [state node]
  (cond (directory? node) (get-dir-children node)
        (vector? node) node
        (file? node) (do (swap! state conj node) nil)))

(defn retrieve-md [k]
  (let [[source destination] [(str "/" (name k) ".md")
                              (str "/" (name k) ".html")]
        f (-> (str "resources" source)
              (slurp)
              (md/md-to-html-string-with-meta))]
    [destination f]))

(defn resource-dir-files [source]
  (let [files (atom [])
        s (str "resources/" (name source))]
    (w/prewalk (partial file-walker files) (io/file s))
    @files))

(defn file-reducer [key coll file]
  (let [src-path (str "resources/" (name key))
        new-path (s/replace (.getPath file) (re-pattern src-path) "")]
    (assoc coll new-path (slurp file))))

(defn get-static-assets []
  (->> (resource-dir-files :static)
       (reduce (partial file-reducer :static) {})))
