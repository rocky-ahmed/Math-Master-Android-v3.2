package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity for saving user bookmarks (formulas, specific multiplication tables, prime number notes, etc.)
 */
@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey
    val id: String, // e.g. "table_17", "formula_sq_1", "prime_73"
    val category: String, // "TABLE", "PRIME", "SQUARE_CUBE", "FORMULA"
    val titleBn: String,
    val subtitleBn: String,
    val detailBn: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Entity for saving quiz results and learning achievements
 */
@Entity(tableName = "quiz_scores")
data class QuizScoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val topicId: String, // e.g. "MULTIPLICATION", "PRIMES", "SQUARES_CUBES", "ALGEBRA"
    val topicTitleBn: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Entity for tracking table / formula mastery (e.g. mark table 12 as completed/memorized)
 */
@Entity(tableName = "learned_progress")
data class ProgressEntity(
    @PrimaryKey
    val itemId: String, // e.g. "table_7", "prime_range_1", "sq_15"
    val isLearned: Boolean,
    val timesPracticed: Int = 0,
    val lastPracticedAt: Long = System.currentTimeMillis()
)
