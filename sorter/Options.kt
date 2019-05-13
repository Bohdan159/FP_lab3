package thirdlab.sorter

import java.io.File


class Options {

    var sourcePath: String? = null
    var outputPath: String? = null
    var tempDirectory: String? = null
    var batchSize: Int? = null

    val areValid: Boolean get() = !validationErrors.any()

    val validationErrors: List<String> by lazy { validate().toList() }


    private fun validate() = sequence {
        mapOf(
            sourcePath to "Source file",
            outputPath to "Output file"
        ).forEach {
            if (it.key.isNullOrEmpty() || it.key.isNullOrBlank()) {
                yield("${it.value} must not be blank!")
            } else if (!File(it.key).isFile) yield("${it.value} does not exist!")
        }

        if (tempDirectory.isNullOrEmpty() || tempDirectory.isNullOrBlank()) {
            yield("Temp directory path must not be blank!")
        } else if (!File(tempDirectory).isDirectory)
            yield("Temp directory does not exist!")

        when (batchSize) {
            null -> yield("Batch size must not be blank!")
            in Int.MIN_VALUE..0 -> yield("Batch size must be positive!")
        }
    }

    val usage = """
Sorter usage:

    -s, --source Required. Source file with the contents to sort

    -o, --output File to save sorting results in

    -t, --temp Directory to save temporary files in

    -b, --batch Batch size (count of items to sort in one step)

Example: dotnet run -c release -s "D:\tmp\big" -t "D:\tmp\b" -o "D:\tmp\big.res" -b 33000000
    """
}
