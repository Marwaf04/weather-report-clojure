(ns weather
  (:require [clojure.string :as str]))

(declare main-menu handle-choice)

(defn parse-line
  "Parses a comma-separated line into a weather report map."
  [line]
  (let [[date location temp condition] (str/split line #",")]
    {:date date
     :location location
     :temperature (Double/parseDouble temp)
     :condition condition}))

(defn load-reports
  "Loads weather reports from a file."
  [filename]
  (if (.exists (java.io.File. filename))
    (->> (slurp filename)
         str/split-lines
         (map parse-line)
         vec)
    []))

(defn save-reports
  "Saving is optional and currently not implemented."
  [reports]
  (println "(Saving not implemented.)"))

(defn view-weather-reports [reports]
  (println (str "\nTotal Weather Reports: " (count reports)))
  (println)
  (println (format "Date       | Location    | Temp (%s) | Condition"
                   (or (:unit (first reports)) "C")))
  (println "-----------|-------------|----------|-----------")
  (doseq [r reports]
    (println (format "%-11s| %-12s| %9.1f | %s"
                     (:date r)
                     (:location r)
                     (:temperature r)
                     (:condition r)))))

(defn c->f [report]
  (assoc report :temperature (+ (* 1.8 (:temperature report)) 32)
         :unit "F"))

(defn f->c [report]
  (assoc report :temperature (* (- (:temperature report) 32) (/ 5.0 9))
         :unit "C"))

(defn transform-weather-reports [reports]
  (println "\nChoose temperature transformation:")
  (println "1. Celsius to Fahrenheit")
  (println "2. Fahrenheit to Celsius")
  (print "Your choice: ")
  (flush)
  (let [choice (read-line)]
    (cond
      (= choice "1") (do (println "Temperatures converted to Fahrenheit.")
                         (vec (map c->f reports)))
      (= choice "2") (do (println "Temperatures converted to Celsius.")
                         (vec (map f->c reports)))
      :else (do (println "Invalid choice.") reports))))

(defn filter-by-condition [reports]
  (print "Enter condition (e.g. Sunny): ")
  (flush)
  (let [condition (read-line)
        result (filter #(= (:condition %) condition) reports)]
    (if (empty? result)
      (println "No matching reports.")
      (view-weather-reports (vec result)))))

(defn filter-by-range [reports]
  (print "Enter min temp: ")
  (flush)
  (let [min-temp (Double/parseDouble (read-line))]
    (print "Enter max temp: ")
    (flush)
    (let [max-temp (Double/parseDouble (read-line))
          result (filter #(and (>= (:temperature %) min-temp)
                               (<= (:temperature %) max-temp)) reports)]
      (if (empty? result)
        (println "No reports in that range.")
        (view-weather-reports (vec result))))))

(defn filter-weather-reports [reports]
  (println "\nFilter Options:")
  (println "1. Filter by Condition")
  (println "2. Filter by Temperature Range")
  (print "Your choice: ")
  (flush)
  (let [choice (read-line)]
    (cond
      (= choice "1") (filter-by-condition reports)
      (= choice "2") (filter-by-range reports)
      :else (println "Invalid choice."))))

(defn weather-statistics [reports]
  (println "\n===== Weather Statistics =====")
  (let [temps (map :temperature reports)
        avg (/ (reduce + temps) (count temps))]
    (println (format "Average Temperature: %.2f" avg)))
  (let [hottest (apply max-key :temperature reports)]
    (println "\nHottest Day:")
    (println (format "%s | %s | %.1f | %s"
                     (:date hottest) (:location hottest) (:temperature hottest) (:condition hottest))))
  (let [coldest (apply min-key :temperature reports)]
    (println "\nColdest Day:")
    (println (format "%s | %s | %.1f | %s"
                     (:date coldest) (:location coldest) (:temperature coldest) (:condition coldest))))
  (let [conditions (set (map :condition reports))]
    (println (str "\nUnique Conditions: " (count conditions) " → " (str/join ", " conditions)))))

(defn exit-program []
  (println "\nThank you for using the Weather Report System. Goodbye!")
  (System/exit 0))

(defn main-menu
  ([file]
   (main-menu file (load-reports file)))
  ([file reports]
   (println "\n=== Weather Report System ===")
   (println "1. View Weather Reports")
   (println "2. Transform Weather Report")
   (println "3. Filter Weather Reports")
   (println "4. Weather Statistics")
   (println "5. Save and Exit")
   (print "Enter your choice (1-5): ")
   (flush)
   (let [choice (read-line)]
     (handle-choice choice reports file))))

(defn handle-choice [choice reports file]
  (case choice
    "1" (do (view-weather-reports reports)
            (main-menu file reports))
    "2" (let [updated (transform-weather-reports reports)]
          (main-menu file updated))
    "3" (do (filter-weather-reports reports)
            (main-menu file reports))
    "4" (do (weather-statistics reports)
            (main-menu file reports))
    "5" (exit-program)
    (do (println "Invalid option. Try again.")
        (main-menu file reports))))

(defn -main [& args]
  (let [file "weather_data.txt"]
    (main-menu file)))

(-main)
