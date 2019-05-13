package thirdlab.generator

import java.time.Instant
import java.util.*
import java.io.FileOutputStream


fun main(args: Array<String>) = args.parse {options ->
    if (options.areValid) {
        println("Starting to generate strings to ${options.filePath} at ${Date.from(Instant.now())}")

        val lines = options.lines
        val generator = Generator(RandomFactory.create, options.batchSize!!)
        val openOutput = { FileOutputStream(options.filePath) }

        generator.generateTo(lines!!, openOutput)

        println("Done at ${Date.from(Instant.now())}")
    } else {
        options.validationErrors.forEach { error -> println(error) }.also { println(options.usage) }
    }
}

fun Array<String>.parse(action: (Options) -> Unit) {
    val options = Options()

    this.forEachIndexed { index, value ->
        when (value) {
            "-l" -> options.lines = this[index + 1].toLongOrNull()
            "-f" -> options.filePath = this[index + 1]
            "-b" -> options.batchSize = this[index + 1].toIntOrNull()
        }
    }

    action(options)
}