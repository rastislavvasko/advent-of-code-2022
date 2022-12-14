// https://adventofcode.com/2022/day/14

import java.io.File
import kotlin.collections.minBy
import kotlin.math.max
import kotlin.math.min

fun input() = File("input/aoc-14.txt").readLines()

// Part 1.

data class Point(val x: Int, val y: Int) {
    override fun toString() = "[$x, $y]"
}

data class Matrix(val m: Int, val n: Int, private val def: Int = 0) : Iterable<Point> {
    private val data = Array(m) { Array<Int>(n) { def } }

    operator fun get(p: Point) = data[p.y][p.x]

    operator fun set(p: Point, z: Int) { data[p.y][p.x] = z }

    fun candidates(p: Point) = sequenceOf(
        Point(p.x, p.y + 1),
        Point(p.x - 1, p.y + 1),
        Point(p.x + 1, p.y + 1),
    )

    fun move(p: Point): Point? {
        candidates(p).forEach { q ->
            if (q.x < 0 || q.x >= n || q.y == m) return null
            if (this[q] == 0) return q
        }
        return p
    }

    override fun iterator(): Iterator<Point> = object : Iterator<Point> {
        private var i = 0

        override fun next() = Point(i % n, i / n).also { i++ }

        override fun hasNext() = i < m*n
    }

    override fun toString() = data.map { r -> r.joinToString("") { it.toString() } }.joinToString("\n")
}

data class Line(val a: Point, val b: Point)

fun Point(s: String): Point = s.split(",").let { Point(it[0].toInt(), it[1].toInt()) }

fun parse(input: List<String>): Pair<Matrix, Point> {
    val lines = input.flatMap { line ->
        line.split(" -> ").windowed(2).map { (a, b) -> Line(Point(a), Point(b)) }
    }

    val points = lines.flatMap { l -> listOf(l.a, l.b) }
    val mMin = 0
    val mMax = points.maxBy { it.y }.y
    val nMin = points.minBy { it.x }.x
    val nMax = points.maxBy { it.x }.x

    val map = Matrix(mMax - mMin + 1, nMax - nMin + 1)

    lines.forEach { line ->
        val a = line.a
        val b = line.b
        if (a.x != b.x) {
            val x0 = min(a.x, b.x)
            val x1 = max(a.x, b.x)
            (x0..x1).forEach { x -> map[Point(x - nMin, a.y - mMin)] = 1 }
        } else {
            val y0 = min(a.y, b.y)
            val y1 = max(a.y, b.y)
            (y0..y1).forEach { y -> map[Point(a.x - nMin, y - mMin)] = 1 }
        }
    }

    return map to Point(nMin, mMin)
}

fun p1(input: List<String>): Int {
    val (map, offset) = parse(input)
    val start = Point(500 - offset.x, 0)

    var p = start
    while (true) {
        val q = map.move(p)
        when (q) {
            null -> break
            p -> {
                map[p] = 2
                p = start
            }
            else -> {
                p = q
            }
        }
    }

    return map.count { map[it] == 2 }
}

// Part 2.

fun p2(input: List<String>): Int {
    val (inputMap, offset) = parse(input)
    val my = inputMap.m
    val mx = inputMap.n

    val map = Matrix(my + 2, mx + my * 2)
    inputMap.forEach { p -> 
        if (inputMap[p] == 1) map[Point(p.x + my, p.y)] = 1
    }
    (0 until map.n).forEach { x -> map[Point(x, map.m - 1)] = 1 }

    val start = Point(500 - offset.x + my, 0)

    var p = start
    while (true) {
        val q = map.move(p)
        when (q) {
            null -> error("oops")
            start -> {
                map[p] = 2
                break
            }
            p -> {
                map[p] = 2
                p = start
            }
            else -> {
                p = q
            }
        }
    }

    return map.count { map[it] == 2 }
}

with(input()) {
    println(p1(this)) // 1199
    println(p2(this)) // 23925
}
