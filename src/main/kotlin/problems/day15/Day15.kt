package problems.day15

import machine.IntCodeMachine
import problems.util.InputReader
import utils.day15.Direction
import utils.day15.move
import java.lang.Exception

val origin = Pair(50, 50)

const val UNEXPLORED = 0
const val OPEN_TILE = 1
const val WALL = 2
const val ROBOT = 3
const val GOAL = 4

var GRID_WIDTH = 500
var GRID_HEIGHT = 500

const val HIT_WALL = 0
const val MOVED_OK = 1
const val MOVED_TO_GOAL = 2

class Day15 {
    var map: MutableList<MutableList<Int>> = MutableList(GRID_HEIGHT) {
        MutableList(GRID_WIDTH) { UNEXPLORED }
    }
    var robotPosition = origin

    private val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day15.txt")

    val machine = IntCodeMachine(program.toMutableList())

    private val logic = sequence {
        val queue = ArrayDeque<List<Direction>>()

        val visited = mutableSetOf(origin)
        Direction.values().forEach {
            queue.add(listOf(it))
            visited.add(origin.move(it))
        }

        var runs = 0
        var lastOK = listOf<Direction>()

        var goal: Pair<Int, Int> = Pair(0, 0)
        var ans = -1

        while (queue.isNotEmpty()) {
            val path = queue.removeFirst()

            val (_, toReverse, toApply) = utils.day15.partition(path, lastOK)

            for (directionToReverse in toReverse.reversed()) {
                val reversedDirection = directionToReverse.reverse()
                machine.feedInputsAndRun(reversedDirection.raw)
                runs += 1

                robotPosition = robotPosition.move(reversedDirection)
                yield(Unit)
            }

            for (direction in toApply.dropLast(1)) {
                machine.feedInputsAndRun(direction.raw)
                runs += 1

                val machineResult = machine.output.last().toInt()
                if (machineResult != 1) {
                    throw Exception("unexpected result $machineResult when traversing path")
                }

                robotPosition = robotPosition.move(direction)
                yield(Unit)
            }
            val lastDirection = toApply.last()
            machine.feedInputsAndRun(lastDirection.raw)
            runs += 1
            yield(Unit)

            when (val output = machine.output.last().toInt()) {
                HIT_WALL -> {
                    val wallPosition = robotPosition.move(lastDirection)
                    map[wallPosition.second][wallPosition.first] = WALL

                    Direction
                        .values()
                        .filter { it != lastDirection && robotPosition.move(it) !in visited }
                        .forEach {
                            visited.add(robotPosition.move(it))
                            queue.addLast(path + it)
                        }

                    for (direction in toApply.dropLast(1).reversed()) {
                        machine.feedInputsAndRun(direction.reverse().raw)
                        runs += 1

                        robotPosition = robotPosition.move(direction.reverse())
                        yield(Unit)
                    }

                    for (direction in toReverse) {
                        machine.feedInputsAndRun(direction.raw)
                        runs += 1

                        robotPosition = robotPosition.move(direction)
                        yield(Unit)
                    }
                }
                MOVED_OK -> {
                    lastOK = path
                    robotPosition = robotPosition.move(lastDirection)
                    map[robotPosition.second][robotPosition.first] = OPEN_TILE

                    Direction
                        .values()
                        .filter { robotPosition.move(it) !in visited }
                        .forEach {
                            visited.add(robotPosition.move(it))
                            queue.addLast(path + it)
                        }
                }
                MOVED_TO_GOAL -> { // found
                    robotPosition = robotPosition.move(lastDirection)
                    map[robotPosition.second][robotPosition.first] = GOAL

                    goal = robotPosition
                    ans = path.size
                    break
                }
                else -> { throw Exception("unexpected output $output") }
            }
        }
    }.iterator()

    fun advance() {
        if (logic.hasNext()) {
            logic.next()
        }
    }
}