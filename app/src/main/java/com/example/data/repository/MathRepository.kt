package com.example.data.repository

import com.example.data.db.MathDao
import com.example.data.model.BookmarkEntity
import com.example.data.model.ProgressEntity
import com.example.data.model.QuizScoreEntity
import kotlinx.coroutines.flow.Flow

class MathRepository(private val dao: MathDao) {
    val bookmarks: Flow<List<BookmarkEntity>> = dao.getAllBookmarks()
    val quizScores: Flow<List<QuizScoreEntity>> = dao.getAllQuizScores()
    val progressList: Flow<List<ProgressEntity>> = dao.getAllProgress()

    fun isBookmarked(id: String): Flow<Boolean> = dao.isBookmarked(id)

    suspend fun toggleBookmark(bookmark: BookmarkEntity, currentlyBookmarked: Boolean) {
        if (currentlyBookmarked) {
            dao.deleteBookmark(bookmark.id)
        } else {
            dao.insertBookmark(bookmark)
        }
    }

    suspend fun removeBookmark(id: String) {
        dao.deleteBookmark(id)
    }

    suspend fun recordQuizScore(score: QuizScoreEntity) {
        dao.insertQuizScore(score)
    }

    suspend fun toggleLearned(itemId: String, currentlyLearned: Boolean) {
        val existing = dao.getProgressById(itemId)
        val practiceCount = (existing?.timesPracticed ?: 0) + 1
        dao.saveProgress(
            ProgressEntity(
                itemId = itemId,
                isLearned = !currentlyLearned,
                timesPracticed = practiceCount,
                lastPracticedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun recordPractice(itemId: String) {
        val existing = dao.getProgressById(itemId)
        val currentLearned = existing?.isLearned ?: false
        val practiceCount = (existing?.timesPracticed ?: 0) + 1
        dao.saveProgress(
            ProgressEntity(
                itemId = itemId,
                isLearned = currentLearned,
                timesPracticed = practiceCount,
                lastPracticedAt = System.currentTimeMillis()
            )
        )
    }
}
