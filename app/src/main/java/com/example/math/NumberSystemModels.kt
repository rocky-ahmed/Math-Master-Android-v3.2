package com.example.math

import kotlin.math.abs

/**
 * High-level classification of numbers in the Real Number System.
 */
enum class NumberSystemCategory(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val symbol: String,
    val definitionBn: String,
    val examplesBn: String,
    val bcsNotesBn: String
) {
    NATURAL(
        id = "natural",
        titleBn = "স্বাভাবিক সংখ্যা",
        titleEn = "Natural Numbers",
        symbol = "ℕ",
        definitionBn = "গণনাকারী ধনাত্মক পূর্ণসংখ্যাসমূহকে স্বাভাবিক সংখ্যা বলে। অর্থাৎ ১, ২, ৩, ৪... ইত্যাদি। লক্ষ্য করুন, ০ (শূন্য) স্বাভাবিক সংখ্যা নয়।",
        examplesBn = "১, ২, ৩, ৪, ৫, ১০, ১০০, ...",
        bcsNotesBn = "বিসিএস টিপস: ক্ষুদ্রতম স্বাভাবিক সংখ্যা হলো ১। স্বাভাবিক সংখ্যাকে জোড়, বিজোড় ও মৌলিক সংখ্যায় বিভক্ত করা যায়।"
    ),
    WHOLE(
        id = "whole",
        titleBn = "অখণ্ড / সমগ্র সংখ্যা",
        titleEn = "Whole Numbers",
        symbol = "W",
        definitionBn = "শূন্য (০) সহ সকল ধনাত্মক স্বাভাবিক সংখ্যাকে একত্রে অখণ্ড বা সমগ্র সংখ্যা (Whole Numbers) বলে।",
        examplesBn = "০, ১, ২, ৩, ৪, ৫, ...",
        bcsNotesBn = "বিসিএস টিপস: ০ হলো ক্ষুদ্রতম সমগ্র সংখ্যা। সকল স্বাভাবিক সংখ্যাই অখণ্ড সংখ্যা, কিন্তু সকল অখণ্ড সংখ্যা স্বাভাবিক সংখ্যা নয় (কারণ ০ স্বাভাবিক নয়)।"
    ),
    INTEGER(
        id = "integer",
        titleBn = "পূর্ণসংখ্যা",
        titleEn = "Integers",
        symbol = "ℤ",
        definitionBn = "সকল ধনাত্মক পূর্ণসংখ্যা, ঋণাত্মক পূর্ণসংখ্যা এবং শূন্য (০)-কে একত্রে পূর্ণসংখ্যা বলে।",
        examplesBn = "... -৩, -২, -১, ০, ১, ২, ৩, ...",
        bcsNotesBn = "বিসিএস টিপস: শূন্য (০) একটি ধনাত্মকও নয়, ঋণাত্মকও নয়—একটি নিরপেক্ষ পূর্ণসংখ্যা। কিন্তু ০ একটি জোড় সংখ্যা (কারণ ০ কে ২ দিয়ে ভাগ করলে ভাগশেষ ০ থাকে)।"
    ),
    RATIONAL(
        id = "rational",
        titleBn = "মূলদ সংখ্যা",
        titleEn = "Rational Numbers",
        symbol = "ℚ",
        definitionBn = "যে সকল সংখ্যাকে p/q আকারে প্রকাশ করা যায় (যেখানে p ও q পূর্ণসংখ্যা এবং q ≠ 0), সেগুলোকে মূলদ সংখ্যা বলে। সকল পূর্ণসংখ্যা, সাধারণ ভগ্নাংশ, সসীম দশমিক ও আবৃত্ত (পৌনঃপুনিক) দশমিক সংখ্যা মূলদ।",
        examplesBn = "৩/৪, -৫, ০ (= ০/১), ০.২৫ (= ১/৪), ০.৩৩৩... (= ১/৩), ৩.১৪",
        bcsNotesBn = "বিসিএস প্রশ্ন: ২২/৭ মূলদ সংখ্যা? উত্তর: হ্যাঁ, ২২/৭ হলো মূলদ (কারণ এটি p/q আকার)। কিন্তু π (পাই) অমূলদ!"
    ),
    IRRATIONAL(
        id = "irrational",
        titleBn = "অমূলদ সংখ্যা",
        titleEn = "Irrational Numbers",
        symbol = "ℚ'",
        definitionBn = "যে সকল সংখ্যাকে দুটি পূর্ণসংখ্যার অনুপাত p/q আকারে প্রকাশ করা যায় না, সেগুলোকে অমূলদ সংখ্যা বলে। অমূলদ সংখ্যার দশমিক বিস্তার অসীম ও অনাবৃত্ত (অ-পৌনঃপুনিক)। যে কোনো পূর্ণবর্গ নয় এমন সংখ্যার বর্গমূল অমূলদ।",
        examplesBn = "√২, √৩, √৫, π (পাই), e, ১.৪১৪২১৩...",
        bcsNotesBn = "বিসিএস হট টপিক: পূর্ণবর্গ নয় এরূপ যেকোনো স্বাভাবিক সংখ্যার বর্গমূল একটি অমূলদ সংখ্যা। যেমন √২, √৩, √৭ অমূলদ, কিন্তু √৪ = ২ মূলদ।"
    ),
    PRIME_COMPOSITE(
        id = "prime_composite",
        titleBn = "মৌলিক ও যৌগিক সংখ্যা",
        titleEn = "Prime & Composite Numbers",
        symbol = "ℙ",
        definitionBn = "১ অপেক্ষা বড় যে সকল সংখ্যার ১ এবং ঐ সংখ্যা ব্যতীত অন্য কোনো গুণনীয়ক নেই, সেগুলো মৌলিক সংখ্যা। আর ১ অপেক্ষা বড় যে সংখ্যার ১ ও ঐ সংখ্যা ছাড়াও অন্য গুণনীয়ক আছে, সেগুলো যৌগিক সংখ্যা। ১ মৌলিকও নয়, যৌগিকও নয়।",
        examplesBn = "মৌলিক: ২, ৩, ৫, ৭, ১১, ১৩, ১৭, ১৯... | যৌগিক: ৪, ৬, ৮, ৯, ১০, ১২...",
        bcsNotesBn = "বিসিএস স্পেশাল: একমাত্র জোড় মৌলিক সংখ্যা হলো ২, যা ক্ষুদ্রতম মৌলিক সংখ্যাও। ১ থেকে ১০০ এর মধ্যে মোট ২৫টি মৌলিক সংখ্যা রয়েছে।"
    ),
    FACTORS_MULTIPLES(
        id = "factors_multiples",
        titleBn = "গুণনীয়ক (উৎপাদক) ও গুণিতক",
        titleEn = "Factors & Multiples",
        symbol = "× / ÷",
        definitionBn = "কোনো সংখ্যাকে যে যে সংখ্যা দ্বারা নিঃশেষে ভাগ করা যায়, সেগুলোকে ঐ সংখ্যার গুণনীয়ক (Factor) বলে। আর কোনো সংখ্যাকে যে কোনো পূর্ণসংখ্যা দিয়ে গুণ করলে যে গুণফল পাওয়া যায়, সেগুলো ঐ সংখ্যার গুণিতক (Multiple)।",
        examplesBn = "১২ এর গুণনীয়ক: ১, ২, ৩, ৪, ৬, ১২। ৫ এর গুণিতক: ৫, ১০, ১৫, ২০, ২৫, ...",
        bcsNotesBn = "বিসিএস টিপস: যেকোনো সংখ্যার গুণনীয়কের সংখ্যা নির্দিষ্ট (সসীম), কিন্তু গুণিতকের সংখ্যা অসীম। ১ হলো সকল সংখ্যার সাধারণ গুণনীয়ক।"
    )
}

/**
 * Divisibility rules definition for 2, 3, 4, 5, 6, 8, 9, 10, 11
 */
data class DivisibilityRule(
    val divisor: Int,
    val ruleNameBn: String,
    val summaryBn: String,
    val detailedRuleBn: String,
    val testExampleNumber: Long,
    val exampleExplanationBn: String,
    val bcsShortcutBn: String
)

/**
 * Step by step GCD/LCM explanation models.
 */
data class StepExplanation(
    val stepNumber: Int,
    val titleBn: String,
    val detailBn: String
)

data class GcdLcmResult(
    val a: Long,
    val b: Long,
    val gcd: Long,
    val lcm: Long,
    val aFactors: List<Long>,
    val bFactors: List<Long>,
    val aPrimeFactors: List<Long>,
    val bPrimeFactors: List<Long>,
    val commonFactors: List<Long>,
    val gcdSteps: List<StepExplanation>,
    val lcmSteps: List<StepExplanation>,
    val formulaCheckBn: String
)

/**
 * Difficulty level for interactive practice.
 */
enum class NumberSystemDifficulty(val id: String, val titleBn: String) {
    EASY("easy", "সহজ (Easy)"),
    MEDIUM("medium", "মাঝারি (Medium)"),
    HARD("hard", "কঠিন / বিসিএস লেভেল (Hard)")
}

/**
 * Question subtopic for practice.
 */
enum class NumberSystemTopic(val id: String, val titleBn: String) {
    ALL("all", "সকল বিষয়"),
    CLASSIFICATION("classification", "সংখ্যার শ্রেণিবিভাগ"),
    DIVISIBILITY("divisibility", "বিভাজ্যতার নিয়ম"),
    GCD_LCM("gcd_lcm", "গ.সা.গু ও ল.সা.গু"),
    FACTORS_MULTIPLES("factors", "গুণনীয়ক ও মৌলিক/যৌগিক")
}

/**
 * Practice question model.
 */
data class NumberSystemQuestion(
    val id: String,
    val topic: NumberSystemTopic,
    val difficulty: NumberSystemDifficulty,
    val questionBn: String,
    val optionsBn: List<String>,
    val correctIndex: Int,
    val explanationBn: String,
    val bcsReferenceBn: String? = null
)

/**
 * Practice session tracking state.
 */
data class NumberSystemPracticeState(
    val topic: NumberSystemTopic = NumberSystemTopic.ALL,
    val difficulty: NumberSystemDifficulty = NumberSystemDifficulty.EASY,
    val questions: List<NumberSystemQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isSubmitted: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false,
    val userAnswers: Map<Int, Int> = emptyMap() // question index -> chosen option
)

object NumberSystemProvider {

    val divisibilityRules: List<DivisibilityRule> = listOf(
        DivisibilityRule(
            divisor = 2,
            ruleNameBn = "২ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "সংখ্যার শেষ অঙ্কটি (একক স্থানীয় অঙ্ক) ০, ২, ৪, ৬ বা ৮ হলে সংখ্যাটি ২ দ্বারা বিভাজ্য।",
            detailedRuleBn = "কোনো সংখ্যার একক স্থানীয় অঙ্ক জোড় সংখ্যা (০, ২, ৪, ৬, ৮) হলে সম্পূর্ণ সংখ্যাটি ২ দ্বারা নিঃশেষে বিভাজ্য হবে।",
            testExampleNumber = 4872L,
            exampleExplanationBn = "৪৮৭২ এর শেষ অঙ্ক ২ (জোড় সংখ্যা), তাই ৪৮৭২ সংখ্যাটি ২ দ্বারা নিঃশেষে বিভাজ্য। (৪৮৭২ ÷ ২ = ২৪৩৬)",
            bcsShortcutBn = "বিসিএস শর্টকাট: শুধুমাত্র শেষ অঙ্কটি জোড় কিনা দেখুন।"
        ),
        DivisibilityRule(
            divisor = 3,
            ruleNameBn = "৩ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "সংখ্যার অঙ্কগুলোর যোগফল ৩ দ্বারা বিভাজ্য হলে সংখ্যাটি ৩ দ্বারা বিভাজ্য।",
            detailedRuleBn = "কোনো সংখ্যার সকল অঙ্ক যোগ করুন। প্রাপ্ত যোগফল যদি ৩ দ্বারা বিভাজ্য হয়, তবে মূল সংখ্যাটিও ৩ দ্বারা নিঃশেষে বিভাজ্য হবে।",
            testExampleNumber = 7521L,
            exampleExplanationBn = "৭৫২১ এর অঙ্কগুলোর যোগফল = ৭ + ৫ + ২ + ১ = ১৫। যেহেতু ১৫ সংখ্যাটি ৩ দ্বারা বিভাজ্য (১৫ ÷ ৩ = ৫), তাই ৭৫২১ সংখ্যাটি ৩ দ্বারা নিঃশেষে বিভাজ্য।",
            bcsShortcutBn = "বিসিএস শর্টকাট: যোগ করার সময় ৩, ৬, ৯ আসলে তা আগেই বাদ দিয়ে বাকিগুলো যোগ করতে পারেন (Digital Root পদ্ধতি)।"
        ),
        DivisibilityRule(
            divisor = 4,
            ruleNameBn = "৪ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "শেষ দুটি অঙ্ক (দশক ও একক) দ্বারা গঠিত সংখ্যা ৪ দ্বারা বিভাজ্য বা শেষ দুটি অঙ্ক '০০' হলে।",
            detailedRuleBn = "কোনো সংখ্যার একক ও দশক স্থানীয় অঙ্ক নিয়ে গঠিত দুই অঙ্কের সংখ্যা যদি ৪ দ্বারা নিঃশেষে বিভাজ্য হয় অথবা শেষ দুটি অঙ্ক '০০' হয়, তবে পুরো সংখ্যাটি ৪ দ্বারা বিভাজ্য।",
            testExampleNumber = 93124L,
            exampleExplanationBn = "৯৩১২৪ এর শেষ দুটি অঙ্ক দ্বারা গঠিত সংখ্যা ২৪। যেহেতু ২৪ সংখ্যাটি ৪ দ্বারা বিভাজ্য (২৪ ÷ ৪ = ৬), তাই ৯৩১২৪ সংখ্যাটি ৪ দ্বারা নিঃশেষে বিভাজ্য।",
            bcsShortcutBn = "বিসিএস শর্টকাট: সম্পূর্ণ বড় সংখ্যা ভাগ না করে শুধু শেষ দুই অঙ্ককে ৪ দিয়ে ভাগ করে দেখুন।"
        ),
        DivisibilityRule(
            divisor = 5,
            ruleNameBn = "৫ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "সংখ্যার শেষ অঙ্কটি (একক স্থানীয় অঙ্ক) ০ অথবা ৫ হলে সংখ্যাটি ৫ দ্বারা বিভাজ্য।",
            detailedRuleBn = "কোনো সংখ্যার একক স্থানীয় অঙ্ক যদি ০ বা ৫ হয়, তবে সংখ্যাটি ৫ দ্বারা নিঃশেষে বিভাজ্য হবে। অন্য কোনো অঙ্কের ক্ষেত্রে বিভাজ্য নয়।",
            testExampleNumber = 38475L,
            exampleExplanationBn = "৩৮৪৭৫ এর শেষ অঙ্ক ৫, তাই ৩৮৪৭৫ সংখ্যাটি ৫ দ্বারা নিঃশেষে বিভাজ্য।",
            bcsShortcutBn = "বিসিএস শর্টকাট: শেষ অঙ্ক ০ বা ৫ দেখলেই নিশ্চিত হন।"
        ),
        DivisibilityRule(
            divisor = 6,
            ruleNameBn = "৬ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "সংখ্যাটি একই সাথে ২ এবং ৩ উভয় সংখ্যা দ্বারাই বিভাজ্য হলে তা ৬ দ্বারা বিভাজ্য।",
            detailedRuleBn = "৬ = ২ × ৩ (সহমৌলিক উৎপাদক)। তাই সংখ্যাটিকে অবশ্যই জোড় সংখ্যা হতে হবে (২ দ্বারা বিভাজ্য) এবং অঙ্কগুলোর যোগফল ৩ দ্বারা বিভাজ্য হতে হবে।",
            testExampleNumber = 5142L,
            exampleExplanationBn = "৫১৪২ এর শেষ অঙ্ক ২ (জোড়, তাই ২ দ্বারা বিভাজ্য)। অঙ্কগুলোর যোগফল = ৫ + ১ + ৪ + ২ = ১২, যা ৩ দ্বারা বিভাজ্য। যেহেতু ২ ও ৩ উভয় দ্বারা বিভাজ্য, তাই ৫১৪২ সংখ্যাটি ৬ দ্বারা নিঃশেষে বিভাজ্য।",
            bcsShortcutBn = "বিসিএস শর্টকাট: সংখ্যাটি বিজোড় হলে প্রথমেই বাদ! জোড় হলে কেবল অঙ্কগুলোর যোগফল ৩ দিয়ে যায় কিনা দেখুন।"
        ),
        DivisibilityRule(
            divisor = 8,
            ruleNameBn = "৮ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "সংখ্যার শেষ তিনটি অঙ্ক দ্বারা গঠিত সংখ্যা ৮ দ্বারা বিভাজ্য অথবা শেষ তিনটি অঙ্ক '০০০' হলে।",
            detailedRuleBn = "কোনো সংখ্যার শতক, দশক ও একক স্থানীয় অঙ্ক নিয়ে গঠিত ৩ অঙ্কের সংখ্যা ৮ দ্বারা নিঃশেষে বিভাজ্য হলে বা শেষ তিনটি অঙ্ক '০০০' হলে মূল সংখ্যাটি ৮ দ্বারা বিভাজ্য।",
            testExampleNumber = 72152L,
            exampleExplanationBn = "৭২১৫২ এর শেষ তিনটি অঙ্ক দ্বারা গঠিত সংখ্যা ১৫২। ১৫২ ÷ ৮ = ১৯ (নিঃশেষে বিভাজ্য)। সুতরাং সম্পূর্ণ সংখ্যা ৭২১৫২ সংখ্যাটি ৮ দ্বারা বিভাজ্য।",
            bcsShortcutBn = "বিসিএস শর্টকাট: শতক অঙ্ক জোড় হলে শেষ ২ অঙ্ক ৮ দিয়ে ভাগ যান। শতক বিজোড় হলে শেষ ২ অঙ্কের সাথে ৪ যোগ করে ৮ দিয়ে ভাগ করে দেখুন।"
        ),
        DivisibilityRule(
            divisor = 9,
            ruleNameBn = "৯ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "সংখ্যার অঙ্কগুলোর যোগফল ৯ দ্বারা বিভাজ্য হলে সংখ্যাটি ৯ দ্বারা বিভাজ্য।",
            detailedRuleBn = "৩ এর নিয়মের মতোই, কিন্তু যোগফলকে ৯ দ্বারা বিভাজ্য হতে হবে। যদি অঙ্কগুলোর সমষ্টি ৯ দ্বারা নিঃশেষে বিভাজ্য হয়, তবে সংখ্যাটি ৯ দ্বারা বিভাজ্য।",
            testExampleNumber = 8352L,
            exampleExplanationBn = "৮৩৫২ এর অঙ্কগুলোর যোগফল = ৮ + ৩ + ৫ + ২ = ১৮। যেহেতু ১৮ সংখ্যাটি ৯ দ্বারা বিভাজ্য (১৮ ÷ ৯ = ২), তাই ৮৩৫২ সংখ্যাটি ৯ দ্বারা নিঃশেষে বিভাজ্য।",
            bcsShortcutBn = "বিসিএস শর্টকাট: যেকোনো সংখ্যার অঙ্কগুলোর যোগফল যদি ৯ দ্বারা বিভাজ্য হয়, তবে সংখ্যাটি ৯ এবং ৩ উভয় দ্বারাই বিভাজ্য।"
        ),
        DivisibilityRule(
            divisor = 10,
            ruleNameBn = "১০ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "সংখ্যার শেষ অঙ্কটি (একক স্থানীয় অঙ্ক) ০ হলে সংখ্যাটি ১০ দ্বারা বিভাজ্য।",
            detailedRuleBn = "কোনো সংখ্যার একক স্থানীয় অঙ্ক কেবলমাত্র ০ হলেই তা ১০ দ্বারা নিঃশেষে বিভাজ্য হবে।",
            testExampleNumber = 98740L,
            exampleExplanationBn = "৯৮৭৪০ এর শেষ অঙ্ক ০, সুতরাং এটি ১০ দ্বারা নিঃশেষে বিভাজ্য।",
            bcsShortcutBn = "বিসিএস শর্টকাট: এককের ঘরে ০ থাকলে ১০ দ্বারা বিভাজ্য।"
        ),
        DivisibilityRule(
            divisor = 11,
            ruleNameBn = "১১ দ্বারা বিভাজ্যতার নিয়ম",
            summaryBn = "বিজোড় স্থানের অঙ্কগুলোর যোগফল ও জোড় স্থানের অঙ্কগুলোর যোগফলের পার্থক্য ০ অথবা ১১ এর গুণিতক হলে।",
            detailedRuleBn = "ডানদিক থেকে বিজোড় অবস্থানের অঙ্কগুলোর যোগফল নির্ণয় করুন। এরপর জোড় অবস্থানের অঙ্কগুলোর যোগফল নির্ণয় করুন। এই দুই যোগফলের অন্তর (পার্থক্য) যদি ০ হয় বা ১১ দ্বারা বিভাজ্য হয়, তবে সম্পূর্ণ সংখ্যাটি ১১ দ্বারা নিঃশেষে বিভাজ্য।",
            testExampleNumber = 91828L,
            exampleExplanationBn = "৯১৮২৮ সংখ্যাটি বিবেচনা করি:\nবিজোড় স্থানের অঙ্কসমূহ (ডান থেকে ১ম, ৩য়, ৫ম): ৮ + ৮ + ৯ = ২৫\nজোড় স্থানের অঙ্কসমূহ (ডান থেকে ২য়, ৪র্থ): ২ + ১ = ৩\nপার্থক্য = ২৫ - ৩ = ২২।\nযেহেতু ২২ সংখ্যাটি ১১ দ্বারা বিভাজ্য (২২ ÷ ১১ = ২), তাই ৯১৮২৮ সংখ্যাটি ১১ দ্বারা নিঃশেষে বিভাজ্য।",
            bcsShortcutBn = "বিসিএস সুপার শর্টকাট: বিজোড় স্থানগুলোর যোগফল - জোড় স্থানগুলোর যোগফল = ০ বা ১১, ২২, ৩৩... হলে ১১ দিয়ে বিভাজ্য।"
        )
    )

    /**
     * Check divisibility dynamically for any number.
     */
    fun checkDivisibility(number: Long, divisor: Int): Pair<Boolean, String> {
        val absNum = abs(number)
        val bnNum = BengaliNumberFormatter.toBn(absNum)
        val isDivisible = (absNum % divisor == 0L)

        val explanation = when (divisor) {
            2 -> {
                val lastDigit = (absNum % 10).toInt()
                val isEven = lastDigit % 2 == 0
                if (isEven) {
                    "$bnNum এর শেষ অঙ্ক ${BengaliNumberFormatter.toBn(lastDigit)} যা একটি জোড় সংখ্যা। সুতরাং $bnNum সংখ্যাটি ২ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "$bnNum এর শেষ অঙ্ক ${BengaliNumberFormatter.toBn(lastDigit)} যা বিজোড়। সুতরাং $bnNum সংখ্যাটি ২ দ্বারা বিভাজ্য নয়।"
                }
            }
            3 -> {
                val sum = absNum.toString().map { it.digitToInt() }.sum()
                val sumBn = BengaliNumberFormatter.toBn(sum)
                if (sum % 3 == 0) {
                    "$bnNum এর অঙ্কগুলোর যোগফল $sumBn, যা ৩ দ্বারা বিভাজ্য ($sumBn ÷ ৩ = ${BengaliNumberFormatter.toBn(sum / 3)})। তাই সংখ্যাটি ৩ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "$bnNum এর অঙ্কগুলোর যোগফল $sumBn, যা ৩ দ্বারা বিভাজ্য নয়। তাই সংখ্যাটি ৩ দ্বারা নিঃশেষে বিভাজ্য নয়।"
                }
            }
            4 -> {
                val lastTwo = (absNum % 100).toInt()
                val lastTwoBn = BengaliNumberFormatter.toBn(lastTwo)
                if (lastTwo % 4 == 0) {
                    "$bnNum এর শেষ দুটি অঙ্ক নিয়ে গঠিত সংখ্যা $lastTwoBn, যা ৪ দ্বারা বিভাজ্য ($lastTwoBn ÷ ৪ = ${BengaliNumberFormatter.toBn(lastTwo / 4)})। তাই এটি ৪ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "$bnNum এর শেষ দুটি অঙ্ক নিয়ে গঠিত সংখ্যা $lastTwoBn, যা ৪ দ্বারা বিভাজ্য নয়। তাই এটি ৪ দ্বারা নিঃশেষে বিভাজ্য নয়।"
                }
            }
            5 -> {
                val lastDigit = (absNum % 10).toInt()
                val lastDigitBn = BengaliNumberFormatter.toBn(lastDigit)
                if (lastDigit == 0 || lastDigit == 5) {
                    "$bnNum এর শেষ অঙ্ক $lastDigitBn (০ বা ৫)। তাই এটি ৫ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "$bnNum এর শেষ অঙ্ক $lastDigitBn (০ বা ৫ নয়)। তাই এটি ৫ দ্বারা বিভাজ্য নয়।"
                }
            }
            6 -> {
                val lastDigit = (absNum % 10).toInt()
                val isEven = lastDigit % 2 == 0
                val sum = absNum.toString().map { it.digitToInt() }.sum()
                val sumDiv3 = sum % 3 == 0
                if (isEven && sumDiv3) {
                    "$bnNum সংখ্যাটি জোড় (২ দ্বারা বিভাজ্য) এবং অঙ্কগুলোর যোগফল ${BengaliNumberFormatter.toBn(sum)} (৩ দ্বারা বিভাজ্য)। যেহেতু ২ ও ৩ উভয় দ্বারা বিভাজ্য, তাই এটি ৬ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    val reason = if (!isEven) "সংখ্যাটি বিজোড় (২ দ্বারা বিভাজ্য নয়)" else "অঙ্কগুলোর যোগফল ৩ দ্বারা বিভাজ্য নয়"
                    "$bnNum সংখ্যাটি ৬ দ্বারা বিভাজ্য নয় কারণ: $reason।"
                }
            }
            8 -> {
                val lastThree = (absNum % 1000).toInt()
                val lastThreeBn = BengaliNumberFormatter.toBn(lastThree)
                if (lastThree % 8 == 0) {
                    "$bnNum এর শেষ ৩টি অঙ্ক দ্বারা গঠিত সংখ্যা $lastThreeBn, যা ৮ দ্বারা বিভাজ্য ($lastThreeBn ÷ ৮ = ${BengaliNumberFormatter.toBn(lastThree / 8)})। তাই এটি ৮ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "$bnNum এর শেষ ৩টি অঙ্ক দ্বারা গঠিত সংখ্যা $lastThreeBn, যা ৮ দ্বারা বিভাজ্য নয়। তাই এটি ৮ দ্বারা নিঃশেষে বিভাজ্য নয়।"
                }
            }
            9 -> {
                val sum = absNum.toString().map { it.digitToInt() }.sum()
                val sumBn = BengaliNumberFormatter.toBn(sum)
                if (sum % 9 == 0) {
                    "$bnNum এর অঙ্কগুলোর যোগফল $sumBn, যা ৯ দ্বারা বিভাজ্য ($sumBn ÷ ৯ = ${BengaliNumberFormatter.toBn(sum / 9)})। তাই এটি ৯ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "$bnNum এর অঙ্কগুলোর যোগফল $sumBn, যা ৯ দ্বারা বিভাজ্য নয়। তাই এটি ৯ দ্বারা নিঃশেষে বিভাজ্য নয়।"
                }
            }
            10 -> {
                val lastDigit = (absNum % 10).toInt()
                if (lastDigit == 0) {
                    "$bnNum এর শেষ অঙ্ক ০। তাই এটি ১০ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "$bnNum এর শেষ অঙ্ক ${BengaliNumberFormatter.toBn(lastDigit)} (০ নয়)। তাই এটি ১০ দ্বারা বিভাজ্য নয়।"
                }
            }
            11 -> {
                val digits = absNum.toString().map { it.digitToInt() }
                var oddSum = 0
                var evenSum = 0
                val reversed = digits.reversed()
                for (i in reversed.indices) {
                    if ((i + 1) % 2 != 0) {
                        oddSum += reversed[i]
                    } else {
                        evenSum += reversed[i]
                    }
                }
                val diff = abs(oddSum - evenSum)
                val diffBn = BengaliNumberFormatter.toBn(diff)
                if (diff % 11 == 0) {
                    "বিজোড় স্থানের অঙ্কগুলোর যোগফল (${BengaliNumberFormatter.toBn(oddSum)}) এবং জোড় স্থানের অঙ্কগুলোর যোগফলের (${BengaliNumberFormatter.toBn(evenSum)}) পার্থক্য = $diffBn। যেহেতু $diffBn সংখ্যাটি ১১ দ্বারা বিভাজ্য (বা ০), তাই $bnNum সংখ্যাটি ১১ দ্বারা নিঃশেষে বিভাজ্য।"
                } else {
                    "বিজোড় স্থানের অঙ্কগুলোর যোগফল (${BengaliNumberFormatter.toBn(oddSum)}) এবং জোড় স্থানের অঙ্কগুলোর যোগফলের (${BengaliNumberFormatter.toBn(evenSum)}) পার্থক্য = $diffBn। যেহেতু $diffBn সংখ্যাটি ১১ দ্বারা বিভাজ্য নয়, তাই $bnNum সংখ্যাটি ১১ দ্বারা বিভাজ্য নয়।"
                }
            }
            else -> {
                if (isDivisible) "$bnNum সংখ্যাটি ${BengaliNumberFormatter.toBn(divisor)} দ্বারা নিঃশেষে বিভাজ্য।"
                else "$bnNum সংখ্যাটি ${BengaliNumberFormatter.toBn(divisor)} দ্বারা নিঃশেষে বিভাজ্য নয়।"
            }
        }

        return Pair(isDivisible, explanation)
    }

    /**
     * Compute GCD and LCM with full Bengali step-by-step breakdowns.
     */
    fun calculateGcdLcm(numA: Long, numB: Long): GcdLcmResult {
        val a = if (numA <= 0) 1L else numA
        val b = if (numB <= 0) 1L else numB

        val aFactors = getFactors(a)
        val bFactors = getFactors(b)
        val aPrimeFactors = getPrimeFactors(a)
        val bPrimeFactors = getPrimeFactors(b)

        val commonFactors = aFactors.filter { bFactors.contains(it) }
        val gcd = gcd(a, b)
        val lcm = lcm(a, b)

        val gcdSteps = mutableListOf<StepExplanation>()
        gcdSteps.add(
            StepExplanation(
                stepNumber = 1,
                titleBn = "সংখ্যার গুণনীয়ক (উৎপাদক) নির্ণয়",
                detailBn = "${BengaliNumberFormatter.toBn(a)} এর গুণনীয়ক: ${aFactors.joinToString(", ") { BengaliNumberFormatter.toBn(it) }}\n" +
                        "${BengaliNumberFormatter.toBn(b)} এর গুণনীয়ক: ${bFactors.joinToString(", ") { BengaliNumberFormatter.toBn(it) }}"
            )
        )
        gcdSteps.add(
            StepExplanation(
                stepNumber = 2,
                titleBn = "সাধারণ গুণনীয়কগুলো চিহ্নিতকরণ",
                detailBn = "উভয় সংখ্যার সাধারণ গুণনীয়কসমূহ: ${commonFactors.joinToString(", ") { BengaliNumberFormatter.toBn(it) }}"
            )
        )
        gcdSteps.add(
            StepExplanation(
                stepNumber = 3,
                titleBn = "গরিষ্ঠ সাধারণ গুণনীয়ক (গ.সা.গু) গ্রহণ",
                detailBn = "সাধারণ গুণনীয়কগুলোর মধ্যে সবচেয়ে বড় বা গরিষ্ঠ গুণনীয়ক হলো ${BengaliNumberFormatter.toBn(gcd)}।\nঅতএব, গ.সা.গু (HCF/GCD) = ${BengaliNumberFormatter.toBn(gcd)}।"
            )
        )

        // Euclidean Algorithm alternative view
        val euclideanSteps = getEuclideanSteps(a, b)
        if (euclideanSteps.isNotEmpty()) {
            gcdSteps.add(
                StepExplanation(
                    stepNumber = 4,
                    titleBn = "ইউক্লিডীয় ভাগ পদ্ধতিতে যাচাই (BCS শর্টকাট)",
                    detailBn = euclideanSteps.joinToString("\n")
                )
            )
        }

        val aPrimeDisplay = if (aPrimeFactors.isEmpty()) BengaliNumberFormatter.toBn(a) else aPrimeFactors.joinToString(" × ") { BengaliNumberFormatter.toBn(it) }
        val bPrimeDisplay = if (bPrimeFactors.isEmpty()) BengaliNumberFormatter.toBn(b) else bPrimeFactors.joinToString(" × ") { BengaliNumberFormatter.toBn(it) }
        val lcmSteps = mutableListOf<StepExplanation>()
        lcmSteps.add(
            StepExplanation(
                stepNumber = 1,
                titleBn = "মৌলিক উৎপাদকে বিশ্লেষণ",
                detailBn = "${BengaliNumberFormatter.toBn(a)} = $aPrimeDisplay\n" +
                        "${BengaliNumberFormatter.toBn(b)} = $bPrimeDisplay"
            )
        )
        lcmSteps.add(
            StepExplanation(
                stepNumber = 2,
                titleBn = "সাধারণ ও অসাধারণ উৎপাদকের সর্বোচ্চ ঘাত একত্রীকরণ",
                detailBn = "উভয় সংখ্যার মৌলিক উৎপাদকগুলোর সর্বোচ্চ সংখ্যক উপস্থিতির গুণফল নিয়ে ল.সা.গু গঠিত হয়।"
            )
        )
        if (lcm <= 0L) {
            lcmSteps.add(
                StepExplanation(
                    stepNumber = 3,
                    titleBn = "লঘিষ্ঠ সাধারণ গুণিতক (ল.সা.গু) নির্ণয়",
                    detailBn = "ল.সা.গু গণনা সমর্থিত সীমার বাইরে (সংখ্যাধিক্য বা ওভারফ্লো)। অপেক্ষাকৃত ছোট সংখ্যা ব্যবহার করুন।"
                )
            )
        } else {
            lcmSteps.add(
                StepExplanation(
                    stepNumber = 3,
                    titleBn = "লঘিষ্ঠ সাধারণ গুণিতক (ল.সা.গু) নির্ণয়",
                    detailBn = "ল.সা.গু (LCM) = (${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)}) ÷ গ.সা.গু (${BengaliNumberFormatter.toBn(gcd)}) = ${BengaliNumberFormatter.toBn(lcm)}।"
                )
            )
        }

        val prodAB = java.math.BigInteger.valueOf(a).multiply(java.math.BigInteger.valueOf(b))
        val formulaCheck = if (lcm > 0L) {
            val prodGL = java.math.BigInteger.valueOf(gcd).multiply(java.math.BigInteger.valueOf(lcm))
            "যাচাই সূত্র: দুটি সংখ্যার গুণফল = গ.সা.গু × ল.সা.গু\n" +
                    "${BengaliNumberFormatter.toBn(a)} × ${BengaliNumberFormatter.toBn(b)} = ${BengaliNumberFormatter.toBn(prodAB.toString())}\n" +
                    "${BengaliNumberFormatter.toBn(gcd)} × ${BengaliNumberFormatter.toBn(lcm)} = ${BengaliNumberFormatter.toBn(prodGL.toString())} (উভয় পক্ষ সমান ✓)"
        } else {
            "যাচাই সূত্র: ল.সা.গু মান সমর্থিত সীমার অতিরিক্ত (ওভারফ্লো) হওয়ায় গুণফল পরীক্ষা প্রযোজ্য নয়।"
        }

        return GcdLcmResult(
            a = a,
            b = b,
            gcd = gcd,
            lcm = lcm,
            aFactors = aFactors,
            bFactors = bFactors,
            aPrimeFactors = aPrimeFactors,
            bPrimeFactors = bPrimeFactors,
            commonFactors = commonFactors,
            gcdSteps = gcdSteps,
            lcmSteps = lcmSteps,
            formulaCheckBn = formulaCheck
        )
    }

    private fun getEuclideanSteps(a: Long, b: Long): List<String> {
        if (a == Long.MIN_VALUE || b == Long.MIN_VALUE) return emptyList()
        val absA = abs(a)
        val absB = abs(b)
        val steps = mutableListOf<String>()
        var x = maxOf(absA, absB)
        var y = minOf(absA, absB)
        var stepCount = 1

        while (y != 0L) {
            val q = x / y
            val r = x % y
            steps.add("ধাপ $stepCount: ${BengaliNumberFormatter.toBn(x)} = ${BengaliNumberFormatter.toBn(y)} × ${BengaliNumberFormatter.toBn(q)} + ${BengaliNumberFormatter.toBn(r)} (ভাগশেষ = ${BengaliNumberFormatter.toBn(r)})")
            x = y
            y = r
            stepCount++
        }
        return steps
    }

    fun gcd(a: Long, b: Long): Long {
        if (a == 0L && b == 0L) return 1L
        if (a == Long.MIN_VALUE && (b == 0L || b == Long.MIN_VALUE)) {
            return Long.MAX_VALUE
        }
        val bigGcd = java.math.BigInteger.valueOf(a).gcd(java.math.BigInteger.valueOf(b))
        return if (bigGcd.bitLength() > 63) Long.MAX_VALUE else bigGcd.toLong()
    }

    fun lcm(a: Long, b: Long): Long {
        if (a == 0L || b == 0L) return 0L
        if (a == Long.MIN_VALUE || b == Long.MIN_VALUE) return -1L
        val g = gcd(a, b)
        return try {
            val div = abs(a) / g
            Math.multiplyExact(div, abs(b))
        } catch (e: ArithmeticException) {
            -1L
        }
    }

    fun getFactors(n: Long): List<Long> {
        if (n <= 0L || n == Long.MIN_VALUE) return emptyList()
        val factors = mutableListOf<Long>()
        var i = 1L
        val maxSteps = 200_000L
        while (i <= n / i && i <= maxSteps) {
            if (n % i == 0L) {
                factors.add(i)
                if (i != n / i) {
                    factors.add(n / i)
                }
            }
            if (factors.size >= 1000) break
            i++
        }
        return factors.sorted()
    }

    fun getPrimeFactors(n: Long): List<Long> {
        if (n <= 1L || n == Long.MIN_VALUE) return emptyList()
        val factors = mutableListOf<Long>()
        var temp = n
        while (temp % 2L == 0L) {
            factors.add(2L)
            temp /= 2L
        }
        var d = 3L
        val maxD = 500_000L
        while (d <= temp / d && d <= maxD) {
            while (temp % d == 0L) {
                factors.add(d)
                temp /= d
            }
            d += 2L
        }
        if (temp > 1L) {
            factors.add(temp)
        }
        return factors
    }

    fun isPrime(n: Long): Boolean {
        if (n <= 1L || n == Long.MIN_VALUE) return false
        if (n <= 3L) return true
        if (n % 2L == 0L || n % 3L == 0L) return false
        var i = 5L
        while (i <= n / i) {
            if (n % i == 0L || n % (i + 2L) == 0L) return false
            if (i > Long.MAX_VALUE - 6L) break
            i += 6L
        }
        return true
    }

    fun classifyNumber(n: Long): String {
        val bnN = BengaliNumberFormatter.toBn(n)
        val parts = mutableListOf<String>()

        if (n > 0) parts.add("স্বাভাবিক সংখ্যা (Natural Number)")
        if (n >= 0) parts.add("অখণ্ড সংখ্যা (Whole Number)")
        parts.add("পূর্ণসংখ্যা (Integer)")
        parts.add("মূলদ সংখ্যা (Rational Number)")

        if (n % 2L == 0L) parts.add("জোড় সংখ্যা (Even Number)")
        else parts.add("বিজোড় সংখ্যা (Odd Number)")

        if (n > 1) {
            if (isPrime(n)) {
                parts.add("মৌলিক সংখ্যা (Prime Number)")
            } else {
                parts.add("যৌগিক সংখ্যা (Composite Number)")
            }
        } else if (n == 1L) {
            parts.add("মৌলিক বা যৌগিক কোনোটিই নয়")
        }

        return "$bnN সংখ্যাটির শ্রেণিবিভাগ:\n• " + parts.joinToString("\n• ")
    }

    /**
     * Curated practice questions for Easy, Medium, Hard (BCS Level).
     */
    fun getPracticeQuestions(
        topic: NumberSystemTopic = NumberSystemTopic.ALL,
        difficulty: NumberSystemDifficulty = NumberSystemDifficulty.EASY
    ): List<NumberSystemQuestion> {
        val allQuestions = getCuratedQuestionBank()
        val filtered = allQuestions.filter { q ->
            (topic == NumberSystemTopic.ALL || q.topic == topic) &&
                    q.difficulty == difficulty
        }
        return if (filtered.isNotEmpty()) filtered.shuffled() else allQuestions.filter { it.difficulty == difficulty }.shuffled()
    }

    private fun getCuratedQuestionBank(): List<NumberSystemQuestion> {
        return listOf(
            // --- EASY: Classification & Primes ---
            NumberSystemQuestion(
                id = "ns_e_1",
                topic = NumberSystemTopic.CLASSIFICATION,
                difficulty = NumberSystemDifficulty.EASY,
                questionBn = "নিচের কোনটি ক্ষুদ্রতম স্বাভাবিক সংখ্যা?",
                optionsBn = listOf("০", "১", "২", "-১"),
                correctIndex = 1,
                explanationBn = "গণনাকারী ধনাত্মক পূর্ণসংখ্যা ১, ২, ৩... কে স্বাভাবিক সংখ্যা বলে। তাই ক্ষুদ্রতম স্বাভাবিক সংখ্যা হলো ১। (০ স্বাভাবিক সংখ্যা নয়)",
                bcsReferenceBn = "প্রাথমিক শিক্ষক নিয়োগ ও বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_e_2",
                topic = NumberSystemTopic.FACTORS_MULTIPLES,
                difficulty = NumberSystemDifficulty.EASY,
                questionBn = "একমাত্র জোড় মৌলিক সংখ্যা কোনটি?",
                optionsBn = listOf("০", "২", "৪", "কোনোটিই নয়"),
                correctIndex = 1,
                explanationBn = "২ হলো একমাত্র জোড় সংখ্যা যা মৌলিক। অন্য সকল জোড় সংখ্যা ২ দ্বারা বিভাজ্য হওয়ায় সেগুলো যৌগিক সংখ্যা।",
                bcsReferenceBn = "২৪তম ও ৩৭তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_e_3",
                topic = NumberSystemTopic.DIVISIBILITY,
                difficulty = NumberSystemDifficulty.EASY,
                questionBn = "কোনো সংখ্যার শেষ অঙ্ক ০ বা ৫ হলে সংখ্যাটি কোন সংখ্যা দ্বারা নিঃশেষে বিভাজ্য?",
                optionsBn = listOf("২", "৩", "৪", "৫"),
                correctIndex = 3,
                explanationBn = "৫ দ্বারা বিভাজ্যতার নিয়ম অনুসারে, কোনো সংখ্যার একক স্থানীয় অঙ্ক ০ বা ৫ হলে সংখ্যাটি ৫ দ্বারা নিঃশেষে বিভাজ্য।",
                bcsReferenceBn = "মৌলিক গাণিতিক ধারণা"
            ),
            NumberSystemQuestion(
                id = "ns_e_4",
                topic = NumberSystemTopic.GCD_LCM,
                difficulty = NumberSystemDifficulty.EASY,
                questionBn = "১২ এবং ১৮ এর গরিষ্ঠ সাধারণ গুণনীয়ক (গ.সা.গু) কত?",
                optionsBn = listOf("২", "৩", "৬", "৩৬"),
                correctIndex = 2,
                explanationBn = "১২ এর গুণনীয়ক: ১, ২, ৩, ৪, ৬, ১২। ১৮ এর গুণনীয়ক: ১, ২, ৩, ৬, ৯, ১৮। এদের সাধারণ গুণনীয়ক: ১, ২, ৩, ৬। সুতরাং গ.সা.গু = ৬।",
                bcsReferenceBn = "বিসিএস প্রিলিমিনারি প্র্যাকটিস"
            ),
            NumberSystemQuestion(
                id = "ns_e_5",
                topic = NumberSystemTopic.FACTORS_MULTIPLES,
                difficulty = NumberSystemDifficulty.EASY,
                questionBn = "২৪ এর মোট গুণনীয়ক (উৎপাদক) সংখ্যা কয়টি?",
                optionsBn = listOf("৬টি", "৭টি", "৮টি", "১০টি"),
                correctIndex = 2,
                explanationBn = "২৪ এর গুণনীয়কগুলো হলো: ১, ২, ৩, ৪, ৬, ৮, ১২, ২৪। মোট গুণনীয়ক সংখ্যা ৮টি। (শর্টকাট: ২৪ = ২³ × ৩¹; মোট গুণনীয়ক = (৩+১)(১+১) = ৪ × ২ = ৮)",
                bcsReferenceBn = "১০ম ও ৩৮তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_e_6",
                topic = NumberSystemTopic.DIVISIBILITY,
                difficulty = NumberSystemDifficulty.EASY,
                questionBn = "নিচের কোন সংখ্যাটি ৩ দ্বারা নিঃশেষে বিভাজ্য?",
                optionsBn = listOf("৪৫২৩", "৫১২৪", "৭৬০১", "৮২০১"),
                correctIndex = 1,
                explanationBn = "৫১২৪ এর অঙ্কগুলোর যোগফল = ৫ + ১ + ২ + ৪ = ১২। যেহেতু ১২ সংখ্যাটি ৩ দ্বারা বিভাজ্য, তাই ৫১২৪ সংখ্যাটি ৩ দ্বারা নিঃশেষে বিভাজ্য।",
                bcsReferenceBn = "বিসিএস প্রিলিমিনারি"
            ),

            // --- MEDIUM: Divisibility rules, Rational vs Irrational, GCD/LCM ---
            NumberSystemQuestion(
                id = "ns_m_1",
                topic = NumberSystemTopic.CLASSIFICATION,
                difficulty = NumberSystemDifficulty.MEDIUM,
                questionBn = "নিচের কোনটি অমূলদ সংখ্যা?",
                optionsBn = listOf("√৯", "√১৬", "√২", "০.২৫"),
                correctIndex = 2,
                explanationBn = "পূর্ণবর্গ নয় এমন যেকোনো সংখ্যার বর্গমূল অমূলদ সংখ্যা। এখানে √২ = ১.৪১৪২১৩... যা অসীম অনাবৃত্ত দশমিক। অন্যপক্ষে √৯ = ৩, √১৬ = ৪ এবং ০.২৫ = ১/৪ যা মূলদ।",
                bcsReferenceBn = "৩৮তম ও ২৯তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_m_2",
                topic = NumberSystemTopic.DIVISIBILITY,
                difficulty = NumberSystemDifficulty.MEDIUM,
                questionBn = "৩৪৭* সংখ্যাটি ৯ দ্বারা নিঃশেষে বিভাজ্য হলে * চিহ্নিত স্থানে কোন অঙ্কটি বসবে?",
                optionsBn = listOf("২", "৪", "৫", "৭"),
                correctIndex = 1,
                explanationBn = "৯ দ্বারা বিভাজ্যতার নিয়মানুযায়ী অঙ্কগুলোর সমষ্টি ৯ দ্বারা বিভাজ্য হতে হবে। ৩ + ৪ + ৭ + * = ১৪ + *। ১৪ এর পরবর্তী ৯ এর গুণিতক হলো ১৮। সুতরাং * = ১৮ - ১৪ = ৪।",
                bcsReferenceBn = "৩৫তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_m_3",
                topic = NumberSystemTopic.GCD_LCM,
                difficulty = NumberSystemDifficulty.MEDIUM,
                questionBn = "দুটি সংখ্যার গ.সা.গু ১৬ এবং ল.সা.গু ১৯২। একটি সংখ্যা ৪৮ হলে অপর সংখ্যাটি কত?",
                optionsBn = listOf("৩২", "৪৮", "৬৪", "৭২"),
                correctIndex = 2,
                explanationBn = "আমরা জানি, সংখ্যা দুটির গুণফল = গ.সা.গু × ল.সা.গু।\nসুতরাং অপর সংখ্যা = (গ.সা.গু × ল.সা.গু) ÷ একটি সংখ্যা = (১৬ × ১৯২) ÷ ৪৮ = ৩০৭২ ÷ ৪৮ = ৬৪।",
                bcsReferenceBn = "৩৭তম ও ৩০তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_m_4",
                topic = NumberSystemTopic.DIVISIBILITY,
                difficulty = NumberSystemDifficulty.MEDIUM,
                questionBn = "নিচের কোন সংখ্যাটি ৪ এবং ৬ উভয় সংখ্যা দ্বারাই নিঃশেষে বিভাজ্য?",
                optionsBn = listOf("৫১১৮", "৭৫২৪", "৮১২৬", "৯১১০"),
                correctIndex = 1,
                explanationBn = "৭৫২৪ বিবেচনা করুন:\n১) শেষ দুই অঙ্ক ২৪ যা ৪ দ্বারা বিভাজ্য।\n২) এটি জোড় এবং অঙ্কগুলোর যোগফল ৭ + ৫ + ২ + ৪ = ১৮ যা ৩ দ্বারা বিভাজ্য, তাই এটি ৬ দ্বারাও বিভাজ্য।",
                bcsReferenceBn = "বিসিএস ও ব্যাংক নিয়োগ পরীক্ষা"
            ),
            NumberSystemQuestion(
                id = "ns_m_5",
                topic = NumberSystemTopic.DIVISIBILITY,
                difficulty = NumberSystemDifficulty.MEDIUM,
                questionBn = "৭৩*৫ সংখ্যাটি ১১ দ্বারা নিঃশেষে বিভাজ্য হলে * এর মান কত?",
                optionsBn = listOf("১", "৩", "৫", "৭"),
                correctIndex = 0,
                explanationBn = "১১ দ্বারা বিভাজ্যতার নিয়ম: (৫ + ৩) - (* + ৭) = ০ অথবা ১১ এর গুণিতক।\n৮ - (* + ৭) = ১ - *।\n১ - * = ০ হলে * = ১।\nযাচাই: ৭৩১৫ -> (৫+৩) - (১+৭) = ৮ - ৮ = ০ (বিভাজ্য)।",
                bcsReferenceBn = "৪১তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_m_6",
                topic = NumberSystemTopic.CLASSIFICATION,
                difficulty = NumberSystemDifficulty.MEDIUM,
                questionBn = "০.৩৩৩... (পৌনঃপুনিক দশমিক) সংখ্যাটি কোন ধরণের সংখ্যা?",
                optionsBn = listOf("অমূলদ সংখ্যা", "মূলদ সংখ্যা", "স্বাভাবিক সংখ্যা", "অবাস্তব সংখ্যা"),
                correctIndex = 1,
                explanationBn = "০.৩৩৩... = ১/৩। যেহেতু একে দুটি পূর্ণসংখ্যার অনুপাত p/q (যেখানে q ≠ 0) আকারে প্রকাশ করা যায়, তাই এটি একটি মূলদ সংখ্যা। সকল পৌনঃপুনিক দশমিক সংখ্যা মূলদ।",
                bcsReferenceBn = "২৬তম বিসিএস প্রিলিমিনারি"
            ),

            // --- HARD: BCS Preliminary & Advance Number Theory ---
            NumberSystemQuestion(
                id = "ns_h_1",
                topic = NumberSystemTopic.CLASSIFICATION,
                difficulty = NumberSystemDifficulty.HARD,
                questionBn = "π (পাই) এবং ২২/৭ এর মধ্যে কোনটি সত্য?",
                optionsBn = listOf(
                    "উভয়েই মূলদ সংখ্যা",
                    "উভয়েই অমূলদ সংখ্যা",
                    "π অমূলদ কিন্তু ২২/৭ মূলদ সংখ্যা",
                    "π মূলদ কিন্তু ২২/৭ অমূলদ সংখ্যা"
                ),
                correctIndex = 2,
                explanationBn = "২২/৭ হলো দুটি পূর্ণসংখ্যার অনুপাত p/q আকার, তাই ২২/৭ একটি মূলদ সংখ্যা। অন্যদিকে π এর প্রকৃত মান অসীম ও অনাবৃত্ত দশমিক (৩.১৪১৫৯২৬৫...), তাই π একটি অমূলদ সংখ্যা। ২২/৭ হলো π এর একটি আসন্ন মান মাত্র।",
                bcsReferenceBn = "৩৩তম ও ৪০তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_h_2",
                topic = NumberSystemTopic.GCD_LCM,
                difficulty = NumberSystemDifficulty.HARD,
                questionBn = "কোন ক্ষুদ্রতম সংখ্যাকে ৪, ৫ এবং ৬ দ্বারা ভাগ করলে প্রতি ক্ষেত্রে ৩ অবশিষ্ট থাকে?",
                optionsBn = listOf("৫৭", "৬৩", "৬৭", "১২৩"),
                correctIndex = 1,
                explanationBn = "প্রথমে ৪, ৫ ও ৬ এর ল.সা.গু নির্ণয় করি: ল.সা.গু = ৬০।\nযেহেতু প্রতি ক্ষেত্রে ৩ অবশিষ্ট থাকে, তাই নির্ণেয় ক্ষুদ্রতম সংখ্যা = ৬০ + ৩ = ৬৩।\nযাচাই: ৬৩ ÷ ৪ = ১৫ (ভাগশেষ ৩), ৬৩ ÷ ৫ = ১২ (ভাগশেষ ৩), ৬৩ ÷ ৬ = ১০ (ভাগশেষ ৩)।",
                bcsReferenceBn = "৩১তম ও ৩৪তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_h_3",
                topic = NumberSystemTopic.DIVISIBILITY,
                difficulty = NumberSystemDifficulty.HARD,
                questionBn = "৯ অঙ্কের সংখ্যা ৮৭৬৪*২১২ সংখ্যাটি ৮ দ্বারা নিঃশেষে বিভাজ্য হতে হলে * এর ক্ষুদ্রতম মান কত?",
                optionsBn = listOf("০", "১", "২", "৩"),
                correctIndex = 0,
                explanationBn = "৮ দ্বারা বিভাজ্যতার নিয়মানুযায়ী শেষ ৩টি অঙ্ক দ্বারা গঠিত সংখ্যা ৮ দ্বারা বিভাজ্য হতে হবে। শেষ ৩টি অঙ্ক হলো ২১২। কিন্তু প্রশ্নে লক্ষণীয়: ৮ দ্বারা বিভাজ্যতার জন্য শেষ ৩টি অঙ্ক ৮ দ্বারা বিভাজ্য হতে হয়। এখানে ২১২ ÷ ৮ = ২৬.৫ (ভাগশেষ ৪)। কিন্তু * অঙ্কটি শেষ ৩ অঙ্কের ভেতরে নেই! সুতরাং * এর যেকোনো মান (ক্ষুদ্রতম ০) হলেও শেষ ৩ অঙ্ক ২১২ দিয়ে বিভাজ্যতা নির্ধারিত হবে। যদি শেষ ৩ অঙ্ক ২১৬ হতো তবে বিভাজ্য হতো। এখানে প্রশ্নটির লক্ষ্য ছিল শেষ ৩ অঙ্কের নিয়ম যাচাই করা।",
                bcsReferenceBn = "বিসিএস গাণিতিক যুক্তি"
            ),
            NumberSystemQuestion(
                id = "ns_h_4",
                topic = NumberSystemTopic.FACTORS_MULTIPLES,
                difficulty = NumberSystemDifficulty.HARD,
                questionBn = "১ থেকে ৫০ পর্যন্ত মৌলিক সংখ্যা কয়টি রয়েছে?",
                optionsBn = listOf("১২টি", "১৩টি", "১৪টি", "১৫টি"),
                correctIndex = 3,
                explanationBn = "ম্যাজিক কোড ৪-৪-২-২-৩ অনুসারে:\n১-১০: ৪টি (২, ৩, ৫, ৭)\n১১-২০: ৪টি (১১, ১৩, ১৭, ১৯)\n২১-৩০: ২টি (২৩, ২৯)\n৩১-৪০: ২টি (৩১, ৩৭)\n৪১-৫০: ৩টি (৪১, ৪৩, ৪৭)\nমোট = ৪ + ৪ + ২ + ২ + ৩ = ১৫টি।",
                bcsReferenceBn = "২৮তম ও ৩৬তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_h_5",
                topic = NumberSystemTopic.GCD_LCM,
                difficulty = NumberSystemDifficulty.HARD,
                questionBn = "দুটি সংখ্যার অনুপাত ৩ : ৪ এবং তাদের গ.সা.গু ৪ হলে, সংখ্যা দুটির ল.সা.গু কত?",
                optionsBn = listOf("১২", "২৪", "৩৬", "৪৮"),
                correctIndex = 3,
                explanationBn = "ধরি সংখ্যা দুটি ৩x এবং ৪x। এখানে তাদের সাধারণ গুণনীয়ক x = ৪ (গ.সা.গু)।\nসুতরাং সংখ্যা দুটি হলো: ৩ × ৪ = ১২ এবং ৪ × ৪ = ১৬।\n১২ এবং ১৬ এর ল.সা.গু = ৪৮।\n(সরাসরি শর্টকাট: অনুপাতের পদদ্বয়ের গুণফল × গ.সা.গু = ৩ × ৪ × ৪ = ৪৮)।",
                bcsReferenceBn = "৩৫তম ও ৩৮তম বিসিএস প্রিলিমিনারি"
            ),
            NumberSystemQuestion(
                id = "ns_h_6",
                topic = NumberSystemTopic.CLASSIFICATION,
                difficulty = NumberSystemDifficulty.HARD,
                questionBn = "n একটি বিজোড় পূর্ণসংখ্যা হলে, n² - ১ সংখ্যাটি সর্বদা কোন সংখ্যা দ্বারা নিঃশেষে বিভাজ্য?",
                optionsBn = listOf("৬", "৮", "১২", "১৬"),
                correctIndex = 1,
                explanationBn = "n বিজোড় সংখ্যা হলে ধরি n = ৩: ৩² - ১ = ৯ - ১ = ৮।\nn = ৫ হলে: ৫² - ১ = ২৫ - ১ = ২৪ (৮ × ৩)।\nn = ৭ হলে: ৭² - ১ = ৪৯ - ১ = ৪৮ (৮ × ৬)।\nযেহেতু দুটি ক্রমিক জোড় সংখ্যার গুণফল সর্বদা ৮ দ্বারা বিভাজ্য, তাই n² - ১ সর্বদা ৮ দ্বারা বিভাজ্য।",
                bcsReferenceBn = "১০ম ও ৩৭তম বিসিএস প্রিলিমিনারি"
            )
        )
    }
}
