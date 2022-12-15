// https://adventofcode.com/2022/day/10

import java.io.File
import kotlin.math.abs

fun input() = File("input/aoc-10.txt").readLines()

fun parse(input: List<String>) = 
    input.flatMap { if (it == "noop") listOf(0) else listOf(0, it.substring(5).toInt()) }

// Part 1.

fun p1(input: List<String>): Int {
    val ops = parse(input)
    return (20..220 step 40).sumOf { cycle -> cycle * (1 + ops.subList(0, cycle - 1).sum()) }
}

// Part 2.

fun p2(input: List<String>) {
    val ops = parse(input)

    var x = 1
    val crt = Array(40 * 6) { ' ' }
    (0 until crt.size).forEach { i -> 
        crt[i] = if (abs(i % 40 - x) <= 1) '#' else '.'
        x += ops[i]
    }

    crt.joinToString("").chunked(40).forEach { println(it) }
}

with(input()) {
    println(p1(this)) // 16020
    p2(this) // ECZUZALR
}
