package machine

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.Exception
import java.math.BigInteger

internal class IntCodeMachineTest {
    @Test
    fun add_Correctly() {
        val code = mutableListOf(1, 9, 10, 5, 99, 0, 0, 0, 0, 10, 20, 30)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(BigInteger.valueOf(30), machine.memory[5])
    }

    @Test
    fun multiply_Correctly() {
        val code = mutableListOf(2, 9, 10, 6, 99, 0, 0, 0, 0, 10, 20, 30)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(BigInteger.valueOf(200), machine.memory[6])
    }

    @Test
    fun halt_Correctly() {
        val code = mutableListOf(99)
        val machine = IntCodeMachine(code)

        assertDoesNotThrow {
            machine.run()
        }
        assertTrue(machine.halted)
    }

    @Test
    fun input_Correctly() {
        val code = mutableListOf(3, 5, 99, 0, 0, 0)
        val input = mutableListOf(1)
        val machine = IntCodeMachine(code, input)

        machine.run()

        assertEquals(BigInteger.valueOf(1), machine.memory[5])
    }

    @Test
    fun input_SuspendIfNoInput() {
        val code = mutableListOf(3, 5, 99, 0, 0, 0)
        val input = mutableListOf<Int>()
        val machine = IntCodeMachine(code, input)

        machine.run()

        assertFalse(machine.halted)
    }

    @Test
    fun output_Correctly() {
        val code = mutableListOf(4, 5, 99, 0, 0, 88)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(machine.output, listOf(BigInteger.valueOf(88)))
    }

    @Test
    fun jumpIfTrue_correctForTrue() {
        val code = mutableListOf(5, 10, 5, 99, 0, 7, 0, 4, 0, 99, 9999)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(machine.output, listOf(BigInteger.valueOf(5)))
    }

    @Test
    fun jumpIfTrue_correctForFalse() {
        val code = mutableListOf(5, 10, 5, 99, 0, 7, 0, 4, 0, 99, 0)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(machine.output, emptyList<BigInteger>())
    }

    @Test
    fun jumpIfFalse_correctForTrue() {
        val code = mutableListOf(6, 10, 5, 99, 0, 7, 0, 4, 0, 99, 0)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(machine.output, listOf(BigInteger.valueOf(6)))
    }

    @Test
    fun jumpIfFalse_correctForFalse() {
        val code = mutableListOf(6, 10, 5, 99, 0, 7, 0, 4, 0, 99, 9999)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(machine.output, emptyList<BigInteger>())
    }

    @Test
    fun lessThan_CorrectForLessThan() {
        val code = mutableListOf(7, 5, 6, 7, 99, 1, 2, -1)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(BigInteger.valueOf(1), machine.memory[7])
    }

    @Test
    fun lessThan_CorrectForNotLessThan() {
        val code = mutableListOf(7, 5, 6, 7, 99, 2, 1, -1)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(BigInteger.valueOf(0), machine.memory[7])
    }

    @Test
    fun equals_CorrectForEqual() {
        val code = mutableListOf(8, 5, 6, 7, 99, 1, 1, -1)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(BigInteger.valueOf(1), machine.memory[7])
    }

    @Test
    fun equals_CorrectForNotEqual() {
        val code = mutableListOf(8, 5, 6, 7, 99, 2, 1, -1)
        val machine = IntCodeMachine(code)

        machine.run()

        assertEquals(BigInteger.valueOf(0), machine.memory[7])
    }

    @Test
    fun feedInputAndRun_Correctly() {
        val code = mutableListOf(3, 5, 4, 5, 99, -1)
        val machine = IntCodeMachine(code)

        machine.run()
        // should be suspended
        assertFalse(machine.halted)
        machine.feedInputsAndRun(7)
        assertTrue(machine.halted)

        assertEquals(BigInteger.valueOf(7), machine.output[0])
    }

    @Test
    fun input_RelativeBaseCorrectly() {
        val code = mutableListOf(109, 5, 203, -4, 99)
        val input = mutableListOf(12223)
        val machine = IntCodeMachine(code, input)

        machine.run()
        assertTrue(machine.halted)

        assertEquals(BigInteger.valueOf(12223), machine.memory.getValue(5))
    }

    @Test
    fun shouldThrowErrorForUnknownOpcode() {
        val code = mutableListOf(98)
        val machine = IntCodeMachine(code)

        assertThrows<Exception> {
            machine.run()
        }
    }
}