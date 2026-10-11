package com.example.math

/**
 * Data model for Square Table exactly as shown in PDF "241713.jpg"
 * Column 1: সংখ্যা (2 to 20)
 * Column 2: বর্গ (Cube) -> e.g. "2² = 4"
 * Column 3: বর্গমূল (Square Root) -> e.g. "√4 = 2"
 */
data class SquareItem(
    val n: Int,
    val squareText: String,      // e.g. "2² = 4"
    val squareRootText: String   // e.g. "√4 = 2"
)

/**
 * Data model for Cube Table exactly as shown in PDF "241715.jpg"
 * Column 1: সংখ্যা (2 to 10)
 * Column 2: ঘন (Cube) -> e.g. "2³ = 8"
 * Column 3: ঘনমূল (Cube Root) -> e.g. "∛8 = 2"
 */
data class CubeItem(
    val n: Int,
    val cubeText: String,        // e.g. "2³ = 8"
    val cubeRootText: String     // e.g. "∛8 = 2"
)

object SquareCubeProvider {
    val squareHeaderTitle: String = "\"241713.jpg\" ছবিতে দেওয়া বর্গ ও বর্গমূলের সম্পূর্ণ তালিকাটি নিচে টেবিলে সাজিয়ে দেওয়া হলো:"
    val squareFooterNote: String = "বিসিএস ও অন্যান্য চাকরির পরীক্ষার গণিত অংশে দ্রুত হিসাব করার জন্য ১ থেকে ২০ পর্যন্ত সংখ্যার বর্গ এবং বর্গমূল মুখস্থ রাখা অত্যন্ত কার্যকরী।"

    val cubeHeaderTitle: String = "\"241715.jpg\" ছবিতে দেওয়া ঘন ও ঘনমূলের সম্পূর্ণ তালিকাটি নিচে টেবিলে সাজিয়ে দেওয়া হলো:"
    val cubeFooterNote: String = "বিসিএস (BCS), এনটিআরসিএ (NTRCA) বা অন্যান্য যেকোনো প্রতিযোগিতামূলক পরীক্ষার গাণিতিক যুক্তি অংশে দ্রুত সময় বাঁচানোর জন্য ১ থেকে ১০ পর্যন্ত সংখ্যার ঘন এবং ঘনমূল মুখস্থ রাখা অত্যন্ত জরুরি।"

    // ১ থেকে ২০ এর বর্গ তালিকা
    val squareTableList: List<SquareItem> = listOf(
        SquareItem(n = 1, squareText = "1² = 1", squareRootText = "√1 = 1"),
        SquareItem(n = 2, squareText = "2² = 4", squareRootText = "√4 = 2"),
        SquareItem(n = 3, squareText = "3² = 9", squareRootText = "√9 = 3"),
        SquareItem(n = 4, squareText = "4² = 16", squareRootText = "√16 = 4"),
        SquareItem(n = 5, squareText = "5² = 25", squareRootText = "√25 = 5"),
        SquareItem(n = 6, squareText = "6² = 36", squareRootText = "√36 = 6"),
        SquareItem(n = 7, squareText = "7² = 49", squareRootText = "√49 = 7"),
        SquareItem(n = 8, squareText = "8² = 64", squareRootText = "√64 = 8"),
        SquareItem(n = 9, squareText = "9² = 81", squareRootText = "√81 = 9"),
        SquareItem(n = 10, squareText = "10² = 100", squareRootText = "√100 = 10"),
        SquareItem(n = 11, squareText = "11² = 121", squareRootText = "√121 = 11"),
        SquareItem(n = 12, squareText = "12² = 144", squareRootText = "√144 = 12"),
        SquareItem(n = 13, squareText = "13² = 169", squareRootText = "√169 = 13"),
        SquareItem(n = 14, squareText = "14² = 196", squareRootText = "√196 = 14"),
        SquareItem(n = 15, squareText = "15² = 225", squareRootText = "√225 = 15"),
        SquareItem(n = 16, squareText = "16² = 256", squareRootText = "√256 = 16"),
        SquareItem(n = 17, squareText = "17² = 289", squareRootText = "√289 = 17"),
        SquareItem(n = 18, squareText = "18² = 324", squareRootText = "√324 = 18"),
        SquareItem(n = 19, squareText = "19² = 361", squareRootText = "√361 = 19"),
        SquareItem(n = 20, squareText = "20² = 400", squareRootText = "√400 = 20")
    )

    // ১ থেকে ২০ এর সম্পূর্ণ ঘন ও ঘনমূল তালিকা (মূল ১-১০ এবং পরিবর্ধিত ১১-২০)
    val cubeTableList1To10: List<CubeItem> = listOf(
        CubeItem(n = 1, cubeText = "1³ = 1", cubeRootText = "∛1 = 1"),
        CubeItem(n = 2, cubeText = "2³ = 8", cubeRootText = "∛8 = 2"),
        CubeItem(n = 3, cubeText = "3³ = 27", cubeRootText = "∛27 = 3"),
        CubeItem(n = 4, cubeText = "4³ = 64", cubeRootText = "∛64 = 4"),
        CubeItem(n = 5, cubeText = "5³ = 125", cubeRootText = "∛125 = 5"),
        CubeItem(n = 6, cubeText = "6³ = 216", cubeRootText = "∛216 = 6"),
        CubeItem(n = 7, cubeText = "7³ = 343", cubeRootText = "∛343 = 7"),
        CubeItem(n = 8, cubeText = "8³ = 512", cubeRootText = "∛512 = 8"),
        CubeItem(n = 9, cubeText = "9³ = 729", cubeRootText = "∛729 = 9"),
        CubeItem(n = 10, cubeText = "10³ = 1000", cubeRootText = "∛1000 = 10")
    )

    val cubeTableList11To20: List<CubeItem> = listOf(
        CubeItem(n = 11, cubeText = "11³ = 1331", cubeRootText = "∛1331 = 11"),
        CubeItem(n = 12, cubeText = "12³ = 1728", cubeRootText = "∛1728 = 12"),
        CubeItem(n = 13, cubeText = "13³ = 2197", cubeRootText = "∛2197 = 13"),
        CubeItem(n = 14, cubeText = "14³ = 2744", cubeRootText = "∛2744 = 14"),
        CubeItem(n = 15, cubeText = "15³ = 3375", cubeRootText = "∛3375 = 15"),
        CubeItem(n = 16, cubeText = "16³ = 4096", cubeRootText = "∛4096 = 16"),
        CubeItem(n = 17, cubeText = "17³ = 4913", cubeRootText = "∛4913 = 17"),
        CubeItem(n = 18, cubeText = "18³ = 5832", cubeRootText = "∛5832 = 18"),
        CubeItem(n = 19, cubeText = "19³ = 6859", cubeRootText = "∛6859 = 19"),
        CubeItem(n = 20, cubeText = "20³ = 8000", cubeRootText = "∛8000 = 20")
    )

    val cubeTableList: List<CubeItem> = cubeTableList1To10 + cubeTableList11To20
}
