package machine.instruction

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class InstructionTest {
    @Test
    fun shouldParseOpCodeCorrectly() {
        assertEquals(Opcode.INPUT, Instruction.fromRaw(3).opcode)
    }

    @Test
    fun shouldThrowErrorForIncorrectOpcode() {
        assertThrows<Exception> {
            Instruction.fromRaw(98)
        }
    }

    @Test
    fun shouldParseParameterModesCorrectly() {
        assertEquals(
            listOf(ParameterMode.POSITION, ParameterMode.IMMEDIATE, ParameterMode.POSITION),
            Instruction.fromRaw(1002).parameterModes
        )
    }

    @Test
    fun shouldThrowErrorForIncorrectParameterMode() {
        assertThrows<Exception> {
            Instruction.fromRaw(902)
        }
    }
}