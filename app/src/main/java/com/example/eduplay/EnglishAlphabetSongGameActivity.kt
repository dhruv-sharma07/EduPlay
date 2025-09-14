package com.example.eduplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eduplay.ui.theme.EduPlayTheme
import kotlin.random.Random

class EnglishAlphabetSongGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                EnglishAlphabetSongGameScreen(
                    onBack = {
                        startActivity(Intent(this, EnglishGamesActivity::class.java))
                        finish()
                    },
                    onComplete = { score ->
                        // Handle game completion
                    }
                )
            }
        }
    }
}

data class AlphabetLetter(
    val letter: String,
    val word: String,
    val emoji: String,
    val color: Color,
    val sound: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnglishAlphabetSongGameScreen(
    onBack: () -> Unit,
    onComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var score by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(1) }
    var gameState by remember { mutableStateOf("playing") }
    var currentLetterIndex by remember { mutableStateOf(0) }
    var selectedLetter by remember { mutableStateOf<String?>(null) }
    var showFeedback by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    
    val alphabetLetters = listOf(
        AlphabetLetter("A", "Apple", "🍎", Color(0xFFFF5722), "A for Apple"),
        AlphabetLetter("B", "Ball", "⚽", Color(0xFF2196F3), "B for Ball"),
        AlphabetLetter("C", "Cat", "🐱", Color(0xFF4CAF50), "C for Cat"),
        AlphabetLetter("D", "Dog", "🐶", Color(0xFF9C27B0), "D for Dog"),
        AlphabetLetter("E", "Elephant", "🐘", Color(0xFFE91E63), "E for Elephant"),
        AlphabetLetter("F", "Fish", "🐠", Color(0xFFFFC107), "F for Fish")
    )
    
    val targetLetters = 6
    val shuffledLetters = alphabetLetters.shuffled()
    val currentLetter = shuffledLetters[currentLetterIndex]
    val wrongOptions = shuffledLetters.filter { it.letter != currentLetter.letter }.shuffled().take(3)
    val allOptions = (listOf(currentLetter) + wrongOptions).shuffled()
    
    LaunchedEffect(currentLetterIndex) {
        if (currentLetterIndex >= targetLetters) {
            gameState = "completed"
            onComplete(score)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "🔤 Alphabet Song Game",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("← Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE1F5FE)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE1F5FE),
                            Color(0xFFF3E5F5),
                            Color(0xFFE8F5E8)
                        )
                    )
                )
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (gameState) {
                "playing" -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header with progress
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFF3E0)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Level $currentLevel",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE65100)
                                    )
                                    Text(
                                        text = "Score: $score",
                                        fontSize = 16.sp,
                                        color = Color(0xFFE65100)
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Letter ${currentLetterIndex + 1}/$targetLetters",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    LinearProgressIndicator(
                                        progress = (currentLetterIndex + 1).toFloat() / targetLetters,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }

                        // Main Letter Display
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE3F2FD)
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Sing along with the alphabet!",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0277BD),
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(20.dp))
                                
                                // Large Letter Display
                                Card(
                                    modifier = Modifier.size(120.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = currentLetter.color.copy(alpha = 0.2f)
                                    ),
                                    shape = CircleShape,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = currentLetter.letter,
                                            fontSize = 48.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = currentLetter.color
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Word and Emoji
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        text = currentLetter.emoji,
                                        fontSize = 32.sp
                                    )
                                    Text(
                                        text = currentLetter.word,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0277BD)
                                    )
                                }
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Play Button
                                Button(
                                    onClick = {
                                        isPlaying = !isPlaying
                                        // Here you would play the sound
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isPlaying) Color(0xFF4CAF50) else Color(0xFF2196F3)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = if (isPlaying) "🔊 Playing..." else "▶️ Play Sound",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Answer Options
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF3E5F5)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Which letter matches the sound?",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7B1FA2),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    allOptions.forEach { option ->
                                        Card(
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clickable {
                                                    if (!showFeedback) {
                                                        selectedLetter = option.letter
                                                        showFeedback = true
                                                        
                                                        if (option.letter == currentLetter.letter) {
                                                            score += 25
                                                        } else {
                                                            score = maxOf(0, score - 5)
                                                        }
                                                    }
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = when {
                                                    showFeedback && option.letter == currentLetter.letter -> Color(0xFFC8E6C9)
                                                    showFeedback && option.letter == selectedLetter && option.letter != currentLetter.letter -> Color(0xFFFFCDD2)
                                                    selectedLetter == option.letter -> Color(0xFFFFCDD2)
                                                    else -> Color(0xFFFFF8E1)
                                                }
                                            ),
                                            shape = RoundedCornerShape(16.dp),
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = if (selectedLetter == option.letter) 8.dp else 4.dp
                                            )
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = option.letter,
                                                    fontSize = 32.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = option.color
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Feedback
                        if (showFeedback) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedLetter == currentLetter.letter) 
                                        Color(0xFFE8F5E8) 
                                    else Color(0xFFFFEBEE)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if (selectedLetter == currentLetter.letter) "🎵 Perfect!" else "🎵 Try Again!",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedLetter == currentLetter.letter) 
                                            Color(0xFF2E7D32) 
                                        else Color(0xFFD32F2F)
                                    )
                                    
                                    Text(
                                        text = currentLetter.sound,
                                        fontSize = 16.sp,
                                        color = if (selectedLetter == currentLetter.letter) 
                                            Color(0xFF2E7D32) 
                                        else Color(0xFFD32F2F),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    Button(
                                        onClick = {
                                            if (currentLetterIndex < targetLetters - 1) {
                                                currentLetterIndex++
                                                selectedLetter = null
                                                showFeedback = false
                                                isPlaying = false
                                            } else {
                                                // Game completed
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF4CAF50)
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            if (currentLetterIndex < targetLetters - 1) "Next Letter" else "Finish",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                "completed" -> {
                    // Completion Screen
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE8F5E8)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎵 Bravo!",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            
                            Text(
                                text = "You're an Alphabet Song Star!",
                                fontSize = 20.sp,
                                color = Color(0xFF2E7D32),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                            
                            Text(
                                text = "Final Score: $score",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100)
                            )
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Button(
                                    onClick = {
                                        currentLevel = 1
                                        score = 0
                                        currentLetterIndex = 0
                                        selectedLetter = null
                                        showFeedback = false
                                        isPlaying = false
                                        gameState = "playing"
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Sing Again",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Button(
                                    onClick = onBack,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2196F3)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Back to Games",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EnglishAlphabetSongGameScreenPreview() {
    EduPlayTheme {
        EnglishAlphabetSongGameScreen(
            onBack = { },
            onComplete = { }
        )
    }
}
