package problems

import machine.IntCodeMachine
import utils.day15.Direction
import utils.day15.move
import utils.day15.toAbbr
import problems.util.InputReader
import java.lang.Exception
import java.lang.Integer.max

val origin = Pair(0, 0)

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day15.txt")

    val machine = IntCodeMachine(program.toMutableList())

    val queue = ArrayDeque<List<Direction>>()

    val visited = mutableSetOf(origin)
    Direction.values().forEach {
        queue.add(listOf(it))
        visited.add(origin.move(it))
    }

    var goal: Pair<Int, Int> = Pair(0, 0)
    var ans = -1
    while (queue.isNotEmpty()) {
        val path = queue.removeFirst()

        var position = origin
        for (direction in path.dropLast(1)) {
            machine.feedInputsAndRun(direction.raw)

            if (machine.output.last().toInt() != 1) {
                throw Exception("unexpected result when traversing path")
            }

            position = position.move(direction)
        }
        val lastDirection = path.last()
        machine.feedInputsAndRun(lastDirection.raw)

        when (val output = machine.output.last().toInt()) {
            0 -> { // hit wall
                Direction
                    .values()
                    .filter { it != lastDirection && position.move(it) !in visited }
                    .forEach {
                        visited.add(position.move(it))
                        queue.addLast(path + it)
                    }

                for (direction in path.dropLast(1).reversed()) {
                    machine.feedInputsAndRun(direction.reverse().raw)
                }
            }
            1 -> { // move OK
                position = position.move(lastDirection)

                Direction
                    .values()
                    .filter { position.move(it) !in visited }
                    .forEach {
                        visited.add(position.move(it))
                        queue.addLast(path + it)
                    }

                for (direction in path.reversed()) {
                    machine.feedInputsAndRun(direction.reverse().raw)
                }
            }
            2 -> { // found
                position = position.move(lastDirection)
                goal = position

                ans = path.size
                break
            }
            else -> { throw Exception("unexpected output $output") }
        }
    }
    println("part 1 answer: $ans")

    val queue2 = ArrayDeque<List<Direction>>()
    val visited2 = mutableSetOf(goal)
    var ans2 = -1

    Direction.values().forEach {
        queue2.add(listOf(it))
        visited2.add(goal.move(it))
    }

    while (queue2.isNotEmpty()) {
        val path = queue2.removeFirst()
        var position = goal

        for (direction in path.dropLast(1)) {
            machine.feedInputsAndRun(direction.raw)

            if (machine.output.last().toInt() != 1) {
                throw Exception("unexpected result when traversing path")
            }

            position = position.move(direction)
        }
        val lastDirection = path.last()
        machine.feedInputsAndRun(lastDirection.raw)
        when (val output = machine.output.last().toInt()) {
            0 -> { // hit wall
                Direction
                    .values()
                    .filter { it != lastDirection && position.move(it) !in visited2 }
                    .forEach {
                        visited2.add(position.move(it))
                        queue2.addLast(path + it)
                    }

                for (direction in path.dropLast(1).reversed()) {
                    machine.feedInputsAndRun(direction.reverse().raw)
                }
            }
            1 -> { // move OK
                position = position.move(lastDirection)

                Direction
                    .values()
                    .filter { position.move(it) !in visited2 }
                    .forEach {
                        visited2.add(position.move(it))
                        queue2.addLast(path + it)
                    }

                for (direction in path.reversed()) {
                    machine.feedInputsAndRun(direction.reverse().raw)
                }
                ans2 = max(ans2, path.size)
            }
            2 -> { // found
                throw Exception("not expected to found goal!!!!!")
            }
            else -> { throw Exception("unexpected output $output") }
        }
    }
    println("part 2 answer: $ans2")
}