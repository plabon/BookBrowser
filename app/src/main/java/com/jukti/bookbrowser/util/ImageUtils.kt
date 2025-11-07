package com.jukti.bookbrowser.util


object ImageUtils {
    private const val COVER_BASE = "https://covers.openlibrary.org/b/id/"

    enum class Size(val code: String) {
        SMALL("S"), MEDIUM("M"), LARGE("L")
    }

    fun coverUrl(coverId: Int?, size: Size = Size.MEDIUM): String? {
        return coverId?.let { "$COVER_BASE${it}-${size.code}.jpg" }
    }
}

