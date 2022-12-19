// https://adventofcode.com/2022/day/18

import java.io.File
import kotlin.collections.ArrayDeque
import kotlin.math.abs

fun input() = File("input/aoc-18.txt").readLines()

data class Point(val x: Int, val y: Int, val z: Int)

fun parse(input: List<String>) = input.map { line ->
    line.split(",").map { it.toInt() }.let { Point(it[0], it[1], it[2]) }
}

fun dist(d: Point, e: Point) = abs(d.x - e.x) + abs(d.y - e.y) + abs(d.z - e.z)

// Part 1.

fun p1(input: List<String>): Int {
    val droplets = parse(input)

    var z = 0
    droplets.forEach { d ->
        var dz = 6
        droplets.forEach { e -> if (dist(d, e) == 1) dz-- }
        z += dz
    }

    return z
}

// Part 2.

data class Matrix(val x: Int, val y: Int, val z: Int, private val def: Int = 0) : Iterable<Point> {
    private val data = Array(z) { Array(y) { Array<Int>(x) { def } } }

    operator fun get(p: Point) = data[p.z][p.y][p.x]

    operator fun set(p: Point, i: Int) { data[p.z][p.y][p.x] = i }

    fun adj(p: Point) = sequenceOf(
        Point(p.x - 1, p.y, p.z),
        Point(p.x + 1, p.y, p.z),
        Point(p.x, p.y - 1, p.z),
        Point(p.x, p.y + 1, p.z),
        Point(p.x, p.y, p.z - 1),
        Point(p.x, p.y, p.z + 1),
    ).filter { q -> q.x >= 0 && q.x < x && q.y >= 0 && q.y < y && q.z >= 0 && q.z < z }

    override fun iterator(): Iterator<Point> = object : Iterator<Point> {
        private var dx = 0
        private var dy = 0
        private var dz = 0

        override fun next() = Point(dx, dy, dz).also {
            dx++
            if (dx == x) {
                dx = 0
                dy++
            }
            if (dy == y) {
                dy = 0
                dz++
            }
        }

        override fun hasNext() = dx * dy * dz < x * y * z && dz < z
    }
}

fun p2(input: List<String>): Int {
    val droplets = parse(input)

    val map = Matrix(droplets.maxBy { it.x }.x + 3, droplets.maxBy { it.y }.y + 3, droplets.maxBy { it.z }.z + 3)
    droplets.forEach { d -> map[Point(d.x + 1, d.y + 1, d.z + 1)] = 1 }

    val queue = ArrayDeque<Point>().apply { add(Point(0, 0, 0)) }
    while (queue.isNotEmpty()) {
        val p = queue.removeFirst()

        if (map[p] != 0) continue
        map[p] = 2
        map.adj(p).forEach { q -> if (map[q] == 0) queue += q }
    }

    var z = 0
    map.forEach { p ->
        if (map[p] == 1) {
            map.adj(p).forEach { q -> if (map[q] == 2) z++ }
        }
    }

    return z
}

with(input()) {
    println(p1(this)) // 3374
    println(p2(this)) // 2010
}
