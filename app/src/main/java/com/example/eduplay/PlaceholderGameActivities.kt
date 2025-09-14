package com.example.eduplay

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eduplay.ui.theme.EduPlayTheme

// Placeholder activities for all game types
class AdditionGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "➕ Addition Game",
                    description = "Learn addition through fun puzzles!",
                    onBack = { startActivity(Intent(this, MathGamesActivity::class.java)) }
                )
            }
        }
    }
}

class SubtractionGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "➖ Subtraction Game",
                    description = "Master subtraction with interactive games!",
                    onBack = { startActivity(Intent(this, MathGamesActivity::class.java)) }
                )
            }
        }
    }
}

class ShapesGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "🔺 Shapes Game",
                    description = "Identify and learn about different shapes!",
                    onBack = { startActivity(Intent(this, MathGamesActivity::class.java)) }
                )
            }
        }
    }
}

class OdiaAlphabetGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "ଅ Odia Alphabet Game",
                    description = "Learn Odia letters and their sounds!",
                    onBack = { startActivity(Intent(this, OdiaGamesActivity::class.java)) }
                )
            }
        }
    }
}

class OdiaWordsGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "📝 Odia Words Game",
                    description = "Build your Odia vocabulary!",
                    onBack = { startActivity(Intent(this, OdiaGamesActivity::class.java)) }
                )
            }
        }
    }
}

class OdiaReadingGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "📖 Odia Reading Game",
                    description = "Practice reading Odia sentences!",
                    onBack = { startActivity(Intent(this, OdiaGamesActivity::class.java)) }
                )
            }
        }
    }
}

class EnglishAlphabetGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "🔤 English Alphabet Game",
                    description = "Learn English letters A-Z!",
                    onBack = { startActivity(Intent(this, EnglishGamesActivity::class.java)) }
                )
            }
        }
    }
}

class EnglishWordsGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "📝 English Words Game",
                    description = "Build your English vocabulary!",
                    onBack = { startActivity(Intent(this, EnglishGamesActivity::class.java)) }
                )
            }
        }
    }
}

class EnglishPhonicsGameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduPlayTheme {
                PlaceholderGameScreen(
                    title = "🔊 English Phonics Game",
                    description = "Learn letter sounds and blending!",
                    onBack = { startActivity(Intent(this, EnglishGamesActivity::class.java)) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceholderGameScreen(
    title: String,
    description: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = title,
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
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🚧",
                        fontSize = 64.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "Game Coming Soon!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    
                    Text(
                        text = "This game is currently under development. Check back soon for the full experience!",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Button(
                        onClick = onBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Back to Games")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceholderGameScreenPreview() {
    EduPlayTheme {
        PlaceholderGameScreen(
            title = "Sample Game",
            description = "This is a sample game description",
            onBack = { }
        )
    }
}
