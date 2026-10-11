package com.example.math

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Top-level mathematics subjects for student reference.
 */
enum class FormulaSubject(
    val titleBn: String,
    val titleEn: String,
    val descriptionBn: String,
    val primaryColor: Color,
    val containerColor: Color
) {
    ALGEBRA(
        titleBn = "বীজগণিত",
        titleEn = "Algebra",
        descriptionBn = "বর্গ, ঘন, সূচক, লগারিদম, ধারা ও সমীকরণ সংক্রান্ত সূত্রাবলি",
        primaryColor = Color(0xFF6366F1), // Indigo
        containerColor = Color(0xFFEEF2FF)
    ),
    GEOMETRY(
        titleBn = "জ্যামিতি",
        titleEn = "Geometry",
        descriptionBn = "ত্রিভুজ, চতুর্ভুজ, বৃত্ত, ঘনবস্তু ও স্থানাঙ্ক জ্যামিতির পরিমাপ",
        primaryColor = Color(0xFF0D9488), // Teal / Emerald
        containerColor = Color(0xFFCCFBF1)
    ),
    CALCULUS(
        titleBn = "ক্যালকুলাস",
        titleEn = "Calculus",
        descriptionBn = "অন্তরীকরণ, সমাকলন, সীমা এবং পরিবর্তনশীল রাশির হিসাব",
        primaryColor = Color(0xFFD97706), // Amber / Warm Orange
        containerColor = Color(0xFFFEF3C7)
    );

    fun getIcon(): ImageVector = when (this) {
        ALGEBRA -> Icons.Default.Functions
        GEOMETRY -> Icons.Default.Straighten
        CALCULUS -> Icons.Default.Calculate
    }
}

/**
 * Data model for a single math formula in the library.
 */
data class LibraryFormula(
    val id: String,
    val subject: FormulaSubject,
    val subCategory: String, // e.g. "বর্গ ও অভেদ", "ত্রিভুজ", "অন্তরীকরণ"
    val titleBn: String,
    val titleEn: String,
    val formulaExpression: String,
    val explanationBn: String,
    val variables: List<Pair<String, String>>, // e.g. "a" to "প্রথম পদ বা রাশি"
    val exampleProblemBn: String,
    val exampleSolutionBn: String,
    val tipsBn: String,
    val tags: List<String>
)

/**
 * Curated repository of complete math formulas across Algebra, Geometry, and Calculus
 * designed for Bengali-speaking students from school to college levels.
 */
object FormulaLibraryProvider {

    val allFormulas: List<LibraryFormula> by lazy {
        algebraFormulas + geometryFormulas + calculusFormulas
    }

    // -------------------------------------------------------------
    // ALGEBRA (বীজগণিত)
    // -------------------------------------------------------------
    val algebraFormulas: List<LibraryFormula> = listOf(
        LibraryFormula(
            id = "lib_alg_01",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "বর্গ ও অভেদ",
            titleBn = "দুটি পদের যোগফলের বর্গ",
            titleEn = "Square of a Binomial Sum",
            formulaExpression = "(a + b)² = a² + 2ab + b²",
            explanationBn = "দুটি রাশির যোগফলের বর্গ হলো প্রথম রাশির বর্গ, দুটি রাশির গুণফলের দ্বিগুণের যোগ এবং দ্বিতীয় রাশির বর্গের সমষ্টি।",
            variables = listOf(
                "a" to "প্রথম পদ বা চলক",
                "b" to "দ্বিতীয় পদ বা চলক"
            ),
            exampleProblemBn = "মান নির্ণয় করো: (3x + 4y)²",
            exampleSolutionBn = "(3x)² + 2 · (3x) · (4y) + (4y)²\n= 9x² + 24xy + 16y²",
            tipsBn = "মাঝের পদ +2ab লিখতে ভুলবেন না। অনেক শিক্ষার্থী শুধু a² + b² লিখে ভুল করে।",
            tags = listOf("বর্গ", "যোগফল", "algebra", "square", "binomial", "formula")
        ),
        LibraryFormula(
            id = "lib_alg_02",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "বর্গ ও অভেদ",
            titleBn = "দুটি পদের বিয়োগফলের বর্গ",
            titleEn = "Square of a Binomial Difference",
            formulaExpression = "(a − b)² = a² − 2ab + b²",
            explanationBn = "দুটি রাশির বিয়োগফলের বর্গ হলো প্রথম রাশির বর্গ থেকে পদদ্বয়ের গুণফলের দ্বিগুণ বিয়োগ এবং দ্বিতীয় রাশির বর্গের যোগফল।",
            variables = listOf(
                "a" to "প্রথম পদ বা চলক",
                "b" to "দ্বিতীয় পদ বা চলক"
            ),
            exampleProblemBn = "মান নির্ণয় করো: (5p − 2q)²",
            exampleSolutionBn = "(5p)² − 2 · (5p) · (2q) + (2q)²\n= 25p² − 20pq + 4q²",
            tipsBn = "মাঝের চিহ্ন ঋণাত্মক (-2ab), কিন্তু শেষ পদ সর্বদা ধনাত্মক (+b²)।",
            tags = listOf("বর্গ", "বিয়োগফল", "algebra", "square", "difference")
        ),
        LibraryFormula(
            id = "lib_alg_03",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "বর্গ ও অভেদ",
            titleBn = "দুটি বর্গের অন্তর (উৎপাদক সূত্র)",
            titleEn = "Difference of Two Squares",
            formulaExpression = "a² − b² = (a + b)(a − b)",
            explanationBn = "দুটি রাশির বর্গের বিয়োগফল তাদের যোগফল ও বিয়োগফলের গুণফলের সমান। উৎপাদকে বিশ্লেষণের সবচেয়ে জরুরি সূত্র।",
            variables = listOf(
                "a" to "প্রথম রাশি",
                "b" to "দ্বিতীয় রাশি"
            ),
            exampleProblemBn = "উৎপাদকে বিশ্লেষণ করো: 49x² − 64y²",
            exampleSolutionBn = "(7x)² − (8y)²\n= (7x + 8y)(7x − 8y)",
            tipsBn = "উভয় পদকে পূর্ণবর্গ আকারে রূপান্তর করে তারপর সূত্র প্রয়োগ করুন।",
            tags = listOf("বর্গ", "অন্তর", "উৎপাদক", "difference of squares", "factorization")
        ),
        LibraryFormula(
            id = "lib_alg_04",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "বর্গ ও অভেদ",
            titleBn = "তিনটি পদের যোগফলের বর্গ",
            titleEn = "Square of a Trinomial",
            formulaExpression = "(a + b + c)² = a² + b² + c² + 2(ab + bc + ca)",
            explanationBn = "তিনটি পদের যোগফলের বর্গ প্রতিটি পদের বর্গের যোগফল এবং জোড়ায় জোড়ায় গুণফলের দ্বিগুণের সমষ্টি।",
            variables = listOf(
                "a, b, c" to "তিনটি প্রদত্ত পদ বা চলক"
            ),
            exampleProblemBn = "মান নির্ণয় করো: (x + 2y + 3z)²",
            exampleSolutionBn = "x² + (2y)² + (3z)² + 2(x·2y + 2y·3z + 3z·x)\n= x² + 4y² + 9z² + 4xy + 12yz + 6zx",
            tipsBn = "যেকোনো একটি পদ ঋণাত্মক হলে চিহ্নের হিসাব অনুযায়ী 2(ab + bc + ca) অংশের চিহ্ন পরিবর্তন হবে।",
            tags = listOf("বর্গ", "ত্রিপদী", "trinomial", "square")
        ),
        LibraryFormula(
            id = "lib_alg_05",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "বর্গ ও অভেদ",
            titleBn = "৪ab এর অনুসিদ্ধান্ত সূত্র",
            titleEn = "Product Formula (4ab)",
            formulaExpression = "4ab = (a + b)² − (a − b)²",
            explanationBn = "যোগফলের বর্গ থেকে বিয়োগফলের বর্গ বাদ দিলে চারটি গুণফল পদ (4ab) পাওয়া যায়। ab = [(a + b)/2]² − [(a − b)/2]² রূপে ব্যবহার্য।",
            variables = listOf(
                "a + b" to "দুটি পদের যোগফল",
                "a - b" to "দুটি পদের বিয়োগফল"
            ),
            exampleProblemBn = "যদি a + b = 7 এবং a − b = 3 হয়, তবে ab এর মান কত?",
            exampleSolutionBn = "ab = [(a + b)/2]² − [(a − b)/2]²\n= (7/2)² − (3/2)² = 49/4 − 9/4 = 40/4 = 10",
            tipsBn = "পরীক্ষায় সরাসরি ab এর মান জানতে চাইলে এই অনুসিদ্ধান্তটি সবচেয়ে দ্রুত সমাধান দেয়।",
            tags = listOf("অনুসিদ্ধান্ত", "4ab", "ab", "বর্গ")
        ),
        LibraryFormula(
            id = "lib_alg_06",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "ঘন ও উৎপাদক",
            titleBn = "দুটি পদের যোগফলের ঘন",
            titleEn = "Cube of a Binomial Sum",
            formulaExpression = "(a + b)³ = a³ + 3a²b + 3ab² + b³ = a³ + b³ + 3ab(a + b)",
            explanationBn = "দুটি রাশির যোগফলের ঘন নির্ণয় এবং মান নির্ণয়ের জন্য অত্যন্ত কার্যকর সূত্র।",
            variables = listOf(
                "a, b" to "প্রদত্ত দুটি রাশি"
            ),
            exampleProblemBn = "যদি x + y = 4 এবং xy = 3 হয়, তবে x³ + y³ এর মান কত?",
            exampleSolutionBn = "x³ + y³ = (x + y)³ − 3xy(x + y)\n= 4³ − 3(3)(4) = 64 − 36 = 28",
            tipsBn = "মান নির্ণয়ের ক্ষেত্রে a³ + b³ = (a + b)³ − 3ab(a + b) রূপটি বেশি ব্যবহৃত হয়।",
            tags = listOf("ঘন", "যোগফল", "cube", "algebra")
        ),
        LibraryFormula(
            id = "lib_alg_07",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "ঘন ও উৎপাদক",
            titleBn = "দুটি পদের বিয়োগফলের ঘন",
            titleEn = "Cube of a Binomial Difference",
            formulaExpression = "(a − b)³ = a³ − 3a²b + 3ab² − b³ = a³ − b³ − 3ab(a − b)",
            explanationBn = "দুটি রাশির বিয়োগফলের ঘন নির্ণয় এবং a³ − b³ মান নির্ণয়ের জন্য প্রয়োগ করা হয়।",
            variables = listOf(
                "a, b" to "প্রদত্ত দুটি রাশি"
            ),
            exampleProblemBn = "যদি m − n = 5 এবং mn = 2 হয়, তবে m³ − n³ এর মান কত?",
            exampleSolutionBn = "m³ − n³ = (m − n)³ + 3mn(m − n)\n= 5³ + 3(2)(5) = 125 + 30 = 155",
            tipsBn = "চিহ্নগুলো মনে রাখুন: −3a²b এবং −b³, কিন্তু +3ab² ধনাত্মক।",
            tags = listOf("ঘন", "বিয়োগফল", "cube difference")
        ),
        LibraryFormula(
            id = "lib_alg_08",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "ঘন ও উৎপাদক",
            titleBn = "দুটি ঘনের যোগফল (উৎপাদক সূত্র)",
            titleEn = "Sum of Two Cubes",
            formulaExpression = "a³ + b³ = (a + b)(a² − ab + b²)",
            explanationBn = "দুটি ঘন রাশির যোগফলকে উৎপাদকে বিশ্লেষণ করতে এই সূত্র ব্যবহৃত হয়।",
            variables = listOf(
                "a, b" to "উৎপাদক নির্ণয়যোগ্য রাশি"
            ),
            exampleProblemBn = "উৎপাদকে বিশ্লেষণ করো: 8x³ + 27",
            exampleSolutionBn = "(2x)³ + 3³\n= (2x + 3)[(2x)² − (2x)(3) + 3²]\n= (2x + 3)(4x² − 6x + 9)",
            tipsBn = "দ্বিতীয় বন্ধনীতে মধ্যপদটি −ab (বিয়োগ চিহ্নযুক্ত), −2ab নয়।",
            tags = listOf("ঘন", "উৎপাদক", "sum of cubes")
        ),
        LibraryFormula(
            id = "lib_alg_09",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "সূচক ও ঘাত",
            titleBn = "সূচকের মৌলিক গুণ ও ভাগ বিধি",
            titleEn = "Laws of Indices (Product & Quotient)",
            formulaExpression = "aᵐ · aⁿ = aᵐ⁺ⁿ  এবং  aᵐ / aⁿ = aᵐ⁻ⁿ",
            explanationBn = "ভিত্তি একই থাকলে গুণ করার সময় সূচক বা ঘাত যোগ হয়, এবং ভাগ করার সময় সূচক বিয়োগ হয়।",
            variables = listOf(
                "a" to "ভিত্তি বা Base (a ≠ 0)",
                "m, n" to "সূচক বা Exponent"
            ),
            exampleProblemBn = "সরল করো: (2⁵ · 2³) / 2⁴",
            exampleSolutionBn = "লব = 2⁵⁺³ = 2⁸\nসুতরাং = 2⁸ / 2⁴ = 2⁸⁻⁴ = 2⁴ = 16",
            tipsBn = "ভিত্তি একই হলেই কেবল ঘাত যোগ বা বিয়োগ করা যাবে।",
            tags = listOf("সূচক", "ঘাত", "indices", "exponents")
        ),
        LibraryFormula(
            id = "lib_alg_10",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "সূচক ও ঘাত",
            titleBn = "সূচকের শূন্য ঘাত ও ঋণাত্মক ঘাত",
            titleEn = "Zero & Negative Exponents",
            formulaExpression = "a⁰ = 1  (a ≠ 0)  এবং  a⁻ⁿ = 1 / aⁿ",
            explanationBn = "যেকোনো অশূন্য সংখ্যার শূন্য ঘাত সর্বদা ১ হয়, এবং ঋণাত্মক ঘাত ভগ্নাংশের বিপরীত রূপে রূপান্তর হয়।",
            variables = listOf(
                "a" to "অশূন্য বাস্তব সংখ্যা",
                "n" to "ধনাত্মক পূর্ণসংখ্যা"
            ),
            exampleProblemBn = "মান নির্ণয় করো: 5⁰ + 3⁻²",
            exampleSolutionBn = "5⁰ = 1 এবং 3⁻² = 1 / 3² = 1/9\nসুতরাং মান = 1 + 1/9 = 10/9",
            tipsBn = "0⁰ কিন্তু অনির্ণেয় (Indeterminate)। সূত্রটি শুধুমাত্র a ≠ 0 এর জন্য প্রযোজ্য।",
            tags = listOf("শূন্য ঘাত", "ঋণাত্মক ঘাত", "zero exponent", "negative exponent")
        ),
        LibraryFormula(
            id = "lib_alg_11",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "লগারিদম",
            titleBn = "লগারিদমের গুণ ও ভাগ বিধি",
            titleEn = "Logarithm Product & Quotient Rules",
            formulaExpression = "logₐ(mn) = logₐ m + logₐ n  এবং  logₐ(m / n) = logₐ m − logₐ n",
            explanationBn = "গুণফলের লগারিদম তাদের আলাদা লগের যোগফলের সমান, এবং ভাগফলের লগ তাদের লগের বিয়োগফলের সমান।",
            variables = listOf(
                "a" to "লগারিদমের ভিত্তি (a > 0, a ≠ 1)",
                "m, n" to "ধনাত্মক বাস্তব সংখ্যা"
            ),
            exampleProblemBn = "যদি log₁₀ 2 = 0.3010 এবং log₁₀ 3 = 0.4771 হয়, তবে log₁₀ 6 কত?",
            exampleSolutionBn = "log₁₀ 6 = log₁₀(2 × 3) = log₁₀ 2 + log₁₀ 3\n= 0.3010 + 0.4771 = 0.7781",
            tipsBn = "মনে রাখবেন: log(m + n) ≠ log m + log n। লগ শুধুমাত্র গুণের ক্ষেত্রে যোগে ভাঙে।",
            tags = listOf("লগারিদম", "লগ", "logarithm", "product rule")
        ),
        LibraryFormula(
            id = "lib_alg_12",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "লগারিদম",
            titleBn = "লগারিদমের ঘাত ও ভিত্তি পরিবর্তন বিধি",
            titleEn = "Log Power Rule & Change of Base",
            formulaExpression = "logₐ(mᵏ) = k · logₐ m  এবং  logₐ b = log꜀ b / log꜀ a",
            explanationBn = "আর্গুমেন্টের ঘাত লগের সামনে সহগ হিসেবে চলে আসে। ভিন্ন ভিত্তিতে রূপান্তরের জন্য ভিত্তি পরিবর্তন সূত্র ব্যবহৃত হয়।",
            variables = listOf(
                "k" to "যেকোনো বাস্তব ঘাত",
                "c" to "নতুন নির্বাচিত ভিত্তি"
            ),
            exampleProblemBn = "মান নির্ণয় করো: log₂ 32",
            exampleSolutionBn = "32 = 2⁵\nসুতরাং log₂ 32 = log₂(2⁵) = 5 · log₂ 2 = 5 · 1 = 5",
            tipsBn = "ভিত্তি এবং আর্গুমেন্ট একই হলে logₐ a = 1 হয়।",
            tags = listOf("লগ", "ভিত্তি পরিবর্তন", "change of base", "power rule")
        ),
        LibraryFormula(
            id = "lib_alg_13",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "ধারা ও অনুক্রম",
            titleBn = "সমান্তর ধারার n-তম পদ ও সমষ্টি",
            titleEn = "Arithmetic Progression (AP)",
            formulaExpression = "Tₙ = a + (n − 1)d  এবং  Sₙ = (n / 2)[2a + (n − 1)d]",
            explanationBn = "পরপর দুটি পদের অন্তর সর্বদা সমান হলে তা সমান্তর ধারা। প্রথম পদ a এবং সাধারণ অন্তর d।",
            variables = listOf(
                "a" to "প্রথম পদ",
                "d" to "সাধারণ অন্তর (Common Difference)",
                "n" to "পদের সংখ্যা",
                "Tₙ" to "n-তম পদ",
                "Sₙ" to "প্রথম n সংখ্যক পদের সমষ্টি"
            ),
            exampleProblemBn = "ধারার 1 + 4 + 7 + 10 + ... এর 15তম পদ কত?",
            exampleSolutionBn = "এখানে a = 1, d = 4 − 1 = 3, n = 15\nT₁₅ = 1 + (15 − 1)·3 = 1 + 14·3 = 1 + 42 = 43",
            tipsBn = "শেষ পদ L জানা থাকলে সমষ্টির সহজ সূত্র: Sₙ = (n / 2)(a + L)।",
            tags = listOf("ধারা", "সমান্তর ধারা", "arithmetic progression", "AP", "series")
        ),
        LibraryFormula(
            id = "lib_alg_14",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "ধারা ও অনুক্রম",
            titleBn = "গুণোত্তর ধারার n-তম পদ ও সমষ্টি",
            titleEn = "Geometric Progression (GP)",
            formulaExpression = "Tₙ = a · rⁿ⁻¹  এবং  Sₙ = a(rⁿ − 1) / (r − 1)  [r > 1]",
            explanationBn = "পরপর দুটি পদের অনুপাত সর্বদা সমান হলে তা গুণোত্তর ধারা। সাধারণ অনুপাত r।",
            variables = listOf(
                "a" to "প্রথম পদ",
                "r" to "সাধারণ অনুপাত (Common Ratio)",
                "n" to "পদের সংখ্যা"
            ),
            exampleProblemBn = "ধারা: 2 + 6 + 18 + 54 + ... এর ৬ষ্ঠ পদ কত?",
            exampleSolutionBn = "এখানে a = 2, r = 6 / 2 = 3, n = 6\nT₆ = 2 · 3⁶⁻¹ = 2 · 3⁵ = 2 · 243 = 486",
            tipsBn = "যদি r < 1 হয় তবে সমষ্টির সূত্র: Sₙ = a(1 − rⁿ) / (1 − r)।",
            tags = listOf("গুণোত্তর ধারা", "GP", "geometric progression", "series")
        ),
        LibraryFormula(
            id = "lib_alg_15",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "সমীকরণ",
            titleBn = "দ্বিঘাত সমীকরণের মূল ও সমাধান সূত্র",
            titleEn = "Quadratic Equation Formula",
            formulaExpression = "x = [−b ± √(b² − 4ac)] / (2a)",
            explanationBn = "যেকোনো এক চলকবিশিষ্ট দ্বিঘাত সমীকরণ ax² + bx + c = 0 (a ≠ 0) এর দুটি মূল নির্ণয়ের মূল সূত্র। শ্রীধর আচার্যের সূত্র নামেও পরিচিত।",
            variables = listOf(
                "a" to "x² এর সহগ (a ≠ 0)",
                "b" to "x এর সহগ",
                "c" to "ধ্রুবক পদ"
            ),
            exampleProblemBn = "সমাধান করো: 2x² − 5x + 2 = 0",
            exampleSolutionBn = "a = 2, b = −5, c = 2\nx = [−(−5) ± √(25 − 4·2·2)] / (2·2)\nx = [5 ± √(25 − 16)] / 4 = [5 ± 3] / 4\nমূল দুটি হলো x = 2 অথবা x = 1/2",
            tipsBn = "ভাগফল 2a দ্বারা হবে, শুধু a দ্বারা নয়। রুটের ভিতরের অংশ b² − 4ac হলো নিশ্চায়ক (Discriminant)।",
            tags = listOf("দ্বিঘাত সমীকরণ", "quadratic equation", "roots", "শ্রীধর আচার্য")
        ),
        LibraryFormula(
            id = "lib_alg_16",
            subject = FormulaSubject.ALGEBRA,
            subCategory = "সমীকরণ",
            titleBn = "দ্বিঘাত সমীকরণের মূলের সম্পর্ক ও নিশ্চায়ক",
            titleEn = "Roots Relationship & Discriminant (D)",
            formulaExpression = "D = b² − 4ac,  α + β = −b/a,  α·β = c/a",
            explanationBn = "D > 0 হলে মূলদ্বয় বাস্তব ও অসমান; D = 0 হলে মূলদ্বয় বাস্তব ও সমান; D < 0 হলে মূলদ্বয় জটিল/অবাস্তব। মূলদ্বয়ের যোগফল ও গুণফল সমীকরণ গঠনে সাহায্য করে।",
            variables = listOf(
                "D" to "নিশ্চায়ক (Discriminant)",
                "α, β" to "সমীকরণের দুটি মূল"
            ),
            exampleProblemBn = "যদি সমীকরণের মূলদ্বয় 3 এবং 5 হয়, তবে সমীকরণটি কী হবে?",
            exampleSolutionBn = "মূলদ্বয়ের যোগফল = 3 + 5 = 8\nমূলদ্বয়ের গুণফল = 3 × 5 = 15\nসমীকরণ: x² − (α + β)x + αβ = 0\nবা, x² − 8x + 15 = 0",
            tipsBn = "D পূর্ণবর্গ হলে মূলদ্বয় মূলদ (rational) হবে।",
            tags = listOf("নিশ্চায়ক", "discriminant", "roots", "সমীকরণ গঠন")
        )
    )

    // -------------------------------------------------------------
    // GEOMETRY (জ্যামিতি)
    // -------------------------------------------------------------
    val geometryFormulas: List<LibraryFormula> = listOf(
        LibraryFormula(
            id = "lib_geom_01",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ত্রিভুজ",
            titleBn = "সাধারণ ত্রিভুজের ক্ষেত্রফল",
            titleEn = "Area of a Triangle",
            formulaExpression = "ক্ষেত্রফল = ½ × ভূমি × উচ্চতা = ½ · b · h",
            explanationBn = "যেকোনো ত্রিভুজের ভূমি এবং ভূমির ওপর বিপরীত শীর্ষবিন্দু থেকে অঙ্কিত লম্ব উচ্চতার গুণফলের অর্ধেক হলো ক্ষেত্রফল।",
            variables = listOf(
                "b" to "ভূমি (Base)",
                "h" to "লম্ব উচ্চতা (Height)"
            ),
            exampleProblemBn = "একটি ত্রিভুজের ভূমি 12 সেমি এবং উচ্চতা 8 সেমি হলে ক্ষেত্রফল কত?",
            exampleSolutionBn = "ক্ষেত্রফল = ½ × 12 × 8 = 48 বর্গ সেমি",
            tipsBn = "উচ্চতা অবশ্যই ভূমির ওপর লম্ব হতে হবে। তির্যক বাহু উচ্চতা নয়।",
            tags = listOf("ত্রিভুজ", "ক্ষেত্রফল", "triangle", "area", "geometry")
        ),
        LibraryFormula(
            id = "lib_geom_02",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ত্রিভুজ",
            titleBn = "হেরনের সূত্র (বিষমবাহু ত্রিভুজের ক্ষেত্রফল)",
            titleEn = "Heron's Formula",
            formulaExpression = "ক্ষেত্রফল = √[s(s − a)(s − b)(s − c)]  যেখানে s = (a + b + c)/2",
            explanationBn = "যখন ত্রিভুজের তিনটি বাহুর দৈর্ঘ্য জানা থাকে কিন্তু উচ্চতা জানা থাকে না, তখন হেরনের সূত্রের মাধ্যমে নির্ভুল ক্ষেত্রফল নির্ণয় করা যায়।",
            variables = listOf(
                "a, b, c" to "ত্রিভুজের তিনটি বাহুর দৈর্ঘ্য",
                "s" to "অর্ধ-পরিসীমা (Semi-perimeter)"
            ),
            exampleProblemBn = "একটি ত্রিভুজের বাহুগুলোর দৈর্ঘ্য 5 সেমি, 6 সেমি এবং 7 সেমি। ক্ষেত্রফল কত?",
            exampleSolutionBn = "s = (5 + 6 + 7) / 2 = 18 / 2 = 9 সেমি\nক্ষেত্রফল = √[9(9 − 5)(9 − 6)(9 − 7)]\n= √[9 × 4 × 3 × 2] = √216 = 6√6 ≈ 14.70 বর্গ সেমি",
            tipsBn = "প্রথমে s এর মান সাবধানে বের করে নিন। s = (a + b + c) / 2।",
            tags = listOf("হেরন", "heron", "ত্রিভুজ", "বিষমবাহু", "area")
        ),
        LibraryFormula(
            id = "lib_geom_03",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ত্রিভুজ",
            titleBn = "সমবাহু ত্রিভুজের ক্ষেত্রফল ও উচ্চতা",
            titleEn = "Equilateral Triangle",
            formulaExpression = "ক্ষেত্রফল = (√3 / 4)a²  এবং  উচ্চতা h = (√3 / 2)a",
            explanationBn = "যে ত্রিভুজের তিনটি বাহুই সমান (দৈর্ঘ্য a), তার ক্ষেত্রফল ও উচ্চতা কেবল এক বাহুর সাহায্যে এই সূত্রে বের করা যায়।",
            variables = listOf(
                "a" to "সমবাহু ত্রিভুজের প্রতিটি বাহুর দৈর্ঘ্য",
                "√3" to "ধ্রুবক (মান প্রায় 1.732)"
            ),
            exampleProblemBn = "একটি সমবাহু ত্রিভুজের বাহুর দৈর্ঘ্য 6 মিটার হলে ক্ষেত্রফল কত?",
            exampleSolutionBn = "ক্ষেত্রফল = (√3 / 4) × 6² = (√3 / 4) × 36 = 9√3 ≈ 15.588 বর্গ মিটার",
            tipsBn = "সমদ্বিবাহু ত্রিভুজের ক্ষেত্রে ক্ষেত্রফল = (b / 4)√(4a² − b²), যেখানে a সমান বাহু ও b ভূমি।",
            tags = listOf("সমবাহু ত্রিভুজ", "equilateral triangle", "area")
        ),
        LibraryFormula(
            id = "lib_geom_04",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ত্রিভুজ",
            titleBn = "পিথাগোরাসের উপপাদ্য (সমকোণী ত্রিভুজ)",
            titleEn = "Pythagorean Theorem",
            formulaExpression = "অতিভুজ² = লম্ব² + ভূমি²  ⇒  c² = a² + b²",
            explanationBn = "যেকোনো সমকোণী ত্রিভুজে অতিভুজের ওপর অঙ্কিত বর্গক্ষেত্রের ক্ষেত্রফল অপর দুই বাহুর ওপর অঙ্কিত বর্গক্ষেত্রদ্বয়ের ক্ষেত্রফলের সমষ্টির সমান।",
            variables = listOf(
                "c" to "অতিভুজ (সমকোণের বিপরীত বৃহত্তম বাহু)",
                "a" to "লম্ব (Perpendicular)",
                "b" to "ভূমি (Base)"
            ),
            exampleProblemBn = "সমকোণী ত্রিভুজের লম্ব 3 সেমি ও ভূমি 4 সেমি হলে অতিভুজ কত?",
            exampleSolutionBn = "c² = 3² + 4² = 9 + 16 = 25\nc = √25 = 5 সেমি",
            tipsBn = "বহুল প্রচলিত পিথাগোরীয় ত্রয়ী (Pythagorean Triplets): (3,4,5), (5,12,13), (8,15,17), (7,24,25)।",
            tags = listOf("পিথাগোরাস", "সমকোণী", "pythagoras", "hypotenuse")
        ),
        LibraryFormula(
            id = "lib_geom_05",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "চতুর্ভুজ",
            titleBn = "আয়তক্ষেত্রের ক্ষেত্রফল, পরিসীমা ও কর্ণ",
            titleEn = "Rectangle Properties",
            formulaExpression = "ক্ষেত্রফল = a · b,  পরিসীমা = 2(a + b),  কর্ণ d = √(a² + b²)",
            explanationBn = "বিপরীত বাহুগুলো পরস্পর সমান ও সমান্তরাল এবং প্রত্যেক কোণ সমকোণ।",
            variables = listOf(
                "a" to "দৈর্ঘ্য (Length)",
                "b" to "প্রস্থ (Width)",
                "d" to "কর্ণের দৈর্ঘ্য (Diagonal)"
            ),
            exampleProblemBn = "একটি আয়তক্ষেত্রের দৈর্ঘ্য 8 মি এবং প্রস্থ 6 মি হলে কর্ণ কত?",
            exampleSolutionBn = "কর্ণ d = √(8² + 6²) = √(64 + 36) = √100 = 10 মিটার",
            tipsBn = "কর্ণ দুটি পরস্পর সমান এবং পরস্পরকে সমদ্বিখণ্ডিত করে।",
            tags = listOf("আয়তক্ষেত্র", "rectangle", "area", "perimeter", "diagonal")
        ),
        LibraryFormula(
            id = "lib_geom_06",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "চতুর্ভুজ",
            titleBn = "বর্গক্ষেত্রের ক্ষেত্রফল, পরিসীমা ও কর্ণ",
            titleEn = "Square Properties",
            formulaExpression = "ক্ষেত্রফল = a²,  পরিসীমা = 4a,  কর্ণ d = a√2",
            explanationBn = "চারটি বাহু সমান এবং প্রত্যেকটি কোণ সমকোণ (90°)।",
            variables = listOf(
                "a" to "বর্গক্ষেত্রের বাহুর দৈর্ঘ্য",
                "d" to "কর্ণের দৈর্ঘ্য"
            ),
            exampleProblemBn = "একটি বর্গক্ষেত্রের পরিসীমা 20 সেমি হলে কর্ণের দৈর্ঘ্য কত?",
            exampleSolutionBn = "বাহু a = 20 / 4 = 5 সেমি\nকর্ণ d = 5√2 ≈ 7.07 সেমি",
            tipsBn = "কর্ণ জানা থাকলে ক্ষেত্রফল সরাসরি নির্ণয়: ক্ষেত্রফল = d² / 2।",
            tags = listOf("বর্গক্ষেত্র", "square", "area", "perimeter")
        ),
        LibraryFormula(
            id = "lib_geom_07",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "চতুর্ভুজ",
            titleBn = "রম্বস ও ট্রাপিজিয়ামের ক্ষেত্রফল",
            titleEn = "Rhombus & Trapezium Area",
            formulaExpression = "রম্বসের ক্ষেত্রফল = ½ · d₁ · d₂  |  ট্রাপিজিয়ামের ক্ষেত্রফল = ½(a + b)h",
            explanationBn = "রম্বসের ক্ষেত্রফল কর্ণদ্বয়ের গুণফলের অর্ধেক। ট্রাপিজিয়ামের ক্ষেত্রফল সমান্তরাল বাহুদ্বয়ের যোগফলের অর্ধেক ও তাদের মধ্যবর্তী দূরত্বের গুণফল।",
            variables = listOf(
                "d₁, d₂" to "রম্বসের দুটি কর্ণ",
                "a, b" to "ট্রাপিজিয়ামের সমান্তরাল দুটি বাহু",
                "h" to "সমান্তরাল বাহুদ্বয়ের লম্ব দূরত্ব বা উচ্চতা"
            ),
            exampleProblemBn = "একটি ট্রাপিজিয়ামের সমান্তরাল বাহু 9 সেমি ও 5 সেমি এবং উচ্চতা 4 সেমি হলে ক্ষেত্রফল কত?",
            exampleSolutionBn = "ক্ষেত্রফল = ½ × (9 + 5) × 4 = ½ × 14 × 4 = 28 বর্গ সেমি",
            tipsBn = "রম্বসের কর্ণদ্বয় পরস্পরকে সমকোণে সমদ্বিখণ্ডিত করে।",
            tags = listOf("রম্বস", "ট্রাপিজিয়াম", "rhombus", "trapezium", "quadrilateral")
        ),
        LibraryFormula(
            id = "lib_geom_08",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "বৃত্ত",
            titleBn = "বৃত্তের পরিধি ও ক্ষেত্রফল",
            titleEn = "Circle Circumference & Area",
            formulaExpression = "পরিধি C = 2πr,  ক্ষেত্রফল A = πr²",
            explanationBn = "বৃত্তের সীমানার মোট দৈর্ঘ্য হলো পরিধি এবং ভিতরের অঞ্চলের পরিমাপ হলো ক্ষেত্রফল।",
            variables = listOf(
                "r" to "বৃত্তের ব্যাসার্ধ (Radius)",
                "d = 2r" to "বৃত্তের ব্যাস (Diameter)",
                "π" to "পাই ধ্রুবক (প্রায় 22/7 বা 3.14159)"
            ),
            exampleProblemBn = "7 সেমি ব্যাসার্ধবিশিষ্ট একটি বৃত্তের পরিধি ও ক্ষেত্রফল কত?",
            exampleSolutionBn = "পরিধি = 2 × (22/7) × 7 = 44 সেমি\nক্ষেত্রফল = (22/7) × 7² = 154 বর্গ সেমি",
            tipsBn = "প্রশ্নে ব্যাস (Diameter) দেওয়া থাকলে প্রথমে ২ দিয়ে ভাগ করে ব্যাসার্ধ r বের করতে ভুলবেন না।",
            tags = listOf("বৃত্ত", "পরিধি", "circle", "circumference", "area")
        ),
        LibraryFormula(
            id = "lib_geom_09",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "বৃত্ত",
            titleBn = "বৃত্তচাপের দৈর্ঘ্য ও বৃত্তকলার ক্ষেত্রফল",
            titleEn = "Arc Length & Sector Area",
            formulaExpression = "চাপের দৈর্ঘ্য s = (θ / 360°) · 2πr  এবং  বৃত্তকলা A = (θ / 360°) · πr²",
            explanationBn = "কেন্দ্রে উৎপন্ন কোণ θ ডিগ্রি হলে বৃত্তচাপ ও বৃত্তকলার পরিমাপ পুরো বৃত্তের (360°) অনুপাত হিসেবে নির্ধারিত হয়।",
            variables = listOf(
                "θ" to "কেন্দ্রে উৎপন্ন কোণ ডিগ্রিতে (Degrees)",
                "r" to "বৃত্তের ব্যাসার্ধ"
            ),
            exampleProblemBn = "ব্যাসার্ধ 6 সেমি এবং কেন্দ্রে কোণ 60° হলে বৃত্তকলার ক্ষেত্রফল কত?",
            exampleSolutionBn = "ক্ষেত্রফল = (60° / 360°) × π × 6² = (1/6) × 36π = 6π ≈ 18.85 বর্গ সেমি",
            tipsBn = "কোণটি রেডিয়ানে দেওয়া থাকলে: চাপের দৈর্ঘ্য s = r·θ এবং ক্ষেত্রফল A = ½ r² θ।",
            tags = listOf("বৃত্তকলা", "বৃত্তচাপ", "arc", "sector", "radians")
        ),
        LibraryFormula(
            id = "lib_geom_10",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ঘনবস্তু",
            titleBn = "বেলন বা সিলিন্ডারের আয়তন ও ক্ষেত্রফল",
            titleEn = "Cylinder Volume & Surface Area",
            formulaExpression = "আয়তন V = πr²h,  বক্রতল = 2πrh,  সমগ্রতল = 2πr(r + h)",
            explanationBn = "বৃত্তাকার ভিত্তির ওপর খাড়া সিলিন্ডারের স্থান দখলকারী ধারণক্ষমতা হলো আয়তন এবং তার তলগুলোর পৃষ্ঠতলের ক্ষেত্রফল।",
            variables = listOf(
                "r" to "ভিত্তির ব্যাসার্ধ",
                "h" to "সিলিন্ডারের উচ্চতা"
            ),
            exampleProblemBn = "ব্যাসার্ধ 7 সেমি এবং উচ্চতা 10 সেমি হলে সিলিন্ডারের আয়তন কত?",
            exampleSolutionBn = "আয়তন V = (22/7) × 7² × 10 = (22/7) × 49 × 10 = 1540 ঘন সেমি",
            tipsBn = "বক্রতল হলো শুধু পাশের ঘোরানো অংশ। দুই প্রান্তের ঢাকনা যোগ করলে সমগ্রতল পাওয়া যায়।",
            tags = listOf("সিলিন্ডার", "বেলন", "cylinder", "volume", "surface area")
        ),
        LibraryFormula(
            id = "lib_geom_11",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ঘনবস্তু",
            titleBn = "গোলকের পৃষ্ঠতল ও আয়তন",
            titleEn = "Sphere Surface Area & Volume",
            formulaExpression = "পৃষ্ঠতল A = 4πr²,  আয়তন V = (4/3)πr³",
            explanationBn = "পূর্ণ গোলাকার বস্তুর বাহ্যিক ক্ষেত্রফল এবং মোট অভ্যন্তরীণ আয়তন বা ধারণক্ষমতা।",
            variables = listOf(
                "r" to "গোলকের ব্যাসার্ধ",
                "π" to "ধ্রুবক (৩.১৪১৬)"
            ),
            exampleProblemBn = "একটি গোলকের ব্যাসার্ধ 3 মিটার হলে এর আয়তন কত?",
            exampleSolutionBn = "V = (4/3) × π × 3³ = (4/3) × π × 27 = 36π ≈ 113.1 ঘন মিটার",
            tipsBn = "অর্ধগোলক (Hemisphere) এর ক্ষেত্রে: আয়তন = (2/3)πr³ এবং সমগ্রতল = 3πr²।",
            tags = listOf("গোলক", "sphere", "volume", "surface area", "3D")
        ),
        LibraryFormula(
            id = "lib_geom_12",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ঘনবস্তু",
            titleBn = "শঙ্কুর আয়তন, বক্রতল ও সমগ্রতল",
            titleEn = "Cone Volume & Surface Area",
            formulaExpression = "আয়তন V = (1/3)πr²h,  বক্রতল = πrl,  সমগ্রতল = πr(r + l)",
            explanationBn = "যেখানে তীর্যক উচ্চতা l = √(r² + h²)। শঙ্কুর আয়তন একই ব্যাসার্ধ ও উচ্চতার সিলিন্ডারের আয়তনের এক-তৃতীয়াংশ।",
            variables = listOf(
                "r" to "ভিত্তির ব্যাসার্ধ",
                "h" to "উল্লম্ব উচ্চতা",
                "l" to "তীর্যক উচ্চতা (Slant Height)"
            ),
            exampleProblemBn = "ব্যাসার্ধ 3 সেমি ও উচ্চতা 4 সেমি হলে শঙ্কুর তীর্যক উচ্চতা ও আয়তন কত?",
            exampleSolutionBn = "l = √(3² + 4²) = √25 = 5 সেমি\nআয়তন = (1/3) × π × 3² × 4 = 12π ≈ 37.7 ঘন সেমি",
            tipsBn = "ক্ষেত্রফল ও আয়তনের সূত্রের পার্থক্য লক্ষ্য করুন: আয়তনে উচ্চতা h, কিন্তু বক্রতলে তীর্যক উচ্চতা l ব্যবহৃত হয়।",
            tags = listOf("শঙ্কু", "cone", "volume", "slant height")
        ),
        LibraryFormula(
            id = "lib_geom_13",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "ঘনবস্তু",
            titleBn = "ঘনক ও আয়তাকার ঘনবস্তু",
            titleEn = "Cube & Cuboid Properties",
            formulaExpression = "ঘনক: V = a³, সমগ্রতল = 6a² | আয়তাকার: V = abc, তল = 2(ab + bc + ca)",
            explanationBn = "ঘনকের প্রতিটি বাহু সমান। আয়তাকার ঘনবস্তুর তিনটি মাত্রা দৈর্ঘ্য a, প্রস্থ b ও উচ্চতা c।",
            variables = listOf(
                "a, b, c" to "দৈর্ঘ্য, প্রস্থ ও উচ্চতা",
                "কর্ণ" to "আয়তাকারের কর্ণ = √(a² + b² + c²), ঘনকের কর্ণ = a√3"
            ),
            exampleProblemBn = "একটি ঘনকের বাহু 4 সেমি হলে এর কর্ণ ও আয়তন কত?",
            exampleSolutionBn = "আয়তন = 4³ = 64 ঘন সেমি\nকর্ণ = 4√3 ≈ 6.93 সেমি",
            tipsBn = "ঘরের মধ্যে রাখা যায় এমন দীর্ঘতম লাঠির দৈর্ঘ্য হলো ঘরের কর্ণের দৈর্ঘ্যের সমান: √(a² + b² + c²)।",
            tags = listOf("ঘনক", "আয়তাকার ঘনবস্তু", "cube", "cuboid", "volume")
        ),
        LibraryFormula(
            id = "lib_geom_14",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "স্থানাঙ্ক জ্যামিতি",
            titleBn = "দুটি বিন্দুর মধ্যকার দূরত্ব নির্ণয়",
            titleEn = "Distance Formula in Coordinate Geometry",
            formulaExpression = "d = √[(x₂ − x₁)² + (y₂ − y₁)²]",
            explanationBn = "কার্তেসীয় সমতলে P(x₁, y₁) এবং Q(x₂, y₂) দুটি বিন্দুর মধ্যবর্তী সরলরেখিক দূরত্বের পরিমাপ।",
            variables = listOf(
                "(x₁, y₁)" to "প্রথম বিন্দুর স্থানাঙ্ক",
                "(x₂, y₂)" to "দ্বিতীয় বিন্দুর স্থানাঙ্ক"
            ),
            exampleProblemBn = "A(1, 2) এবং B(4, 6) বিন্দুদ্বয়ের মধ্যবর্তী দূরত্ব কত?",
            exampleSolutionBn = "d = √[(4 − 1)² + (6 − 2)²] = √[3² + 4²] = √[9 + 16] = √25 = 5 একক",
            tipsBn = "বর্গ করার কারণে (x₂ − x₁)² এবং (x₁ − x₂)² একই ফলাফল প্রদান করে।",
            tags = listOf("স্থানাঙ্ক জ্যামিতি", "দূরত্ব", "distance formula", "coordinate")
        ),
        LibraryFormula(
            id = "lib_geom_15",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "স্থানাঙ্ক জ্যামিতি",
            titleBn = "মধ্যবিন্দু ও বিভক্তিকরণ সূত্র",
            titleEn = "Midpoint & Section Formula",
            formulaExpression = "মধ্যবিন্দু M = ((x₁ + x₂) / 2, (y₁ + y₂) / 2)",
            explanationBn = "দুটি বিন্দুর সংযোগকারী সরলরেখাংশের ঠিক মধ্যবর্তী বিন্দুর স্থানাঙ্ক নির্ণয়।",
            variables = listOf(
                "(x₁, y₁), (x₂, y₂)" to "প্রান্তবিন্দুদ্বয়"
            ),
            exampleProblemBn = "P(2, 8) এবং Q(6, 4) এর মধ্যবিন্দু কত?",
            exampleSolutionBn = "M = ((2 + 6)/2, (8 + 4)/2) = (8/2, 12/2) = (4, 6)",
            tipsBn = "m:n অনুপাতে অন্তর্বিভক্তকারী বিন্দুর স্থানাঙ্ক: ((mx₂ + nx₁)/(m+n), (my₂ + ny₁)/(m+n))।",
            tags = listOf("মধ্যবিন্দু", "midpoint", "section formula", "স্থানাঙ্ক")
        ),
        LibraryFormula(
            id = "lib_geom_16",
            subject = FormulaSubject.GEOMETRY,
            subCategory = "স্থানাঙ্ক জ্যামিতি",
            titleBn = "সরলরেখার ঢাল ও সমীকরণ",
            titleEn = "Slope & Equation of a Straight Line",
            formulaExpression = "ঢাল m = (y₂ − y₁) / (x₂ − x₁),  সমীকরণ: y = mx + c  বা  y − y₁ = m(x − x₁)",
            explanationBn = "ঢাল হলো x-অক্ষের সাথে রেখাটির খাড়া হওয়ার মাত্রা (tan θ)। m হলো ঢাল এবং c হলো y-অক্ষের ছেদাংশ।",
            variables = listOf(
                "m" to "সরলরেখার ঢাল (Gradient/Slope)",
                "c" to "y-অক্ষের ছেদাংশ (y-intercept)"
            ),
            exampleProblemBn = "(1, 3) ও (3, 7) বিন্দুগামী রেখার ঢাল কত?",
            exampleSolutionBn = "m = (7 − 3) / (3 − 1) = 4 / 2 = 2\nরেখাটির সমীকরণ: y − 3 = 2(x − 1) ⇒ y = 2x + 1",
            tipsBn = "দুটি রেখা সমান্তরাল হলে তাদের ঢাল সমান (m₁ = m₂)। লম্ব হলে ঢালদ্বয়ের গুণফল −1 (m₁ · m₂ = −1)।",
            tags = listOf("ঢাল", "সরলরেখা", "slope", "straight line", "equation")
        )
    )

    // -------------------------------------------------------------
    // CALCULUS (ক্যালকুলাস)
    // -------------------------------------------------------------
    val calculusFormulas: List<LibraryFormula> = listOf(
        LibraryFormula(
            id = "lib_calc_01",
            subject = FormulaSubject.CALCULUS,
            subCategory = "অন্তরীকরণ (Derivatives)",
            titleBn = "বীজগণিতীয় ঘাত নিয়ম (Power Rule)",
            titleEn = "Power Rule of Differentiation",
            formulaExpression = "d/dx (xⁿ) = n · xⁿ⁻¹",
            explanationBn = "যেকোনো চলকের ঘাতের অন্তরজ নির্ণয় করতে ঘাতটি সামনে সহগ হিসেবে গুণ হয় এবং নতুন ঘাত ১ কমে যায়।",
            variables = listOf(
                "x" to "স্বাধীন চলক",
                "n" to "যেকোনো বাস্তব সংখ্যা (ঘাত)"
            ),
            exampleProblemBn = "অন্তরজ নির্ণয় করো: d/dx (x⁴)",
            exampleSolutionBn = "d/dx (x⁴) = 4 · x⁴⁻¹ = 4x³",
            tipsBn = "যদি রুট থাকে: d/dx (√x) = d/dx (x¹/²) = (1/2)x⁻¹/² = 1 / (2√x)।",
            tags = listOf("অন্তরীকরণ", "ঘাত নিয়ম", "derivative", "power rule", "calculus")
        ),
        LibraryFormula(
            id = "lib_calc_02",
            subject = FormulaSubject.CALCULUS,
            subCategory = "অন্তরীকরণ (Derivatives)",
            titleBn = "ধ্রুবক ও ধ্রুব গুণিতক নিয়ম",
            titleEn = "Constant & Constant Multiple Rule",
            formulaExpression = "d/dx (c) = 0  এবং  d/dx [c · f(x)] = c · f'(x)",
            explanationBn = "স্বাধীন চলক ছাড়া যেকোনো একক ধ্রুবকের পরিবর্তনের হার শূন্য। সহগ হিসেবে ধ্রুবক থাকলে তা বাইরে এসে ফাংশনের অন্তরজের সাথে গুণ হয়।",
            variables = listOf(
                "c" to "যেকোনো বাস্তব ধ্রুব সংখ্যা",
                "f(x)" to "অন্তরীকরণযোগ্য ফাংশন"
            ),
            exampleProblemBn = "অন্তরজ নির্ণয় করো: d/dx (7x³ + 15)",
            exampleSolutionBn = "d/dx (7x³) + d/dx (15) = 7 · (3x²) + 0 = 21x²",
            tipsBn = "ধ্রুবক একা থাকলে তার অন্তরজ সর্বদা ০। যেমন d/dx(100) = 0।",
            tags = listOf("ধ্রুবক", "constant rule", "derivative")
        ),
        LibraryFormula(
            id = "lib_calc_03",
            subject = FormulaSubject.CALCULUS,
            subCategory = "অন্তরীকরণ (Derivatives)",
            titleBn = "গুণফল নিয়ম (Product Rule)",
            titleEn = "Product Rule of Differentiation",
            formulaExpression = "d/dx (u · v) = u(dv/dx) + v(du/dx) = u v' + v u'",
            explanationBn = "দুটি ফাংশনের গুণফলের অন্তরজ = প্রথম ফাংশন × দ্বিতীয়টির অন্তরজ + দ্বিতীয় ফাংশন × প্রথমটির অন্তরজ।",
            variables = listOf(
                "u, v" to "x-এর দুটি অন্তরীকরণযোগ্য ফাংশন"
            ),
            exampleProblemBn = "d/dx (x² · sin x) এর মান কত?",
            exampleSolutionBn = "u = x², v = sin x\n= x² · d/dx(sin x) + sin x · d/dx(x²)\n= x² · cos x + 2x · sin x",
            tipsBn = "মনে রাখবেন: (uv)' কিন্তু u'v' নয়। অবশ্যই যোগফল আকারে লিখতে হবে।",
            tags = listOf("গুণফল নিয়ম", "product rule", "derivative")
        ),
        LibraryFormula(
            id = "lib_calc_04",
            subject = FormulaSubject.CALCULUS,
            subCategory = "অন্তরীকরণ (Derivatives)",
            titleBn = "ভাগফল নিয়ম (Quotient Rule)",
            titleEn = "Quotient Rule of Differentiation",
            formulaExpression = "d/dx (u / v) = [v(du/dx) − u(dv/dx)] / v² = [v u' − u v'] / v²",
            explanationBn = "দুটি ফাংশনের ভাগফলের অন্তরজ: [হর × লবের অন্তরজ − লব × হরের অন্তরজ] ভাগ (হরের বর্গ)।",
            variables = listOf(
                "u" to "লব ফাংশন",
                "v" to "হর ফাংশন (v ≠ 0)"
            ),
            exampleProblemBn = "d/dx (x / (x + 1)) এর মান কত?",
            exampleSolutionBn = "u = x, v = x + 1\n= [(x + 1)·(1) − x·(1)] / (x + 1)²\n= [x + 1 − x] / (x + 1)² = 1 / (x + 1)²",
            tipsBn = "ক্রমটি খুব গুরুত্বপূর্ণ! লবের অন্তরজ আগে হবে: v u' − u v'। আগে u v' লিখলে চিহ্ন উল্টে ভুল হবে।",
            tags = listOf("ভাগফল নিয়ম", "quotient rule", "derivative")
        ),
        LibraryFormula(
            id = "lib_calc_05",
            subject = FormulaSubject.CALCULUS,
            subCategory = "অন্তরীকরণ (Derivatives)",
            titleBn = "শৃঙ্খল নিয়ম (Chain Rule)",
            titleEn = "Chain Rule for Composite Functions",
            formulaExpression = "dy/dx = (dy/du) · (du/dx)  বা  d/dx [f(g(x))] = f'(g(x)) · g'(x)",
            explanationBn = "ফাংশনের ভেতর অন্য ফাংশন (সংযুক্ত ফাংশন) থাকলে বাইরের ফাংশনটির অন্তরজ করে ভিতরের ফাংশনের অন্তরজ দিয়ে গুণ করতে হয়।",
            variables = listOf(
                "y = f(u)" to "বহিঃস্থ ফাংশন",
                "u = g(x)" to "অন্তঃস্থ ফাংশন"
            ),
            exampleProblemBn = "d/dx [sin(3x²)] এর মান কত?",
            exampleSolutionBn = "বাইরের ফাংশন sin(u) এর অন্তরজ cos(u)\nভিতরের ফাংশন 3x² এর অন্তরজ 6x\n= cos(3x²) · 6x = 6x cos(3x²)",
            tipsBn = "সব সময় বাইরে থেকে ভেতরে স্তরে স্তরে অন্তরীকরণ করতে হয়।",
            tags = listOf("শৃঙ্খল নিয়ম", "chain rule", "composite function")
        ),
        LibraryFormula(
            id = "lib_calc_06",
            subject = FormulaSubject.CALCULUS,
            subCategory = "অন্তরীকরণ (Derivatives)",
            titleBn = "সূচকীয় ও লগারিদমিক অপেক্ষকের অন্তরজ",
            titleEn = "Exponential & Logarithmic Derivatives",
            formulaExpression = "d/dx (eˣ) = eˣ,  d/dx (aˣ) = aˣ ln a,  d/dx (ln x) = 1/x",
            explanationBn = "স্বাভাবিক সূচক eˣ এর অন্তরজ হুবহু একই থাকে। প্রাকৃতিক লগারিদম ln x এর অন্তরজ হলো 1/x।",
            variables = listOf(
                "e" to "অয়লারের ধ্রুবক (প্রায় 2.718)",
                "ln x" to "e ভিত্তিক লগারিদম (x > 0)"
            ),
            exampleProblemBn = "d/dx (e⁵ˣ + ln(4x)) এর মান কত?",
            exampleSolutionBn = "d/dx(e⁵ˣ) = 5e⁵ˣ\nd/dx(ln 4x) = 1/x\n= 5e⁵ˣ + 1/x",
            tipsBn = "ভিত্তি a হলে d/dx (logₐ x) = 1 / (x ln a)।",
            tags = listOf("সূচকীয়", "লগারিদমিক", "exponential", "logarithm", "e^x")
        ),
        LibraryFormula(
            id = "lib_calc_07",
            subject = FormulaSubject.CALCULUS,
            subCategory = "অন্তরীকরণ (Derivatives)",
            titleBn = "ত্রিকোণমিতিক ফাংশনের অন্তরজ",
            titleEn = "Trigonometric Derivatives",
            formulaExpression = "d/dx(sin x) = cos x,  d/dx(cos x) = −sin x,  d/dx(tan x) = sec² x",
            explanationBn = "মৌলিক ত্রিকোণমিতিক অনুপাতগুলোর সাধারণ অন্তরজের সেট।",
            variables = listOf(
                "x" to "কোণ (রেডিয়ানে পরিমাপকৃত)"
            ),
            exampleProblemBn = "অন্যান্য ত্রিকোণমিতিক অন্তরজসমূহ:",
            exampleSolutionBn = "d/dx(cot x) = −csc² x\nd/dx(sec x) = sec x tan x\nd/dx(csc x) = −csc x cot x",
            tipsBn = "স্মরণীয় কৌশল: যে ত্রিকোণমিতিক ফাংশনগুলোর নাম 'co' দিয়ে শুরু (cos, cot, csc), তাদের অন্তরজের সামনে ঋণাত্মক (−) চিহ্ন বসে!",
            tags = listOf("ত্রিকোণমিতি", "trigonometry", "sin", "cos", "tan", "derivative")
        ),
        LibraryFormula(
            id = "lib_calc_08",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সমাকলন (Integration)",
            titleBn = "সমাকলনের মৌলিক ঘাত সূত্র",
            titleEn = "Power Rule of Integration",
            formulaExpression = "∫ xⁿ dx = (xⁿ⁺¹ / (n + 1)) + C  [n ≠ −1]",
            explanationBn = "অনির্দিষ্ট সমাকলন হলো অন্তরীকরণের বিপরীত প্রক্রিয়া। ঘাত ১ বৃদ্ধি পায় এবং নতুন ঘাত দিয়ে ভাগ হয়, সাথে সমাকলন ধ্রুবক C যোগ হয়।",
            variables = listOf(
                "n" to "ঘাত (n ≠ −1)",
                "C" to "সমাকলন ধ্রুবক (Constant of Integration)"
            ),
            exampleProblemBn = "সমাকলন করো: ∫ x⁵ dx",
            exampleSolutionBn = "∫ x⁵ dx = (x⁵⁺¹ / (5 + 1)) + C = (x⁶ / 6) + C",
            tipsBn = "অনির্দিষ্ট সমাকলনে সর্বদা +C লিখতে ভুলবেন না।",
            tags = listOf("সমাকলন", "ইন্টিগ্রেশন", "power rule", "integration", "calculus")
        ),
        LibraryFormula(
            id = "lib_calc_09",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সমাকলন (Integration)",
            titleBn = "লগারিদমিক ও সূচকীয় সমাকলন",
            titleEn = "Logarithmic & Exponential Integrals",
            formulaExpression = "∫ (1 / x) dx = ln|x| + C  এবং  ∫ eˣ dx = eˣ + C",
            explanationBn = "যেহেতু d/dx (ln x) = 1/x, তাই 1/x এর সমাকলন হলো প্রাকৃতিক লগারিদম ln|x|। eˣ এর সমাকলন অপরিবর্তিত থাকে।",
            variables = listOf(
                "|x|" to "পরম মান (যেহেতু লগের ডোমেন ধনাত্মক)",
                "C" to "সমাকলন ধ্রুবক"
            ),
            exampleProblemBn = "সমাকলন করো: ∫ (3/x + 2eˣ) dx",
            exampleSolutionBn = "3 ∫ (1/x) dx + 2 ∫ eˣ dx\n= 3 ln|x| + 2eˣ + C",
            tipsBn = "যেকোনো অশূন্য a-এর ক্ষেত্রে: ∫ aˣ dx = (aˣ / ln a) + C।",
            tags = listOf("লগারিদম", "সূচকীয়", "1/x", "e^x", "integration")
        ),
        LibraryFormula(
            id = "lib_calc_10",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সমাকলন (Integration)",
            titleBn = "ত্রিকোণমিতিক ফাংশনের সমাকলন",
            titleEn = "Trigonometric Integrals",
            formulaExpression = "∫ sin x dx = −cos x + C,  ∫ cos x dx = sin x + C,  ∫ sec² x dx = tan x + C",
            explanationBn = "মৌলিক ত্রিকোণমিতিক অপেক্ষকের প্রতি-অন্তরজ (Anti-derivatives)।",
            variables = listOf(
                "C" to "সমাকলন ধ্রুবক"
            ),
            exampleProblemBn = "অন্যান্য আদর্শ ত্রিকোণমিতিক সমাকলন:",
            exampleSolutionBn = "∫ csc² x dx = −cot x + C\n∫ sec x tan x dx = sec x + C\n∫ csc x cot x dx = −csc x + C",
            tipsBn = "সতর্কতা: sin x এর অন্তরজ +cos x, কিন্তু sin x এর সমাকলন −cos x!",
            tags = listOf("ত্রিকোণমিতি", "সমাকলন", "sin", "cos", "tan", "integral")
        ),
        LibraryFormula(
            id = "lib_calc_11",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সমাকলন (Integration)",
            titleBn = "খণ্ডশ সমাকলন (Integration by Parts)",
            titleEn = "Integration by Parts Formula",
            formulaExpression = "∫ u v dx = u ∫ v dx − ∫ [ (du/dx) · ∫ v dx ] dx",
            explanationBn = "দুটি ফাংশনের গুণফলের সমাকলন নির্ণয়ের সবচেয়ে শক্তিশালী পদ্ধতি। 'ILATE' নিয়মের সাহায্যে u নির্ধারণ করা হয়।",
            variables = listOf(
                "u, v" to "ফাংশনদ্বয়",
                "ILATE" to "ক্রম: Inverse, Logarithmic, Algebraic, Trigonometric, Exponential"
            ),
            exampleProblemBn = "সমাকলন করো: ∫ x eˣ dx",
            exampleSolutionBn = "ILATE অনুসারে: u = x (বীজগণিতীয়), v = eˣ (সূচকীয়)\n= x ∫ eˣ dx − ∫ [d/dx(x) · ∫ eˣ dx] dx\n= x eˣ − ∫ (1 · eˣ) dx\n= x eˣ − eˣ + C = eˣ(x − 1) + C",
            tipsBn = "ILATE নিয়মে যে ফাংশনটির নাম তালিকায় আগে আসবে, তাকে 'u' ধরতে হয়।",
            tags = listOf("খণ্ডশ সমাকলন", "integration by parts", "ILATE")
        ),
        LibraryFormula(
            id = "lib_calc_12",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সমাকলন (Integration)",
            titleBn = "নির্দিষ্ট সমাকলন ও কলনবিদ্যার মৌলিক উপপাদ্য",
            titleEn = "Definite Integral & FTC",
            formulaExpression = "∫[a to b] f(x) dx = F(b) − F(a)  যেখানে F'(x) = f(x)",
            explanationBn = "নির্দিষ্ট সমাকলনের মাধ্যমে বক্ররেখার নিচের আবদ্ধ অঞ্চলের ক্ষেত্রফল নির্ধারিত হয়। এতে কোনো ধ্রুবক C থাকে না।",
            variables = listOf(
                "a" to "নিম্ন সীমা (Lower Limit)",
                "b" to "উচ্চ সীমা (Upper Limit)",
                "F(x)" to "f(x)-এর প্রতি-অন্তরজ (Anti-derivative)"
            ),
            exampleProblemBn = "মান নির্ণয় করো: ∫[1 to 3] 2x dx",
            exampleSolutionBn = "F(x) = x²\nF(3) − F(1) = 3² − 1² = 9 − 1 = 8",
            tipsBn = "সীমা প্রতিস্থাপন করার সময় সবসময় উচ্চ সীমা বসিয়ে মান থেকে নিম্ন সীমা বসানো মান বিয়োগ করবেন।",
            tags = listOf("নির্দিষ্ট সমাকলন", "definite integral", "FTC", "area under curve")
        ),
        LibraryFormula(
            id = "lib_calc_13",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সীমা (Limits)",
            titleBn = "ত্রিকোণমিতিক সীমার মৌলিক সূত্র",
            titleEn = "Fundamental Trig Limits",
            formulaExpression = "lim(x→0) [sin x / x] = 1  এবং  lim(x→0) [tan x / x] = 1",
            explanationBn = "যখন কোণ x শূন্যের খুব কাছাকাছি যায় (রেডিয়ানে), তখন sin x ও x এর অনুপাত ১ এর দিকে ধাবিত হয়।",
            variables = listOf(
                "x" to "কোণ রেডিয়ানে পরিমাপিত"
            ),
            exampleProblemBn = "মান নির্ণয় করো: lim(x→0) [sin(5x) / x]",
            exampleSolutionBn = "lim(x→0) [5 · sin(5x) / (5x)] = 5 · lim(5x→0) [sin(5x)/(5x)] = 5 · 1 = 5",
            tipsBn = "লব ও হরের চলক হুবহু একই হতে হবে। যদি sin(kx) থাকে, তবে হরেও kx তৈরি করতে হবে।",
            tags = listOf("সীমা", "limits", "trig limit", "sin x / x")
        ),
        LibraryFormula(
            id = "lib_calc_14",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সীমা (Limits)",
            titleBn = "অয়লারের ধ্রুবক ও সূচকীয় সীমা",
            titleEn = "Euler's Constant & Exponential Limits",
            formulaExpression = "lim(x→∞) (1 + 1/x)ˣ = e  এবং  lim(x→0) [(eˣ − 1) / x] = 1",
            explanationBn = "চক্রবৃদ্ধি বৃদ্ধি এবং প্রাকৃতিক ঘাতের ভিত্তি ধ্রুবক e (প্রায় ২.৭১৮২৮) এর গাণিতিক সংজ্ঞায়ন ও সীমা।",
            variables = listOf(
                "e" to "অয়লারের ধ্রুবক (Euler's number ≈ 2.71828)"
            ),
            exampleProblemBn = "মান নির্ণয় করো: lim(x→0) [(e³ˣ − 1) / x]",
            exampleSolutionBn = "= lim(x→0) [3 · (e³ˣ − 1) / (3x)] = 3 · 1 = 3",
            tipsBn = "সূচকীয় বৃদ্ধির হারে এই সীমাটি অত্যন্ত মৌলিক।",
            tags = listOf("সীমা", "e", "euler", "exponential limit")
        ),
        LibraryFormula(
            id = "lib_calc_15",
            subject = FormulaSubject.CALCULUS,
            subCategory = "সীমা (Limits)",
            titleBn = "লা'হস্পিটালের নিয়ম (L'Hôpital's Rule)",
            titleEn = "L'Hôpital's Rule for Indeterminate Forms",
            formulaExpression = "lim(x→c) [f(x) / g(x)] = lim(x→c) [f'(x) / g'(x)]  [রূপ: 0/0 বা ∞/∞]",
            explanationBn = "সীমা সরাসরি বসিয়ে যদি 0/0 বা ∞/∞ অনির্ণেয় রূপ পাওয়া যায়, তবে লব ও হরকে পৃথকভাবে অন্তরীকরণ করে পুনরায় সীমা বের করা যায়।",
            variables = listOf(
                "f'(x), g'(x)" to "লব ও হরের আলাদা অন্তরজ (ভাগফল নিয়ম নয়)"
            ),
            exampleProblemBn = "মান নির্ণয় করো: lim(x→0) [(1 − cos x) / x²]",
            exampleSolutionBn = "0 বসালে রূপ 0/0 হয়। লা'হস্পিটাল প্রয়োগ করে:\n= lim(x→0) [sin x / 2x] (পুনরায় 0/0 রূপ)\n= lim(x→0) [cos x / 2] = cos(0) / 2 = 1/2",
            tipsBn = "মনে রাখবেন: এটি ভাগফল নিয়ম (Quotient Rule) নয়! লবকে একা এবং হরকে একা অন্তরীকরণ করবেন।",
            tags = listOf("লা হস্পিটাল", "lhopital", "অনির্ণেয় রূপ", "0/0", "limits")
        )
    )

    fun getFormulasBySubject(subject: FormulaSubject): List<LibraryFormula> {
        return allFormulas.filter { it.subject == subject }
    }

    fun getSubCategories(subject: FormulaSubject?): List<String> {
        val list = if (subject == null) allFormulas else allFormulas.filter { it.subject == subject }
        return list.map { it.subCategory }.distinct()
    }
}
