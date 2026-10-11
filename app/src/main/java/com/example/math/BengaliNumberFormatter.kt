package com.example.math

object BengaliNumberFormatter {
    private val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBn(number: Int): String {
        return toBn(number.toString())
    }

    fun toBn(number: Long): String {
        return toBn(number.toString())
    }

    fun toBn(numberStr: String): String {
        val sb = StringBuilder()
        for (ch in numberStr) {
            if (ch in '0'..'9') {
                sb.append(bnDigits[ch - '0'])
            } else {
                sb.append(ch)
            }
        }
        return sb.toString()
    }
}
