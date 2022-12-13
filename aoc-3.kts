// https://adventofcode.com/2022/day/3

import java.io.File

fun input() = File("input/aoc-3.txt").readLines()

// Part 1.

fun p1(lines: List<String>): Int {
    return lines.map { s -> 
        val c = (s.substring(0, s.length / 2).toList() intersect s.substring(s.length / 2).toList()).single()
        if (c.code > 'a'.code) c.code - 'a'.code + 1 else c.code - 'A'.code + 27
    }.sum()
}

// Part 2.

fun p2(lines: List<String>): Int {
    return lines.chunked(3).map { l -> 
        val c = (l[0].toList() intersect l[1].toList() intersect l[2].toList()).single()
        if (c.code > 'a'.code) c.code - 'a'.code + 1 else c.code - 'A'.code + 27
    }.sum()
}

with(input()) {
    println(p1(this)) // 8349
    println(p2(this)) // 2681
}
