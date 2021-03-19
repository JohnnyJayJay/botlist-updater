(ns botlist-updater.core
  (:import (java.util.concurrent Executors TimeUnit ScheduledExecutorService)
           (org.apache.logging.log4j LogManager Logger))
  (:require [clojure.edn :as edn]
            [clojure.pprint :refer [pprint]]
            [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [clojure.core.async :refer [chan <!!]]
            [discljord.messaging :as discord-rest]
            [discljord.connections :as discord-ws]
            [discljord.formatting :refer [user-tag]]
            [discljord.events :refer [message-pump! dispatch-handlers]]))

(defn url [bot-id]
  (str "https://top.gg/api/bots/" bot-id "/stats"))

(def ^Logger logger (LogManager/getLogger "Updater"))

(def ^ScheduledExecutorService scheduler (Executors/newSingleThreadScheduledExecutor))

(def guilds (atom #{}))

(defn post-update! [dbl-token bot-id]
  (let [guild-count (count @guilds)]
    (.info logger ^CharSequence "Updating DBL stats, current guild count is {}" ^Object guild-count)
    (let [{:keys [status] :as response}
          @(http/post (url bot-id)
                {:headers {"Content-Type" "application/json"
                           "Authorization" dbl-token}

                 :body (json/write-str {:server_count guild-count})})]
      (when (not= status 200)
        (.error logger "Failed with status {}" status)
        (binding [*out* *err*]
          (pprint response))))))

(def scheduled (atom false))

(defn -main [& args]
  (let [{:keys [discord-token dbl-token period]} (edn/read-string (slurp "config.edn"))
        discord-rest-channel (discord-rest/start-connection! discord-token)
        event-channel (chan 100)
        discord-ws-channel (discord-ws/connect-bot! discord-token event-channel :intents #{:guilds})
        {bot-id :id :as bot} @(discord-rest/get-current-user! discord-rest-channel)]
    (.info logger "Logged in to Discord as {} ({})" (user-tag bot) bot-id)
    (letfn [(add-guild [_ {guild-id :id}]
              (swap! guilds conj guild-id))
            (start-schedule [_ _]
              (when-not @scheduled
                (.info logger "Beginning to post stats every {} minutes..." period)
                (.scheduleAtFixedRate scheduler (partial post-update! dbl-token bot-id) period period TimeUnit/MINUTES)
                (reset! scheduled true)))]
      (message-pump! event-channel (partial dispatch-handlers {:guild-create [add-guild] :ready [start-schedule]})))))

