package machine.instruction

import java.lang.Exception

data class Instruction(val opcode: Opcode, val parameterModes: List<ParameterMode>) {
    companion object {
        fun fromRaw(rawInstruction: Int): Instruction {
            val opcode = Opcode.fromRaw(rawInstruction % 100)
                ?: throw Exception("unknown instruction $rawInstruction")

            var divider = 100
            val parameterModes = mutableListOf<ParameterMode>()
            for (i in 0 until opcode.numberOfParameters) {
                val result = rawInstruction / divider
                val parameterModeRaw = result % 10
                parameterModes.add(
                    ParameterMode.fromRaw(parameterModeRaw)
                        ?: throw Exception("unknown parameter mode $parameterModeRaw at instruction $rawInstruction")
                )
                divider *= 10
            }

            return Instruction(opcode, parameterModes)
        }
    }
}
