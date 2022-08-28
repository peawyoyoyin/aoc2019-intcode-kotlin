package problems

import machine.IntCodeMachine
import problems.util.InputReader
import problems.util.max
import java.math.BigInteger

private fun permutations(elements: List<Int>): Sequence<List<Int>> = sequence {
    if (elements.isEmpty()) {
        yield(emptyList())
    }

    for (index in elements.indices) {
        val otherElements = elements.filterIndexed { elementIndex, _ -> elementIndex != index }

        val subPermutations: Sequence<List<Int>> = permutations(otherElements)

        subPermutations.forEach { subPermutation -> yield(listOf(elements[index]) + subPermutation) }
    }
}

fun main() {
    val program = InputReader.readInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day7.txt")

    val allSequences = permutations((0..4).toList())

    var ans = BigInteger.valueOf(-1)
    for (sequence in allSequences) {
        var input = BigInteger.ZERO
        for (element in sequence) {
            val machine = IntCodeMachine(program.toMutableList(), mutableListOf(element, input.toInt()))
            machine.run()
            input = machine.output[0]
        }
        ans = max(input, ans)
    }
    println("part 1 answer: $ans")

    // part2
    val allSequences2 = permutations((5..9).toList())

    var ans2 = BigInteger.valueOf(-1)
    for (sequence in allSequences2) {
        var input = BigInteger.ZERO
        val machines = sequence.map{ IntCodeMachine(program.toMutableList(), mutableListOf(it)) }
        var machinePointer = 0

        while (true) {
            val machine = machines[machinePointer]
            machine.feedInputsAndRun(input)
            input = machine.output.last()

            if (machines.all { it.halted }) {
                break
            }

            machinePointer += 1
            machinePointer %= machines.size
        }
        ans2 = max(input, ans2)
    }

    println("part 2 answer : $ans2")
}