package io.ichos.words.analysis

import io.ichos.words.Word
import io.ichos.words.source.Source

fun main() {
    //BANGERS
//    val dwords = Source.getWords()
//    val words = dwords.filter { it.toCharArray().distinct().count() == it.length }
//    val score = letterCount(dwords)
//    val bangerList = findBangers(Word("crate"), words).associateWith { wordsScore(it.toList(), score) }
//    for (bangers in bangerList.toList().sortedBy { it.second}) {
//        for (banger in bangers.first) {
//            print("$banger, ")
//        }
//        println(bangers.second)
//    }
//    val max = (bangerList.maxOf { it.key.count() })
//    println("max banger: $max")
//    println("# of max bangers: ${bangerList.count {it.key.count() == max}}")

    //WORD SCORE
//    val words = Source.getWords()
//    val letterCount = letterCount(words)
//    for (letter in letterCount.toList().sortedByDescending { it.second })
//        println("${letter.first}: ${letter.second}")

//    FIND WORDS
//    val words = Source.getWords()
//    val r = findWords(words, 't', 'a', 'n')
//    r.sortedBy { it.toString() }.forEach {
//        println(it)
//    }

    //PLACEMENT
//    val words = Source.getWords()
//    placement(words).forEach {
//        println(it)
//    }

    //TOP RATED BANGERS
//    val dwords = Source.getWords()
//    val words = dwords.filter { it.toCharArray().distinct().count() == it.length }
//    val scores = bangerScores(words, dwords)
//    for (score in scores.toList().sortedByDescending { it.second }.take(100)) {
//        println("${score.first}: ${score.second}")
//    }


//    val dwords = Source.getWords()
//    val words = dwords.filter { it.toCharArray().distinct().count() == it.length }
//    val charScores = letterCount(dwords)
//    val topWords = words.associateWith { wordScore(it, charScores) }
//    topWords
//        .toList()
//        .sortedByDescending { it.second }
//        .take(20)
//        .forEach { println("${it.first}: ${it.second}") }

//    val words = Source.getWords()
//    val pos = possibilities(
//        words,
//        listOf(Placement('p', 0), Placement('r', 1), Placement('o', 2)),
//        listOf(),
//        listOf()
//    )
//    pos.forEach { println(it) }

    //PERCENTAGE BY CHARS
//    val words = Source.getWords()
//    val r = percentage(words, *"serum".toCharArray())
//    println("$r%")

    //PERCENTAGE RANKING
//    val words = Source.getWords()
//    val r = percentageRank(words)
//    r.toList().sortedByDescending { it.second }.take(50).forEach { (w, p) ->
//        println("$w: $p%")
//    }

//    val words = Source.getWords()
//    val r = letterPercentageRank(words)
//    r.toList().sortedByDescending { it.second }.forEach { (l, p) ->
//        println("$l: $p%")
//    }

    //2 layer percentage
    val words = Source.getWords()
    val top = wordPercentageRank(words).toList().maxByOrNull { it.second }!!.first
    val remaining = remaining(words, top)
    val r = wordPercentageRank(remaining)
    r.toList().sortedByDescending { it.second }.take(20).forEach { (w, p) ->
        println("$w: $p%")
    }
}


fun bangerScores(words: List<Word>, allWords: List<Word>): Map<Map<Word, Int>, Int> {
    val charScores = letterCount(allWords)
    val result = mutableMapOf<Map<Word, Int>, Int>()

    for (word in words) {
        val bangers = findBangers(word, words, 2)
        val max = bangers.maxOf { it.count() }
        val topBangers = bangers.filter { it.count() == max }
        val b = topBangers.map { o -> o.associateWith { u -> wordScore(u, charScores) } }
        val scores = b.associateWith { it.values.sum() }
        result.putAll(scores)
    }
    return result
}

fun wordsScore(words: List<Word>, scores: Map<Char, Int>) = words.sumOf { wordScore(it, scores) }

fun wordScore(word: Word, scores: Map<Char, Int>) = word.asIterable().sumOf { scores[it]!! }

fun findBangers(word: Word, words: List<Word>, max: Int? = null): Set<Set<Word>> {
    val all = mutableSetOf<Set<Word>>()

    fun inner(word: Word, words: List<Word>, result: Set<Word>) {
        val bangers = word.bangers(words)
        if (bangers.isEmpty() || (max != null && result.count() + 1 >= max)) {
            all.add(result + word)
            return
        }
        for (banger in bangers)
            inner(banger, bangers, result + word)
    }
    inner(word, words, setOf())
    return all
}

fun letterCount(words: List<Word>) =
    words.flatMap { it.asIterable() }.groupingBy { it }.eachCount()

fun letterCount(letter: Char, words: List<Word>) = words.flatMap { it.asIterable() }.count { it == letter }

fun findWords(words: List<Word>, vararg letters: Char) = words.filter { it.all(*letters) }

fun placement(words: List<Word>): Map<Char, Map<Int, Int>> {
    val chars = "abcdefghijklmnopqrstuvwxyz".associateWith { (0 until 5).associateWith { 0 }.toMutableMap() }

    for (word in words) {
        for ((i, l) in word.asIterable().withIndex()) {
            val map = chars[l]!!
            val inc = map[i]!! + 1
            map[i] = inc
        }
    }

    return chars
}

class Placement(val letter: Char, val index: Int)

fun possibilities(words: List<Word>, hits: List<Placement>, misses: List<Placement>, absents: List<Char>) =
    words.filter { w ->
        hits.all { w.exists(it.letter, it.index) } &&
                misses.all { !w.exists(it.letter, it.index) && w.contains(it.letter) } &&
                absents.all { !w.contains(it) }
    }

fun percentage(words: List<Word>, vararg letters: Char): Double {
    val count = words.count()
    val num = words.count { it.any(*letters) }
    return num.toDouble() / count * 100
}

fun wordPercentageRank(words: List<Word>) =
    words.associateWith { percentage(words, *it.toCharArray()) }

fun remaining(words: List<Word>, word: Word) = words.filter { !it.any(word) }


fun letterPercentageRank(words: List<Word>) =
    "abcdefghijklmnopqrstuvwxyz".toCharArray().associateWith { percentage(words, it) }