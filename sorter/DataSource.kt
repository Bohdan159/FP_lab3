package thirdlab.sorter


class DataSource(
    var readLines: () -> Array<out String>,
    var linesInBatch: Int? = null,
    var saveLines: ((Array<out String>) -> DataSource)? = null,
    var deleteSource: (() -> Boolean)? = null,
    var comparer: DefaultComparer? = null) {


    fun orderLines() = SortingResult(readLines.invoke()
        .asSequence()
        .chunked(linesInBatch!!)
        .map { it.toTypedArray() }
        .map { it.sortedArrayWith(comparer!!) }
        .map { saveLines?.invoke(it) ?: DataSource({ it }) }
        .toList()
        .toTypedArray(), comparer!!)

    fun delete() = deleteSource?.invoke() ?: false
}