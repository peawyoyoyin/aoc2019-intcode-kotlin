package problems

import machine.IntCodeMachine
import problems.util.InputReader

fun main() {
    val program = InputReader.readInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day9.txt")

    val machine1 = IntCodeMachine(program.toMutableList(), mutableListOf(1))
    machine1.run()
    println("part 1 answer: ${machine1.output[0]}")

    val machine2 = IntCodeMachine(program.toMutableList(), mutableListOf(2))
    machine2.run()
    println("part 2 answer: ${machine2.output[0]}")
}