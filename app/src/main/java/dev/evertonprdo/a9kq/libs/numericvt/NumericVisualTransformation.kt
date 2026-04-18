package dev.evertonprdo.a9kq.libs.numericvt

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class NumericVisualTransformation(
    val numberFormatter: NumberFormatter = NumberFormatter()
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {

        val annotatedString = buildAnnotatedString {
            val (integer, decimal) = numberFormatter.extractFormattedWholeAndDecimalPart(text.text)

            append(integer)
            append(numberFormatter.decimalSeparator)
            append(decimal)

            toAnnotatedString()
        }

        val offsetMapping =
            MaskedDecimalOffsetMapping(
                text.length,
                numberFormatter.decimalLength,
                numberFormatter.groupLength
            )

        return TransformedText(annotatedString, offsetMapping)
    }
}