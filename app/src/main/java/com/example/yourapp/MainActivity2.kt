package com.example.yourapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.database.Note
import com.example.viewmodels.GlobalViewModel
import com.example.viewmodels.NoteViewModel
import com.example.yourapp.ui.theme.NotepadTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val myViewModel: NoteViewModel = viewModel()
            val globalViewModel : GlobalViewModel by viewModels()

            NotepadTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { content ->
                    initialCompose()
                    if (myViewModel != null) {
                        notesGrid(myViewModel)
                    } else {
                        errorNote()
                    }
                    addButton(globalViewModel)
                }
            }
        }
    }
}




@Composable
fun initialCompose(){
    Box(Modifier.fillMaxSize().background(color = Color.Black.copy(alpha = 0.1f)))
    Box(Modifier.fillMaxSize().statusBarsPadding().background(color = MaterialTheme.colorScheme.background))

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
fun addButton(expansionBool : GlobalViewModel) {
    val expanded = expansionBool.getBoolean()
    Log.d("bool", "bool is ${expansionBool.globalBoolean.value}")

    val myViewModel: NoteViewModel = viewModel()

    val coroutineScope = rememberCoroutineScope()
    val threshold = 100f
    val thresholdCircle = 60f
    val offsetAmp = 0.6f
    val offsetAmpCircle = 0.11f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(color = Color.Transparent), contentAlignment = Alignment.BottomEnd
    ) {
        val offsetAnimatable = remember { androidx.compose.animation.core.Animatable(Offset(0f, 0f), Offset.VectorConverter) }
        LaunchedEffect(Unit) {
            offsetAnimatable.snapTo(Offset.Zero)
        }

        AnimatedContent(
            label = "addButton",
            targetState = expanded,
            transitionSpec = {
                fadeIn(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow
                    )
                ) + scaleIn(
                    initialScale = 1.0f, transformOrigin = TransformOrigin(
                        0.0f + (offsetAnimatable.value.x * offsetAmp ),
                        0.0f + (offsetAnimatable.value.y * offsetAmp )
                    )
                ) togetherWith fadeOut(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow
                    )
                ) + scaleOut(animationSpec = spring(stiffness =Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioLowBouncy ),
                    transformOrigin = TransformOrigin(offsetAnimatable.value.x * offsetAmp, offsetAnimatable.value.y * offsetAmp))
            },
        ) { targetExpanded ->
            run {
                BackHandler(enabled = expanded) {
                    expansionBool.setBoolean(false)
                    coroutineScope.launch { // This basicaly backs down
                        offsetAnimatable.animateTo(
                            Offset(0f, 0f),
                            animationSpec = spring(
                                stiffness = Spring.StiffnessMedium,
                                dampingRatio = Spring.DampingRatioMediumBouncy
                            )
                        )
                    }
                    Log.d("BackHandler", "Back handler called bool is ${expansionBool.getBoolean()}")
                }
                if (targetExpanded) {
                    Log.d("bool", "Target expanded. bool is ${expansionBool.globalBoolean.value}")

                    val swipeDown = Modifier
                        .offset {
                            IntOffset(offsetAnimatable.value.x.toInt(), offsetAnimatable.value.y.toInt())

                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDragEnd = {
                                if (abs(offsetAnimatable.value.y) > threshold) {
                                    coroutineScope.launch {
                                        expansionBool.setBoolean(false)
                                        offsetAnimatable.animateTo(Offset(0f,0f),animationSpec = spring(stiffness=Spring.StiffnessMedium,dampingRatio = Spring.DampingRatioMediumBouncy))
                                    }
                                } else {
                                    coroutineScope.launch {
                                        offsetAnimatable.animateTo(Offset(0f,0f),animationSpec = spring(stiffness=Spring.StiffnessMedium,dampingRatio = Spring.DampingRatioMediumBouncy))
                                    }
                                }


                            }, onDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    if (abs(offsetAnimatable.value.y)>threshold) {
                                        expansionBool.setBoolean(false)
                                        val targetOffset = Offset(offsetAnimatable.value.x + dragAmount.x*offsetAmp, offsetAnimatable.value.y + dragAmount.y*offsetAmp)
                                        offsetAnimatable.snapTo(targetOffset)
                                    } else {
                                        val targetOffset = Offset(offsetAnimatable.value.x + dragAmount.x*offsetAmp, offsetAnimatable.value.y + dragAmount.y*offsetAmp)
                                        offsetAnimatable.snapTo(targetOffset)
                                    }

                                }
                            })
                        }
                    textBox(myViewModel, true, swipeDown, expansionBool)
                } else {
                    Box(contentAlignment = Alignment.Center,modifier = Modifier.size(100.dp).offset(offsetAnimatable.value.x.toInt().dp, offsetAnimatable.value.y.toInt().dp).pointerInput(Unit){
                        detectDragGestures(onDragEnd = {
                            if (abs(offsetAnimatable.value.y) > thresholdCircle) {
                                coroutineScope.launch {
                                    expansionBool.setBoolean(true)
                                    offsetAnimatable.animateTo(Offset(0f,0f),animationSpec = spring(stiffness=Spring.StiffnessMedium,dampingRatio = Spring.DampingRatioLowBouncy))
                                }
                            } else {
                                coroutineScope.launch {
                                    offsetAnimatable.animateTo(Offset(0f,0f),animationSpec = spring(stiffness=Spring.StiffnessMedium,dampingRatio = Spring.DampingRatioLowBouncy))
                                }
                            }


                        }, onDrag = { change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                if (offsetAnimatable.value.y > thresholdCircle) {
                                    expansionBool.setBoolean(true)
                                    val targetOffset = Offset(offsetAnimatable.value.x + dragAmount.x*offsetAmpCircle, offsetAnimatable.value.y + dragAmount.y*offsetAmpCircle)
                                    offsetAnimatable.snapTo(targetOffset)
                                } else {
                                    val targetOffset = Offset(offsetAnimatable.value.x + dragAmount.x*offsetAmpCircle, offsetAnimatable.value.y + dragAmount.y*offsetAmpCircle)
                                    offsetAnimatable.snapTo(targetOffset)
                                }

                            }
                        })
                    }) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = CircleShape
                                )
                                .clip(shape = CircleShape)
                                .clickable { expansionBool.setBoolean(true)
                                           Log.d("Button", "Button clicked bool is ${expansionBool.globalBoolean.value}")},
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

fun linearInerpolation(input: Float, min: Float, max: Float, outMin: Float, outMax: Float) : Float{
    if(input < min || input > max){
        throw IllegalArgumentException("Input value must be between $min and $max")
    }else{
        return outMin + ((input.toFloat() - min) / (max - min)) * (outMax - outMin)
    }
}

@Composable
fun titleTextField(initialText: String) {
    var text by remember { mutableStateOf(initialText) }
    TextField(
        singleLine = true, colors = TextFieldDefaults.colors(
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
        ), textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        ), value = text, onValueChange = { newText ->
            text = newText.replace("\n", " ")
        }, modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun contentTextField(initialText: String) {
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
        ), textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp
        ), value = text, onValueChange = { newText ->
            text = newText
        }, modifier = Modifier.fillMaxSize()
    )
}


@SuppressLint("UnrememberedMutableState")
@Composable
fun notesGrid(myViewModel2: NoteViewModel?) {
    val gridState = rememberLazyStaggeredGridState()
    val notes by myViewModel2?.allNotesObserved?.observeAsState(emptyList()) ?: mutableStateOf(
        emptyList()
    )
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(minSize=150.dp),
        verticalItemSpacing = (3.dp),
        state = gridState,

        horizontalArrangement = Arrangement.spacedBy(3.dp),
        content = {

            items(notes) { note ->
                AnimatedVisibility(visible = true,enter = fadeIn() + scaleIn(), exit=fadeOut()+scaleOut()) {
                    if (note != null) {
                        previewNote(myViewModel2,note)
                    } else {
                        errorNote()
                    }
                }

            }

        })
}

//val note1 = com.example.yourapp.myViewModel.addNote(Note(uid = 0, "Market alışverişleri", content = "Maydonoz \n Patates \n Elma", date = "Date"))
//val note2 = com.example.yourapp.myViewModel.addNote(Note(uid = 1, title = "Kolye", content = "Swarovski kolye N38571", date = "Date"))
//val note3 = com.example.yourapp.myViewModel.addNote(Note(uid = 2, "Ahmetin dediği numara", content = "Ahmet benim 05316699622 numarasını aramamı istedi. Sanırım bu insanlar dolandırıcı", date = "Date"))

@Composable
fun errorNote() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer)
            .clip(MaterialTheme.shapes.medium)
            .border(
                2.dp,
                MaterialTheme.colorScheme.onErrorContainer,
                shape = MaterialTheme.shapes.medium
            ))
    {
        Text("ERROR",color=MaterialTheme.colorScheme.onError)
    }


}

@Composable
fun previewNote(viewmodel: NoteViewModel?, noteParameter: Note?) {
    var expanded by remember { mutableStateOf(false) }
    val characterLimit = if (expanded) 300 else 60

    // Clamp text length to avoid index out of bounds
    val displayText = if ((noteParameter?.content?.length ?: 0) > characterLimit) {
        noteParameter?.content?.substring(0, characterLimit) + "..."
    } else {
        noteParameter?.content
    }

    Card(shape=MaterialTheme.shapes.large,elevation = CardDefaults.cardElevation(defaultElevation = 3.dp, hoveredElevation = 30.dp, pressedElevation = 3.dp)
    , modifier = Modifier.padding(3.dp).animateContentSize(tween(durationMillis = linearInerpolation(noteParameter?.content?.length?.toFloat() ?: 1f,60f,300f,100f,500f).toInt()),spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium)).clickable { expanded=!expanded }) {
        Column(
            modifier = Modifier
                .background(Color.Red)
                .wrapContentSize()
                .clip(MaterialTheme.shapes.medium)
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.medium
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .padding(5.dp)
            ) {
                Text(
                    noteParameter?.title ?: "Title Empty",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Box(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.secondaryContainer)
                    .padding(5.dp)
            ) {
                Text(
                    displayText ?: "Content Empty",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
    }


@Composable
fun textBox(myViewModel3: NoteViewModel?, newNote: Boolean?, swipeDown: Modifier,expansionBool: GlobalViewModel) {

    var expanded = expansionBool.getBoolean()

    val imeVisible =
        WindowInsets.ime.getBottom(LocalDensity.current) > 0 // I wanted to use WindowInsets.isImeVisible but it is
    // experimental so the builder didn't let me use it unless i use @OptIn(ExperimentalComposeUiApi::class)

    //val titleWeight = if (imeVisible) 2.0f else 1.0f
    //val contentWeight = if (imeVisible) 6.0f else 8.0f
    //val barWeight = if (imeVisible) 2.0f else 1.0f

    //val animatedTitleWeight by animateFloatAsState(targetValue = titleWeight)
    //val animatedContentWeight by animateFloatAsState(targetValue = contentWeight)
    //val animatedBarWeight by animateFloatAsState(targetValue = barWeight)

    val animatedTitleSpacing by animateDpAsState(targetValue = if (imeVisible) 3.dp else 12.dp)
    val animatedBarSpacing by animateDpAsState(targetValue = if (imeVisible) 3.dp else 8.dp)

    var mTitle by remember { mutableStateOf("") }
    var mContent by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .fillMaxSize()
            .then(swipeDown)
    ) {
        // Title (1/10 height)
        Box(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(animatedTitleSpacing), contentAlignment = Alignment.CenterStart
        ) {

            TextField(
                singleLine = true, colors = TextFieldDefaults.colors(
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
                ), textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                ), value = mTitle, onValueChange = { text ->
                    mTitle = text.replace("\n", " ")
                }, label = { Text("Title") }, modifier = Modifier.fillMaxWidth()
            )
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
                ), textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                ), value = mContent, onValueChange = { newText ->
                    mContent = newText
                }, modifier = Modifier.fillMaxSize()
            )
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
                onClick = {
                    if (mContent != "" && mTitle != "") {
                        val note = Note(title = mTitle, content = mContent, date = "test")
                        if (newNote == true) {
                            myViewModel3?.addNote(
                                Note(
                                    title = mTitle,
                                    content = mContent,
                                    date = "test"
                                )
                            )
                            expansionBool.setBoolean(false)
                        } else if (newNote == false) {
                            myViewModel3?.updateNote(
                                Note(
                                    title = mTitle,
                                    content = mContent,
                                    date = "test"
                                )
                            )
                        } else {
                            Log.d("Empty", "New note is null")
                        }
                    }
                }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text("Save", fontWeight = FontWeight.ExtraBold)
            }
            if (newNote == true) {
                ElevatedButton(
                    onClick = { expansionBool.setBoolean(false) }, modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text("Delete", fontWeight = FontWeight.ExtraBold)

                }
            } else {
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
    }
}