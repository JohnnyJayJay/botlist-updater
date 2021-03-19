(defproject botlist-updater "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :repositories [["jitpack" "https://jitpack.io"]]
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.apache.logging.log4j/log4j-core "2.13.3"]
                 [org.apache.logging.log4j/log4j-api "2.13.3"]
                 [org.suskalo/discljord "1.2.2"]]

  :repl-options {:init-ns botlist-updater.core}
  :aot :all
  :main botlist-updater.core
  :global-vars {*warn-on-reflection* true})
