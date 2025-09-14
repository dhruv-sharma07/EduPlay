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

class OdiaLetterRecognitionGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                OdiaLetterRecognitionGameScreen(
                    onBack = {
                        startActivity(Intent(this, OdiaGamesActivity::class.java))
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

data class OdiaLetter(
    val letter: String,
    val word: String,
    val emoji: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OdiaLetterRecognitionGameScreen(
    onBack: () -> Unit,
    onComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var score by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(1) }
    var gameState by remember { mutableStateOf("playing") }
    var correctAnswers by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var currentQuestion by remember { mutableStateOf(0) }
    var showFeedback by remember { mutableStateOf(false) }
    
    val odiaLetters = listOf(
        OdiaLetter("ଅ", "ଅମ୍ବୁ", "🥭", Color(0xFFFF5722)),
        OdiaLetter("ଆ", "ଆମ୍ବ", "🥭", Color(0xFF2196F3)),
        OdiaLetter("ଇ", "ଇଚ୍ଛା", "💭", Color(0xFF4CAF50)),
        OdiaLetter("ଈ", "ଈଶ୍ୱର", "🙏", Color(0xFF9C27B0)),
        OdiaLetter("ଉ", "ଉତ୍ତର", "🧭", Color(0xFFE91E63)),
        OdiaLetter("ଊ", "ଊର୍ଣ୍ଣା", "🕸️", Color(0xFFFFC107))
    )
    
    val targetAnswers = 6
    val shuffledLetters = odiaLetters.shuffled()
    val currentLetter = shuffledLetters[currentQuestion]
    val wrongOptions = shuffledLetters.filter { it.letter != currentLetter.letter }.shuffled().take(3)
    val allOptions = (listOf(currentLetter) + wrongOptions).shuffled()
    
    LaunchedEffect(correctAnswers) {
        if (correctAnswers >= targetAnswers) {
            gameState = "completed"
            onComplete(score)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "ଅ Odia Letter Recognition",
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
                    containerColor = Color(0xFFE8F5E8)
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
                            Color(0xFFE8F5E8),
                            Color(0xFFF3E5F5),
                            Color(0xFFE1F5FE)
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
                                        text = "Correct: $correctAnswers/$targetAnswers",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    LinearProgressIndicator(
                                        progress = correctAnswers.toFloat() / targetAnswers,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }

                        // Question Card
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
                                    text = "Question ${currentQuestion + 1} of $targetAnswers",
                                    fontSize = 16.sp,
                                    color = Color(0xFF0277BD),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                Text(
                                    text = "Which letter matches this word?",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0277BD),
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Word Display
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFFF8E1)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(20.dp),
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
                                            color = Color(0xFFE65100)
                                        )
                                    }
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
                                    text = "Choose the correct letter:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7B1FA2),
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
                                                        selectedAnswer = option.letter
                                                        showFeedback = true
                                                        
                                                        if (option.letter == currentLetter.letter) {
                                                            score += 20
                                                            correctAnswers++
                                                        } else {
                                                            score = maxOf(0, score - 5)
                                                        }
                                                    }
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = when {
                                                    showFeedback && option.letter == currentLetter.letter -> Color(0xFFC8E6C9)
                                                    showFeedback && option.letter == selectedAnswer && option.letter != currentLetter.letter -> Color(0xFFFFCDD2)
                                                    selectedAnswer == option.letter -> Color(0xFFFFCDD2)
                                                    else -> Color(0xFFFFF8E1)
                                                }
                                            ),
                                            shape = RoundedCornerShape(16.dp),
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = if (selectedAnswer == option.letter) 8.dp else 4.dp
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
                                    containerColor = if (selectedAnswer == currentLetter.letter) 
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
                                        text = if (selectedAnswer == currentLetter.letter) "✅ Correct!" else "❌ Try Again!",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selectedAnswer == currentLetter.letter) 
                                            Color(0xFF2E7D32) 
                                        else Color(0xFFD32F2F)
                                    )
                                    
                                    if (selectedAnswer == currentLetter.letter) {
                                        Text(
                                            text = "The letter '${currentLetter.letter}' matches '${currentLetter.word}'!",
                                            fontSize = 16.sp,
                                            color = Color(0xFF2E7D32),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "The correct answer is '${currentLetter.letter}' for '${currentLetter.word}'",
                                            fontSize = 16.sp,
                                            color = Color(0xFFD32F2F),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    }
                                    
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    Button(
                                        onClick = {
                                            if (currentQuestion < targetAnswers - 1) {
                                                currentQuestion++
                                                selectedAnswer = null
                                                showFeedback = false
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
                                            if (currentQuestion < targetAnswers - 1) "Next Question" else "Finish",
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
                                text = "🎉 ଅତ୍ୟୁତ୍ତମ! (Excellent!)",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            
                            Text(
                                text = "You're an Odia Letter Master!",
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
                                        correctAnswers = 0
                                        currentQuestion = 0
                                        selectedAnswer = null
                                        showFeedback = false
                                        gameState = "playing"
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50)
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        "Play Again",
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
fun OdiaLetterRecognitionGameScreenPreview() {
    EduPlayTheme {
        OdiaLetterRecognitionGameScreen(
            onBack = { },
            onComplete = { }
        )
    }
}
