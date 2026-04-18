package dev.evertonprdo.a9kq.lib.numericvt

import androidx.compose.ui.text.input.OffsetMapping

class MaskedDecimalOffsetMapping(
    private val originalTextLength: Int,
    private val decimalLength: Int,
    private val groupLength: Int
) : OffsetMapping {

    private val decimalSeparator = 1
    private val minWholeNumberLength = 1

    override fun originalToTransformed(offset: Int): Int {

        val threshold = minWholeNumberLength + decimalSeparator + decimalLength

        if (originalTextLength <= threshold - decimalSeparator) {
            var separatorPadding = 0
            if (originalTextLength > decimalLength && offset == 0) {
                separatorPadding += decimalSeparator
            }

            val delta = threshold - (originalTextLength + separatorPadding)
            return delta + offset
        }

        val wholeNumberLength = originalTextLength - decimalLength
        val remainingLength = wholeNumberLength % groupLength

        var cursorOffset = offset
        var separatorCount = (wholeNumberLength - 1) / groupLength

        if (offset >= wholeNumberLength)
            cursorOffset++

        if (remainingLength != 0 && offset >= remainingLength) {
            cursorOffset++
            separatorCount--
        }

        cursorOffset += ((offset - remainingLength)
            .coerceAtLeast(0) / groupLength)
            .coerceAtMost(separatorCount)

        return cursorOffset
    }

    override fun transformedToOriginal(offset: Int): Int {

        val threshold = minWholeNumberLength + decimalSeparator + decimalLength

        if (originalTextLength <= threshold - decimalSeparator) {
            var separatorPadding = 0
            if (originalTextLength > decimalLength && offset == 0) {
                separatorPadding += decimalSeparator
            }

            val delta = (originalTextLength + separatorPadding) - threshold
            return (delta + offset).coerceAtLeast(0)
        }

        val wholeNumberLength = originalTextLength - decimalLength
        val remainingLength = wholeNumberLength % groupLength
        val groupSizeWithSeparator = groupLength + 1

        var separatorCount = (wholeNumberLength - 1) / groupLength
        var cursorOffset = offset

        if (offset >= wholeNumberLength + separatorCount)
            cursorOffset--

        if (remainingLength != 0 && offset >= remainingLength) {
            cursorOffset--
            separatorCount--
        }

        cursorOffset -= ((offset - remainingLength)
            .coerceAtLeast(0) / groupSizeWithSeparator)
            .coerceAtMost(separatorCount)

        return cursorOffset
    }
}
