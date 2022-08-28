package problems

import machine.IntCodeMachine
import machine.instruction.Opcode
import problems.util.InputReader

enum class HullColor(val raw: Int) {
    BLACK(0),
    WHITE(1);

    companion object {
        fun fromRaw(raw: Int) = HullColor.values().firstOrNull { it.raw == raw }
    }
}

enum class RobotDirection {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class Robot {
    var position = Pair(0, 0)
    private var direction = RobotDirection.UP

    fun moveForward() {
        val (x, y) = position
        position = when (direction) {
            RobotDirection.UP -> Pair(x, y-1)
            RobotDirection.DOWN -> Pair(x, y+1)
            RobotDirection.LEFT -> Pair(x-1, y)
            RobotDirection.RIGHT -> Pair(x+1, y)
        }
    }

    fun turnLeft() {
        direction = when (direction) {
            RobotDirection.UP -> RobotDirection.LEFT
            RobotDirection.DOWN -> RobotDirection.RIGHT
            RobotDirection.LEFT -> RobotDirection.DOWN
            RobotDirection.RIGHT -> RobotDirection.UP
        }
    }

    fun turnRight() {
        direction = when (direction) {
            RobotDirection.UP -> RobotDirection.RIGHT
            RobotDirection.DOWN -> RobotDirection.LEFT
            RobotDirection.LEFT -> RobotDirection.UP
            RobotDirection.RIGHT -> RobotDirection.DOWN
        }
    }
}

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day11.txt")

    val machine = IntCodeMachine(program.toMutableList())
    val robot = Robot()
    val hull = mutableMapOf<Pair<Int, Int>, HullColor>().withDefault { HullColor.BLACK }

    val positionsCovered = emptySet<Pair<Int, Int>>().toMutableSet()

    while (!machine.halted) {
        val color = hull.getValue(robot.position)
        machine.feedInputsAndRun(color.raw)
        val (colorToPaint, directionToTurn) = machine.output.takeLast(2)

        hull[robot.position] =
            HullColor.fromRaw(colorToPaint.toInt()) ?: throw Exception("unknown color to paint $colorToPaint")
        positionsCovered.add(robot.position)

        when (directionToTurn.toInt()) {
            0 -> robot.turnLeft()
            1 -> robot.turnRight()
            else -> throw Exception("unknown direction to turn $directionToTurn")
        }
        robot.moveForward()
    }

    println("part 1 answer: ${positionsCovered.size}")

    val machine2 = IntCodeMachine(program.toMutableList())
    val robot2 = Robot()
    val hull2 = mutableMapOf<Pair<Int, Int>, HullColor>().withDefault { HullColor.BLACK }
    hull2[Pair(0, 0)] = HullColor.WHITE

    val positionsCovered2 = emptySet<Pair<Int, Int>>().toMutableSet()

    while (!machine2.halted) {
        val color = hull2.getValue(robot2.position)
        machine2.feedInputsAndRun(color.raw)
        val (colorToPaint, directionToTurn) = machine2.output.takeLast(2)

        hull2[robot2.position] =
            HullColor.fromRaw(colorToPaint.toInt()) ?: throw Exception("unknown color to paint $colorToPaint")
        positionsCovered2.add(robot2.position)

        when (directionToTurn.toInt()) {
            0 -> robot2.turnLeft()
            1 -> robot2.turnRight()
            else -> throw Exception("unknown direction to turn $directionToTurn")
        }
        robot2.moveForward()
    }

    println("part 2 answer:")

    val xs = positionsCovered2.map { it.first }
    val ys = positionsCovered2.map { it.second }

    val minX = xs.min()
    val maxX = xs.max()

    val minY = ys.min()
    val maxY = ys.max()

    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val character = when(hull2.getValue(Pair(x, y))) {
                HullColor.WHITE -> '0'
                HullColor.BLACK -> ' '
            }
            print(character)
        }
        println()
    }
}