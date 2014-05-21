(ns wacnet.views.common
  (:require [hiccup.core :as h]
            [hiccup.page :as hp]
            [hiccup.element :as he]
            [hiccup.form :as hf]
            [wacnet.ring-utils :refer [*url*]]
            [wacnet.views.templates :as tp]
            ))


(defn menu-item [link content]
  [:li {:class (when (re-find (re-pattern (str "^" link)) *url*) "active")} (he/link-to link content)])


;; perhaps the services could be added as a plugin? This way Wacnet
;; could remain somewhat 'neutral'.
(def services
   [:ul.dropdown-menu
    [:li (he/link-to "/services/logger" "Data logging")]])


(defn navbar-header []
  [:div.navbar-header
   [:button.navbar-toggle {:type "button" :data-toggle "collapse" :data-target "#main-navbar"}
    [:span.sr-only "Toggle navigation"]
    (repeat 3 [:span.icon-bar])]
   [:a.navbar-brand {:href "/"} "Wacnet"]])

  
(defn navbar-content []
  [:div.collapse.navbar-collapse {:id "main-navbar"}
   [:ul.nav.navbar-nav
       (menu-item "/explorer" "Explorer")
       (menu-item "/configs" "Configs")
       (menu-item "/repl" "REPL")
    [:li [:a [:i.fa.fa-globe]]]]
   [:ul.nav.navbar-nav.navbar-right]])

(defn menu []
  [:div.nav.navbar-inverse.navbar-fixed-top {:role "navigation"}
   [:div.container
    (navbar-header)
    (navbar-content)]])


(defn with-sidebar [{:keys [header sidebar body]}]
  `[:div.container-fluid
    [:div.row-fluid
     [:div.span3
      [:div.well.sidebar-nav
       [:ul.nav.nav-list
        [:li.nav-header ~header]
        ~@sidebar]]]
     [:div.span9 ~body]]])

(def style
  "  body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }

      @media (max-width: 980px) {
        /* Enable use of floated navbar text */
        .navbar-text.pull-right {
          float: none;
          padding-left: 5px;
          padding-right: 5px;
        }
      }")

(defn layout [& content]
  (hp/html5 {:lang "en"}
            [:head
             [:link {:rel "shortcut icon" :href "/img/favicon.png"}]
             [:style style]
             [:title "Wacnet - BACnet network explorer"]
             (hp/include-css "/css/bootstrap.min.css")
             (hp/include-css "/css/bootstrap-responsive.min.css")
             (hp/include-js "/js/jquery-1.11.0.min.js")
             (hp/include-js "/js/bootstrap.min.js")
             (hp/include-js "js/cljs.js" 
                            ;"js/cljs-min.js"
                            )
             ]
            [:body
             (menu)
             content]))