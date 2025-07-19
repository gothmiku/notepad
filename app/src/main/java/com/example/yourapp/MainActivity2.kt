package com.example.yourapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.example.yourapp.ui.theme.NotepadTheme
import java.io.*
import kotlin.text.*

import androidx.room.*

import com.example.database.ViewModel
import com.example.database.Note

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NotepadTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

    Box(modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding().background(color = MaterialTheme.colorScheme.background), contentAlignment = Alignment.BottomEnd) {
        AnimatedContent(
            label = "addButton",
            targetState = expanded,
            transitionSpec = { fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                    scaleIn(initialScale = 0.0f,
                        transformOrigin = TransformOrigin(0.0f,0.0f)) togetherWith
                    fadeOut(animationSpec = spring(stiffness = Spring.StiffnessHigh)) +
                    scaleOut() },
        ) { targetExpanded ->
            run {
                BackHandler(enabled=expanded){
                    expanded=false
                }
                if (targetExpanded) {
                        textBox(null)
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
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        ),
        value = text,
        onValueChange = { newText ->
            text = newText.replace("\n", " ")
                        },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun contentTextField(initialText : String){
    var text by remember { mutableStateOf(initialText) }
    TextField(
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
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        ),
        value = text,
        onValueChange = {
            newText -> text = newText
        },
        modifier = Modifier.fillMaxSize())
}


@Composable
fun textBox(file: File?) {

    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0 // I wanted to use WindowInsets.isImeVisible but it is
    // experimental so the builder didn't let me use it unless i use @OptIn(ExperimentalComposeUiApi::class)

    //val titleWeight = if (imeVisible) 2.0f else 1.0f
    //val contentWeight = if (imeVisible) 6.0f else 8.0f
    //val barWeight = if (imeVisible) 2.0f else 1.0f

    //val animatedTitleWeight by animateFloatAsState(targetValue = titleWeight)
    //val animatedContentWeight by animateFloatAsState(targetValue = contentWeight)
    //val animatedBarWeight by animateFloatAsState(targetValue = barWeight)

    val animatedTitleSpacing by animateDpAsState(targetValue = if (imeVisible) 3.dp else 12.dp)
    val animatedBarSpacing by animateDpAsState(targetValue = if (imeVisible) 3.dp else 8.dp)


    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .fillMaxSize()
    ) {
        // Title (1/10 height)
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(animatedTitleSpacing)
            ,
            contentAlignment = Alignment.CenterStart
        ) {
            titleTextField("Tarçın ile en iyi arkadaşları")
            //Text("Tarçın ile en iyi arkadaşları", fontStyle = MaterialTheme.typography.titleLarge.fontStyle, fontSize = MaterialTheme.typography.titleLarge.fontSize,fontWeight = FontWeight.ExtraBold , color = MaterialTheme.colorScheme.onTertiaryContainer)

        }


        // Content (7/10 height)
        Box(
            modifier = Modifier
                .weight(8.0f)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.Start),
            contentAlignment = Alignment.TopStart,
        ) {
            contentTextField("")
        }

        // Buttons Row (1/10 height)
        Row(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                .padding(animatedBarSpacing),
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
            if(file != null){
                ElevatedButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text("Delete", fontWeight = FontWeight.ExtraBold)
                }
            }
            else{
                ElevatedButton(
                    onClick = { /*TODO*/ },
                    enabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text("Delete", fontWeight = FontWeight.ExtraBold)
                }
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