// https://adventofcode.com/2022/day/5

import java.io.File
import kotlin.collections.ArrayDeque

fun input() = File("input/aoc-5.txt").readLines()

// Part 1.

data class Move(val count: Int, val a: Int, val b: Int)

fun parse(input: List<String>): Pair<List<ArrayDeque<Char>>, List<Move>> {
    val nLine = input.indexOf("") - 1
    val n = input[nLine].trim().split(" ").last().toInt()
    val crates = (0 until n).map { ArrayDeque<Char>() }
    repeat(n) { i ->
        input.subList(0, nLine).forEach { line ->
            val c = line.getOrNull(i * 4 + 1) ?: ' '
            if (c != ' ') crates[i].addFirst(c)
        }
    }

    val pattern = """move (\d+) from (\d+) to (\d+)""".toRegex()
    val moves = input.subList(nLine + 2, input.size).map { line ->
        val (_, count, a, b) = pattern.find(line)!!.groupValues
        Move(count.toInt(), a.toInt() - 1, b.toInt() - 1)
    }
    return crates to moves
}

fun p1(input: List<String>): String {
    val (crates, moves) = parse(input)

    moves.forEach { move ->
        repeat(move.count) {
            val c = crates[move.a].removeLast()
            crates[move.b].addLast(c)
        }
    }

    return crates.map { it.last() }.joinToString("")
}

// Part 2.

fun p2(input: List<String>): String {
    val (crates, moves) = parse(input)

    moves.forEach { move ->
        var s = ""
        repeat(move.count) { s += crates[move.a].removeLast() }
        s.reversed().forEach { c -> crates[move.b].addLast(c) }
    }

    return crates.map { it.last() }.joinToString("")
}

with(input()) {
    println(p1(this)) // LBLVVTVLP
    println(p2(this)) // TPFFBDRJD
}
