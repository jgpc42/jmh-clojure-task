(ns jmh.main
  "Entry-point that simply delegates to the task runner."
  (:require [jmh.task :as task])
  (:gen-class))

(defn -main [& [arg]]
  (let [arg (case arg
              ("help" "-h" "-help" "--help") ":help"
              arg)]
    (if arg
      (task/main (read-string arg))
      (task/main))))
