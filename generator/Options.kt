package thirdlab.generator

import java.io.File


class Options {

    var lines: Long? = null
    var filePath: String? = null
    var batchSize: Int? = null

    val areValid: Boolean get() = !validationErrors.any()

    val validationErrors: List<String> by lazy { validate().toList() }


    private fun validate() = sequence {
        when (lines) {
            null -> yield("Number of lines must not be blank!")
            in Long.MIN_VALUE..0 -> yield("Number of lines must be positive!")
        }

        if (filePath.isNullOrEmpty() || filePath.isNullOrBlank()) {
            yield("Output file path must not be blank!")
        } else if (!File(filePath).isFile)
            yield("Output file does not exist!")

        when (batchSize) {
            null -> yield("Batch size must not be blank!")
            in Int.MIN_VALUE..0 -> yield("Batch size must be positive!")
        }
    }

    val usage = """
Generator usage:

    -l, --lines Required. Lines to generate

    -f, --file Output file path

    -b, --batch Batch size (count of items to generate in one step)

Example: dotnet run -c release -l 2000000000 --file "d:\tmp\big"
    """
}
