package problems

import machine.IntCodeMachine
import problems.util.InputReader
import java.math.BigInteger
import kotlin.math.max

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day19.txt")

    val memo = mutableMapOf<Pair<Int, Int>, Boolean>()

    var checked = 0

    fun check(x: Int, y: Int): Boolean {
        if (Pair(x, y) in memo) {
            return memo[Pair(x, y)]!!
        }

        val machine = IntCodeMachine(program.toMutableList())
        machine.feedInputsAndRun(x, y)
        checked += 1

        val result = machine.output.last() == BigInteger.ONE
        memo[Pair(x, y)]
        return result
    }

    var affected = 0

    for (x in 0..49) {
        for (y in 0..49) {

            if (check(x, y)) {
                affected += 1
            }
        }
    }

    println("part 1 ans: $affected")

    var ansX = -1
    var ansY = -1

    var lastMinX = 0
    var lastMaxX = 0
    loop@for (y in 0..2000) {
        for (pt1 in lastMinX..2000) {
            if (check(pt1, y)) {
                lastMinX = pt1
                break
            }
        }

        for(pt2 in max(lastMinX, lastMaxX)..2000) {
            if (!check(pt2, y)) {
                lastMaxX = pt2-1
                break
            }
        }

        if (lastMaxX - lastMinX < 99) {
            continue
        }

        var l = lastMinX
        var r = lastMaxX

        while (l < r) {
            var mid = (l+r) / 2
            if (!check(mid+99,y)) {
                r = mid
            } else {
                l = mid + 1
            }
        }

        if (check(l-1+99, y) && check(l-1, y+99)) {
            ansX = l-1
            ansY = y
            break@loop
        }
    }

    println("checked $checked times")
    println("part 2 ans: ${10000*ansX+ansY}")
}