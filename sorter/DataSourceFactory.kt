package thirdlab.sorter

import java.nio.file.Files
import java.nio.file.Paths


class DataSourceFactory {

    companion object {
        fun create(options: Options) = DataSource(
            readLines = { Files.readAllLines(Paths.get(options.sourcePath)).toTypedArray() },
            linesInBatch = options.batchSize!!,
            saveLines = { lines ->
                val tempFile = getTempFilePath(options)

                Files.write(Paths.get(tempFile), lines.toMutableList())

                return@DataSource DataSource(
                    readLines = { Files.readAllLines(Paths.get(tempFile)).toTypedArray() },
                    linesInBatch = options.batchSize!!,
                    deleteSource = {
                        Files.deleteIfExists(Paths.get(tempFile))
                    }
                )
            },
            comparer = DefaultComparer()
        )

        private fun getTempFilePath(options: Options) = Files
            .createTempFile(
                Paths.get(options.tempDirectory),
                "temp",
                ".tmp"
            ).toString()
    }
}