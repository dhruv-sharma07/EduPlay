package com.example.eduplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
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

class EnglishTrainEngineGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                TrainEngineGameScreen(
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

data class TrainCar(
    val id: Int,
    val letter: String,
    val word: String,
    val emoji: String,
    val color: Color,
    val isAttached: Boolean = false,
    val isCorrect: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainEngineGameScreen(
    onBack: () -> Unit,
    onComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var score by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(1) }
    var gameState by remember { mutableStateOf("playing") }
    var correctCars by remember { mutableStateOf(0) }
    var currentQuestion by remember { mutableStateOf(0) }
    var targetLetter by remember { mutableStateOf("") }
    var trainCars by remember { mutableStateOf(listOf<TrainCar>()) }
    var attachedCars by remember { mutableStateOf(listOf<TrainCar>()) }
    var showFeedback by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf(false) }
    
    val targetAnswers = 5
    val trainCarData = listOf(
        TrainCar(1, "A", "Apple", "🍎", Color(0xFFFF5722)),
        TrainCar(2, "B", "Ball", "⚽", Color(0xFF2196F3)),
        TrainCar(3, "C", "Cat", "🐱", Color(0xFF4CAF50)),
        TrainCar(4, "D", "Dog", "🐶", Color(0xFF9C27B0)),
        TrainCar(5, "E", "Elephant", "🐘", Color(0xFFE91E63)),
        TrainCar(6, "F", "Fish", "🐠", Color(0xFFFFC107)),
        TrainCar(7, "G", "Guitar", "🎸", Color(0xFF795548)),
        TrainCar(8, "H", "House", "🏠", Color(0xFF607D8B))
    )
    
    // Initialize game
    LaunchedEffect(currentQuestion) {
        if (currentQuestion < targetAnswers) {
            val randomCar = trainCarData.random()
            targetLetter = randomCar.letter
            val correctCar = randomCar
            val wrongCars = trainCarData.filter { it.letter != targetLetter }.shuffled().take(3)
            trainCars = (listOf(correctCar) + wrongCars).shuffled()
            attachedCars = emptyList()
        }
    }
    
    LaunchedEffect(correctCars) {
        if (correctCars >= targetAnswers) {
            gameState = "completed"
            onComplete(score)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "🚂 Train Engine Game",
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
                                        text = "Cars: $correctCars/$targetAnswers",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    LinearProgressIndicator(
                                        progress = correctCars.toFloat() / targetAnswers,
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
                                    text = "🚂 Attach the car that starts with letter:",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0277BD),
                                    textAlign = TextAlign.Center
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Target Letter Display
                                Card(
                                    modifier = Modifier.size(80.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFF2196F3)
                                    ),
                                    shape = CircleShape,
                                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = targetLetter,
                                            fontSize = 32.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }

                        // Train Engine and Attached Cars
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
                                    text = "🚂 Your Train:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7B1FA2),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Train Engine
                                    item {
                                        TrainCarCard(
                                            car = TrainCar(0, "E", "Engine", "🚂", Color(0xFF424242)),
                                            isEngine = true
                                        )
                                    }
                                    
                                    // Attached Cars
                                    items(attachedCars) { car ->
                                        TrainCarCard(
                                            car = car,
                                            isEngine = false
                                        )
                                    }
                                }
                            }
                        }

                        // Available Cars to Attach
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E8)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "🎯 Click on the correct car to attach:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    trainCars.forEach { car ->
                                        TrainCarCard(
                                            car = car,
                                            isEngine = false,
                                            onClick = {
                                                if (!showFeedback) {
                                                    isCorrect = car.letter == targetLetter
                                                    showFeedback = true
                                                    
                                                    if (car.letter == targetLetter) {
                                                        score += 25
                                                        correctCars++
                                                        attachedCars = attachedCars + car
                                                        feedbackMessage = "🚂 Choo Choo! Car attached!"
                                                    } else {
                                                        score = maxOf(0, score - 5)
                                                        feedbackMessage = "🚫 Wrong car! Try again!"
                                                    }
                                                }
                                            }
                                        )
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
                                            text = "The ${trainCars.find { it.letter == targetLetter }?.word} car is now attached!",
                                            fontSize = 16.sp,
                                            color = Color(0xFF2E7D32),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(top = 8.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "Look for a car that starts with '$targetLetter'",
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
                                            if (currentQuestion < targetAnswers - 1) "Next Car" else "Finish",
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
                                text = "🚂 All Aboard!",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            
                            Text(
                                text = "Your train is ready to go!",
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
                                        correctCars = 0
                                        currentQuestion = 0
                                        attachedCars = emptyList()
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
                                        "Build Another Train",
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
fun TrainCarCard(
    car: TrainCar,
    isEngine: Boolean = false,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = Modifier
            .size(if (isEngine) 100.dp else 80.dp)
            .let { if (onClick != null) it.clickable { onClick() } else it },
        colors = CardDefaults.cardColors(
            containerColor = if (isEngine) car.color else car.color
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = car.emoji,
                fontSize = if (isEngine) 32.sp else 24.sp
            )
            Text(
                text = car.letter,
                fontSize = if (isEngine) 20.sp else 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            if (!isEngine) {
                Text(
                    text = car.word,
                    fontSize = 10.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrainEngineGameScreenPreview() {
    EduPlayTheme {
        TrainEngineGameScreen(
            onBack = { },
            onComplete = { }
        )
    }
}
