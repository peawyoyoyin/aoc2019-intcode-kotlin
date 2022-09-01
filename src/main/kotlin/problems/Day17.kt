package problems

import machine.IntCodeMachine
import problems.util.InputReader
import java.math.BigInteger

const val OPEN_SPACE = 0
const val SCAFFOLD = 1
const val ROBOT_UP = 2
const val ROBOT_DOWN = 3
const val ROBOT_LEFT = 4
const val ROBOT_RIGHT = 5

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day17.txt")

    val machine = IntCodeMachine(program.toMutableList())

    machine.run()

    var rawMap = machine.ASCIIOutput()
//    print(rawMap.joinToString(""))
    machine.flushOutput()

    var map = mutableListOf<MutableList<Int>>()

    var tempRow = mutableListOf<Int>()

    for (char in rawMap) {
        when (char) {
            '.' -> tempRow.add(OPEN_SPACE)
            '#' -> tempRow.add(SCAFFOLD)
            Char(10) -> {
                map.add(tempRow)
                tempRow = mutableListOf()
            } // newline
            '^' -> tempRow.add(ROBOT_UP)
            'v' -> tempRow.add(ROBOT_DOWN)
            '<' -> tempRow.add(ROBOT_LEFT)
            '>' -> tempRow.add(ROBOT_RIGHT)
            else -> { } // robot
        }
    }

    map = map.dropLast(1).toMutableList()

    var ans = 0
    for ((rowIndex, row) in map.withIndex()) {
        for ((colIndex, col) in row.withIndex()) {
            if (rowIndex == 0 || colIndex == 0) {
                continue
            }

            if (rowIndex == map.size - 1 || colIndex == row.size - 1) {
                continue
            }

            if (
                map[rowIndex-1][colIndex] > 0 &&
                map[rowIndex+1][colIndex] > 0 &&
                map[rowIndex][colIndex-1] > 0 &&
                map[rowIndex][colIndex+1] > 0 &&
                col > 0
            ) {
                val alignmentParameter = rowIndex * colIndex
                ans += alignmentParameter
            }
        }
    }
    println("part 1 ans: $ans")

    val program2 = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day17.txt").toMutableList()
    program2[0] = BigInteger.valueOf(2)

    val machine2 = IntCodeMachine(program.toMutableList())

    machine2.feedASCIIStringAndRun("A,B,A,B,C,C,B,A,B,C" + Char(10))
    machine2.feedASCIIStringAndRun("L,8,R,12,R,12,R,10" + Char(10)) // A
    machine2.feedASCIIStringAndRun("R,10,R,12,R,10" + Char(10)) // B
    machine2.feedASCIIStringAndRun("L,10,R,10,L,6" + Char(10)) // C
    machine2.feedASCIIStringAndRun("n" + Char(10))
    val ans2 = machine2.output.last()
    print("part 2 ans: $ans2")
}