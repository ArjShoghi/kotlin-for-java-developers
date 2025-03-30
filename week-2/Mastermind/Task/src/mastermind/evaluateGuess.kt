package mastermind

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    val rightPosition = secret.zip(guess)
                              .count { (secretChar, guessChar) ->
                                  secretChar == guessChar
                              }

    val secretCountMap = getCharCountMap(secret)
    val guessCountMap = getCharCountMap(guess)

    val commonLetters = secretCountMap.values.zip(guessCountMap.values)
                                          .sumOf { (secretCount, guessCount) ->
                                              //the minimum of the counts will be
                                              // the count of letters that are in both strings
                                              Math.min(secretCount, guessCount)
                                          }
    val wrongPosition = commonLetters - rightPosition

    return Evaluation(rightPosition, wrongPosition)
}

fun getCharCountMap(string : String) : Map<Char,Int> {
    val charCountMap = mutableMapOf<Char,Int>()
    val possibleLetters = "ABCDEF"
    possibleLetters.forEach { char ->
        charCountMap[char] = string.count { char == it }
    }
    return charCountMap
}

