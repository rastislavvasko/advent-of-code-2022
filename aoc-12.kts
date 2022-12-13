// https://adventofcode.com/2022/day/12

import java.io.File
import kotlin.collections.ArrayDeque

fun input() = File("input/aoc-12.txt").readLines()

// Part 1.

data class Point(val x: Int, val y: Int) {
    override fun toString() = "[$x, $y]"
}

data class Matrix(val m: Int, val n: Int, private val def: Int = 0) : Iterable<Point> {
    private val data = Array(m) { Array<Int>(n) { def } }

    operator fun get(p: Point) = data[p.y][p.x]

    operator fun set(p: Point, z: Int) { data[p.y][p.x] = z }

    fun adj(p: Point) = sequenceOf(
        Point(p.x - 1, p.y),
        Point(p.x, p.y - 1),
        Point(p.x + 1, p.y),
        Point(p.x, p.y + 1),
    ).filter { q -> q.x >= 0 && q.x < n && q.y >= 0 && q.y < m && this[p] >= this[q] - 1 }

    override fun iterator(): Iterator<Point> = object : Iterator<Point> {
        private var i = 0

        override fun next() = Point(i % n, i / n).also { i++ }

        override fun hasNext() = i < m*n
    }

    override fun toString() = data.map { r -> r.joinToString("") { it.toChar().toString() } }.joinToString("\n")
}

fun Matrix(lines: List<String>) = Matrix(lines.size, lines.first().length).apply {
    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c -> this[Point(x, y)] = c.code }
    }
}

fun p1(lines: List<String>): Int {
    val map = Matrix(lines)
    val s = map.first { map[it] == 'S'.code }.also { map.set(it, 'a'.code) }
    val e = map.first { map[it] == 'E'.code }.also { map.set(it, 'z'.code) }

    val res = Matrix(map.m, map.n, Int.MAX_VALUE).apply { this[s] = 0 }
    val queue = ArrayDeque<Point>().apply { add(s) }
    while (queue.isNotEmpty()) {
        val p = queue.removeFirst()
        map.adj(p).forEach { q ->
            if (res[q] > res[p] + 1) {
                res[q] = res[p] + 1
                queue += q
            }
        }
    }

    return res[e]
}

// Part 2.

fun p2(lines: List<String>): Int {
    val map = Matrix(lines)
    map.first { map[it] == 'S'.code }.also { map.set(it, 'a'.code) }
    val e = map.first { map[it] == 'E'.code }.also { map.set(it, 'z'.code) }

    val starts = map.filter { map[it] == 'a'.code }
    var min = Int.MAX_VALUE
    starts.forEach { s -> 
        val res = Matrix(map.m, map.n, Int.MAX_VALUE).apply { this[s] = 0 }
        val queue = ArrayDeque<Point>().apply { add(s) }
        while (queue.isNotEmpty()) {
            val p = queue.removeFirst()
            map.adj(p).forEach { q ->
                if (res[q] > res[p] + 1) {
                    res[q] = res[p] + 1
                    queue += q
                }
            }
        }
        if (res[e] < min) min = res[e]
    }

    return min
}

with(input()) {
    println(p1(this)) // 361
    println(p2(this)) // 354
}
