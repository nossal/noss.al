(ns nossal.shop.data
  (:require [clojure.spec.alpha :as s]
            [re-frame.core :refer [reg-event-db reg-event-fx inject-cofx path after]]))

(s/def ::category string?)

(s/def ::id int?)
(s/def ::quantity (s/and int? #(> % 0)))
(s/def ::price (s/and float? #(> % 0)))
(s/def ::market-price (s/and float? #(> % 0)))
(s/def ::title string?)
(s/def ::description string?)
(s/def ::stock-count (s/and int? #(>= % 0)))
(s/def ::available? boolean?)
(s/def ::images (s/coll-of uri? :kind vector? :distinct true :into []))
(s/def ::product (s/keys :req-un [::id
                                  ::market-price
                                  ::price
                                  ::title
                                  ::description
                                  ::images
                                  ::category
                                  ::stock-count
                                  ::available?]))
(s/def ::products (s/and
                    (s/map-of ::id ::product)
                    #(instance? PersistentTreeMap %)))

(s/def ::basket-item (s/keys :req-un [::product ::quantity]))
(s/def ::basket-itens (s/coll-of ::basket-item :kind vector? :distinct true :into []))
(s/def ::basket (s/keys :req-un [::id ::basket-itens]))

(s/def ::db (s/keys :req-un [::products ::basket]))

(def products-db
  [{:code 1
    :name "TS100 Soldering Iron"
    :price 89.87
    :descr ""
    :link "https://www.banggood.com/MINI-TS100-Digital-OLED-Programmable-Interface-DC-5525-Soldering-Iron-Station-Built-in-STM32-Chip-p-984214.html"
    :partner "banggood"
    :images ["https://img.staticbg.com/thumb/large/oaupload/banggood/images/88/63/264c0b48-0560-46b2-9213-c357c90b5815.jpg"
             "https://img.staticbg.com/thumb/view/upload/2015/12/552/38.jpg"
             "https://img.staticbg.com/thumb/view/upload/2015/12/SKU252598/15.jpg"
             "https://img.staticbg.com/thumb/view/oaupload/banggood/images/7B/44/6cf50167-7cd1-480c-9848-af37daffcfce.jpg"]}
   {:code 15
    :name "Roborock S50 Smart Robot"
    :price 2800.89
    :descr ""
    :link "https://www.banggood.com/MINI-TS100-Digital-OLED-Programmable-Interface-DC-5525-Soldering-Iron-Station-Built-in-STM32-Chip-p-984214.html"
    :partner "banggood"
    :images ["https://img.staticbg.com/thumb/large/oaupload/banggood/images/7F/9A/f6bcda05-e8d8-46a0-a051-9b02dfd6ed96.gif"]}
   {:code 16
    :name "Stainless Steel Mini Pocket Folding Knife"
    :price 37.69
    :descr ""
    :link "https://www.banggood.com/MINI-TS100-Digital-OLED-Programmable-Interface-DC-5525-Soldering-Iron-Station-Built-in-STM32-Chip-p-984214.html"
    :partner "banggood"
    :images ["https://img.staticbg.com/thumb/large/oaupload/banggood/images/4D/6A/311f446a-bca0-4d12-853d-1b95d517d019.jpg.webp"]}
   {:code 17
    :name "Hantek DSO5072P"
    :price 850.89
    :descr ""
    :link "https://www.banggood.com/MINI-TS100-Digital-OLED-Programmable-Interface-DC-5525-Soldering-Iron-Station-Built-in-STM32-Chip-p-984214.html"
    :partner "banggood"
    :images ["https://img.staticbg.com/thumb/large/oaupload/banggood/images/18/97/55a957e6-ad5d-411c-a20b-2088279c13f9.jpg.webp"]}])


(defn check-and-throw
  "Throws an exception if `db` doesn't match the Spec `a-spec`."
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info (str "spec check failed: " (s/explain-str a-spec db)) {}))))

(def check-spec-interceptor (after (partial check-and-throw :nossal.data/db)))