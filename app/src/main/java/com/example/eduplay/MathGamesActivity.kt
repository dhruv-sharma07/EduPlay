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

class MathGamesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                MathGamesScreen(
                    onGameClick = { gameId ->
                        when (gameId) {
                            "counting" -> startActivity(Intent(this, CountingGameActivity::class.java))
                            "number-matching" -> startActivity(Intent(this, MathNumberMatchingGameActivity::class.java))
                            "shape-sorting" -> startActivity(Intent(this, MathShapeSortingGameActivity::class.java))
                            "dart-balloon" -> startActivity(Intent(this, MathDartBalloonGameActivity::class.java))
                            "addition" -> startActivity(Intent(this, AdditionGameActivity::class.java))
                            "subtraction" -> startActivity(Intent(this, SubtractionGameActivity::class.java))
                            "shapes" -> startActivity(Intent(this, ShapesGameActivity::class.java))
                        }
                    },
                    onBack = {
                        startActivity(Intent(this, StudentDashboardActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

data class MathGame(
    val id: String,
    val name: String,
    val emoji: String,
    val description: String,
    val difficulty: String,
    val completed: Boolean,
    val stars: Int,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathGamesScreen(
    onGameClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mathGames = listOf(
        MathGame(
            "counting", "Number Counting", "🔢", 
            "Learn to count from 1 to 20", "Easy", true, 3,
            Color(0xFF4CAF50)
        ),
        MathGame(
            "number-matching", "Number Matching", "🎯", 
            "Match numbers with objects", "Easy", true, 2,
            Color(0xFFFF5722)
        ),
        MathGame(
            "shape-sorting", "Shape Sorting", "🔺", 
            "Sort shapes into categories", "Medium", true, 3,
            Color(0xFF9C27B0)
        ),
        MathGame(
            "dart-balloon", "Dart Balloon Game", "🎈", 
            "Pop balloons with correct numbers", "Easy", true, 3,
            Color(0xFFE91E63)
        ),
        MathGame(
            "addition", "Addition Fun", "➕", 
            "Add numbers and solve puzzles", "Medium", false, 0,
            Color(0xFF2196F3)
        ),
        MathGame(
            "subtraction", "Subtraction Quest", "➖", 
            "Subtract numbers in fun scenarios", "Medium", false, 0,
            Color(0xFFFF9800)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "🔢 Mathematics Games",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🔢 Mathematics Adventure",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Master numbers, shapes, and calculations through fun games!",
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            // Progress Overview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "2/6",
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
                                text = "⭐⭐⭐",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                            Text(
                                text = "Total Stars",
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

            // Games List
            item {
                Text(
                    text = "🎮 Available Games",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(mathGames) { game ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGameClick(game.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (game.completed) 
                            MaterialTheme.colorScheme.primaryContainer 
                        else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = game.emoji,
                            fontSize = 40.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = game.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = game.description,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Difficulty: ${game.difficulty}",
                                    fontSize = 12.sp,
                                    color = when (game.difficulty) {
                                        "Easy" -> Color(0xFF4CAF50)
                                        "Medium" -> Color(0xFFFF9800)
                                        "Hard" -> Color(0xFFF44336)
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                                
                                if (game.completed) {
                                    Text(
                                        text = " • ⭐".repeat(game.stars),
                                        fontSize = 12.sp,
                                        color = Color(0xFFFFD700),
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                        
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            if (game.completed) {
                                Text(
                                    text = "✅",
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "Completed",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(
                                    text = "▶️",
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "Play",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Learning Tips
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "💡 Learning Tips",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "• Start with easy games to build confidence\n• Practice regularly for better results\n• Use visual aids to understand concepts\n• Ask for help when you need it",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MathGamesScreenPreview() {
    EduPlayTheme {
        MathGamesScreen(
            onGameClick = { },
            onBack = { }
        )
    }
}
