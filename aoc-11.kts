// https://adventofcode.com/2022/day/11

import java.io.File
import java.math.BigInteger

fun input() = File("input/aoc-11.txt").readLines()

// Part 1.

data class Monkey(val items: MutableList<Int>, val op: (old: Int) -> Int, val test: Int, val ifTrue: Int, val ifFalse: Int)

fun parse(input: List<String>) = (0 until input.size step 7).map { i ->
    val items = input[i+1].substringAfter(": ").split(", ").map { it.toInt() }.toMutableList()
    val opString = input[i+2].substringAfter(" = ")
    val opMod = if ("+" in opString) "+" else "*"
    val (opA, opB) = opString.split(" $opMod ")
    val op: (Int) -> Int = { old ->
        val a = if (opA == "old") old else opA.toInt()
        val b = if (opB == "old") old else opB.toInt()
        if (opMod == "+") a + b else a * b
    }
    val test = input[i+3].substringAfter("divisible by ").toInt()
    val ifTrue = input[i+4].substringAfter("monkey ").toInt()
    val ifFalse = input[i+5].substringAfter("monkey ").toInt()
    Monkey(items, op, test, ifTrue, ifFalse)
}

fun round(monkeys: List<Monkey>, counts: Array<Int>) {
    monkeys.forEachIndexed { i, m ->
        m.items.forEach { wOld ->
            val w = m.op(wOld) / 3
            val j = if (w % m.test == 0) m.ifTrue else m.ifFalse
            monkeys[j].items += w
            counts[i] += 1
        }
        m.items.clear()
    }
}

fun p1(input: List<String>): Int {
    val monkeys = parse(input)
    val counts = Array(monkeys.size) { 0 }

    repeat(20) { round(monkeys, counts) }
    
    counts.sortDescending()
    return counts[0] * counts[1]
}

// Part 2.

data class Monkey2(val items: MutableList<BigInteger>, val op: (old: BigInteger) -> BigInteger, val test: BigInteger, val ifTrue: Int, val ifFalse: Int)

fun parse2(input: List<String>) = (0 until input.size step 7).map { i ->
    val items = input[i+1].substringAfter(": ").split(", ").map { it.toBigInteger() }.toMutableList()
    val opString = input[i+2].substringAfter(" = ")
    val opMod = if ("+" in opString) "+" else "*"
    val (opA, opB) = opString.split(" $opMod ")
    val op: (BigInteger) -> BigInteger = { old ->
        val a = if (opA == "old") old else opA.toBigInteger()
        val b = if (opB == "old") old else opB.toBigInteger()
        if (opMod == "+") a + b else a * b
    }
    val test = input[i+3].substringAfter("divisible by ").toBigInteger()
    val ifTrue = input[i+4].substringAfter("monkey ").toInt()
    val ifFalse = input[i+5].substringAfter("monkey ").toInt()
    Monkey2(items, op, test, ifTrue, ifFalse)
}

fun round2(monkeys: List<Monkey2>, counts: Array<Int>, mod: BigInteger) {
    monkeys.forEachIndexed { i, m ->
        m.items.forEach { wOld ->
            val w = m.op(wOld) % mod
            val j = if (w % m.test == 0.toBigInteger()) m.ifTrue else m.ifFalse
            monkeys[j].items += w
            counts[i] += 1
        }
        m.items.clear()
    }
}

fun p2(input: List<String>): Long {
    val monkeys = parse2(input)
    val counts = Array(monkeys.size) { 0 }
    val lcm = monkeys.fold(1.toBigInteger()) { acc, m -> acc * m.test }

    repeat(10000) { round2(monkeys, counts, lcm) }

    counts.sortDescending()
    return counts[0].toLong() * counts[1].toLong()
}

with(input()) {
    println(p1(this)) // 182293
    println(p2(this)) // 54832778815
}
