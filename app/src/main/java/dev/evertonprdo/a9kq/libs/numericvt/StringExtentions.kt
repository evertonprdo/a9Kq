package dev.evertonprdo.a9kq.libs.numericvt

fun String.splitAt(char: Char): Pair<String, String> {
    val substrings = split(char)
    return when (substrings.size) {
        2 -> Pair(substrings[0], substrings[1])
        in 0..1 -> Pair(this, "")
        else -> Pair(substrings[0], substrings.drop(1).joinToString(" "))
    }
}

fun String.splitAt(idx: Int): Pair<String, String> {
    val n = this.length
    val splitIndex = if (idx >= 0) idx else n + idx

    val i = splitIndex.coerceIn(0, n)
    val first = this.substring(0, i)
    val second = this.substring(i)

    return Pair(first, second)
}

fun String.chunkedFromEnd(size: Int): List<String> {
    require(size > 0) { "Chunk size must be positive, but was $size" }
    if (this.isEmpty()) return emptyList()

    val result = mutableListOf<String>()
    var i = this.length

    while (i > 0) {
        val start = (i - size).coerceAtLeast(0)
        result.add(this.substring(start, i))
        i -= size
    }

    return result.asReversed()
}

fun String.titlecase(): String =
    this
        .lowercase()
        .split(' ')
        .joinToString(" ") { it.replaceFirstChar { ch -> ch.uppercaseChar() } }

fun String.isNotEmptyOrNull(): String? = this.ifEmpty { null }

fun String.extractInitials(): String {
    val words = this.trim().split(' ').filter { it.isNotEmpty() }

    val first = words.firstOrNull()?.first()?.uppercase() ?: ""
    val last = if (words.size > 1) words.last().first().uppercase() else ""

    return "$first$last"
}