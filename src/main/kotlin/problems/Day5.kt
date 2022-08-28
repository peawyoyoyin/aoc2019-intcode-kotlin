package problems

import machine.IntCodeMachine
import problems.util.InputReader

fun main() {
    val program = InputReader.readInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day5.txt")

    val part1Program = program.toMutableList()
    val part1Machine = IntCodeMachine(part1Program, mutableListOf(1))
    part1Machine.run()

    println("part 1 answer: ${part1Machine.output.last()}")

    val part2Program = program.toMutableList()
    val part2Machine = IntCodeMachine(part2Program, mutableListOf(5))
    part2Machine.run()

    println("part 2 answer: ${part2Machine.output.last()}")
}