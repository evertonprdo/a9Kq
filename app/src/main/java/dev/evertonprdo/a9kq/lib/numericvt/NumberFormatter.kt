package dev.evertonprdo.a9kq.lib.numericvt

class NumberFormatter(
    val decimalLength: Int = 2,
    val groupLength: Int = 3,
    val decimalSeparator: Char = ',',
    val groupSeparator: Char = '.'
) {
    private val padChar: Char = '0'

    fun extractIntegerGroupsAndDecimalFrom(rawDigits: String): Pair<List<String>, String> {
        val (integer, rawDecimal) = rawDigits.splitAt(-decimalLength)

        val chunkedGroup = integer.chunkedFromEnd(groupLength)
        val decimal = rawDecimal.padStart(decimalLength, padChar)

        return Pair(chunkedGroup, decimal)
    }

    fun extractFormattedWholeAndDecimalPart(rawDigits: String): Pair<String, String> {
        val (groups, decimal) = extractIntegerGroupsAndDecimalFrom(rawDigits)
        val integer = groups.joinToString(groupSeparator.toString()).padStart(1, padChar)

        return Pair(integer, decimal)
    }

    fun extractFormattedWholeAndDecimalPart(number: Long): Pair<String, String> {
        val number = number.toString()
        return extractFormattedWholeAndDecimalPart(number)
    }

    fun extractFlatIntegerAndDecimalFrom(rawDigits: String): Pair<String, String> {
        val (rawInteger, rawDecimal) = rawDigits.splitAt(-decimalLength)

        val integer = rawInteger.padStart(1, padChar)
        val decimal = rawDecimal.padStart(decimalLength, padChar)

        return Pair(integer, decimal)
    }
}