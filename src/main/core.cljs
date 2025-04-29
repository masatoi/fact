;; Welcome to Cloudflare Workers! This is your first worker.

;; - Build `npx shadow-cljs release worker`
;; - Run `npm run dev` in your terminal to start a development server
;; - Open a browser tab at http://localhost:8787/ to see your worker in action
;; - Run `npm run deploy` to publish your worker

;; Learn more at https://developers.cloudflare.com/workers/

(ns core)

(defn fact [n]
  (if (= n 0)
    1
    (* n (fact (- n 1)))))

#_
(defn ^:export fetch [request env ctx]
  (js/Promise.resolve
   (js/Response. (str "fact(10)=" (fact 10)))))

#_
(defn ^:export fetch [request env ctx]
  (let [data #js {:fact (fact 10)
                  :request (js-obj
                            "url" (.-url request)
                            "method" (.-method request))
                  :env env
                  :ctx ctx}
        body (js/JSON.stringify data)]
    (js/Promise.resolve
     (js/Response. body
                   #js {:status 200
                        :headers #js {"Content-Type" "application/json"}}))))

(defn respond-json [data]
  (let [body (js/JSON.stringify (clj->js data))]
    (js/Response. body
                  #js {:status 200
                       :headers #js {"Content-Type" "application/json"}})))

(defn ^:export fetch [request env ctx]
  (let [url (new js/URL (.-url request))
        path (.-pathname url)
        method (.-method request)]
    (js/Promise.resolve
     (cond
       ;; GET /fact
       (and (= method "GET") (= path "/fact"))
       (respond-json {:fact (fact 10)})

       ;; GET /ping
       (and (= method "GET") (= path "/ping"))
       (respond-json {:message "pong"})

       ;; POST /echo
       (and (= method "POST") (= path "/echo"))
       (-> (.text request)
           (.then (fn [text]
                    (respond-json {:echo text})))
           (.catch (fn [err]
                     (js/Response. (str "Error: " err)
                                   #js {:status 500}))))

       ;; Not Found
       :else
       (js/Response. "Not Found" #js {:status 404})))))
