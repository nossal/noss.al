(ns nossal.data
  (:require #?(:clj [environ.core :refer [env]])
            [clojure.string :as str])
  #?(:cljs (:require-macros [nossal.data :refer [ga-tracking-id]])))



#?(:clj (defmacro ga-tracking-id [] (env :ga-tracking-id)))


(def allowed-image-sizes #{16 32 48 72 76 96 120 144 150 152 180 192 196 512 1024})


(def pwa-manifest
    {:name "Nossal, Rodrigo Nossal"
     :short_name "NOSSAL"
     :description "Rodrigo Nossal Personal Website"
     :display "fullscreen"
     :theme_color "#747f90"
     :background_color "#747f90"
     :orientation "portrait"
     :start_url "https://noss.al/#utm_source=web_app_manifest"
     :icons (map (fn [s]
                   {:src (str "image/icon-" s ".png") :sizes (str s "x" s) :type "image/png"})
                 (keep #(if (> % 32) %) allowed-image-sizes))})

(def public-profiles {"Facebook"   "https://facebook.com/nossal"
                      "Twitter"    "https://twitter.com/nossal"
                      "Instagram"  "https://instagram.com/nossal"
                      "GitHub"     "https://github.com/nossal"
                      "linkedin"   "https://linkedin.com/in/nossal"
                      "Spotify"    "https://open.spotify.com/user/nossal"
                      "SoundCloud" "https://soundcloud.com/nossal"
                      "Last.fm"    "https://last.fm/user/nossal"
                      "Medium"     "https://medium.com/@nossal"
                      "Flickr"     "https://flickr.com/photos/nossal"
                      "SlideShare" "https://slideshare.net/nossal"
                      "Google+"    "https://google.com/+RodrigoNossal"
                      "Ello"       "https://ello.co/nossal"
                      "About.me"   "https://about.me/nossal"
                      "myspace"    "https://myspace.com/nossal"
                      "Pinterest"  "https://pinterest.com/thenossal"})

(def data-person {"@context" "https://schema.org"
                  "@type" "Person"
                  :name "Rodrigo Nossal"
                  :url "https://noss.al"
                  :sameAs (vals public-profiles)})

(def data-website {"@context" "https://schema.org"
                   "@type" "WebSite"
                   :name "Nossal's"
                   :alternateName "Rodrigo Nossal"
                   :url "https://noss.al"})

(def data-analytics
  {:vars {:gtag_id (ga-tracking-id)}
   (ga-tracking-id) {:config {:groups "default"}}
   :triggers {:trackPageview {:on "visible" :request "pageview"}
              :outboundLinks {:on "click"
                              :selector "a.out"
                              :request "event"
                              :vars {:eventCategory "outbound"
                                     :eventLabel "${outboundLink}"
                                     :eventAction "click"}}
              :trackClickOnCoupom {:on "click"
                                   :selector "#get-coupom"
                                   :request "event"
                                   :vars {:eventCategory "ui-components"
                                          :eventAction "get-coupom"}}
              :trackClickOnPI {:on "click"
                               :selector "a#tnet"
                               :request "event"
                               :vars {:eventCategory "ui-components"
                                      :eventAction "the-net"}}

              :trackPWAInstall {:on "beforeinstallprompt"
                                :request "event"
                                :vars {:eventCategory "ui-components"
                                       :eventAction "PWA Install"}}}})

#?(:clj (defn breadcrumbs [path]
          {"@context" "http://schema.org"
           "@type" "BreadcrumbList"
           "itemListElement" (map-indexed (fn [idx item]
                                           {"@type" "ListItem"
                                            "position" (+ idx 1)
                                            "name" item
                                            "item" (str "https://noss.al/" (subs path 0 (str/index-of path item)) item)})
                                          (str/split path #"\/"))}))
