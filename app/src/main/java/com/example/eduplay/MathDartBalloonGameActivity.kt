package com.example.eduplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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

class MathDartBalloonGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                DartBalloonGameScreen(
                    onBack = {
                        startActivity(Intent(this, MathGamesActivity::class.java))
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

data class Balloon(
    val id: Int,
    val value: Int,
    val color: Color,
    val isPopped: Boolean = false,
    val isCorrect: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DartBalloonGameScreen(
    onBack: () -> Unit,
    onComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var score by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(1) }
    var gameState by remember { mutableStateOf("playing") }
    var correctAnswers by remember { mutableStateOf(0) }
    var currentQuestion by remember { mutableStateOf(0) }
    var targetNumber by remember { mutableStateOf(0) }
    var balloons by remember { mutableStateOf(listOf<Balloon>()) }
    var showFeedback by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }
    
    val targetAnswers = 5
    val balloonColors = listOf(
        Color(0xFFFF5722), Color(0xFF2196F3), Color(0xFF4CAF50),
        Color(0xFF9C27B0), Color(0xFFE91E63), Color(0xFFFFC107),
        Color(0xFF795548), Color(0xFF607D8B)
    )
    
    // Initialize game
    LaunchedEffect(currentQuestion) {
        if (currentQuestion < targetAnswers) {
            targetNumber = Random.nextInt(5, 21) // Random number between 5-20
            val correctBalloon = Random.nextInt(0, 6)
            balloons = (0..5).map { index ->
                val value = if (index == correctBalloon) {
                    targetNumber
                } else {
                    val wrongValue = Random.nextInt(1, 21)
                    if (wrongValue == targetNumber) wrongValue + 1 else wrongValue
                }
                Balloon(
                    id = index,
                    value = value,
                    color = balloonColors[index % balloonColors.size],
                    isCorrect = index == correctBalloon
                )
            }
        }
    }
    
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
                        text = "🎯 Dart Balloon Game",
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
                    containerColor = Color(0xFFFFF3E0)
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
                            Color(0xFFE3F2FD),
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
                                containerColor = Color(0xFFFFF8E1)
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
                                        text = "Hit: $correctAnswers/$targetAnswers",
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
                                    text = "🎯 Throw a dart at the balloon with number:",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0277BD),
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Target Number Display
                                Card(
                                    modifier = Modifier.size(80.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFFF5722)
                                    ),
                                    shape = CircleShape,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = targetNumber.toString(),
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        // Balloons Grid
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
                                    text = "🎈 Click on the correct balloon!",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7B1FA2),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                // Balloons in 2 rows
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // First row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        balloons.take(3).forEach { balloon ->
                                            BalloonCard(
                                                balloon = balloon,
                                                onClick = {
                                                    if (!showFeedback) {
                                                        isCorrect = balloon.isCorrect
                                                        showFeedback = true
                                                        
                                                        if (balloon.isCorrect) {
                                                            score += 30
                                                            correctAnswers++
                                                            feedbackMessage = "🎉 POP! Correct!"
                                                        } else {
                                                            score = maxOf(0, score - 5)
                                                            feedbackMessage = "💥 Wrong balloon! Try again!"
                                                        }
                                                    }
                                                }
                                            )
                                        }
                                    }
                                    
                                    // Second row
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceEvenly
                                    ) {
                                        balloons.drop(3).forEach { balloon ->
                                            BalloonCard(
                                                balloon = balloon,
                                                onClick = {
                                                    if (!showFeedback) {
                                                        isCorrect = balloon.isCorrect
                                                        showFeedback = true
                                                        
                                                        if (balloon.isCorrect) {
                                                            score += 30
                                                            correctAnswers++
                                                            feedbackMessage = "🎉 POP! Correct!"
                                                        } else {
                                                            score = maxOf(0, score - 5)
                                                            feedbackMessage = "💥 Wrong balloon! Try again!"
                                                        }
                                                    }
                                                }
                                            )
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
                                    containerColor = if (isCorrect) 
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
                                        text = feedbackMessage,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCorrect) 
                                            Color(0xFF2E7D32) 
                                        else Color(0xFFD32F2F),
                                        textAlign = TextAlign.Center
                                    )
                                    
                                    if (isCorrect) {
                                        Text(
                                            text = "The balloon with $targetNumber popped! 🎈💥",
                                            fontSize = 16.sp,
                                            color = Color(0xFF2E7D32),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "Look for the balloon with $targetNumber",
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
                                                showFeedback = false
                                                isCorrect = false
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
                                            if (currentQuestion < targetAnswers - 1) "Next Balloon" else "Finish",
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
                                text = "🎉 Balloon Master!",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            
                            Text(
                                text = "You popped all the right balloons!",
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
                                        showFeedback = false
                                        isCorrect = false
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

@Composable
fun BalloonCard(
    balloon: Balloon,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .size(80.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (balloon.isPopped) 
                Color(0xFFE0E0E0) 
            else balloon.color
        ),
        shape = CircleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (balloon.isPopped) 2.dp else 6.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (balloon.isPopped) {
                Text(
                    text = "💥",
                    fontSize = 24.sp
                )
            } else {
                Text(
                    text = balloon.value.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DartBalloonGameScreenPreview() {
    EduPlayTheme {
        DartBalloonGameScreen(
            onBack = { },
            onComplete = { }
        )
    }
}
