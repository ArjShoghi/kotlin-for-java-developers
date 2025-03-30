package rationals

import java.math.BigInteger


infix fun BigInteger.divBy(d : BigInteger) = Rational(this, d)
infix fun Int.divBy(d : Int) = Rational(toBigInteger(), d.toBigInteger())
infix fun Long.divBy(d : Long) = Rational(toBigInteger(), d.toBigInteger())

fun String.toRational() : Rational {
    val results = split("/")

    return when {
        results.size == 1 -> Rational(results[0].toBigInteger(), 1.toBigInteger()).normalize()
        else -> Rational(results[0].toBigInteger(), results[1].toBigInteger()).normalize()
    }
}

fun greatestCommonDivisor(x : BigInteger, y : BigInteger) : BigInteger {
    return when {
        y == 0.toBigInteger() -> x
        x == 0.toBigInteger() -> y
        x.abs() > y.abs() -> greatestCommonDivisor(x.rem(y).abs(), y.abs())
        else -> greatestCommonDivisor(x.abs(), y.rem(x).abs())
    }
}

class Rational(private var n : BigInteger, private var d : BigInteger ) : Comparable<Rational>{
    operator fun plus(rational: Rational) : Rational {
        val numerator = n.times(rational.d) + d.times(rational.n)
        val denominator = d.times(rational.d)
        return numerator divBy denominator
    }

    operator fun minus(rational: Rational) : Rational {
        val numerator = n.times(rational.d) - d.times(rational.n)
        val denominator = d.times(rational.d)
        return numerator divBy denominator
    }

    operator fun div(rational: Rational) : Rational {
        val numerator = n.times(rational.d)
        val denominator = d.times(rational.n)
        return numerator divBy denominator
    }

    operator fun times(rational: Rational) : Rational {
        val numerator = n.times(rational.n)
        val denominator = d.times(rational.d)
        return numerator divBy denominator
    }

    override fun compareTo(other: Rational) : Int {
        val r1 = n.times(other.d)
        val r2 = d.times(other.n)
        return r1.compareTo(r2)
    }

    fun normalize() : Rational {
        val gcd = greatestCommonDivisor(n, d)
        val rational = Rational(n.div(gcd), d.div(gcd))
        return when {
            rational.d < 0.toBigInteger() -> Rational(rational.n.negate(), rational.d.negate())
            else -> rational
        }
    }

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other !is Rational -> false
            else -> {
                val r1 = this.normalize()
                val r2 = other.normalize()
                r1.n == r2.n && r1.d == r2.d
            }
        }
    }

    override fun toString() : String {
        return when {
            n.rem(d) == 0.toBigInteger() -> n.div(d).toString()
            else -> {
                val r = normalize()
                r.n.toString() + "/" + r.d.toString()
            }
        }
    }

    operator fun unaryMinus() : Rational = Rational(n.negate(), d)
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy
            "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}