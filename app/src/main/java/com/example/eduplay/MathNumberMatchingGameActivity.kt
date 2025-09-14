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

class MathNumberMatchingGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                NumberMatchingGameScreen(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumberMatchingGameScreen(
    onBack: () -> Unit,
    onComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var score by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(1) }
    var gameState by remember { mutableStateOf("playing") }
    var selectedNumber by remember { mutableStateOf<Int?>(null) }
    var matchedPairs by remember { mutableStateOf(0) }
    
    val targetPairs = 5
    val numbers = (1..10).shuffled().take(6)
    val objects = listOf("🍎", "🍌", "🍊", "🍇", "🍓", "🥝", "🍑", "🍒", "🥭", "🍍")
    val selectedObjects = objects.take(6)
    
    LaunchedEffect(matchedPairs) {
        if (matchedPairs >= targetPairs) {
            gameState = "completed"
            onComplete(score)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "🔢 Number Matching Game",
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
                    containerColor = Color(0xFFE3F2FD)
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
                            Color(0xFFF3E5F5)
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
                                        text = "Matched: $matchedPairs/$targetPairs",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    LinearProgressIndicator(
                                        progress = matchedPairs.toFloat() / targetPairs,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp),
                                        color = Color(0xFF4CAF50)
                                    )
                                }
                            }
                        }

                        // Game Instructions
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE1F5FE)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "🎯 Match the number with the correct amount of objects!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0277BD),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        // Numbers Row
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFFFEBEE)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Numbers",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    numbers.forEach { number ->
                                        Card(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clickable {
                                                    selectedNumber = if (selectedNumber == number) null else number
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (selectedNumber == number) 
                                                    Color(0xFFFFCDD2) 
                                                else Color(0xFFFFF5F5)
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = if (selectedNumber == number) 8.dp else 4.dp
                                            )
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = number.toString(),
                                                    fontSize = 24.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFD32F2F)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Objects Row
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
                                    text = "Objects",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF7B1FA2),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    selectedObjects.forEachIndexed { index, objectEmoji ->
                                        Card(
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clickable {
                                                    if (selectedNumber != null) {
                                                        val correctNumber = index + 1
                                                        if (selectedNumber == correctNumber) {
                                                            score += 20
                                                            matchedPairs++
                                                            selectedNumber = null
                                                        } else {
                                                            score = maxOf(0, score - 5)
                                                        }
                                                    }
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFFFFF8E1)
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = objectEmoji,
                                                    fontSize = 24.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Feedback
                        if (selectedNumber != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E8)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "✅ Great! Now click on the matching objects above!",
                                    fontSize = 16.sp,
                                    color = Color(0xFF2E7D32),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
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
                                text = "🎉 Congratulations!",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            
                            Text(
                                text = "You completed the Number Matching Game!",
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
                                        matchedPairs = 0
                                        selectedNumber = null
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
fun NumberMatchingGameScreenPreview() {
    EduPlayTheme {
        NumberMatchingGameScreen(
            onBack = { },
            onComplete = { }
        )
    }
}
