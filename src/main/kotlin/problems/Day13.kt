package problems

import machine.IntCodeMachine
import problems.util.InputReader
import java.math.BigInteger

fun printScreen(screen:  MutableMap<Pair<Int, Int>, Int>, score: BigInteger) {
    val minX = screen.minOf { it.key.first }
    val maxX = screen.maxOf { it.key.first }

    val minY = screen.minOf { it.key.second }
    val maxY = screen.maxOf { it.key.second }

    repeat(10) {
        println()
    }
    println("score: $score")
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            val character = when(screen[Pair(x, y)]) {
                0 -> ' '
                1 -> 'W'
                2 -> 'B'
                3 -> '_'
                4 -> 'o'
                else -> '?'
            }
            print(character)
        }
        println()
    }
}

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day13.txt")

    val machine = IntCodeMachine(program.toMutableList())
    machine.run()

    val screen: MutableMap<Pair<Int, Int>, Int> = emptyMap<Pair<Int, Int>, Int>().toMutableMap().withDefault { 0 }
    machine.output.chunked(3).forEach {
        (x, y, tile) -> screen[Pair(x.toInt(), y.toInt())] = tile.toInt()
    }
    val blocks = screen.filterValues { it == 2 }
    println("part 1 answer: ${blocks.size}")

    val hackedProgram = program.toMutableList().apply { this[0] = BigInteger.valueOf(2)}

    val machine2 = IntCodeMachine(hackedProgram)

    var score = BigInteger.valueOf(-1)
    val screen2: MutableMap<Pair<Int, Int>, Int> = emptyMap<Pair<Int, Int>, Int>().toMutableMap().withDefault { 0 }

    machine2.run()
    while (!machine2.halted) {
        machine2.output.chunked(3).forEach {
            (x, y, tile) -> if(x.toInt() == -1 && y.toInt() == 0) {
                score = tile
            } else {
                screen2[Pair(x.toInt(), y.toInt())] = tile.toInt()
            }
        }
        val ball = screen2.filterValues { it == 4 }
        val paddle = screen2.filterValues { it == 3 }

        val (ballX, _) = ball.keys.first()
        val (paddleX, _) = paddle.keys.first()

        var input = 0
        if (paddleX < ballX) {
            input = 1
        } else if (paddleX > ballX) {
            input = -1
        }

        machine2.feedInputsAndRun(input)
//        printScreen(screen2, score)
//        Thread.sleep(2)
    }

    machine2.output.chunked(3).forEach {
            (x, y, tile) -> if(x.toInt() == -1 && y.toInt() == 0) {
        score = tile
        } else {
            screen2[Pair(x.toInt(), y.toInt())] = tile.toInt()
        }
    }
//    printScreen(screen2, score)
    println("part 2 answer: $score")
}