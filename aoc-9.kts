// https://adventofcode.com/2022/day/9

import java.io.File
import kotlin.math.abs
import kotlin.math.sign

fun input() = File("input/aoc-9.txt").readLines()

data class Point(val x: Int, val y: Int)

fun parse(input: List<String>) =
    input.map { line -> line.split(" ").let { (c, n) -> c.repeat(n.toInt()) } }.joinToString("")

fun move(h: Point, t: Point): Point = when {
    abs(h.x - t.x) <= 1 && abs(h.y - t.y) <= 1 -> t
    else -> {
        val dx = (h.x - t.x).sign
        val dy = (h.y - t.y).sign
        Point(t.x + dx, t.y + dy)
    }
}

// Part 1.

fun p1(input: List<String>): Int {
    val moves = parse(input)

    var h = Point(0, 0)
    var t = Point(0, 0)
    val res = mutableSetOf<Point>().apply { add(t) }

    moves.forEach { m ->
        h = when (m) {
            'L' -> Point(h.x - 1, h.y)
            'U' -> Point(h.x, h.y - 1)
            'R' -> Point(h.x + 1, h.y)
            'D' -> Point(h.x, h.y + 1)
            else -> error("oops")
        }
        t = move(h, t)
        res += t
    }

    return res.size
}

// Part 2.

fun p2(input: List<String>): Int {
    val moves = parse(input)

    var h = Point(0, 0)
    val t = Array(9) { Point(0, 0) }
    val res = mutableSetOf<Point>().apply { add(t.last()) }

    moves.forEach { m ->
        h = when (m) {
            'L' -> Point(h.x - 1, h.y)
            'U' -> Point(h.x, h.y - 1)
            'R' -> Point(h.x + 1, h.y)
            'D' -> Point(h.x, h.y + 1)
            else -> error("oops")
        }
        t.indices.forEach { i -> t[i] = move(if (i == 0) h else t[i-1], t[i]) }
        res += t.last()
    }

    return res.size
}

with(input()) {
    println(p1(this)) // 6044
    println(p2(this)) // 2384
}
