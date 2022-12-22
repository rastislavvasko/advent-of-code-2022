// https://adventofcode.com/2022/day/8

import java.io.File

fun input() = File("input/aoc-8.txt").readLines()

data class Point(val x: Int, val y: Int)

data class Matrix(val x: Int, val y: Int, private val def: Int = 0) : Iterable<Point> {
    private val data = Array(y) { Array<Int>(x) { def } }

    operator fun get(p: Point) = data[p.y][p.x]

    operator fun set(p: Point, z: Int) { data[p.y][p.x] = z }

    fun isEdge(p: Point) = p.x == 0 || p.x == x - 1 || p.y == 0 || p.y == y - 1

    override fun iterator(): Iterator<Point> = object : Iterator<Point> {
        private var i = 0

        override fun next() = Point(i % x, i / x).also { i++ }

        override fun hasNext() = i < y*x
    }

    override fun toString() = data.map { r -> r.joinToString("") }.joinToString("\n")
}

fun parse(input: List<String>) = Matrix(x = input.first().length, y = input.size).apply {
    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c -> this[Point(x, y)] = c.digitToInt() }
    }
}

// Part 1.

fun Matrix.isVisible(p: Point): Boolean {
    if (isEdge(p)) return true
    val z = get(p)
    if ((0 until p.x).all { x -> get(Point(x, p.y)) < z }) return true
    if ((p.x + 1 until x).all { x -> get(Point(x, p.y)) < z }) return true
    if ((0 until p.y).all { y -> get(Point(p.x, y)) < z }) return true
    if ((p.y + 1 until y).all { y -> get(Point(p.x, y)) < z }) return true
    return false
}

fun p1(input: List<String>): Int {
    val map = parse(input)
    return map.count { p -> map.isVisible(p) }
}

// Part 2.

fun Matrix.scenicScore(p: Point) = if (isEdge(p)) 0 else sequenceOf(
    (p.x - 1 downTo 0).indexOfFirst { x -> get(Point(x, p.y)) >= get(p) }.let { if (it == -1) p.x else it + 1 },
    (p.y - 1 downTo 0).indexOfFirst { y -> get(Point(p.x, y)) >= get(p) }.let { if (it == -1) p.y else it + 1 },
    (p.x + 1 until x).indexOfFirst { x -> get(Point(x, p.y)) >= get(p) }.let { if (it == -1) x - 1 - p.x else it + 1 },
    (p.y + 1 until y).indexOfFirst { y -> get(Point(p.x, y)) >= get(p) }.let { if (it == -1) y - 1 - p.y else it + 1 },
).fold(1) { acc, s -> (acc * s) }

fun p2(input: List<String>): Int {
    val map = parse(input)

    val p = Point(2, 3)
    println("??? " + (p.x - 1 downTo 0))
    println("!!! " + map.scenicScore(p))
    // println("AAA " + (p.y + 1 until map.y).indexOfFirst { y -> map.get(Point(p.x, y)) >= map.get(p) }.let { if (it == -1) (map.y - 1) - p.y else it + 1 })
    
    return map.maxOf { map.scenicScore(it) }
}

with(input()) {
    println(p1(this)) // 1533
    println(p2(this)) // ?
}
