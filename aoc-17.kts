// https://adventofcode.com/2022/day/17

import java.io.File
import kotlin.math.min

fun input() = File("input/aoc-17.txt").readLines()

// Part 1.

data class Point(val x: Int, val y: Int) {
    override fun toString() = "[$x, $y]"

    operator fun plus(p: Point) = Point(x + p.x, y + p.y)
}

data class Rock(val pattern: List<String>) : Iterable<Point> {
    private val matrix = Matrix(pattern)

    val x = matrix.x
    val y = matrix.y

    operator fun get(p: Point) = matrix[p]

    override fun iterator() = matrix.iterator()

    override fun toString() = matrix.toString()
}

data class Matrix(val x: Int, val y: Int, private val def: Int = 0) : Iterable<Point> {
    private val data = Array(y) { Array<Int>(x) { def } }

    operator fun get(p: Point) = data[p.y][p.x]

    operator fun set(p: Point, z: Int) { data[p.y][p.x] = z }

    fun set(rock: Rock, offset: Point) {
        rock.forEach { p ->
            if (rock[p] == 1) {
                if (this[p + offset] == 1) error("oops: $p + $offset | $rock")
                this[p + offset] = 1
            }
        }
    }

    fun set(map: Matrix, offset: Point) {
        map.forEach { p ->
            if (map[p] == 1) {
                if (this[p + offset] == 1) error("oops: $p + $offset | $map")
                this[p + offset] = 1
            }
        }
    }

    fun valid(p: Point) = p.x >= 0 && p.x < x && p.y >= 0 && p.y < y

    fun valid(rock: Rock, offset: Point) = rock.all { p -> rock[p] == 0 || (valid(p + offset) && this[p + offset] == 0)  }

    override fun iterator(): Iterator<Point> = object : Iterator<Point> {
        private var i = 0

        override fun next() = Point(i % x, i / x).also { i++ }

        override fun hasNext() = i < y*x
    }

    override fun toString() = data.map { r -> r.joinToString("") { if (it == 0) "." else "#" } }.joinToString("\n")
}

fun Matrix(pattern: List<String>) = Matrix(pattern[0].length, pattern.size).apply {
    pattern.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') this[Point(x, y)] = 1
        }
    }
}

fun Point.jet(dx: Int, max: Int) = when {
    dx == -1 && x > 0 -> Point(x - 1, y)
    dx == 1 && x < max -> Point(x + 1, y)
    else -> this
}

fun Point.down() = Point(x, y + 1)

fun parse(input: List<String>) = input.first()

fun jet(jets: String, index: Int) = if (jets[index % jets.length] == '<') -1 else 1

fun move(map: Matrix, top: Int, rock: Rock, jets: String, jetCounter: Int): Pair<Point, Int> {
    var offset = Point(2, top - rock.y - 3)
    var step = 0
    while (true) {
        val dx = jet(jets, jetCounter + step)
        offset.jet(dx, map.x - rock.x).let { jetOffset ->
            step++
            if (map.valid(rock, jetOffset)) offset = jetOffset
        }
        offset.down().let { downOffset ->
            if (map.valid(rock, downOffset)) {
                offset = downOffset
            } else {
                return offset to step
            }
        }
    }
}

fun p1(input: List<String>): Int {
    val jets = parse(input)
    val n = 2022
    val rocks = listOf(
        listOf("####"),
        listOf(".#.", "###", ".#."),
        listOf("..#", "..#", "###"),
        listOf("#", "#", "#", "#"),
        listOf("##", "##"),
    ).map { Rock(it) }

    val map = Matrix(7, n * 4)
    var top = map.y
    var jetCounter = 0
    (0 until n).forEach { turn -> 
        val rock = rocks[turn % rocks.size]
        move(map, top, rock, jets, jetCounter).let { (moveOffset, moveStep) ->
            map.set(rock, moveOffset)
            top = min(moveOffset.y, top)
            jetCounter += moveStep
        }
    }

    return map.y - top
}

// Part 2.

data class State(val topMap: String, val jetIndex: Int)

fun simulate(rocks: List<Rock>, default: Matrix, jets: String, jetIndex: Int, n: Int): Pair<State, Long> {
    val map = Matrix(7, n * 4)
    map.set(default, Point(0, map.y - default.y))

    var top = map.findTop()

    var newJetIndex = jetIndex
    (0 until n).forEach { turn -> 
        val rock = rocks[turn % rocks.size]
        move(map, top, rock, jets, newJetIndex).let { (moveOffset, moveStep) ->
            map.set(rock, moveOffset)
            top = min(moveOffset.y, top)
            newJetIndex = (newJetIndex + moveStep) % jets.length
        }
    }

    val r = 100
    var topMap = IntArray(7 * r) { 0 }
    (top until top + r).forEach { y ->
        (0 until map.x).forEach { x ->
            if (map[Point(x, y)] == 1) topMap[7 * (y - top) + x] = 1
        }
    }
    val topMapStr = topMap.joinToString("") { if (it == 1) "#" else "." }

    val height = map.y - top - default.y
    return State(topMapStr, newJetIndex) to height.toLong()
}

fun Matrix.findTop() = (y downTo 1).firstOrNull { y2 -> (0 until x).all { x2 -> this[Point(x2, y2 - 1)] == 0 } } ?: 0

fun p2(input: List<String>): Long {
    val jets = parse(input)

    val rocks = listOf(
        listOf("####"),
        listOf(".#.", "###", ".#."),
        listOf("..#", "..#", "###"),
        listOf("#", "#", "#", "#"),
        listOf("##", "##"),
    ).map { Rock(it) }

    val maps = mutableMapOf<String, Matrix>()
    val mem = mutableMapOf<State, Pair<State, Long>>()
    var state = State(".".repeat(7), 0)
    var z = 1L // default state height offset

    val sim = 100_000
    val steps = (1_000_000_000_000L / sim).toInt()
    (0 until steps).forEach {
        val (newState, height) = mem[state] ?: simulate(rocks, maps.getOrPut(state.topMap) { Matrix(state.topMap.chunked(7)) }, jets, state.jetIndex, sim).also { mem[state] = it }
        state = newState
        z += height
    }

    return z
}

with(input()) {
    println(p1(this)) // 3071
    println(p2(this)) // 1523615160362
}
