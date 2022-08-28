package problems

import machine.IntCodeMachine
import problems.util.InputReader
import java.io.File

fun main() {
    val program = InputReader.readInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day2.txt")

    // part1
    val part1Program = program.toMutableList()
    part1Program[1] = 12
    part1Program[2] = 2
    val part1Machine = IntCodeMachine(part1Program)
    part1Machine.run()
    println("part 1 answer: " + part1Machine.memory[0])

    // part2
    val target = 19690720
    loop@ for (noun in 0..99) {
        for (verb in 0..99) {
            val code = program.toMutableList()
            code[1] = noun
            code[2] = verb

            val machine = IntCodeMachine(code)
            machine.run()

            val output = machine.memory[0]
            if (output?.toInt() == target) {
                println("part 2 answer: ${100*noun+verb}")
                break@loop
            }
        }
    }
}