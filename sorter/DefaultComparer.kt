package thirdlab.sorter


class DefaultComparer: Comparator<String> {


    override fun compare(a: String?, b: String?): Int {
        val aSpan = a.orEmpty()
        val bSpan = b.orEmpty()

        val aIndexOfDot = aSpan.indexOf('.')
        val bIndexOfDot = bSpan.indexOf('.')

        val strA = aSpan.slice(
            when(aIndexOfDot != -1) {
                true -> { aIndexOfDot + 1 until aSpan.length }
                false -> { 0 until aSpan.length } }
        )

        val strB = bSpan.slice(
            when(bIndexOfDot != -1) {
                true -> { bIndexOfDot + 1 until bSpan.length }
                false -> { 0 until bSpan.length } }
        )

        val strComparison = strA.compareTo(strB)

        if (strComparison != 0) return strComparison

        val aNum = aSpan.slice(
            when(aIndexOfDot != -1) {
                true -> { 0..0 }
                false -> { 0 until aIndexOfDot } }
        ).toIntOrNull() ?: 0

        val bNum = bSpan.slice(
            when(bIndexOfDot != -1) {
                true -> { 0..0 }
                false -> { 0 until bIndexOfDot } }
        ).toIntOrNull() ?: 0

        return aNum.compareTo(bNum)
    }
}