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

class EnglishGamesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                EnglishGamesScreen(
                    onGameClick = { gameId ->
                        when (gameId) {
                            "alphabet" -> startActivity(Intent(this, EnglishAlphabetGameActivity::class.java))
                            "alphabet-song" -> startActivity(Intent(this, EnglishAlphabetSongGameActivity::class.java))
                            "train-engine" -> startActivity(Intent(this, EnglishTrainEngineGameActivity::class.java))
                            "words" -> startActivity(Intent(this, EnglishWordsGameActivity::class.java))
                            "phonics" -> startActivity(Intent(this, EnglishPhonicsGameActivity::class.java))
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

data class EnglishGame(
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
fun EnglishGamesScreen(
    onGameClick: (String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val englishGames = listOf(
        EnglishGame(
            "alphabet", "English Alphabet", "🔤", 
            "Learn A-Z letters and sounds", "Easy", true, 3,
            Color(0xFF4CAF50)
        ),
        EnglishGame(
            "alphabet-song", "Alphabet Song", "🎵", 
            "Sing along with the alphabet", "Easy", true, 2,
            Color(0xFFFF5722)
        ),
        EnglishGame(
            "train-engine", "Train Engine Game", "🚂", 
            "Build a train with letter cars", "Easy", true, 3,
            Color(0xFF2196F3)
        ),
        EnglishGame(
            "words", "English Words", "📝", 
            "Build vocabulary with English words", "Medium", true, 2,
            Color(0xFF2196F3)
        ),
        EnglishGame(
            "phonics", "Phonics Fun", "🔊", 
            "Learn letter sounds and blending", "Medium", false, 0,
            Color(0xFFFF9800)
        ),
        EnglishGame(
            "reading", "Reading Practice", "📖", 
            "Read simple English sentences", "Hard", false, 0,
            Color(0xFF9C27B0)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "🔤 English Games",
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
                            text = "🔤 English Learning Adventure",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Master English through fun and interactive games!",
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
                                text = "80%",
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

            items(englishGames) { game ->
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
                            text = "• Practice alphabet sounds daily\n• Use phonics to learn new words\n• Read simple sentences regularly\n• Listen to English audio for pronunciation",
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
fun EnglishGamesScreenPreview() {
    EduPlayTheme {
        EnglishGamesScreen(
            onGameClick = { },
            onBack = { }
        )
    }
}
