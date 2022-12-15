// https://adventofcode.com/2022/day/15

import java.io.File
import kotlin.math.abs

fun input() = File("input/aoc-15.txt").readLines()

// Part 1.

data class Point(val x: Int, val y: Int) {
    override fun toString() = "[$x, $y]"
}

fun parse(input: List<String>) = input.map { line ->
    val m = Regex("(-?[0-9]+)").findAll(line).toList().map { it.value.toInt() }
    Point(m[0], m[1]) to Point(m[2], m[3])
}.unzip()

fun dist(p: Point, q: Point) = abs(p.x - q.x) + abs(p.y - q.y)

fun intersect(s: Point, d: Int, y: Int): Pair<Int, Int>? {
    val dx = d - abs(s.y - y)
    return if (dx < 0) null else (s.x - dx) to (s.x + dx)
}

fun p1(input: List<String>): Int {
    val (sensors, beacons) = parse(input)
    val n = sensors.size
    val dists = (0 until n).map { dist(sensors[it], beacons[it]) }

    val res = mutableSetOf<Int>()
    val y = 2000000

    sensors.forEachIndexed { i, s ->
        val d = dists[i]
        val xd = intersect(s, d, y)
        xd?.let { (x0, x1) -> (x0..x1).forEach { x -> res += x } }
    }
    res -= beacons.filter { it.y == y }.map { it.x }

    return res.size
}

// Part 2.

fun p2(input: List<String>): Long {
    val (sensors, beacons) = parse(input)
        val n = sensors.size
        val dists = (0 until n).map { dist(sensors[it], beacons[it]) }

        val max = 4000000

        (0..max).forEach { y ->
            val lines = sensors.mapIndexedNotNull { i, s ->
                val d = dists[i]
                val dx = d - abs(s.y - y)
                if (dx < 0) null else (s.x - dx) to (s.x + dx)
            }.sortedBy { it.second }.sortedBy { it.first }

            var x = 0
            lines.forEach { l ->
                if (x + 1 < l.first) return (x + 1) * 4_000_000L + y
                if (l.second > x) x = l.second
            }
        }
        error("oops")
}

with(input()) {
    println(p1(this)) // 5403290
    println(p2(this)) // 10291582906626
}
