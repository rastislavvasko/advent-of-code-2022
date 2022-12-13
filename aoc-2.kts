// https://adventofcode.com/2022/day/2

import java.io.File

fun input() = File("input/aoc-2.txt").readLines()

// Part 1.

fun p1(lines: List<String>): Int {
    return lines.sumOf { s ->
        when (s) {
            "A X" -> 1+3
            "A Y" -> 2+6
            "A Z" -> 3+0
            "B X" -> 1+0
            "B Y" -> 2+3
            "B Z" -> 3+6
            "C X" -> 1+6
            "C Y" -> 2+0
            "C Z" -> 3+3
            else -> error("oops")
        } as Int
    }
}

// Part 2.

fun p2(lines: List<String>): Int {
    return lines.sumOf { s ->
        when (s) {
            "A X" -> 3+0
            "A Y" -> 1+3
            "A Z" -> 2+6
            "B X" -> 1+0
            "B Y" -> 2+3
            "B Z" -> 3+6
            "C X" -> 2+0
            "C Y" -> 3+3
            "C Z" -> 1+6
            else -> error("oops")
        } as Int
    }
}

with(input()) {
    println(p1(this)) // 14375
    println(p2(this)) // 10274
}
