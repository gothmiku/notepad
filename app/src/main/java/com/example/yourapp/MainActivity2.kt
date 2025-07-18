package com.example.yourapp

import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.yourapp.ui.theme.NotepadTheme

import androidx.compose.material3.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

import java.io.*
import java.time.LocalDate
import kotlin.text.*

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotepadTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting3(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                    addButton()
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        color = androidx.compose.ui.graphics.Color.Black,
        fontSize = 30.sp,
        modifier = Modifier.background(color = androidx.compose.ui.graphics.Color.Red)
    )
}

@Composable
fun addButton() {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background), contentAlignment = Alignment.BottomEnd) {
        AnimatedContent(
            label = "addButton",
            targetState = expanded,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
        ) { targetExpanded ->
            run {
                BackHandler(enabled=expanded){
                    expanded=false
                }
                if (targetExpanded) {
                        val dateString = LocalDate.now().toString()
                        val newFile = File("〜/", "$dateString.txt")
                        textBox(newFile)
                } else {
                    Box(modifier=Modifier.size(100.dp)){
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(color=MaterialTheme.colorScheme.secondaryContainer, shape = CircleShape)
                                .clickable { expanded = true },
                            contentAlignment = Alignment.Center

                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Expand",
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }

                }
            }

        }
    }
}

@Composable
fun titleTextField(initialText : String){
    var text by remember { mutableStateOf(initialText) }
    TextField(
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            errorContainerColor = Color.Transparent,
            cursorColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledTextColor = Color.Transparent,
            disabledPlaceholderColor = Color.Transparent,
            disabledLeadingIconColor = Color.Transparent,
            disabledTrailingIconColor = Color.Transparent,
            errorCursorColor = Color.Transparent,
            errorLeadingIconColor = Color.Transparent,
            errorTrailingIconColor = Color.Transparent,
            errorPlaceholderColor = Color.Transparent,
            focusedLeadingIconColor = Color.Transparent,
            focusedTrailingIconColor = Color.Transparent,
            unfocusedLeadingIconColor = Color.Transparent,
            unfocusedTrailingIconColor = Color.Transparent,
            ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        ),
        value = text,
        onValueChange = { newText ->
            val replaced = newText.replace("\n", " ")
                        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun textBox(file: File) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        // Title (1/10 height)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .border(brush= Brush.horizontalGradient(listOf(MaterialTheme.colorScheme.secondaryContainer,MaterialTheme.colorScheme.secondary))
                ,width=4.dp , shape = RectangleShape)
                .padding(15.dp)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            titleTextField("Tarçın ile en iyi arkadaşları")
            //Text("Tarçın ile en iyi arkadaşları", fontStyle = MaterialTheme.typography.titleLarge.fontStyle, fontSize = MaterialTheme.typography.titleLarge.fontSize,fontWeight = FontWeight.ExtraBold , color = MaterialTheme.colorScheme.onTertiaryContainer)

        }


        // Content (7/10 height)
        Box(
            modifier = Modifier
                .weight(7f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Your content here
        }

        // Buttons Row (2/10 height)
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text("Save", fontWeight = FontWeight.ExtraBold)
            }
            ElevatedButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text("Delete", fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    NotepadTheme {
        addButton()
    }
}