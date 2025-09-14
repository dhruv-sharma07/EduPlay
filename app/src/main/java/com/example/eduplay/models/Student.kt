package com.example.eduplay.models

data class Student(
    val id: Long = 0,
    val name: String,
    val className: String,
    val rollNumber: String,
    val email: String? = null,
    val phone: String? = null,
    val dateOfBirth: String? = null,
    val parentName: String? = null,
    val address: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

data class GameProgress(
    val id: Long = 0,
    val studentId: Long,
    val gameName: String,
    val subject: String,
    val difficulty: String,
    val score: Int,
    val maxScore: Int,
    val starsEarned: Int,
    val isCompleted: Boolean,
    val timeSpent: Long, // in seconds
    val attempts: Int,
    val lastPlayedAt: Long = System.currentTimeMillis(),
    val createdAt: Long = System.currentTimeMillis()
)

data class SubjectProgress(
    val subject: String,
    val totalGames: Int,
    val completedGames: Int,
    val totalScore: Int,
    val averageScore: Double,
    val totalStars: Int,
    val lastPlayedAt: Long
)

data class StudentStats(
    val studentId: Long,
    val totalGamesPlayed: Int,
    val totalScore: Int,
    val averageScore: Double,
    val totalStars: Int,
    val favoriteSubject: String,
    val lastActivityAt: Long,
    val streak: Int // consecutive days played
)
