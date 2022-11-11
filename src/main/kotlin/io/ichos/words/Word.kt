package io.ichos.words

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class Word(private val lit: String) {
    val length: Int
        get() = lit.length

    fun toCharArray() = lit.toCharArray()

    fun asSequence() = lit.asSequence()

    fun asIterable() = lit.asIterable()

    fun contains(char: Char) = lit.contains(char)

    fun contains(other: CharSequence) = lit.contains(other)

    fun contains(word: Word) = lit.contains(word.toString())

    fun all(vararg chars: Char) = chars.all { lit.contains(it) }

    fun all(other: Word) = other.toString().all { lit.contains(it) }

    fun any(vararg chars: Char) = chars.any { lit.contains(it) }

    fun any(other: Word) = other.toString().any { lit.contains(it) }

    fun intersect(vararg chars: Char) = chars.filter { lit.contains(it) }

    fun intersect(other: Word) = other.toString().filter { lit.contains(it) }

    fun bangers(words: List<Word>) = words.filter { !any(it) }

    fun exists(letter: Char, index: Int) = toString()[index] == letter

    override fun toString() = lit
}
