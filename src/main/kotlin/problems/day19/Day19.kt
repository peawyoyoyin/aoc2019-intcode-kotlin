package problems.day19

import machine.IntCodeMachine
import problems.util.InputReader
import java.math.BigInteger
import kotlin.math.max

class Day19 {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day19.txt")

    private val memo = mutableMapOf<Pair<Int, Int>, Boolean>()
    private fun check(x: Int, y: Int): Boolean {
        if (Pair(x, y) in memo) {
            return memo[Pair(x, y)]!!
        }

        val machine = IntCodeMachine(program.toMutableList())
        machine.feedInputsAndRun(x, y)

        val result = machine.output.last() == BigInteger.ONE
        memo[Pair(x, y)]
        return result
    }

    val beamBoundaries = mutableListOf<Pair<Int, Int>>()

    private val logic = sequence {
        var affected = 0

        for (x in 0..49) {
            for (y in 0..49) {

                if (check(x, y)) {
                    affected += 1
                }
            }
        }

        print("part 1 ans: $affected")

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

            beamBoundaries.add(Pair(lastMinX, lastMaxX))
            yield(Unit)

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
        println("part 2 ans: ${10000*ansX+ansY}")
    }.iterator()

    fun advance() {
        if (logic.hasNext()) {
            logic.next()
        }
    }
}