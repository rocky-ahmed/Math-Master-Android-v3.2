package com.example.math

data class MultiplicationRow(
    val multiplier: Int,
    val multiplicand: Int,
    val product: Int
) {
    val bnMultiplier: String get() = BengaliNumberFormatter.toBn(multiplier)
    val bnMultiplicand: String get() = BengaliNumberFormatter.toBn(multiplicand)
    val bnProduct: String get() = BengaliNumberFormatter.toBn(product)
    val formulaBn: String get() = "$bnMultiplier × $bnMultiplicand = $bnProduct"
}

data class MultiplicationTable(
    val number: Int,
    val rows: List<MultiplicationRow>
) {
    val bnNumber: String get() = BengaliNumberFormatter.toBn(number)
    val titleBn: String get() = "$bnNumber এর ঘরের নামতা"
}

enum class TableRange(val rangeId: String, val titleBn: String, val startNum: Int, val endNum: Int) {
    RANGE_1_10("range_1_10", "১ থেকে ১০ এর নামতা", 1, 10),
    RANGE_11_20("range_11_20", "১১ থেকে ২০ এর নামতা", 11, 20),
    RANGE_21_30("range_21_30", "২১ থেকে ৩০ এর নামতা", 21, 30),
    RANGE_31_40("range_31_40", "৩১ থেকে ৪০ এর নামতা", 31, 40)
}

object MultiplicationProvider {
    fun getTable(n: Int): MultiplicationTable {
        val rows = (1..10).map { i ->
            MultiplicationRow(multiplier = n, multiplicand = i, product = n * i)
        }
        return MultiplicationTable(number = n, rows = rows)
    }

    fun getTablesForRange(range: TableRange): List<MultiplicationTable> {
        return (range.startNum..range.endNum).map { getTable(it) }
    }
}
