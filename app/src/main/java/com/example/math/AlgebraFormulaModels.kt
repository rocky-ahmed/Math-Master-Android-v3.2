package com.example.math

data class FormulaExample(
    val problemBn: String,
    val stepByStepBn: List<String>,
    val answerBn: String
)

data class AlgebraicFormula(
    val id: String,
    val serialNo: Int, // ১ থেকে ১৪
    val category: FormulaCategory, // SQUARE or CUBE
    val nameBn: String,
    val formulaLatex: String, // e.g. "(a + b)² = a² + 2ab + b²"
    val descriptionBn: String,
    val derivationBn: String,
    val redProofNotesBn: List<String> = emptyList(), // প্রমাণ
    val examples: List<FormulaExample>,
    val corollariesBn: List<String> = emptyList()
)

enum class FormulaCategory(val titleBn: String) {
    SQUARE("বর্গের সূত্রাবলি ও প্রমাণ (১৪টি সূত্র)"),
    CUBE("ঘনের সূত্রাবলি ও প্রমাণ")
}

object AlgebraFormulaProvider {
    // পর্ব ১ ও পর্ব ২: বীজগাণিতিক সূত্রাবলি (১ থেকে ১৪) হুবহু ব্যবহারকারীর নতুন ডেটানুযায়ী সাজানো
    val squareFormulas: List<AlgebraicFormula> = listOf(
        // সূত্র–১: (a + b)² = a² + 2ab + b²
        AlgebraicFormula(
            id = "sq_1",
            serialNo = 1,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–১: যোগফলের বর্গ",
            formulaLatex = "(a + b)² = a² + 2ab + b²",
            descriptionBn = "বামপক্ষ ও ডানপক্ষের অভেদ। দুটি রাশির যোগফলের বর্গ।",
            derivationBn = "বামপক্ষ = (a + b)² = (a + b)(a + b) = a(a + b) + b(a + b) = a² + ab + ab + b² = a² + 2ab + b² = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a + b)²",
                "= (a + b)(a + b)",
                "= a(a + b) + b(a + b)",
                "= a² + ab + ab + b²",
                "= a² + 2ab + b² = ডানপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "মান নির্ণয় করো: (2x + 3y)²",
                    stepByStepBn = listOf(
                        "এখানে a = 2x এবং b = 3y",
                        "(2x)² + 2 · (2x) · (3y) + (3y)²",
                        "= 4x² + 12xy + 9y²"
                    ),
                    answerBn = "4x² + 12xy + 9y²"
                )
            ),
            corollariesBn = listOf(
                "(a + b)² সূত্রে মাঝের পদ +2ab"
            )
        ),

        // সূত্র–২: (a + b)² = (a − b)² + 4ab
        AlgebraicFormula(
            id = "sq_2",
            serialNo = 2,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–২: যোগফলের বর্গের অনুসিদ্ধান্ত",
            formulaLatex = "(a + b)² = (a − b)² + 4ab",
            descriptionBn = "বিয়োগফলের বর্গ ও 4ab এর মাধ্যমে যোগফলের বর্গ নির্ণয়।",
            derivationBn = "ডানপক্ষ = (a − b)² + 4ab = a² − 2ab + b² + 4ab = a² + 2ab + b² = (a + b)² = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a − b)² + 4ab",
                "= a² − 2ab + b² + 4ab",
                "= a² + 2ab + b²",
                "= (a + b)² = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a − b = 5 এবং ab = 6 হয়, তবে (a + b)² এর মান কত?",
                    stepByStepBn = listOf(
                        "(a + b)² = (a − b)² + 4ab",
                        "= 5² + 4 · 6 = 25 + 24 = 49"
                    ),
                    answerBn = "49"
                )
            ),
            corollariesBn = listOf(
                "(a + b)² = (a − b)² + 4ab"
            )
        ),

        // সূত্র–৩: (a − b)² = a² − 2ab + b²
        AlgebraicFormula(
            id = "sq_3",
            serialNo = 3,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–৩: বিয়োগফলের বর্গ",
            formulaLatex = "(a − b)² = a² − 2ab + b²",
            descriptionBn = "দুটি রাশির বিয়োগফলের বর্গের মূল সূত্র।",
            derivationBn = "বামপক্ষ = (a − b)² = (a − b)(a − b) = a(a − b) − b(a − b) = a² − ab − ab + b² = a² − 2ab + b² = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a − b)²",
                "= (a − b)(a − b)",
                "= a(a − b) − b(a − b)",
                "= a² − ab − ab + b²",
                "= a² − 2ab + b² = ডানপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "মান নির্ণয় করো: (3x − 4y)²",
                    stepByStepBn = listOf(
                        "(3x)² − 2 · (3x) · (4y) + (4y)²",
                        "= 9x² − 24xy + 16y²"
                    ),
                    answerBn = "9x² − 24xy + 16y²"
                )
            ),
            corollariesBn = listOf(
                "(a − b)² সূত্রে মাঝের পদ −2ab"
            )
        ),

        // সূত্র–৪: (a − b)² = (a + b)² − 4ab
        AlgebraicFormula(
            id = "sq_4",
            serialNo = 4,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–৪: বিয়োগফলের বর্গের অনুসিদ্ধান্ত",
            formulaLatex = "(a − b)² = (a + b)² − 4ab",
            descriptionBn = "যোগফলের বর্গ ও 4ab এর মাধ্যমে বিয়োগফলের বর্গ নির্ণয়।",
            derivationBn = "ডানপক্ষ = (a + b)² − 4ab = a² + 2ab + b² − 4ab = a² − 2ab + b² = (a − b)² = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a + b)² − 4ab",
                "= a² + 2ab + b² − 4ab",
                "= a² − 2ab + b²",
                "= (a − b)² = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a + b = 9 এবং ab = 20 হয়, তবে (a − b)² এর মান কত?",
                    stepByStepBn = listOf(
                        "(a − b)² = (a + b)² − 4ab",
                        "= 9² − 4 · 20 = 81 − 80 = 1"
                    ),
                    answerBn = "1"
                )
            ),
            corollariesBn = listOf(
                "(a − b)² = (a + b)² − 4ab"
            )
        ),

        // সূত্র–৫: a² − b² = (a + b)(a − b)
        AlgebraicFormula(
            id = "sq_5",
            serialNo = 5,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–৫: বর্গের অন্তর (উৎপাদক সূত্র)",
            formulaLatex = "a² − b² = (a + b)(a − b)",
            descriptionBn = "দুটি রাশির বর্গের বিয়োগফল তাদের যোগফল ও বিয়োগফলের গুণফলের সমান।",
            derivationBn = "ডানপক্ষ = (a + b)(a − b) = a(a − b) + b(a − b) = a² − ab + ab − b² = a² − b² = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a + b)(a − b)",
                "= a(a − b) + b(a − b)",
                "= a² − ab + ab − b²",
                "= a² − b² = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "উৎপাদকে বিশ্লেষণ করো: 25x² − 16y²",
                    stepByStepBn = listOf(
                        "(5x)² − (4y)² = (5x + 4y)(5x − 4y)"
                    ),
                    answerBn = "(5x + 4y)(5x − 4y)"
                )
            ),
            corollariesBn = listOf(
                "(a + b)(a − b) = a² − b²"
            )
        ),

        // সূত্র–৬: a² + b² = (a + b)² − 2ab
        AlgebraicFormula(
            id = "sq_6",
            serialNo = 6,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–৬: বর্গের সমষ্টি (যোগফলের মাধ্যমে)",
            formulaLatex = "a² + b² = (a + b)² − 2ab",
            descriptionBn = "দুটি রাশির বর্গের সমষ্টি যোগফল ও গুণফলের সাহায্যে নির্ণয়।",
            derivationBn = "ডানপক্ষ = (a + b)² − 2ab = a² + 2ab + b² − 2ab = a² + b² = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a + b)² − 2ab",
                "= a² + 2ab + b² − 2ab",
                "= a² + b² = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a + b = 7 এবং ab = 10 হয়, তবে a² + b² এর মান কত?",
                    stepByStepBn = listOf(
                        "a² + b² = (a + b)² − 2ab",
                        "= 7² − 2 · 10 = 49 − 20 = 29"
                    ),
                    answerBn = "29"
                )
            ),
            corollariesBn = listOf(
                "a² + b² = (a + b)² − 2ab"
            )
        ),

        // সূত্র–৭: a² + b² = (a − b)² + 2ab
        AlgebraicFormula(
            id = "sq_7",
            serialNo = 7,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–৭: বর্গের সমষ্টি (বিয়োগফলের মাধ্যমে)",
            formulaLatex = "a² + b² = (a − b)² + 2ab",
            descriptionBn = "দুটি রাশির বর্গের সমষ্টি বিয়োগফল ও গুণফলের সাহায্যে নির্ণয়।",
            derivationBn = "ডানপক্ষ = (a − b)² + 2ab = a² − 2ab + b² + 2ab = a² + b² = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a − b)² + 2ab",
                "= a² − 2ab + b² + 2ab",
                "= a² + b² = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a − b = 9 এবং ab = 5 হয়, তবে a² + b² এর মান কত?",
                    stepByStepBn = listOf(
                        "a² + b² = (a − b)² + 2ab",
                        "= 9² + 2 · 5 = 81 + 10 = 91"
                    ),
                    answerBn = "91"
                )
            ),
            corollariesBn = listOf(
                "a² + b² = (a − b)² + 2ab"
            )
        ),

        // সূত্র–৮: 2(a² + b²) = (a + b)² + (a − b)²
        AlgebraicFormula(
            id = "sq_8",
            serialNo = 8,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–৮: দ্বিগুণ বর্গ সমষ্টি",
            formulaLatex = "2(a² + b²) = (a + b)² + (a − b)²",
            descriptionBn = "যোগফলের বর্গ ও বিয়োগফলের বর্গের সমষ্টি দ্বিগুণ বর্গের সমষ্টির সমান।",
            derivationBn = "ডানপক্ষ = (a + b)² + (a − b)² = (a² + 2ab + b²) + (a² − 2ab + b²) = a² + 2ab + b² + a² − 2ab + b² = 2a² + 2b² = 2(a² + b²) = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a + b)² + (a − b)²",
                "= (a² + 2ab + b²) + (a² − 2ab + b²)",
                "= a² + 2ab + b² + a² − 2ab + b²",
                "= 2a² + 2b² = 2(a² + b²) = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a + b = 8 এবং a − b = 4 হয়, তবে 2(a² + b²) এর মান কত?",
                    stepByStepBn = listOf(
                        "2(a² + b²) = 8² + 4² = 64 + 16 = 80"
                    ),
                    answerBn = "80"
                )
            ),
            corollariesBn = listOf(
                "(a + b)² + (a − b)² = 2(a² + b²)"
            )
        ),

        // সূত্র–৯: 4ab = (a + b)² − (a − b)²
        AlgebraicFormula(
            id = "sq_9",
            serialNo = 9,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–৯: চার গুণফলের সূত্র (4ab)",
            formulaLatex = "4ab = (a + b)² − (a − b)²",
            descriptionBn = "যোগফলের বর্গ থেকে বিয়োগফলের বর্গ বাদ দিলে 4ab পাওয়া যায়।",
            derivationBn = "ডানপক্ষ = (a + b)² − (a − b)² = (a² + 2ab + b²) − (a² − 2ab + b²) = a² + 2ab + b² − a² + 2ab − b² = 4ab = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a + b)² − (a − b)²",
                "= (a² + 2ab + b²) − (a² − 2ab + b²)",
                "= a² + 2ab + b² − a² + 2ab − b²",
                "= 4ab = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a + b = 9 এবং a − b = 5 হয়, তবে ab এর মান কত?",
                    stepByStepBn = listOf(
                        "4ab = 9² − 5² = 81 − 25 = 56",
                        "ab = 56 / 4 = 14"
                    ),
                    answerBn = "14"
                )
            ),
            corollariesBn = listOf(
                "(a + b)² − (a − b)² = 4ab"
            )
        ),

        // সূত্র–১০: ab = ¼[(a + b)² − (a − b)²]
        AlgebraicFormula(
            id = "sq_10",
            serialNo = 10,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–১০: গুণফলের সূত্র (ab)",
            formulaLatex = "ab = ¼[(a + b)² − (a − b)²]",
            descriptionBn = "দুটি রাশির গুণফলকে দুটি বর্গের অন্তর বা বিয়োগফল রূপে প্রকাশ।",
            derivationBn = "প্রমাণ: আমরা জানি, 4ab = (a + b)² − (a − b)²\nউভয় পক্ষকে ৪ দিয়ে ভাগ করলে, ab = [(a + b)² − (a − b)²] / 4\nঅতএব, ab = ¼[(a + b)² − (a − b)²] (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: আমরা জানি, 4ab = (a + b)² − (a − b)²",
                "উভয় পক্ষকে ৪ দিয়ে ভাগ করলে,",
                "ab = [(a + b)² − (a − b)²] / 4",
                "অতএব, ab = ¼[(a + b)² − (a − b)²] (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "13 × 7 কে দুটি বর্গের অন্তররূপে প্রকাশ করো:",
                    stepByStepBn = listOf(
                        "এখানে a = 13 এবং b = 7",
                        "ab = ¼[(13 + 7)² − (13 − 7)²]",
                        "= ¼[20² − 6²] = ((20)/2)² − ((6)/2)² = 10² − 3²"
                    ),
                    answerBn = "10² − 3²"
                )
            ),
            corollariesBn = listOf(
                "ab = ((a + b)/2)² − ((a − b)/2)²"
            )
        ),

        // সূত্র–১১: (a + b + c)² = a² + b² + c² + 2ab + 2bc + 2ca
        AlgebraicFormula(
            id = "sq_11",
            serialNo = 11,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–১১: তিন পদের বর্গের মূল সূত্র",
            formulaLatex = "(a + b + c)² = a² + b² + c² + 2ab + 2bc + 2ca",
            descriptionBn = "তিনটি পদের যোগফলের বর্গের পূর্ণাঙ্গ রূপ।",
            derivationBn = "বামপক্ষ = (a + b + c)² = [(a + b) + c]² = (a + b)² + 2(a + b)c + c² = a² + 2ab + b² + 2ac + 2bc + c² = a² + b² + c² + 2ab + 2bc + 2ca = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a + b + c)²",
                "= [(a + b) + c]²",
                "= (a + b)² + 2(a + b)c + c²",
                "= a² + 2ab + b² + 2ac + 2bc + c²",
                "= a² + b² + c² + 2ab + 2bc + 2ca = ডানপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a + b + c = 9 এবং ab + bc + ca = 20 হয়, তবে a² + b² + c² এর মান কত?",
                    stepByStepBn = listOf(
                        "a² + b² + c² = (a + b + c)² − 2(ab + bc + ca)",
                        "= 9² − 2 · 20 = 81 − 40 = 41"
                    ),
                    answerBn = "41"
                )
            ),
            corollariesBn = listOf(
                "তিনটি রাশির বর্গে তিনটি রাশির বর্গের সঙ্গে জোড়ায় জোড়ায় গুণফলের দ্বিগুণ যোগ হয়।"
            )
        ),

        // সূত্র–১২: (a + b − c)² = a² + b² + c² + 2ab − 2bc − 2ca
        AlgebraicFormula(
            id = "sq_12",
            serialNo = 12,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–১২: তিন পদের বর্গ (তৃতীয় পদ ঋণাত্মক: −c)",
            formulaLatex = "(a + b − c)² = a² + b² + c² + 2ab − 2bc − 2ca",
            descriptionBn = "তৃতীয় পদ c এর আগে ঋণাত্মক চিহ্ন থাকলে বিস্তার।",
            derivationBn = "বামপক্ষ = (a + b − c)² = [(a + b) − c]² = (a + b)² − 2(a + b)c + c² = a² + 2ab + b² − 2ac − 2bc + c² = a² + b² + c² + 2ab − 2bc − 2ca = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a + b − c)²",
                "= [(a + b) − c]²",
                "= (a + b)² − 2(a + b)c + c²",
                "= a² + 2ab + b² − 2ac − 2bc + c²",
                "= a² + b² + c² + 2ab − 2bc − 2ca = ডানপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "বিস্তার করো: (2x + y − 3z)²",
                    stepByStepBn = listOf(
                        "(2x)² + y² + (3z)² + 2(2x)(y) − 2(y)(3z) − 2(3z)(2x)",
                        "= 4x² + y² + 9z² + 4xy − 6yz − 12zx"
                    ),
                    answerBn = "4x² + y² + 9z² + 4xy − 6yz − 12zx"
                )
            ),
            corollariesBn = listOf(
                "c সম্বলিত পদগুলো (−2bc ও −2ca) ঋণাত্মক হবে।"
            )
        ),

        // সূত্র–১৩: (a − b + c)² = a² + b² + c² − 2ab − 2bc + 2ca
        AlgebraicFormula(
            id = "sq_13",
            serialNo = 13,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–১৩: তিন পদের বর্গ (দ্বিতীয় পদ ঋণাত্মক: −b)",
            formulaLatex = "(a − b + c)² = a² + b² + c² − 2ab − 2bc + 2ca",
            descriptionBn = "দ্বিতীয় পদ b এর আগে ঋণাত্মক চিহ্ন থাকলে বিস্তার।",
            derivationBn = "বামপক্ষ = (a − b + c)² = [(a − b) + c]² = (a − b)² + 2(a − b)c + c² = a² − 2ab + b² + 2ac − 2bc + c² = a² + b² + c² − 2ab − 2bc + 2ca = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a − b + c)²",
                "= [(a − b) + c]²",
                "= (a − b)² + 2(a − b)c + c²",
                "= a² − 2ab + b² + 2ac − 2bc + c²",
                "= a² + b² + c² − 2ab − 2bc + 2ca = ডানপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "বিস্তার করো: (x − 2y + 3z)²",
                    stepByStepBn = listOf(
                        "x² + (-2y)² + (3z)² + 2(x)(-2y) + 2(-2y)(3z) + 2(3z)(x)",
                        "= x² + 4y² + 9z² − 4xy − 12yz + 6zx"
                    ),
                    answerBn = "x² + 4y² + 9z² − 4xy − 12yz + 6zx"
                )
            ),
            corollariesBn = listOf(
                "b সম্বলিত পদগুলো (−2ab ও −2bc) ঋণাত্মক হবে।"
            )
        ),

        // সূত্র–১৪: (a − b − c)² = a² + b² + c² − 2ab + 2bc − 2ca
        AlgebraicFormula(
            id = "sq_14",
            serialNo = 14,
            category = FormulaCategory.SQUARE,
            nameBn = "সূত্র–১৪: তিন পদের বর্গ (উভয় b ও c ঋণাত্মক)",
            formulaLatex = "(a − b − c)² = a² + b² + c² − 2ab + 2bc − 2ca",
            descriptionBn = "উভয় b এবং c ঋণাত্মক হলে বিস্তার। এখানে ab পদ ঋণাত্মক, bc পদ ধনাত্মক এবং ca পদ ঋণাত্মক।",
            derivationBn = "বামপক্ষ = (a − b − c)² = [(a − b) − c]² = (a − b)² − 2(a − b)c + c² = a² − 2ab + b² − 2ac + 2bc + c² = a² + b² + c² − 2ab + 2bc − 2ca = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a − b − c)²",
                "= [(a − b) − c]²",
                "= (a − b)² − 2(a − b)c + c²",
                "= a² − 2ab + b² − 2ac + 2bc + c²",
                "= a² + b² + c² − 2ab + 2bc − 2ca = ডানপক্ষ। (প্রমাণিত)",
                "বিশেষ সতর্কতা: (a − b − c)²-এ ab-এর পদ ঋণাত্মক, bc-এর পদ ধনাত্মক এবং ca-এর পদ ঋণাত্মক।"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "বিস্তার করো: (p − q − r)²",
                    stepByStepBn = listOf(
                        "p² + q² + r² − 2pq + 2qr − 2rp"
                    ),
                    answerBn = "p² + q² + r² − 2pq + 2qr − 2rp"
                )
            ),
            corollariesBn = listOf(
                "বিশেষ সতর্কতা: ab ঋণাত্মক, bc ধনাত্মক (+2bc কারণ (-b)(-c) = +bc), এবং ca ঋণাত্মক।"
            )
        )
    )

    // পর্ব ১ ও পর্ব ২: ঘনের সূত্রাবলি ও প্রমাণ (১ থেকে ৮) হুবহু ব্যবহারকারীর ডেটানুযায়ী
    val cubeFormulas: List<AlgebraicFormula> = listOf(
        // সূত্র–১: (a + b)³ = a³ + 3a²b + 3ab² + b³
        AlgebraicFormula(
            id = "cube_1",
            serialNo = 1,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–১: যোগফলের ঘন (বিস্তার রূপ)",
            formulaLatex = "(a + b)³ = a³ + 3a²b + 3ab² + b³",
            descriptionBn = "দুটি রাশির যোগফলের ঘনের বিস্তার সূত্র।",
            derivationBn = "বামপক্ষ = (a + b)³ = (a + b)(a + b)² = (a + b)(a² + 2ab + b²) = a(a² + 2ab + b²) + b(a² + 2ab + b²) = a³ + 2a²b + ab² + a²b + 2ab² + b³ = a³ + 3a²b + 3ab² + b³ = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a + b)³",
                "= (a + b)(a + b)²",
                "= (a + b)(a² + 2ab + b²)",
                "= a(a² + 2ab + b²) + b(a² + 2ab + b²)",
                "= a³ + 2a²b + ab² + a²b + 2ab² + b³",
                "= a³ + 3a²b + 3ab² + b³ = ডানপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "মান নির্ণয় করো: (2x + 1)³",
                    stepByStepBn = listOf(
                        "(2x)³ + 3·(2x)²·1 + 3·(2x)·1² + 1³",
                        "= 8x³ + 3·(4x²) + 6x + 1",
                        "= 8x³ + 12x² + 6x + 1"
                    ),
                    answerBn = "8x³ + 12x² + 6x + 1"
                )
            ),
            corollariesBn = listOf(
                "(a + b)³-এ সব চিহ্ন ধনাত্মক: a³ + 3a²b + 3ab² + b³"
            )
        ),

        // সূত্র–২: (a + b)³ = a³ + b³ + 3ab(a + b)
        AlgebraicFormula(
            id = "cube_2",
            serialNo = 2,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–২: যোগফলের ঘন (উৎপাদক/মান নির্ণয় রূপ)",
            formulaLatex = "(a + b)³ = a³ + b³ + 3ab(a + b)",
            descriptionBn = "যোগফলের ঘনের মান নির্ণয়ের সংক্ষিপ্ত রূপ।",
            derivationBn = "ডানপক্ষ = a³ + b³ + 3ab(a + b) = a³ + b³ + 3a²b + 3ab² = a³ + 3a²b + 3ab² + b³ = (a + b)³ = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = a³ + b³ + 3ab(a + b)",
                "= a³ + b³ + 3a²b + 3ab²",
                "= a³ + 3a²b + 3ab² + b³",
                "= (a + b)³ = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a + b = 3 হয়, তবে a³ + b³ + 9ab এর মান কত?",
                    stepByStepBn = listOf(
                        "a³ + b³ + 3ab(3) = a³ + b³ + 3ab(a + b) = (a + b)³",
                        "= 3³ = 27"
                    ),
                    answerBn = "27"
                )
            ),
            corollariesBn = listOf(
                "(a + b)³ = a³ + b³ + 3ab(a + b)"
            )
        ),

        // সূত্র–৩: a³ + b³ = (a + b)³ − 3ab(a + b)
        AlgebraicFormula(
            id = "cube_3",
            serialNo = 3,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–৩: দুই ঘনের যোগফল (মান নির্ণয় সূত্র)",
            formulaLatex = "a³ + b³ = (a + b)³ − 3ab(a + b)",
            descriptionBn = "দুটি রাশির ঘনের সমষ্টির মান নির্ণয়ের সূত্র।",
            derivationBn = "ডানপক্ষ = (a + b)³ − 3ab(a + b) = (a³ + 3a²b + 3ab² + b³) − (3a²b + 3ab²) = a³ + 3a²b + 3ab² + b³ − 3a²b − 3ab² = a³ + b³ = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a + b)³ − 3ab(a + b)",
                "= (a³ + 3a²b + 3ab² + b³) − (3a²b + 3ab²)",
                "= a³ + 3a²b + 3ab² + b³ − 3a²b − 3ab²",
                "= a³ + b³ = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি x + y = 4 এবং xy = 3 হয়, তবে x³ + y³ এর মান কত?",
                    stepByStepBn = listOf(
                        "x³ + y³ = (x + y)³ − 3xy(x + y)",
                        "= 4³ − 3 · 3 · 4 = 64 − 36 = 28"
                    ),
                    answerBn = "28"
                )
            ),
            corollariesBn = listOf(
                "a³ + b³ = (a + b)³ − 3ab(a + b)"
            )
        ),

        // সূত্র–৪: a³ + b³ = (a + b)(a² − ab + b²)
        AlgebraicFormula(
            id = "cube_4",
            serialNo = 4,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–৪: দুই ঘনের যোগফল (উৎপাদক সূত্র)",
            formulaLatex = "a³ + b³ = (a + b)(a² − ab + b²)",
            descriptionBn = "দুটি রাশির ঘনের সমষ্টির উৎপাদকে বিশ্লেষণ।",
            derivationBn = "ডানপক্ষ = (a + b)(a² − ab + b²) = a(a² − ab + b²) + b(a² − ab + b²) = a³ − a²b + ab² + a²b − ab² + b³ = a³ + b³ = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a + b)(a² − ab + b²)",
                "= a(a² − ab + b²) + b(a² − ab + b²)",
                "= a³ − a²b + ab² + a²b − ab² + b³",
                "= a³ + b³ = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "উৎপাদকে বিশ্লেষণ করো: 8x³ + 27y³",
                    stepByStepBn = listOf(
                        "(2x)³ + (3y)³ = (2x + 3y)((2x)² − (2x)(3y) + (3y)²)",
                        "= (2x + 3y)(4x² − 6xy + 9y²)"
                    ),
                    answerBn = "(2x + 3y)(4x² − 6xy + 9y²)"
                )
            ),
            corollariesBn = listOf(
                "সতর্কতা: a³ + b³-এর উৎপাদক রূপে মাঝের পদ −ab।"
            )
        ),

        // সূত্র–৫: (a − b)³ = a³ − 3a²b + 3ab² − b³
        AlgebraicFormula(
            id = "cube_5",
            serialNo = 5,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–৫: বিয়োগফলের ঘন (বিস্তার রূপ)",
            formulaLatex = "(a − b)³ = a³ − 3a²b + 3ab² − b³",
            descriptionBn = "দুটি রাশির বিয়োগফলের ঘনের বিস্তার সূত্র।",
            derivationBn = "বামপক্ষ = (a − b)³ = (a − b)(a − b)² = (a − b)(a² − 2ab + b²) = a(a² − 2ab + b²) − b(a² − 2ab + b²) = a³ − 2a²b + ab² − a²b + 2ab² − b³ = a³ − 3a²b + 3ab² − b³ = ডানপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: বামপক্ষ = (a − b)³",
                "= (a − b)(a − b)²",
                "= (a − b)(a² − 2ab + b²)",
                "= a(a² − 2ab + b²) − b(a² − 2ab + b²)",
                "= a³ − 2a²b + ab² − a²b + 2ab² − b³",
                "= a³ − 3a²b + 3ab² − b³ = ডানপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "মান নির্ণয় করো: (3x − 2)³",
                    stepByStepBn = listOf(
                        "(3x)³ − 3·(3x)²·2 + 3·(3x)·2² − 2³",
                        "= 27x³ − 54x² + 36x − 8"
                    ),
                    answerBn = "27x³ − 54x² + 36x − 8"
                )
            ),
            corollariesBn = listOf(
                "(a − b)³-এ চিহ্ন পর্যায়ক্রমে (+, −, +, −): a³ − 3a²b + 3ab² − b³"
            )
        ),

        // সূত্র–৬: (a − b)³ = a³ − b³ − 3ab(a − b)
        AlgebraicFormula(
            id = "cube_6",
            serialNo = 6,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–৬: বিয়োগফলের ঘন (উৎপাদক/মান নির্ণয় রূপ)",
            formulaLatex = "(a − b)³ = a³ − b³ − 3ab(a − b)",
            descriptionBn = "বিয়োগফলের ঘনের মান নির্ণয়ের সংক্ষিপ্ত রূপ।",
            derivationBn = "ডানপক্ষ = a³ − b³ − 3ab(a − b) = a³ − b³ − 3a²b + 3ab² = a³ − 3a²b + 3ab² − b³ = (a − b)³ = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = a³ − b³ − 3ab(a − b)",
                "= a³ − b³ − 3a²b + 3ab²",
                "= a³ − 3a²b + 3ab² − b³",
                "= (a − b)³ = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a − b = 2 হয়, তবে a³ − b³ − 6ab এর মান কত?",
                    stepByStepBn = listOf(
                        "a³ − b³ − 3ab(2) = a³ − b³ − 3ab(a − b) = (a − b)³",
                        "= 2³ = 8"
                    ),
                    answerBn = "8"
                )
            ),
            corollariesBn = listOf(
                "(a − b)³ = a³ − b³ − 3ab(a − b)"
            )
        ),

        // সূত্র–৭: a³ − b³ = (a − b)³ + 3ab(a − b)
        AlgebraicFormula(
            id = "cube_7",
            serialNo = 7,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–৭: দুই ঘনের বিয়োগফল (মান নির্ণয় সূত্র)",
            formulaLatex = "a³ − b³ = (a − b)³ + 3ab(a − b)",
            descriptionBn = "দুটি রাশির ঘনের অন্তরের মান নির্ণয়ের সূত্র।",
            derivationBn = "ডানপক্ষ = (a − b)³ + 3ab(a − b) = (a³ − 3a²b + 3ab² − b³) + (3a²b − 3ab²) = a³ − 3a²b + 3ab² − b³ + 3a²b − 3ab² = a³ − b³ = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a − b)³ + 3ab(a − b)",
                "= (a³ − 3a²b + 3ab² − b³) + (3a²b − 3ab²)",
                "= a³ − 3a²b + 3ab² − b³ + 3a²b − 3ab²",
                "= a³ − b³ = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "যদি a − b = 5 এবং ab = 6 হয়, তবে a³ − b³ এর মান কত?",
                    stepByStepBn = listOf(
                        "a³ − b³ = (a − b)³ + 3ab(a − b)",
                        "= 5³ + 3 · 6 · 5 = 125 + 90 = 215"
                    ),
                    answerBn = "215"
                )
            ),
            corollariesBn = listOf(
                "a³ − b³ = (a − b)³ + 3ab(a − b)"
            )
        ),

        // সূত্র–৮: a³ − b³ = (a − b)(a² + ab + b²)
        AlgebraicFormula(
            id = "cube_8",
            serialNo = 8,
            category = FormulaCategory.CUBE,
            nameBn = "সূত্র–৮: দুই ঘনের বিয়োগফল (উৎপাদক সূত্র)",
            formulaLatex = "a³ − b³ = (a − b)(a² + ab + b²)",
            descriptionBn = "দুটি রাশির ঘনের অন্তরের উৎপাদকে বিশ্লেষণ।",
            derivationBn = "ডানপক্ষ = (a − b)(a² + ab + b²) = a(a² + ab + b²) − b(a² + ab + b²) = a³ + a²b + ab² − a²b − ab² − b³ = a³ − b³ = বামপক্ষ। (প্রমাণিত)",
            redProofNotesBn = listOf(
                "প্রমাণ: ডানপক্ষ = (a − b)(a² + ab + b²)",
                "= a(a² + ab + b²) − b(a² + ab + b²)",
                "= a³ + a²b + ab² − a²b − ab² − b³",
                "= a³ − b³ = বামপক্ষ। (প্রমাণিত)"
            ),
            examples = listOf(
                FormulaExample(
                    problemBn = "উৎপাদকে বিশ্লেষণ করো: 64a³ − 125b³",
                    stepByStepBn = listOf(
                        "(4a)³ − (5b)³ = (4a − 5b)((4a)² + (4a)(5b) + (5b)²)",
                        "= (4a − 5b)(16a² + 20ab + 25b²)"
                    ),
                    answerBn = "(4a − 5b)(16a² + 20ab + 25b²)"
                )
            ),
            corollariesBn = listOf(
                "সতর্কতা: a³ − b³-এর উৎপাদক রূপে মাঝের পদ +ab।"
            )
        )
    )

    // পর্ব ৩: বর্গের পরীক্ষার জন্য গুরুত্বপূর্ণ নিয়মাবলী ও পরামর্শ
    val importantExamRulesBn: List<String> = listOf(
        "১. (a + b)² সূত্রে মাঝের পদ +2ab।",
        "২. (a − b)² সূত্রে মাঝের পদ −2ab।",
        "৩. (a + b)(a − b) = a² − b²।",
        "৪. (a + b)² − (a − b)² = 4ab।",
        "৫. (a + b)² + (a − b)² = 2(a² + b²)।",
        "৬. তিনটি রাশির বর্গে তিনটি রাশির বর্গের সঙ্গে জোড়ায় জোড়ায় গুণফলের দ্বিগুণ যোগ বা বিয়োগ হয়।"
    )

    val specialWarningBn: String = "বিশেষ সতর্কতা: (a − b − c)²-এ ab-এর পদ ঋণাত্মক, bc-এর পদ ধনাত্মক এবং ca-এর পদ ঋণাত্মক। চিহ্নগুলো ভালোভাবে খেয়াল করতে হবে।"

    val studyAdviceBn: String = "পড়ার পরামর্শ: সূত্রগুলো শুধু মুখস্থ না করে প্রতিটি প্রমাণ নিজে খাতায় লিখে অনুশীলন করো। এতে BCS প্রিলিমিনারি গণিতে সূত্র প্রয়োগ, মান নির্ণয় এবং দ্রুত সমাধানের ভিত্তি শক্ত হবে।"

    // পর্ব ৩: ঘনের দ্রুত মনে রাখার নিয়ম
    val cubeMemoryRulesBn: List<String> = listOf(
        "১. (a + b)³-এ চিহ্ন সব ধনাত্মক: a³ + 3a²b + 3ab² + b³",
        "২. (a − b)³-এ চিহ্ন পর্যায়ক্রমে: a³ − 3a²b + 3ab² − b³",
        "৩. দুই ঘনের যোগফল: a³ + b³ = (a + b)(a² − ab + b²)",
        "৪. দুই ঘনের বিয়োগফল: a³ − b³ = (a − b)(a² + ab + b²)",
        "৫. সতর্কতা: a³ + b³-এর উৎপাদক রূপে মাঝের পদ −ab; a³ − b³-এর উৎপাদক রূপে মাঝের পদ +ab।"
    )

    fun getAll(): List<AlgebraicFormula> = squareFormulas + cubeFormulas

    fun getById(id: String): AlgebraicFormula? = getAll().find { it.id == id }
}
