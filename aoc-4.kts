// https://adventofcode.com/2022/day/4

import java.io.File

fun input() = File("input/aoc-4.txt").readLines()

// Part 1.

fun p1(lines: List<String>): Int {
    return lines.map { s ->
        val (a, b) = s.split(",").map { it.split("-").map { it.toInt() } }
        if ((a[0] <= b[0] && a[1] >= b[1]) || (b[0] <= a[0] && b[1] >= a[1])) 1 else 0
    }.sum()
}

// Part 2.

fun p2(lines: List<String>): Int {
    return lines.map { s ->
        val (a, b) = s.split(",").map { it.split("-").map { it.toInt() } }
        when {
            a[0] >= b[0] && a[0] <= b[1] -> 1
            a[1] >= b[0] && a[1] <= b[1] -> 1
            b[0] >= a[0] && b[0] <= a[1] -> 1
            b[1] >= a[0] && b[1] <= a[1] -> 1
            else -> 0
        }
    }.sum()
}

with(input()) {
    println(p1(this)) // 483
    println(p2(this)) // 874
}
