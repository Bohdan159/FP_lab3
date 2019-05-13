package thirdlab.sorter


class SortingResult(
    private val temporarySources: Array<DataSource>,
    private val comparer: Comparator<String>?) {


    fun mergeSources() = temporarySources
        .map { it.readLines.invoke() }
        .reduce { acc, unit -> unit
            .merge(acc, comparer)
            .toList()
            .toTypedArray()
        }

    fun clearTempSources() = temporarySources.all { it.delete() }

    private fun Array<out String>.merge(seqB: Array<out String>, comparer: Comparator<String>?) = sequence {
        val compare: (String, String) -> Int = { s1, s2 ->
            when (comparer != null) {
                true -> comparer.compare(s1, s2)
                false -> defaultComparison(s1, s2)
            }
        }

        val iteratorA = this@merge.iterator()
        val iteratorB = seqB.iterator()

        var a = ""
        var b = ""
        var hasA = false
        var hasB = false


        fun getNextA() {
            hasA = iteratorA.hasNext()
            a = if (hasA) iteratorA.next() else ""
        }

        fun getNextB() {
            hasB = iteratorB.hasNext()
            b = if (hasB) iteratorB.next() else ""
        }


        getNextA()
        getNextB()

        do {
            if (hasA && hasB) {
                if (compare.invoke(a, b) < 0) {
                    yield(a)
                    getNextA()
                } else {
                    yield(b)
                    getNextB()
                }
            } else if (hasA) {
                yield(a)
                getNextA()
            } else if (hasB) {
                yield(b)
                getNextB()
            }
        } while (hasA || hasB)
    }

    private fun defaultComparison(x: String, y: String) = x.compareTo(y)
}