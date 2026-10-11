package com.example.math

data class PrimeNumberInfo(
    val number: Int,
    val isPrime: Boolean,
    val explanationBn: String,
    val factors: List<Int>
) {
    val bnNumber: String get() = BengaliNumberFormatter.toBn(number)
}

enum class PrimeRange(val rangeId: String, val titleBn: String, val startNum: Int, val endNum: Int, val expectedCount: Int) {
    RANGE_1_100("prime_1_100", "১ থেকে ১০০ এর মৌলিক সংখ্যা (২৫ টি)", 1, 100, 25),
    RANGE_101_200("prime_101_200", "১০১ থেকে ২০০ এর মৌলিক সংখ্যা (২১ টি)", 101, 200, 21)
}

data class PrimeDecadeGroup(
    val rangeLabelBn: String,
    val startNum: Int,
    val endNum: Int,
    val magicDigit: Int,
    val primes: List<Int>
) {
    val magicDigitBn: String get() = BengaliNumberFormatter.toBn(magicDigit)
    val primesBn: String get() = primes.joinToString(", ") { BengaliNumberFormatter.toBn(it) }
}

data class PrimeMagicCodeInfo(
    val range: PrimeRange,
    val codeString: String,
    val codeDisplayEn: String,
    val formattedCodeBn: String,
    val formulaExplanationBn: String,
    val tipBn: String,
    val totalPrimes: Int,
    val decadeGroups: List<PrimeDecadeGroup>
)

object PrimeNumberProvider {
    fun isPrime(n: Int): Boolean {
        if (n <= 1) return false
        if (n <= 3) return true
        if (n % 2 == 0 || n % 3 == 0) return false
        var i = 5
        while (i <= n / i) {
            if (n % i == 0 || (i + 2 <= n && n % (i + 2) == 0)) return false
            if (i > Int.MAX_VALUE - 6) break
            i += 6
        }
        return true
    }

    fun getFactors(n: Int): List<Int> {
        if (n <= 0) return emptyList()
        val factors = mutableListOf<Int>()
        var i = 1
        while (i <= n / i) {
            if (n % i == 0) {
                factors.add(i)
                if (i != n / i) {
                    factors.add(n / i)
                }
            }
            i++
        }
        return factors.sorted()
    }

    fun getPrimesInRange(start: Int, end: Int): List<Int> {
        return (start..end).filter { isPrime(it) }
    }

    fun getPrimeInfo(n: Int): PrimeNumberInfo {
        val prime = isPrime(n)
        val factors = getFactors(n)
        val bnNum = BengaliNumberFormatter.toBn(n)
        val explanation = when {
            n <= 1 -> "$bnNum মৌলিক বা যৌগিক কোনো সংখ্যাই নয়।"
            prime -> "$bnNum একটি মৌলিক সংখ্যা। এর কেবল দুটি গুণনীয়ক রয়েছে: ১ এবং $bnNum।"
            else -> {
                val factorsBn = factors.joinToString(", ") { BengaliNumberFormatter.toBn(it) }
                "$bnNum একটি যৌগিক সংখ্যা। এর গুণনীয়কগুলো হলো: $factorsBn।"
            }
        }
        return PrimeNumberInfo(
            number = n,
            isPrime = prime,
            explanationBn = explanation,
            factors = factors
        )
    }

    // Quick verification: 1-100 has 25 primes, 101-200 has 21 primes
    val primes1To100: List<Int> by lazy { getPrimesInRange(1, 100) }
    val primes101To200: List<Int> by lazy { getPrimesInRange(101, 200) }

    // --- Magic Code Technique Models ---
    val magicCode1To100: PrimeMagicCodeInfo by lazy {
        val decades = listOf(
            PrimeDecadeGroup("১ - ১০", 1, 10, 4, listOf(2, 3, 5, 7)),
            PrimeDecadeGroup("১১ - ২০", 11, 20, 4, listOf(11, 13, 17, 19)),
            PrimeDecadeGroup("২১ - ৩০", 21, 30, 2, listOf(23, 29)),
            PrimeDecadeGroup("৩১ - ৪০", 31, 40, 2, listOf(31, 37)),
            PrimeDecadeGroup("৪১ - ৫০", 41, 50, 3, listOf(41, 43, 47)),
            PrimeDecadeGroup("৫১ - ৬০", 51, 60, 2, listOf(53, 59)),
            PrimeDecadeGroup("৬১ - ৭০", 61, 70, 2, listOf(61, 67)),
            PrimeDecadeGroup("৭১ - ৮০", 71, 80, 3, listOf(71, 73, 79)),
            PrimeDecadeGroup("৮১ - ৯০", 81, 90, 2, listOf(83, 89)),
            PrimeDecadeGroup("৯১ - ১০০", 91, 100, 1, listOf(97))
        )
        PrimeMagicCodeInfo(
            range = PrimeRange.RANGE_1_100,
            codeString = "4422322321",
            codeDisplayEn = "44 - 222 - 322 - 321",
            formattedCodeBn = "৪৪ - ২২২ - ৩২২ - ৩২১ (বা ৪৪ - ২২ - ৩২ - ২৩ - ২১)",
            formulaExplanationBn = "৪ + ৪ + ২ + ২ + ৩ + ২ + ২ + ৩ + ২ + ১ = ২৫ টি মৌলিক সংখ্যা",
            tipBn = "ম্যাজিক কোড: '44 - 222 - 322 - 321' (৪৪-২২-৩২-২৩-২১)। ১০ ডিজিটের মোবাইল নম্বরের মতো মনে রেখে যেকোনো সীমার মধ্যকার মৌলিক সংখ্যা চটজলদি বের করুন!",
            totalPrimes = 25,
            decadeGroups = decades
        )
    }

    val magicCode101To200: PrimeMagicCodeInfo by lazy {
        val decades = listOf(
            PrimeDecadeGroup("১০১ - ১১০", 101, 110, 4, listOf(101, 103, 107, 109)),
            PrimeDecadeGroup("১১১ - ১২০", 111, 120, 1, listOf(113)),
            PrimeDecadeGroup("১২১ - ১৩০", 121, 130, 1, listOf(127)),
            PrimeDecadeGroup("১৩১ - ১৪০", 131, 140, 3, listOf(131, 137, 139)),
            PrimeDecadeGroup("১৪১ - ১৫০", 141, 150, 1, listOf(149)),
            PrimeDecadeGroup("১৫১ - ১৬০", 151, 160, 2, listOf(151, 157)),
            PrimeDecadeGroup("১৬১ - ১৭০", 161, 170, 2, listOf(163, 167)),
            PrimeDecadeGroup("১৭১ - ১৮০", 171, 180, 2, listOf(173, 179)),
            PrimeDecadeGroup("১৮১ - ১৯০", 181, 190, 1, listOf(181)),
            PrimeDecadeGroup("১৯১ - ২০০", 191, 200, 4, listOf(191, 193, 197, 199))
        )
        PrimeMagicCodeInfo(
            range = PrimeRange.RANGE_101_200,
            codeString = "4113122214",
            codeDisplayEn = "411 - 312 - 214",
            formattedCodeBn = "৪১১ - ৩১২ - ২১৪ (বা ৪১ - ১৩ - ১২ - ২২ - ১৪)",
            formulaExplanationBn = "৪ + ১ + ১ + ৩ + ১ + ২ + ২ + ২ + ১ + ৪ = ২১ টি মৌলিক সংখ্যা",
            tipBn = "ম্যাজিক কোড: '411 - 312 - 214' (৪১-১৩-১২-২২-১৪)। এর মাধ্যমে ১০১ থেকে ২০০ এর যেকোনো রেঞ্জের মৌলিক সংখ্যা নিমিষেই হিসাব করা যায়!",
            totalPrimes = 21,
            decadeGroups = decades
        )
    }

    fun getMagicCodeForRange(range: PrimeRange): PrimeMagicCodeInfo {
        return when (range) {
            PrimeRange.RANGE_1_100 -> magicCode1To100
            PrimeRange.RANGE_101_200 -> magicCode101To200
        }
    }
}
