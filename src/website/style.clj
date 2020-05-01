(ns website.style
  (:require [garden.core :as g]
            [garden.stylesheet :as s]
            [garden.selectors :as selectors]))

;; :orange "#ffa329"
;; :lightblue "#a9e2f7"
;; :blue "#02bbee"
;; :code-background "#f4f5f6"

(def clrs
  {:primary "#ffa329"
   :secondary "#02bbee"
   :soft-gray "#ECEFF1"

   :ba 'white;"#cbf3f5"
   :bb 'white;"#b4dcde"
   :bc 'white;"#9dc5c7"
   :bd 'white;"#87aeb0"
             })

(def head-fonts
  (list
   [:h1 :h2 :h3 :h4 :h5 :h6
    {:font-weight 'bold
     :color (:primary clrs)
     :font-family "Roboto Slab"}]))

(def globals
  (list
   [:html {:background-color (:ba clrs)}]

   [:.p-2 {:padding-left "1rem"
           :padding-right "1rem"}]

   [:pre {:padding "0 !important"
          :border-left-style 'none}
    [:code {:margin 0}]]

   [:a {:color "#606c76"}
    [:&:hover {:color (:secondary clrs)}]]

   [:.icon--margin {:margin-left ".5rem"}]

   [:body {:margin 0}]

   [:nav {:position 'fixed}]

   [:.drawer__container
    {:padding-top "6rem"
     :padding-right "1rem"
     ;; :z-index 4
     :margin-bottom 0
     :top 0
     :bottom 0
     :text-align 'right
     :border-right-style 'none
     :background-color 'white
     }]

   [:.drawer__list {:list-style 'none}]

   ;; aside => position absolute
   ;;          max-width 60rem
   [:.content {:padding-top "6rem"
               :padding-bottom "6rem"
               :max-width "60rem"}]))

(def drawer
  (list
   [:#drawer--toggle {:position 'absolute :opacity 0}]
   [:#drawer--overlay {:display 'none}]

   ;; move to the left
   [:#drawer__bars
    {:position 'absolute
     :z-index 4
     :padding-top "2rem"}
    [:i {:font-size "3rem"}]]

   [:.drawer__container
    {:position 'fixed
     :width "20rem"
     ;; :height "100%"
     :left "-20rem"
     :transition ".25s ease-in-out"}]

   [:.content--transition
    {:transition ".25s ease-in-out"}]

   [(selectors/- :#drawer--toggle:checked :.content--transition)
    {:margin-left "20rem"
     ;; :left "0rem"
     }
    [:.content {:opacity ".25"}]]

   [(selectors/- :#drawer--toggle:checked :#drawer--overlay)
    {:display 'block
     :z-index 4
     :position 'absolute
     :left "20rem"
     :right 0
     :top 0
     :bottom 0}]

   [(selectors/- :#drawer--toggle:checked :.drawer__container)
    {:margin-right "-20rem"
     :left "0rem"}]

   ))

(def media-queries
  (list
   (s/at-media
    {:min-width "40rem"}
    [:html {:background-color (:bb clrs)}])

   (s/at-media
    {:min-width "80rem"}
    [:html {:background-color (:bc clrs)}]

    [:.content
     {:margin-left "20rem"}]

    [:.drawer__container
     {
      ;; :position 'inherit
      :border-right-style 'solid
      :left 'unset
      :bottom 'unset
      :padding-bottom "3rem"}]

    [:#drawer__bars {:display 'none}])

   (s/at-media
    {:min-width "120rem"}
    [:html {:background-color (:bd clrs)}])

   ))

(defn gen-css []
  (g/css
   (concat head-fonts
           globals
           drawer
           media-queries)))
