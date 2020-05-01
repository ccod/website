(defproject website "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [stasis "2.5.0"]
                 [hiccup "1.0.5"]
                 [markdown-clj "1.10.2"]
                 [garden "1.3.9"]
                 [clj-time "0.15.2"]
                 [ring "1.8.0"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler website.core/app 
         ;; :reload-paths ["resources/posts" "src"]
         ;; :auto-refresh? true
         }
  :aliases {"build" ["run" "-m" "website.core/export"]}
  :repl-options {:init-ns website.core})
