// https://adventofcode.com/2022/day/6

import java.io.File

fun input() = File("input/aoc-6.txt").readLines()

// Part 1.

fun p1(input: List<String>): Int {
    input.first().windowed(4).forEachIndexed { i, s ->
        if (s.toSet().size == 4) return i + 4
    }
    error("oops")
}

// Part 2.

fun p2(input: List<String>): Int {
    input.first().windowed(14).forEachIndexed { i, s ->
        if (s.toSet().size == 14) return i + 14
    }
    error("oops")
}

with(input()) {
    println(p1(this)) // 1582
    println(p2(this)) // 3588
}
