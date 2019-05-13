package thirdlab.sorter

import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import java.util.*


fun main(args: Array<String>) = args.parse {options ->
    if (options.areValid) {
        println("Starting to process ${options.sourcePath} at ${Date.from(Instant.now())}")

        val dataSource = DataSourceFactory.create(options)
        val sortingResult = dataSource.orderLines()

        Files.write(Paths.get(options.outputPath), sortingResult.mergeSources().toMutableList())

        println("Finished processing ${options.sourcePath} at ${Date.from(Instant.now())}")
        println("Cleaning temp files")

        sortingResult.clearTempSources()

        println("Done.")
    } else {
        options.validationErrors.forEach { error -> println(error) }.also { println(options.usage) }
    }
}

fun Array<String>.parse(action: (Options) -> Unit) {
    val options = Options()

    this.forEachIndexed { index, value ->
        when (value) {
            "-s" -> options.sourcePath = this[index + 1]
            "-o" -> options.outputPath = this[index + 1]
            "-t" -> options.tempDirectory = this[index + 1]
            "-b" -> options.batchSize = this[index + 1].toIntOrNull()
        }
    }

    action(options)
}