package nicestring

fun String.isNice(): Boolean {
    val vowels = "aeiou"
    var badStrings = listOf("bu", "ba", "be")
    var badCount = 0;

    if(badStrings.any(::contains))
        badCount++

    if(vowels.sumOf {vowel -> this.count{it == vowel}} < 3)
        badCount++

    if(!zipWithNext().any{pair -> pair.first == pair.second})
        badCount++

    return badCount <= 1
}

