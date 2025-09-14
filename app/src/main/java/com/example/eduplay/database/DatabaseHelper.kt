package com.example.eduplay.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.eduplay.models.GameProgress
import com.example.eduplay.models.Student
import com.example.eduplay.models.StudentStats
import com.example.eduplay.models.SubjectProgress

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    
    companion object {
        private const val DATABASE_NAME = "EduPlay.db"
        private const val DATABASE_VERSION = 1
        
        // Table names
        const val TABLE_STUDENTS = "students"
        const val TABLE_GAME_PROGRESS = "game_progress"
        
        // Students table columns
        const val COL_STUDENT_ID = "id"
        const val COL_STUDENT_NAME = "name"
        const val COL_CLASS_NAME = "class_name"
        const val COL_ROLL_NUMBER = "roll_number"
        const val COL_EMAIL = "email"
        const val COL_PHONE = "phone"
        const val COL_DATE_OF_BIRTH = "date_of_birth"
        const val COL_PARENT_NAME = "parent_name"
        const val COL_ADDRESS = "address"
        const val COL_CREATED_AT = "created_at"
        const val COL_LAST_LOGIN_AT = "last_login_at"
        const val COL_IS_ACTIVE = "is_active"
        
        // Game progress table columns
        const val COL_GAME_ID = "id"
        const val COL_GAME_STUDENT_ID = "student_id"
        const val COL_GAME_NAME = "game_name"
        const val COL_SUBJECT = "subject"
        const val COL_DIFFICULTY = "difficulty"
        const val COL_SCORE = "score"
        const val COL_MAX_SCORE = "max_score"
        const val COL_STARS_EARNED = "stars_earned"
        const val COL_IS_COMPLETED = "is_completed"
        const val COL_TIME_SPENT = "time_spent"
        const val COL_ATTEMPTS = "attempts"
        const val COL_LAST_PLAYED_AT = "last_played_at"
        const val COL_GAME_CREATED_AT = "created_at"
    }
    
    // SQL statements for table creation
    private val CREATE_STUDENTS_TABLE = """
        CREATE TABLE $TABLE_STUDENTS (
            $COL_STUDENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_STUDENT_NAME TEXT NOT NULL,
            $COL_CLASS_NAME TEXT NOT NULL,
            $COL_ROLL_NUMBER TEXT UNIQUE NOT NULL,
            $COL_EMAIL TEXT,
            $COL_PHONE TEXT,
            $COL_DATE_OF_BIRTH TEXT,
            $COL_PARENT_NAME TEXT,
            $COL_ADDRESS TEXT,
            $COL_CREATED_AT INTEGER NOT NULL,
            $COL_LAST_LOGIN_AT INTEGER NOT NULL,
            $COL_IS_ACTIVE INTEGER NOT NULL DEFAULT 1
        )
    """.trimIndent()
    
    private val CREATE_GAME_PROGRESS_TABLE = """
        CREATE TABLE $TABLE_GAME_PROGRESS (
            $COL_GAME_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COL_GAME_STUDENT_ID INTEGER NOT NULL,
            $COL_GAME_NAME TEXT NOT NULL,
            $COL_SUBJECT TEXT NOT NULL,
            $COL_DIFFICULTY TEXT NOT NULL,
            $COL_SCORE INTEGER NOT NULL DEFAULT 0,
            $COL_MAX_SCORE INTEGER NOT NULL DEFAULT 100,
            $COL_STARS_EARNED INTEGER NOT NULL DEFAULT 0,
            $COL_IS_COMPLETED INTEGER NOT NULL DEFAULT 0,
            $COL_TIME_SPENT INTEGER NOT NULL DEFAULT 0,
            $COL_ATTEMPTS INTEGER NOT NULL DEFAULT 1,
            $COL_LAST_PLAYED_AT INTEGER NOT NULL,
            $COL_GAME_CREATED_AT INTEGER NOT NULL,
            FOREIGN KEY ($COL_GAME_STUDENT_ID) REFERENCES $TABLE_STUDENTS ($COL_STUDENT_ID)
        )
    """.trimIndent()
    
    // Indexes for better performance
    private val CREATE_INDEX_STUDENT_ROLL = """
        CREATE INDEX idx_student_roll ON $TABLE_STUDENTS ($COL_ROLL_NUMBER)
    """.trimIndent()
    
    private val CREATE_INDEX_GAME_STUDENT = """
        CREATE INDEX idx_game_student ON $TABLE_GAME_PROGRESS ($COL_GAME_STUDENT_ID)
    """.trimIndent()
    
    private val CREATE_INDEX_GAME_SUBJECT = """
        CREATE INDEX idx_game_subject ON $TABLE_GAME_PROGRESS ($COL_SUBJECT)
    """.trimIndent()
    
    private val CREATE_INDEX_GAME_NAME = """
        CREATE INDEX idx_game_name ON $TABLE_GAME_PROGRESS ($COL_GAME_NAME)
    """.trimIndent()
    
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_STUDENTS_TABLE)
        db.execSQL(CREATE_GAME_PROGRESS_TABLE)
        db.execSQL(CREATE_INDEX_STUDENT_ROLL)
        db.execSQL(CREATE_INDEX_GAME_STUDENT)
        db.execSQL(CREATE_INDEX_GAME_SUBJECT)
        db.execSQL(CREATE_INDEX_GAME_NAME)
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades here
        when (oldVersion) {
            1 -> {
                // Future migrations can be added here
            }
        }
    }
    
    // ==================== STUDENT CRUD OPERATIONS ====================
    
    fun insertStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_STUDENT_NAME, student.name)
            put(COL_CLASS_NAME, student.className)
            put(COL_ROLL_NUMBER, student.rollNumber)
            put(COL_EMAIL, student.email)
            put(COL_PHONE, student.phone)
            put(COL_DATE_OF_BIRTH, student.dateOfBirth)
            put(COL_PARENT_NAME, student.parentName)
            put(COL_ADDRESS, student.address)
            put(COL_CREATED_AT, student.createdAt)
            put(COL_LAST_LOGIN_AT, student.lastLoginAt)
            put(COL_IS_ACTIVE, if (student.isActive) 1 else 0)
        }
        return db.insert(TABLE_STUDENTS, null, values)
    }
    
    fun getStudent(rollNumber: String): Student? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            "$COL_ROLL_NUMBER = ?",
            arrayOf(rollNumber),
            null, null, null
        )
        
        return if (cursor.moveToFirst()) {
            cursorToStudent(cursor)
        } else {
            cursor.close()
            null
        }
    }
    
    fun getStudentById(id: Long): Student? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            "$COL_STUDENT_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        
        return if (cursor.moveToFirst()) {
            cursorToStudent(cursor)
        } else {
            cursor.close()
            null
        }
    }
    
    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            "$COL_IS_ACTIVE = ?",
            arrayOf("1"),
            null, null,
            "$COL_LAST_LOGIN_AT DESC"
        )
        
        while (cursor.moveToNext()) {
            students.add(cursorToStudent(cursor))
        }
        cursor.close()
        return students
    }
    
    fun updateStudent(student: Student): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_STUDENT_NAME, student.name)
            put(COL_CLASS_NAME, student.className)
            put(COL_ROLL_NUMBER, student.rollNumber)
            put(COL_EMAIL, student.email)
            put(COL_PHONE, student.phone)
            put(COL_DATE_OF_BIRTH, student.dateOfBirth)
            put(COL_PARENT_NAME, student.parentName)
            put(COL_ADDRESS, student.address)
            put(COL_LAST_LOGIN_AT, student.lastLoginAt)
            put(COL_IS_ACTIVE, if (student.isActive) 1 else 0)
        }
        
        val rowsAffected = db.update(
            TABLE_STUDENTS,
            values,
            "$COL_STUDENT_ID = ?",
            arrayOf(student.id.toString())
        )
        return rowsAffected > 0
    }
    
    fun deleteStudent(studentId: Long): Boolean {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_STUDENTS,
            "$COL_STUDENT_ID = ?",
            arrayOf(studentId.toString())
        )
        return rowsAffected > 0
    }
    
    // ==================== GAME PROGRESS CRUD OPERATIONS ====================
    
    fun insertGameProgress(gameProgress: GameProgress): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_GAME_STUDENT_ID, gameProgress.studentId)
            put(COL_GAME_NAME, gameProgress.gameName)
            put(COL_SUBJECT, gameProgress.subject)
            put(COL_DIFFICULTY, gameProgress.difficulty)
            put(COL_SCORE, gameProgress.score)
            put(COL_MAX_SCORE, gameProgress.maxScore)
            put(COL_STARS_EARNED, gameProgress.starsEarned)
            put(COL_IS_COMPLETED, if (gameProgress.isCompleted) 1 else 0)
            put(COL_TIME_SPENT, gameProgress.timeSpent)
            put(COL_ATTEMPTS, gameProgress.attempts)
            put(COL_LAST_PLAYED_AT, gameProgress.lastPlayedAt)
            put(COL_GAME_CREATED_AT, gameProgress.createdAt)
        }
        return db.insert(TABLE_GAME_PROGRESS, null, values)
    }
    
    fun updateGameProgress(gameProgress: GameProgress): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_SCORE, gameProgress.score)
            put(COL_MAX_SCORE, gameProgress.maxScore)
            put(COL_STARS_EARNED, gameProgress.starsEarned)
            put(COL_IS_COMPLETED, if (gameProgress.isCompleted) 1 else 0)
            put(COL_TIME_SPENT, gameProgress.timeSpent)
            put(COL_ATTEMPTS, gameProgress.attempts)
            put(COL_LAST_PLAYED_AT, gameProgress.lastPlayedAt)
        }
        
        val rowsAffected = db.update(
            TABLE_GAME_PROGRESS,
            values,
            "$COL_GAME_ID = ?",
            arrayOf(gameProgress.id.toString())
        )
        return rowsAffected > 0
    }
    
    fun getGameProgress(studentId: Long, gameName: String): GameProgress? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_GAME_PROGRESS,
            null,
            "$COL_GAME_STUDENT_ID = ? AND $COL_GAME_NAME = ?",
            arrayOf(studentId.toString(), gameName),
            null, null,
            "$COL_LAST_PLAYED_AT DESC"
        )
        
        return if (cursor.moveToFirst()) {
            cursorToGameProgress(cursor)
        } else {
            cursor.close()
            null
        }
    }
    
    fun getAllGameProgress(studentId: Long): List<GameProgress> {
        val gameProgressList = mutableListOf<GameProgress>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_GAME_PROGRESS,
            null,
            "$COL_GAME_STUDENT_ID = ?",
            arrayOf(studentId.toString()),
            null, null,
            "$COL_LAST_PLAYED_AT DESC"
        )
        
        while (cursor.moveToNext()) {
            gameProgressList.add(cursorToGameProgress(cursor))
        }
        cursor.close()
        return gameProgressList
    }
    
    fun getGameProgressBySubject(studentId: Long, subject: String): List<GameProgress> {
        val gameProgressList = mutableListOf<GameProgress>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_GAME_PROGRESS,
            null,
            "$COL_GAME_STUDENT_ID = ? AND $COL_SUBJECT = ?",
            arrayOf(studentId.toString(), subject),
            null, null,
            "$COL_LAST_PLAYED_AT DESC"
        )
        
        while (cursor.moveToNext()) {
            gameProgressList.add(cursorToGameProgress(cursor))
        }
        cursor.close()
        return gameProgressList
    }
    
    fun deleteGameProgress(gameProgressId: Long): Boolean {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_GAME_PROGRESS,
            "$COL_GAME_ID = ?",
            arrayOf(gameProgressId.toString())
        )
        return rowsAffected > 0
    }
    
    fun deleteAllGameProgress(studentId: Long): Boolean {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_GAME_PROGRESS,
            "$COL_GAME_STUDENT_ID = ?",
            arrayOf(studentId.toString())
        )
        return rowsAffected > 0
    }
    
    // ==================== ANALYTICS AND STATISTICS ====================
    
    fun getSubjectProgress(studentId: Long): List<SubjectProgress> {
        val subjectProgressList = mutableListOf<SubjectProgress>()
        val db = readableDatabase
        
        val query = """
            SELECT 
                $COL_SUBJECT,
                COUNT(*) as total_games,
                SUM(CASE WHEN $COL_IS_COMPLETED = 1 THEN 1 ELSE 0 END) as completed_games,
                SUM($COL_SCORE) as total_score,
                AVG($COL_SCORE) as average_score,
                SUM($COL_STARS_EARNED) as total_stars,
                MAX($COL_LAST_PLAYED_AT) as last_played_at
            FROM $TABLE_GAME_PROGRESS 
            WHERE $COL_GAME_STUDENT_ID = ?
            GROUP BY $COL_SUBJECT
            ORDER BY last_played_at DESC
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(studentId.toString()))
        
        while (cursor.moveToNext()) {
            subjectProgressList.add(SubjectProgress(
                subject = cursor.getString(0),
                totalGames = cursor.getInt(1),
                completedGames = cursor.getInt(2),
                totalScore = cursor.getInt(3),
                averageScore = cursor.getDouble(4),
                totalStars = cursor.getInt(5),
                lastPlayedAt = cursor.getLong(6)
            ))
        }
        cursor.close()
        return subjectProgressList
    }
    
    fun getStudentStats(studentId: Long): StudentStats? {
        val db = readableDatabase
        
        val query = """
            SELECT 
                COUNT(*) as total_games_played,
                SUM($COL_SCORE) as total_score,
                AVG($COL_SCORE) as average_score,
                SUM($COL_STARS_EARNED) as total_stars,
                MAX($COL_LAST_PLAYED_AT) as last_activity_at
            FROM $TABLE_GAME_PROGRESS 
            WHERE $COL_GAME_STUDENT_ID = ?
        """.trimIndent()
        
        val cursor = db.rawQuery(query, arrayOf(studentId.toString()))
        
        if (cursor.moveToFirst()) {
            val favoriteSubjectQuery = """
                SELECT $COL_SUBJECT, COUNT(*) as game_count
                FROM $TABLE_GAME_PROGRESS 
                WHERE $COL_GAME_STUDENT_ID = ?
                GROUP BY $COL_SUBJECT
                ORDER BY game_count DESC
                LIMIT 1
            """.trimIndent()
            
            val favoriteCursor = db.rawQuery(favoriteSubjectQuery, arrayOf(studentId.toString()))
            val favoriteSubject = if (favoriteCursor.moveToFirst()) {
                favoriteCursor.getString(0)
            } else {
                "None"
            }
            favoriteCursor.close()
            
            val stats = StudentStats(
                studentId = studentId,
                totalGamesPlayed = cursor.getInt(0),
                totalScore = cursor.getInt(1),
                averageScore = cursor.getDouble(2),
                totalStars = cursor.getInt(3),
                favoriteSubject = favoriteSubject,
                lastActivityAt = cursor.getLong(4),
                streak = calculateStreak(studentId) // Implement streak calculation
            )
            cursor.close()
            return stats
        }
        cursor.close()
        return null
    }
    
    private fun calculateStreak(studentId: Long): Int {
        // Simple streak calculation - can be enhanced
        val db = readableDatabase
        val query = """
            SELECT COUNT(DISTINCT DATE($COL_LAST_PLAYED_AT/1000, 'unixepoch')) as streak_days
            FROM $TABLE_GAME_PROGRESS 
            WHERE $COL_GAME_STUDENT_ID = ? 
            AND $COL_LAST_PLAYED_AT >= ?
        """.trimIndent()
        
        val sevenDaysAgo = System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000)
        val cursor = db.rawQuery(query, arrayOf(studentId.toString(), sevenDaysAgo.toString()))
        
        val streak = if (cursor.moveToFirst()) {
            cursor.getInt(0)
        } else {
            0
        }
        cursor.close()
        return streak
    }
    
    // ==================== CURSOR CONVERSION METHODS ====================
    
    private fun cursorToStudent(cursor: Cursor): Student {
        return Student(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
            name = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)),
            className = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLASS_NAME)),
            rollNumber = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROLL_NUMBER)),
            email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
            phone = cursor.getString(cursor.getColumnIndexOrThrow(COL_PHONE)),
            dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE_OF_BIRTH)),
            parentName = cursor.getString(cursor.getColumnIndexOrThrow(COL_PARENT_NAME)),
            address = cursor.getString(cursor.getColumnIndexOrThrow(COL_ADDRESS)),
            createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT)),
            lastLoginAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_LAST_LOGIN_AT)),
            isActive = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_ACTIVE)) == 1
        )
    }
    
    private fun cursorToGameProgress(cursor: Cursor): GameProgress {
        return GameProgress(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_GAME_ID)),
            studentId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_GAME_STUDENT_ID)),
            gameName = cursor.getString(cursor.getColumnIndexOrThrow(COL_GAME_NAME)),
            subject = cursor.getString(cursor.getColumnIndexOrThrow(COL_SUBJECT)),
            difficulty = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIFFICULTY)),
            score = cursor.getInt(cursor.getColumnIndexOrThrow(COL_SCORE)),
            maxScore = cursor.getInt(cursor.getColumnIndexOrThrow(COL_MAX_SCORE)),
            starsEarned = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STARS_EARNED)),
            isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COL_IS_COMPLETED)) == 1,
            timeSpent = cursor.getLong(cursor.getColumnIndexOrThrow(COL_TIME_SPENT)),
            attempts = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ATTEMPTS)),
            lastPlayedAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_LAST_PLAYED_AT)),
            createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_GAME_CREATED_AT))
        )
    }
    
    // ==================== UTILITY METHODS ====================
    
    fun clearAllData(): Boolean {
        val db = writableDatabase
        try {
            db.delete(TABLE_GAME_PROGRESS, null, null)
            db.delete(TABLE_STUDENTS, null, null)
            return true
        } catch (e: Exception) {
            return false
        }
    }
    
    fun getDatabaseSize(): Long {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT page_count * page_size as size FROM pragma_page_count(), pragma_page_size()", null)
        val size = if (cursor.moveToFirst()) {
            cursor.getLong(0)
        } else {
            0L
        }
        cursor.close()
        return size
    }
}
