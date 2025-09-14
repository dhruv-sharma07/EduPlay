package com.example.eduplay.webview

import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.example.eduplay.models.GameProgress
import com.example.eduplay.models.Student
import com.example.eduplay.models.StudentStats
import com.example.eduplay.models.SubjectProgress
import com.example.eduplay.repository.StudentRepository
import org.json.JSONArray
import org.json.JSONObject

class WebViewDataBridge(private val context: Context, private val webView: WebView) {
    private val repository = StudentRepository(context)
    
    // ==================== JAVASCRIPT INTERFACE METHODS ====================
    
    @JavascriptInterface
    fun getAllStudents(): String {
        val students = repository.getAllStudents()
        return convertStudentsToJson(students)
    }
    
    @JavascriptInterface
    fun getStudent(rollNumber: String): String {
        val student = repository.getStudent(rollNumber)
        return if (student != null) {
            convertStudentToJson(student)
        } else {
            "null"
        }
    }
    
    @JavascriptInterface
    fun getStudentById(studentId: Long): String {
        val student = repository.getStudentById(studentId)
        return if (student != null) {
            convertStudentToJson(student)
        } else {
            "null"
        }
    }
    
    @JavascriptInterface
    fun getStudentGameProgress(studentId: Long): String {
        val gameProgressList = repository.getAllGameProgress(studentId)
        return convertGameProgressListToJson(gameProgressList)
    }
    
    @JavascriptInterface
    fun getStudentSubjectProgress(studentId: Long): String {
        val subjectProgressList = repository.getSubjectProgress(studentId)
        return convertSubjectProgressListToJson(subjectProgressList)
    }
    
    @JavascriptInterface
    fun getStudentStats(studentId: Long): String {
        val stats = repository.getStudentStats(studentId)
        return if (stats != null) {
            convertStudentStatsToJson(stats)
        } else {
            "null"
        }
    }
    
    @JavascriptInterface
    fun saveGameProgress(
        studentId: Long,
        gameName: String,
        subject: String,
        difficulty: String,
        score: Int,
        maxScore: Int,
        starsEarned: Int,
        isCompleted: Boolean,
        timeSpent: Long,
        attempts: Int
    ): String {
        val gameProgress = GameProgress(
            studentId = studentId,
            gameName = gameName,
            subject = subject,
            difficulty = difficulty,
            score = score,
            maxScore = maxScore,
            starsEarned = starsEarned,
            isCompleted = isCompleted,
            timeSpent = timeSpent,
            attempts = attempts
        )
        
        val result = repository.saveGameProgress(gameProgress)
        return if (result > 0) {
            "{\"success\": true, \"id\": $result}"
        } else {
            "{\"success\": false, \"error\": \"Failed to save game progress\"}"
        }
    }
    
    @JavascriptInterface
    fun createStudent(
        name: String,
        className: String,
        rollNumber: String,
        email: String?,
        phone: String?,
        parentName: String?,
        address: String?
    ): String {
        val student = Student(
            name = name,
            className = className,
            rollNumber = rollNumber,
            email = email,
            phone = phone,
            parentName = parentName,
            address = address
        )
        
        val result = repository.createStudent(student)
        return if (result > 0) {
            "{\"success\": true, \"id\": $result}"
        } else {
            "{\"success\": false, \"error\": \"Failed to create student\"}"
        }
    }
    
    @JavascriptInterface
    fun resetStudentProgress(studentId: Long): String {
        val result = repository.resetStudentProgress(studentId)
        return if (result) {
            "{\"success\": true}"
        } else {
            "{\"success\": false, \"error\": \"Failed to reset progress\"}"
        }
    }
    
    @JavascriptInterface
    fun getDatabaseInfo(): String {
        val size = repository.getDatabaseSize()
        val students = repository.getAllStudents()
        
        return JSONObject().apply {
            put("databaseSize", size)
            put("totalStudents", students.size)
            put("totalGameProgress", students.sumOf { 
                repository.getAllGameProgress(it.id).size 
            })
        }.toString()
    }
    
    // ==================== JSON CONVERSION METHODS ====================
    
    private fun convertStudentsToJson(students: List<Student>): String {
        val jsonArray = JSONArray()
        students.forEach { student ->
            jsonArray.put(convertStudentToJsonObject(student))
        }
        return jsonArray.toString()
    }
    
    private fun convertStudentToJson(student: Student): String {
        return convertStudentToJsonObject(student).toString()
    }
    
    private fun convertStudentToJsonObject(student: Student): JSONObject {
        return JSONObject().apply {
            put("id", student.id)
            put("name", student.name)
            put("className", student.className)
            put("rollNumber", student.rollNumber)
            put("email", student.email ?: "")
            put("phone", student.phone ?: "")
            put("dateOfBirth", student.dateOfBirth ?: "")
            put("parentName", student.parentName ?: "")
            put("address", student.address ?: "")
            put("createdAt", student.createdAt)
            put("lastLoginAt", student.lastLoginAt)
            put("isActive", student.isActive)
        }
    }
    
    private fun convertGameProgressListToJson(gameProgressList: List<GameProgress>): String {
        val jsonArray = JSONArray()
        gameProgressList.forEach { progress ->
            jsonArray.put(convertGameProgressToJsonObject(progress))
        }
        return jsonArray.toString()
    }
    
    private fun convertGameProgressToJsonObject(gameProgress: GameProgress): JSONObject {
        return JSONObject().apply {
            put("id", gameProgress.id)
            put("studentId", gameProgress.studentId)
            put("gameName", gameProgress.gameName)
            put("subject", gameProgress.subject)
            put("difficulty", gameProgress.difficulty)
            put("score", gameProgress.score)
            put("maxScore", gameProgress.maxScore)
            put("starsEarned", gameProgress.starsEarned)
            put("isCompleted", gameProgress.isCompleted)
            put("timeSpent", gameProgress.timeSpent)
            put("attempts", gameProgress.attempts)
            put("lastPlayedAt", gameProgress.lastPlayedAt)
            put("createdAt", gameProgress.createdAt)
        }
    }
    
    private fun convertSubjectProgressListToJson(subjectProgressList: List<SubjectProgress>): String {
        val jsonArray = JSONArray()
        subjectProgressList.forEach { progress ->
            jsonArray.put(convertSubjectProgressToJsonObject(progress))
        }
        return jsonArray.toString()
    }
    
    private fun convertSubjectProgressToJsonObject(subjectProgress: SubjectProgress): JSONObject {
        return JSONObject().apply {
            put("subject", subjectProgress.subject)
            put("totalGames", subjectProgress.totalGames)
            put("completedGames", subjectProgress.completedGames)
            put("totalScore", subjectProgress.totalScore)
            put("averageScore", subjectProgress.averageScore)
            put("totalStars", subjectProgress.totalStars)
            put("lastPlayedAt", subjectProgress.lastPlayedAt)
            put("completionPercentage", 
                if (subjectProgress.totalGames > 0) {
                    (subjectProgress.completedGames.toDouble() / subjectProgress.totalGames * 100)
                } else {
                    0.0
                }
            )
        }
    }
    
    private fun convertStudentStatsToJson(stats: StudentStats): String {
        return JSONObject().apply {
            put("studentId", stats.studentId)
            put("totalGamesPlayed", stats.totalGamesPlayed)
            put("totalScore", stats.totalScore)
            put("averageScore", stats.averageScore)
            put("totalStars", stats.totalStars)
            put("favoriteSubject", stats.favoriteSubject)
            put("lastActivityAt", stats.lastActivityAt)
            put("streak", stats.streak)
        }.toString()
    }
    
    // ==================== WEBVIEW INTEGRATION METHODS ====================
    
    fun injectJavaScriptInterface() {
        webView.addJavascriptInterface(this, "AndroidBridge")
    }
    
    fun loadStudentData(studentId: Long) {
        val student = repository.getStudentById(studentId)
        val gameProgress = repository.getAllGameProgress(studentId)
        val subjectProgress = repository.getSubjectProgress(studentId)
        val stats = repository.getStudentStats(studentId)
        
        val data = JSONObject().apply {
            put("student", if (student != null) convertStudentToJsonObject(student) else JSONObject.NULL)
            put("gameProgress", convertGameProgressListToJson(gameProgress))
            put("subjectProgress", convertSubjectProgressListToJson(subjectProgress))
            put("stats", if (stats != null) convertStudentStatsToJsonObject(stats) else JSONObject.NULL)
        }
        
        val script = """
            if (typeof window.onStudentDataLoaded === 'function') {
                window.onStudentDataLoaded($data);
            }
        """.trimIndent()
        
        webView.post {
            webView.evaluateJavascript(script, null)
        }
    }
    
    private fun convertStudentStatsToJsonObject(stats: StudentStats): JSONObject {
        return JSONObject().apply {
            put("studentId", stats.studentId)
            put("totalGamesPlayed", stats.totalGamesPlayed)
            put("totalScore", stats.totalScore)
            put("averageScore", stats.averageScore)
            put("totalStars", stats.totalStars)
            put("favoriteSubject", stats.favoriteSubject)
            put("lastActivityAt", stats.lastActivityAt)
            put("streak", stats.streak)
        }
    }
}
