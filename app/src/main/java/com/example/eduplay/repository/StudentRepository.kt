package com.example.eduplay.repository

import android.content.Context
import com.example.eduplay.database.DatabaseHelper
import com.example.eduplay.models.GameProgress
import com.example.eduplay.models.Student
import com.example.eduplay.models.StudentStats
import com.example.eduplay.models.SubjectProgress

class StudentRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)
    
    // ==================== STUDENT OPERATIONS ====================
    
    fun createStudent(student: Student): Long {
        return dbHelper.insertStudent(student)
    }
    
    fun getStudent(rollNumber: String): Student? {
        return dbHelper.getStudent(rollNumber)
    }
    
    fun getStudentById(id: Long): Student? {
        return dbHelper.getStudentById(id)
    }
    
    fun getAllStudents(): List<Student> {
        return dbHelper.getAllStudents()
    }
    
    fun updateStudent(student: Student): Boolean {
        return dbHelper.updateStudent(student)
    }
    
    fun deleteStudent(studentId: Long): Boolean {
        return dbHelper.deleteStudent(studentId)
    }
    
    // ==================== GAME PROGRESS OPERATIONS ====================
    
    fun saveGameProgress(gameProgress: GameProgress): Long {
        // Check if progress already exists for this student and game
        val existingProgress = dbHelper.getGameProgress(gameProgress.studentId, gameProgress.gameName)
        
        return if (existingProgress != null) {
            // Update existing progress
            val updatedProgress = existingProgress.copy(
                score = maxOf(existingProgress.score, gameProgress.score),
                maxScore = maxOf(existingProgress.maxScore, gameProgress.maxScore),
                starsEarned = maxOf(existingProgress.starsEarned, gameProgress.starsEarned),
                isCompleted = existingProgress.isCompleted || gameProgress.isCompleted,
                timeSpent = existingProgress.timeSpent + gameProgress.timeSpent,
                attempts = existingProgress.attempts + 1,
                lastPlayedAt = gameProgress.lastPlayedAt
            )
            if (dbHelper.updateGameProgress(updatedProgress)) {
                updatedProgress.id
            } else {
                -1L
            }
        } else {
            // Insert new progress
            dbHelper.insertGameProgress(gameProgress)
        }
    }
    
    fun getGameProgress(studentId: Long, gameName: String): GameProgress? {
        return dbHelper.getGameProgress(studentId, gameName)
    }
    
    fun getAllGameProgress(studentId: Long): List<GameProgress> {
        return dbHelper.getAllGameProgress(studentId)
    }
    
    fun getGameProgressBySubject(studentId: Long, subject: String): List<GameProgress> {
        return dbHelper.getGameProgressBySubject(studentId, subject)
    }
    
    fun updateGameProgress(gameProgress: GameProgress): Boolean {
        return dbHelper.updateGameProgress(gameProgress)
    }
    
    fun deleteGameProgress(gameProgressId: Long): Boolean {
        return dbHelper.deleteGameProgress(gameProgressId)
    }
    
    fun resetStudentProgress(studentId: Long): Boolean {
        return dbHelper.deleteAllGameProgress(studentId)
    }
    
    // ==================== ANALYTICS OPERATIONS ====================
    
    fun getSubjectProgress(studentId: Long): List<SubjectProgress> {
        return dbHelper.getSubjectProgress(studentId)
    }
    
    fun getStudentStats(studentId: Long): StudentStats? {
        return dbHelper.getStudentStats(studentId)
    }
    
    // ==================== UTILITY OPERATIONS ====================
    
    fun clearAllData(): Boolean {
        return dbHelper.clearAllData()
    }
    
    fun getDatabaseSize(): Long {
        return dbHelper.getDatabaseSize()
    }
    
    // ==================== DEMO DATA CREATION ====================
    
    fun createDemoData() {
        // Create demo students
        val demoStudents = listOf(
            Student(
                name = "Rahul Kumar",
                className = "Class 3",
                rollNumber = "STU001",
                email = "rahul@example.com",
                parentName = "Mr. Kumar",
                address = "Bhubaneswar, Odisha"
            ),
            Student(
                name = "Priya Das",
                className = "Class 3",
                rollNumber = "STU002",
                email = "priya@example.com",
                parentName = "Mrs. Das",
                address = "Cuttack, Odisha"
            ),
            Student(
                name = "Arjun Patra",
                className = "Class 3",
                rollNumber = "STU003",
                email = "arjun@example.com",
                parentName = "Mr. Patra",
                address = "Puri, Odisha"
            )
        )
        
        demoStudents.forEach { student ->
            val studentId = createStudent(student)
            
            // Create demo game progress for each student
            val demoGameProgress = listOf(
                GameProgress(
                    studentId = studentId,
                    gameName = "Number Counting",
                    subject = "Mathematics",
                    difficulty = "Easy",
                    score = 85,
                    maxScore = 100,
                    starsEarned = 3,
                    isCompleted = true,
                    timeSpent = 300, // 5 minutes
                    attempts = 1
                ),
                GameProgress(
                    studentId = studentId,
                    gameName = "Number Matching",
                    subject = "Mathematics",
                    difficulty = "Easy",
                    score = 90,
                    maxScore = 100,
                    starsEarned = 3,
                    isCompleted = true,
                    timeSpent = 240, // 4 minutes
                    attempts = 1
                ),
                GameProgress(
                    studentId = studentId,
                    gameName = "Odia Letter Recognition",
                    subject = "Odia Language",
                    difficulty = "Easy",
                    score = 75,
                    maxScore = 100,
                    starsEarned = 2,
                    isCompleted = true,
                    timeSpent = 360, // 6 minutes
                    attempts = 2
                ),
                GameProgress(
                    studentId = studentId,
                    gameName = "English Alphabet Song",
                    subject = "English",
                    difficulty = "Easy",
                    score = 95,
                    maxScore = 100,
                    starsEarned = 3,
                    isCompleted = true,
                    timeSpent = 180, // 3 minutes
                    attempts = 1
                )
            )
            
            demoGameProgress.forEach { progress ->
                saveGameProgress(progress)
            }
        }
    }
}
