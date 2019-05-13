package thirdlab.generator

import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import kotlin.random.Random
import java.util.stream.Stream


class Generator(private val createRandom: ()-> Random, private val batchSize: Int) {

    private val lazyStrings: Array<String> by lazy { Data.randomPoemLines }
    private val strings: Array<String> by lazy { lazyStrings }


    private fun generateString(random: Random): String {
        val rsi = random.nextInt(0, strings.size)
        val rs = strings[rsi]
        val ri = random.nextInt(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())

        return "$ri. $rs"
    }

    private fun generateBatch(linesInBatch: Int): String {
        val random = createRandom()
        val presumableSize = 48 * linesInBatch
        val sb = StringBuilder(presumableSize)

        (0 until linesInBatch).forEach { _->
            sb.appendln(generateString(random))
        }

        return sb.toString()
    }

    fun generateTo(linesCount: Long, openOutput: () -> FileOutputStream) {
        val batches = when(linesCount > batchSize) {
            true -> (0..linesCount / batchSize)
                .map { batchSize }
                .filter { it != 0 }
                .toTypedArray()
            false -> arrayOfNulls<Int>(linesCount.toInt())
        }

        Stream.of(batches)
            .parallel()
            .map { generateBatch(it.first()!!) }
            .map { it.toByteArray() }
            .forEach { saveToStream(it, openOutput) }
    }

    private fun saveToStream(data: ByteArray, openOutput: () -> FileOutputStream) {
        synchronized(this) {
            openOutput().use { stream ->
                ByteArrayOutputStream().use { memory ->
                    memory.write(data)
                    memory.writeTo(stream)
                }
            }
        }
    }
}