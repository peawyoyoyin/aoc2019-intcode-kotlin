package problems

import machine.IntCodeMachine
import problems.util.InputReader

fun IntCodeMachine.feedSpringScript(script: String) {
    script.split("\n").forEach {
        this.feedASCIIStringAndRun(it + Char(10))
    }
}

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day21.txt")

    val machine = IntCodeMachine(program.toMutableList())

    machine.feedSpringScript(
        """
            NOT C J
            AND D J
            NOT A T
            OR T J
            WALK
        """.trimIndent()
    )
    val ans1 = machine.output.last()
    println("part 1 answer : $ans1")

    val machine2 = IntCodeMachine(program.toMutableList())

    machine2.feedSpringScript(
        """
            NOT C J
            AND D J
            AND H J
            NOT A T
            OR T J
            NOT B T
            AND C T
            AND D T
            OR T J
            RUN
        """.trimIndent()
    )
    val ans2 = machine2.output.last()
    println("part 2 answer : $ans2")
}