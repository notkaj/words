package io.ichos.words.source

import io.ichos.words.Word
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.FileNotFoundException
import java.io.InputStream

object Source {
    private val stream: InputStream
        get() =
            Source::class.java
                .getResource("/words.json")?.openStream() ?:
                throw FileNotFoundException("Word List Missing")


    @OptIn(ExperimentalSerializationApi::class)
    fun getWords(predicate: (Word) -> Boolean) =
        Json.decodeFromStream<List<Word>>(stream).filter(predicate)

    @OptIn(ExperimentalSerializationApi::class)
    fun getWords() = Json.decodeFromStream<List<Word>>(stream)
}