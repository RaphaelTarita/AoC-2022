![](https://img.shields.io/badge/day%20📅-4-yellow)
![](https://img.shields.io/badge/stars%20⭐-6-blue)
![](https://img.shields.io/badge/days%20completed-3-green)

# AoC-2022

My solutions for the Advent of Code 2022 (in Kotlin). Note that the vast majority of the scaffolding and util code was
taken from my [AoC-2021 repository](https://github.com/RaphaelTarita/AoC-2021) from last year.

### Contents:

- input: All input files, named as `day${day-of-month}.txt`
- output: Output files containing results (and possibly benchmarks) for the different days. Named
  either `day${day-of-month}.txt` for a single day or `days.txt`for all outputs in one file
- src/main/kotlin: Kotlin source root
    - com.rtarita: packagae root
        - days: package containing all AoC day solutions, beginning with a (fallback) Day 0 and going to Day 25.
          Named `Day${day-of-month}.kt`
        - structure: package containing the infrastructure which defines how AoC Day challenges are defined, executed,
          benchmarked, outputted and printed to files
            - fmt: utility classes helping me to format the outputs of AoC challenges
        - util: package for `.kt` files which contain top-level / extension utilities
            - ds: data structures
                - graph: A graph data structure implemented based on adjacency lists
                - heap: A heap data structure implemented based on an array
            - `bitops.kt`: utilities related to bitwise operations or byte-level conversions
            - `collections.kt`: various extensions related to kotlin standard collections
            - `comparisons.kt`: certain utilites concerning `Comparable`s and `Comparator`s
            - `generic.kt`: control flow or generic utilities
            - `internal.kt`: internal utilites that are useful for maintaining the infrastructure
            - `io.kt`: utilities for reading from / writing to files, or working with paths and files in general
            - `maths.kt`: mathematical utilities, mostly definitions of stdlib functions for other numeric datatypes
            - `strings.kt`: utilities related to strings and string manipulation
        - `main.kt`: contains the main function which will execute the current AoC day challenge
- other top-level files: build files, license, etc.