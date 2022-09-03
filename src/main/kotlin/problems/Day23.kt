package problems

import machine.IntCodeMachine
import problems.util.InputReader
import java.math.BigInteger

fun main() {
    val program = InputReader.readBigInputFromFile("F:/code/kotlin/aoc2019-intcode/src/main/resources/inputs/day23.txt")

    val machines = (0 until 50).map { IntCodeMachine(program.toMutableList()) }
    val packetQueues = (0 until 50).map { mutableListOf<Pair<BigInteger, BigInteger>>() }

    for ((address, machine) in machines.withIndex()) {
        machine.feedInputsAndRun(address)
    }

    var foundPart1 = false

    var natPacket: Pair<BigInteger, BigInteger> = Pair(BigInteger.ZERO, BigInteger.ZERO)

    var lastSentNatPacket: Pair<BigInteger, BigInteger>? = null

    part2@while (true) {
        var hasNetworkActivity = false

        for ((address, machine) in machines.withIndex()) {
            // take output to packet queue
            if (machine.output.isNotEmpty()) {
                hasNetworkActivity = true

                for ((destination, X, Y) in machine.output.chunked(3)) {
                    if (destination.toInt() == 255) {
                        if (!foundPart1) {
                            println("part 1 ans: $Y")
                            foundPart1 = true
                        }
                        natPacket = Pair(X, Y)
                        println("[RNAT] NAT received packet $natPacket from $address")
                    } else {
                        println("[SEND] machine $address sending packet to $destination")
                        packetQueues[destination.toInt()].add(Pair(X, Y))
                    }
                }

                machine.flushOutput()
            }

            // take queue to input
            if (packetQueues[address].isNotEmpty()) {
                hasNetworkActivity = true
                while(packetQueues[address].isNotEmpty()) {
                    val (X, Y) = packetQueues[address].removeFirst()
                    println("[RECV] machine $address receiving packet")
                    machine.feedInputsAndRun(X, Y)
                }
            } else {
                machine.feedInputsAndRun(-1)
            }
        }

        if (!hasNetworkActivity) {
            println("[SNAT] NAT sending packet to 0 $natPacket")
            packetQueues[0].add(natPacket)
            if (natPacket == lastSentNatPacket) {
                break@part2
            }
            lastSentNatPacket = natPacket
        }
    }
    println("part 2 ans: ${natPacket.second}")
}