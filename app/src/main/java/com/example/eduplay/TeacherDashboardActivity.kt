package com.example.eduplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eduplay.ui.theme.EduPlayTheme

class TeacherDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                TeacherDashboardScreen(
                    onStudentClick = { studentId ->
                        // Navigate to detailed student progress
                    },
                    onAIAnalysis = {
                        // Show AI analysis dialog
                    },
                    onLogout = {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

data class Student(
    val id: String,
    val name: String,
    val classGrade: String,
    val overallProgress: Int,
    val lastActive: String,
    val performance: String,
    val subjects: List<SubjectProgress>
)

data class SubjectProgress(
    val subject: String,
    val progress: Int,
    val gamesCompleted: Int,
    val averageScore: Int
)

data class AIInsight(
    val title: String,
    val description: String,
    val recommendation: String,
    val priority: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    onStudentClick: (String) -> Unit,
    onAIAnalysis: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val students = listOf(
        Student(
            "1", "Rahul Kumar", "Class 3", 78, "2 hours ago", "Good",
            listOf(
                SubjectProgress("Mathematics", 85, 8, 88),
                SubjectProgress("Odia Language", 70, 6, 75),
                SubjectProgress("English", 80, 7, 82)
            )
        ),
        Student(
            "2", "Priya Das", "Class 3", 65, "1 day ago", "Average",
            listOf(
                SubjectProgress("Mathematics", 60, 5, 65),
                SubjectProgress("Odia Language", 70, 6, 70),
                SubjectProgress("English", 65, 5, 68)
            )
        ),
        Student(
            "3", "Arjun Patra", "Class 3", 92, "30 min ago", "Excellent",
            listOf(
                SubjectProgress("Mathematics", 95, 10, 95),
                SubjectProgress("Odia Language", 90, 9, 92),
                SubjectProgress("English", 90, 9, 90)
            )
        )
    )

    val aiInsights = listOf(
        AIInsight(
            "Learning Pattern Analysis",
            "Students show 23% improvement in mathematics when games are played in sequence",
            "Consider implementing progressive difficulty levels",
            "High"
        ),
        AIInsight(
            "Engagement Optimization",
            "Odia language games have 15% lower completion rates",
            "Add more visual elements and audio support",
            "Medium"
        ),
        AIInsight(
            "Performance Prediction",
            "Rahul Kumar is likely to excel in advanced mathematics",
            "Recommend advanced level games for this student",
            "Low"
        )
    )

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "👨‍🏫 Teacher Dashboard",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab Row
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Students") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("AI Insights") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Analytics") }
                )
            }

            when (selectedTab) {
                0 -> StudentsTab(students, onStudentClick)
                1 -> AIInsightsTab(aiInsights, onAIAnalysis)
                2 -> AnalyticsTab()
            }
        }
    }
}

@Composable
fun StudentsTab(
    students: List<Student>,
    onStudentClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "📊 Student Progress Overview",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(students) { student ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onStudentClick(student.id) },
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = student.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = student.classGrade,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "${student.overallProgress}%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    student.overallProgress >= 80 -> Color(0xFF4CAF50)
                                    student.overallProgress >= 60 -> Color(0xFFFF9800)
                                    else -> Color(0xFFF44336)
                                }
                            )
                            Text(
                                text = student.performance,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Subject Progress
                    student.subjects.forEach { subject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = subject.subject,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "${subject.progress}%",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        LinearProgressIndicator(
                            progress = subject.progress / 100f,
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "Last active: ${student.lastActive}",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AIInsightsTab(
    insights: List<AIInsight>,
    onAIAnalysis: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🤖 AI-Powered Insights",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Button(
                    onClick = onAIAnalysis,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Generate New Analysis")
                }
            }
        }

        items(insights) { insight ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (insight.priority) {
                        "High" -> Color(0xFFFFEBEE)
                        "Medium" -> Color(0xFFFFF3E0)
                        else -> Color(0xFFE8F5E8)
                    }
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = insight.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = insight.priority,
                            fontSize = 10.sp,
                            color = when (insight.priority) {
                                "High" -> Color(0xFFD32F2F)
                                "Medium" -> Color(0xFFFF9800)
                                else -> Color(0xFF4CAF50)
                            },
                            modifier = Modifier
                                .background(
                                    when (insight.priority) {
                                        "High" -> Color(0xFFFFCDD2)
                                        "Medium" -> Color(0xFFFFE0B2)
                                        else -> Color(0xFFC8E6C9)
                                    }
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    
                    Text(
                        text = insight.description,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    Text(
                        text = "💡 Recommendation: ${insight.recommendation}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticsTab() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "📈 Class Analytics",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Overall Class Performance",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "78%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Average Progress",
                                fontSize = 12.sp
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "24",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Games Completed",
                                fontSize = 12.sp
                            )
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "85%",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Average Score",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Subject-wise Performance",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    listOf("Mathematics", "Odia Language", "English").forEach { subject ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = subject,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${(70..95).random()}%",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherDashboardScreenPreview() {
    EduPlayTheme {
        TeacherDashboardScreen(
            onStudentClick = { },
            onAIAnalysis = { },
            onLogout = { }
        )
    }
}
