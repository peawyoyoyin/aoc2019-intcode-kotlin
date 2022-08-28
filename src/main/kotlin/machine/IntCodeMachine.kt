package machine

import machine.instruction.Instruction
import machine.instruction.Opcode
import machine.instruction.ParameterMode
import java.math.BigInteger

class IntCodeMachine(
    val memory: MutableMap<Int, BigInteger> = createMemory(),
    private val input: MutableList<BigInteger> = mutableListOf()
) {
    companion object {
        fun createMemory() = mutableMapOf<Int, BigInteger>().withDefault { BigInteger.ZERO }
    }

    constructor(memoryAsList: MutableList<Int>, input: MutableList<Int> = mutableListOf()) : this(
        createMemory(), input.map { BigInteger.valueOf(it.toLong()) }.toMutableList()
    ) {
        memoryAsList.forEachIndexed { index, i ->
            memory[index] = BigInteger.valueOf(i.toLong())
        }
    }

    constructor(memoryAsList: MutableList<BigInteger>) : this(
        createMemory(), emptyList<BigInteger>().toMutableList()
    ) {
        memoryAsList.forEachIndexed { index, i ->
            memory[index] = i
        }
    }

    val output: MutableList<BigInteger> = mutableListOf()
    var halted: Boolean = false

    private var instructionPointer = 0
    private var relativeBase = 0

    private fun resolveParameter(value: BigInteger, mode: ParameterMode): BigInteger {
        return when (mode) {
            ParameterMode.POSITION -> memory.getValue(value.toInt())
            ParameterMode.IMMEDIATE -> value // TODO Add tests for parameter mode
            ParameterMode.RELATIVE -> memory.getValue(value.toInt()+relativeBase)
        }
    }
    private fun resolveOutputParameter(value: BigInteger, mode: ParameterMode): BigInteger {
        return when (mode) {
            ParameterMode.POSITION -> value
            ParameterMode.IMMEDIATE -> value // TODO Add tests for parameter mode
            ParameterMode.RELATIVE -> value + BigInteger.valueOf(relativeBase.toLong())
        }
    }

    private fun readParams(instruction: Instruction, block: (params: List<BigInteger>) -> Unit) {
        val (opcode, modes) = instruction

        val resolvedParams = memory
            .filterKeys { it in instructionPointer+1..instructionPointer+opcode.numberOfParameters+1 }
            .toList()
            .sortedBy { it.first }
            .map { it.second }
            .zip(modes)
            .mapIndexed {
                index, (value, mode) -> if (index < opcode.parametersToResolve) {
                    resolveParameter(value, mode)
                } else {
                    resolveOutputParameter(value, mode)
                }
            }

        block(resolvedParams)

        if (opcode.autoIncrementInstructionPointer) {
            instructionPointer += opcode.numberOfParameters + 1
        }
    }

    private fun conditionalJump(condition: Boolean, jumpTo: Int, elseTo: Int) {
        instructionPointer = if(condition) jumpTo else elseTo
    }

    fun feedInputsAndRun(vararg inputs: Int) {
        feedInputsAndRun(*inputs.map { BigInteger.valueOf(it.toLong()) }.toTypedArray())
    }

    fun feedInputsAndRun(vararg inputs: BigInteger) {
        input.addAll(inputs.toList())
        run()
    }

    fun run() {
        var shouldContinue = true
        while (shouldContinue) {
            val instruction = Instruction.fromRaw(memory.getValue(instructionPointer).toInt())

            readParams(instruction) {
                when (instruction.opcode) {
                    Opcode.ADD -> memory[it[2].toInt()] = it[0] + it[1]
                    Opcode.MULTIPLY -> memory[it[2].toInt()] = it[0] * it[1]
                    Opcode.LESS_THAN -> memory[it[2].toInt()] = if (it[0] < it[1]) BigInteger.ONE else BigInteger.ZERO
                    Opcode.EQUALS -> memory[it[2].toInt()] = if (it[0] == it[1]) BigInteger.ONE else BigInteger.ZERO
                    Opcode.INPUT -> {
                        if (this.input.size > 0) {
                            memory[it[0].toInt()] = input.removeFirst()
                            instructionPointer += 2
                        } else {
                            // suspend the machine and wait for the next input
                            shouldContinue = false
                        }
                    }
                    Opcode.OUTPUT -> output.add(it[0])
                    Opcode.JUMP_IF_TRUE -> conditionalJump(it[0] != BigInteger.ZERO, it[1].toInt(), instructionPointer + 3)
                    Opcode.JUMP_IF_FALSE -> conditionalJump(it[0] == BigInteger.ZERO, it[1].toInt(), instructionPointer + 3)
                    Opcode.ADJUST_RELATIVE_BASE -> relativeBase += it[0].toInt()
                    Opcode.HALT -> {
                        halted = true
                        shouldContinue = false
                    }
                }
            }
        }
    }
}
