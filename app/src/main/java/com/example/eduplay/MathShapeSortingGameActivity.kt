package com.example.eduplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
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

class MathShapeSortingGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                ShapeSortingGameScreen(
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

data class ShapeItem(
    val id: Int,
    val name: String,
    val emoji: String,
    val color: Color,
    val category: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeSortingGameScreen(
    onBack: () -> Unit,
    onComplete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var score by remember { mutableStateOf(0) }
    var currentLevel by remember { mutableStateOf(1) }
    var gameState by remember { mutableStateOf("playing") }
    var sortedShapes by remember { mutableStateOf(0) }
    var selectedShape by remember { mutableStateOf<ShapeItem?>(null) }
    
    val shapes = listOf(
        ShapeItem(1, "Circle", "⭕", Color(0xFFFF5722), "Round"),
        ShapeItem(2, "Square", "⬜", Color(0xFF2196F3), "Angular"),
        ShapeItem(3, "Triangle", "🔺", Color(0xFF4CAF50), "Angular"),
        ShapeItem(4, "Heart", "❤️", Color(0xFFE91E63), "Round"),
        ShapeItem(5, "Star", "⭐", Color(0xFFFFC107), "Angular"),
        ShapeItem(6, "Diamond", "💎", Color(0xFF9C27B0), "Angular")
    )
    
    val targetSorts = 6
    val shuffledShapes = shapes.shuffled()
    
    LaunchedEffect(sortedShapes) {
        if (sortedShapes >= targetSorts) {
            gameState = "completed"
            onComplete(score)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "🔺 Shape Sorting Game",
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
                                        text = "Sorted: $sortedShapes/$targetSorts",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    LinearProgressIndicator(
                                        progress = sortedShapes.toFloat() / targetSorts,
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
                                containerColor = Color(0xFFE3F2FD)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "🎯 Sort the shapes into Round and Angular categories!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0277BD),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        // Sorting Areas
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // Round Shapes Area
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(200.dp)
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFFEBEE)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Round Shapes",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .background(
                                                Color(0xFFFFF5F5),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                2.dp,
                                                if (selectedShape?.category == "Round") Color(0xFF4CAF50) else Color(0xFFFFCDD2),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                if (selectedShape?.category == "Round") {
                                                    score += 15
                                                    sortedShapes++
                                                    selectedShape = null
                                                } else if (selectedShape != null) {
                                                    score = maxOf(0, score - 3)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (selectedShape?.category == "Round") {
                                            Text(
                                                text = "✅ Correct!",
                                                fontSize = 14.sp,
                                                color = Color(0xFF2E7D32),
                                                fontWeight = FontWeight.Bold
                                            )
                                        } else {
                                            Text(
                                                text = "Drop Round\nShapes Here",
                                                fontSize = 12.sp,
                                                color = Color(0xFFD32F2F),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }

                            // Angular Shapes Area
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(200.dp)
                                    .padding(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E8)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Angular Shapes",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF2E7D32)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                            .background(
                                                Color(0xFFF1F8E9),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .border(
                                                2.dp,
                                                if (selectedShape?.category == "Angular") Color(0xFF4CAF50) else Color(0xFFC8E6C9),
                                                RoundedCornerShape(12.dp)
                                            )
                                            .clickable {
                                                if (selectedShape?.category == "Angular") {
                                                    score += 15
                                                    sortedShapes++
                                                    selectedShape = null
                                                } else if (selectedShape != null) {
                                                    score = maxOf(0, score - 3)
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (selectedShape?.category == "Angular") {
                                            Text(
                                                text = "✅ Correct!",
                                                fontSize = 14.sp,
                                                color = Color(0xFF2E7D32),
                                                fontWeight = FontWeight.Bold
                                            )
                                        } else {
                                            Text(
                                                text = "Drop Angular\nShapes Here",
                                                fontSize = 12.sp,
                                                color = Color(0xFF2E7D32),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Shapes to Sort
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
                                    text = "Click on a shape to select it, then click the correct category!",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF7B1FA2),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    shuffledShapes.take(6).forEach { shape ->
                                        Card(
                                            modifier = Modifier
                                                .size(70.dp)
                                                .clickable {
                                                    selectedShape = if (selectedShape?.id == shape.id) null else shape
                                                },
                                            colors = CardDefaults.cardColors(
                                                containerColor = if (selectedShape?.id == shape.id) 
                                                    Color(0xFFFFCDD2) 
                                                else Color(0xFFFFF8E1)
                                            ),
                                            shape = RoundedCornerShape(12.dp),
                                            elevation = CardDefaults.cardElevation(
                                                defaultElevation = if (selectedShape?.id == shape.id) 8.dp else 4.dp
                                            )
                                        ) {
                                            Box(
                                                modifier = Modifier.fillMaxSize(),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = shape.emoji,
                                                    fontSize = 28.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Feedback
                        if (selectedShape != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E8)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "✅ Selected: ${selectedShape!!.name} (${selectedShape!!.category}) - Now click the correct category above!",
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
                                text = "🎉 Excellent!",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                            
                            Text(
                                text = "You're a Shape Sorting Master!",
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
                                        sortedShapes = 0
                                        selectedShape = null
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
fun ShapeSortingGameScreenPreview() {
    EduPlayTheme {
        ShapeSortingGameScreen(
            onBack = { },
            onComplete = { }
        )
    }
}
