# Weather Report System – Clojure

A fully functional weather report system written in Clojure. Built as part of a university assignment, this project demonstrates core principles of functional programming, immutability, and data transformation.

## Overview

This command-line application loads weather data from a file and provides options to:
- View weather reports
- Convert temperature units
- Filter reports by condition or temperature range
- Calculate basic weather statistics

All logic is written using pure functional constructs without mutation or loops.

## Features

- **View Reports**: Formatted table of all records (date, location, temp, condition)
- **Transform Reports**: Convert between Celsius and Fahrenheit
- **Filter Reports**:
  - By exact weather condition
  - By temperature range
- **Statistics**:
  - Average temperature
  - Hottest and coldest day
  - Number of unique conditions

## Project Files

- `weather.clj`: Main Clojure source file
- `weather_data.txt`: Input file containing comma-separated weather records

## Sample Input (weather_data.txt)

```
2024-07-01,Montreal,26.5,Sunny
2024-07-02,Ottawa,19.0,Rainy
2024-07-03,Toronto,22.8,Cloudy
```

## Run the Program

Make sure you have Clojure installed, then run:

```bash
clojure weather.clj
```

## Functional Concepts Used

- `map`, `filter`, `reduce`, `apply`
- `assoc`, `set`, `str/split`
- Immutability and recursion
- Separation of concerns with pure functions

## Author

**Marwa Fekri**  
Software Engineering Student, Concordia University  
GitHub: [@Marwaf04](https://github.com/Marwaf04)
