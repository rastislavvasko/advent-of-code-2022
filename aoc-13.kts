// https://adventofcode.com/2022/day/13

import java.io.File

fun input() = File("input/aoc-13.txt").readLines()

// Part 1.

data class Node(val value: Int?, val children: List<Node>?): Comparable<Node> {
    override fun toString(): String = when {
        value != null -> value.toString()
        else -> "[${children!!.joinToString(",")}]"
    }

    fun length(): Int = toString().length

    override fun compareTo(other: Node) = when (compare(this, other)) {
        null -> 0
        true -> -1
        else -> 1
    }
}

fun parse(s: String, n: Int, i: Int): Node? {
    when {
        i >= n || s[i] == ',' || s[i] == ']' -> {
            return null
        }
        s[i] >= '0' && s[i] <= '9' -> {
            for (j in (i+1)..n) {
                if (s[j] < '0' || s[j] > '9') return Node(s.substring(i, j).toInt(), null)
            }
        }
        s[i] == '[' -> {
            var j = i+1
            val children = mutableListOf<Node>()
            while (true) {
                val c = parse(s, n, j)
                if (c == null) {
                    break
                } else {
                    children.add(c)
                    j += c.length() + 1
                }
            }
            return Node(null, children)
        }
        else -> error("oops")
    }
    error("oops")
}

fun compare(a: Node, b: Node): Boolean? {
    when {
        a.value != null && b.value != null -> {
            return if (a.value == b.value) null else a.value < b.value
        }
        a.value == null && b.value != null -> {
            return compare(a, Node(null, listOf(b))) 
        }
        a.value != null && b.value == null -> {
            return compare(Node(null, listOf(a)), b)
        }
        a.value == null && b.value == null -> {
            val an = a.children!!.size
            val bn = b.children!!.size
            val n = if (an < bn) an else bn
            for (i in 0 until n) {
                compare(a.children[i], b.children[i])?.let { return it }
            }
            return if (an == bn) null else an < bn
        }
        else -> error("oops")
    }
}

fun p1(lines: List<String>): Int {
    var z = 0
    for (i in 0..lines.size step 3) {
        val a = parse(lines[i], lines[i].length, 0)!!
        val b = parse(lines[i+1], lines[i+1].length, 0)!!
        if (compare(a, b) == true) z += 1 + (i / 3)
    }
    return z
}

// Part 2.

fun p2(lines: List<String>): Int {
    val packets = lines.filter { it != "" } + "[[2]]" + "[[6]]"
    val res = packets.map { parse(it, it.length, 0)!! }.sorted().map { it.toString() }
    return (res.indexOf("[[2]]") + 1) * (res.indexOf("[[6]]") + 1)
}

with(input()) {
    println(p1(this)) // 5682
    println(p2(this)) // 20304
}
