// https://adventofcode.com/2022/day/1

import java.io.File
import kotlin.math.max

fun input() = File("input/aoc-1.txt").readLines()

// Part 1.

fun p1(lines: List<String>): Int {
    var max = 0
    val res = lines.fold(0) { acc, s ->
        if (s == "") {
            max = max(max, acc)
            0
        } else {
            acc + s.toInt()
        }
    }
    return max(max, res)
}

// Part 2.

fun p2(lines: List<String>): Int {
    return lines.joinToString(",").split(",,").map { it.split(",").map { it.toInt() }.sum() }.sortedDescending().take(3).sum()
}

with(input()) {
    println(p1(this)) // 66616
    println(p2(this)) // 199172
}
