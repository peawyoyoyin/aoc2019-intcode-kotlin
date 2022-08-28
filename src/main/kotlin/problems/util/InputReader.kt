package problems.util

import java.io.File
import java.math.BigInteger

object InputReader {
    fun readInputFromFile(filename: String): List<Int> {
        val inputFile = File(filename)
        return inputFile.readText().split(',').map { it.toInt() }
    }

    fun readBigInputFromFile(filename: String): List<BigInteger> {
        val inputFile = File(filename)
        return inputFile.readText().split(',').map { BigInteger(it) }
    }
}