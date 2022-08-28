package machine.instruction

enum class Opcode (val opcode: Int,
                   val numberOfParameters: Int,
                   val parametersToResolve: Int = numberOfParameters,
                   val autoIncrementInstructionPointer: Boolean = true
) {
    ADD(1, 3, 2),
    MULTIPLY(2, 3, 2),
    INPUT(3, 1, 0, false),
    OUTPUT(4, 1),
    JUMP_IF_TRUE(5, 2, autoIncrementInstructionPointer = false),
    JUMP_IF_FALSE(6, 2, autoIncrementInstructionPointer = false),
    LESS_THAN(7, 3, 2),
    EQUALS(8, 3, 2),
    ADJUST_RELATIVE_BASE(9, 1),
    HALT(99, 0);

    companion object {
        fun fromRaw(raw: Int) = Opcode.values().firstOrNull { it.opcode == raw }
    }
}
