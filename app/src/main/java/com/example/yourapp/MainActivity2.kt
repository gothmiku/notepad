package com.example.yourapp


import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.asComposeRenderEffect
import android.os.VibrationEffect
import android.os.Vibrator
import android.content.Context
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.collection.mutableIntIntMapOf
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.Spring.StiffnessHigh
import androidx.compose.animation.core.Spring.StiffnessLow
import androidx.compose.animation.core.Spring.StiffnessMedium
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.simulateHotReload
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.substring
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.database.Note
import com.example.viewmodels.GlobalViewModel
import com.example.viewmodels.NoteViewModel
import com.example.viewmodels.SelectedNoteViewModel
import com.example.viewmodels.ShowNoteViewModel
import com.example.yourapp.ui.theme.NotepadTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import com.example.shaders.animation.GradientShader
import com.example.system.rememberScreenSizePx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlin.math.max


class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val myViewModel: NoteViewModel = viewModel()
            val globalViewModel: GlobalViewModel by viewModels()
            val showNoteBool: ShowNoteViewModel by viewModels()
            val selectedNoteViewModel: SelectedNoteViewModel by viewModels()

            NotepadTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { content ->
                    initialCompose()
                    if (myViewModel != null) {
                        notesGrid(myViewModel, showNoteBool, selectedNoteViewModel)
                    } else {
                        errorNote()
                    }
                    addButton(globalViewModel)
                    showNote(myViewModel, showNoteBool, selectedNoteViewModel)
                }

            }
        }
    }
}


@Composable
fun initialCompose() {
    val (width, height) = rememberScreenSizePx()
    val colors =
        listOf(MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.tertiaryContainer)
    val brush = Brush.radialGradient(
        colors = colors,
        center = Offset(width / 1.5f, height / 4f),
        radius = (max(width, height))
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(color = Color.Black.copy(alpha = 0.07f))
    )
    Box(
        Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(brush = brush)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val position = event.changes.firstOrNull()?.position
                        if (position != null) {
                            Log.d("TouchLogger", "Touched at: x=${position.x}, y=${position.y}")
                        }
                    }
                }
            }
    )

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
fun addButton(expansionBool: GlobalViewModel) {
    val expanded = expansionBool.getBoolean()
    Log.d("bool", "bool is ${expansionBool.globalBoolean.value}")

    val myViewModel: NoteViewModel = viewModel()

    val coroutineScope = rememberCoroutineScope()
    val threshold = 100f
    val thresholdCircle = 60f
    val offsetAmp = 0.15f
    val offsetAmpCircle = 0.11f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(color = Color.Transparent), contentAlignment = Alignment.BottomEnd
    ) {
        val offsetAnimatable = remember {
            androidx.compose.animation.core.Animatable(
                Offset(0f, 0f),
                Offset.VectorConverter
            )
        }
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
                        0.0f + (offsetAnimatable.value.x * offsetAmp),
                        0.0f + (offsetAnimatable.value.y * offsetAmp)
                    )
                ) togetherWith fadeOut(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow
                    )
                ) + scaleOut(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessMedium,
                        dampingRatio = Spring.DampingRatioLowBouncy
                    ),
                    transformOrigin = TransformOrigin(
                        offsetAnimatable.value.x * offsetAmp,
                        offsetAnimatable.value.y * offsetAmp
                    )
                )
            },
        ) { targetExpanded ->
            run {
                val haptic = LocalHapticFeedback.current
                BackHandler(enabled = expanded) {
                    expansionBool.setBoolean(false)
                    haptic.performHapticFeedback(HapticFeedbackType.ToggleOff)
                    coroutineScope.launch { // This basicaly backs down
                        offsetAnimatable.animateTo(
                            Offset(0f, 0f),
                            animationSpec = spring(
                                stiffness = Spring.StiffnessMedium,
                                dampingRatio = Spring.DampingRatioMediumBouncy
                            )
                        )
                    }
                    Log.d(
                        "BackHandler",
                        "Back handler called bool is ${expansionBool.getBoolean()}"
                    )
                }
                if (targetExpanded) {
                    Log.d("bool", "Target expanded. bool is ${expansionBool.globalBoolean.value}")
                    val swipeDown = Modifier
                        .offset {
                            IntOffset(
                                offsetAnimatable.value.x.toInt(),
                                offsetAnimatable.value.y.toInt()
                            )

                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDragEnd = {
                                if (abs(offsetAnimatable.value.y) > threshold) {
                                    coroutineScope.launch {
                                        expansionBool.setBoolean(false)
                                        haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                        offsetAnimatable.animateTo(
                                            Offset(0f, 0f),
                                            animationSpec = spring(
                                                stiffness = Spring.StiffnessMedium,
                                                dampingRatio = Spring.DampingRatioMediumBouncy
                                            )
                                        )
                                    }
                                } else {
                                    coroutineScope.launch {
                                        offsetAnimatable.animateTo(
                                            Offset(0f, 0f),
                                            animationSpec = spring(
                                                stiffness = Spring.StiffnessMedium,
                                                dampingRatio = Spring.DampingRatioMediumBouncy
                                            )
                                        )
                                    }
                                }


                            }, onDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    if (abs(offsetAnimatable.value.y) > threshold) {
                                        expansionBool.setBoolean(false)
                                        val targetOffset = Offset(
                                            offsetAnimatable.value.x + dragAmount.x * offsetAmp,
                                            offsetAnimatable.value.y + dragAmount.y * offsetAmp
                                        )
                                        offsetAnimatable.snapTo(targetOffset)
                                    } else {
                                        val targetOffset = Offset(
                                            offsetAnimatable.value.x + dragAmount.x * offsetAmp,
                                            offsetAnimatable.value.y + dragAmount.y * offsetAmp
                                        )
                                        offsetAnimatable.snapTo(targetOffset)
                                    }

                                }
                            })
                        }
                    textBox(myViewModel, true, swipeDown, expansionBool)
                } else {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .offset(
                                offsetAnimatable.value.x.toInt().dp,
                                offsetAnimatable.value.y.toInt().dp
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(onDragEnd = {
                                    if (abs(offsetAnimatable.value.y) > thresholdCircle) {
                                        haptic.performHapticFeedback(HapticFeedbackType.GestureEnd)
                                        coroutineScope.launch {
                                            expansionBool.setBoolean(true)
                                            offsetAnimatable.animateTo(
                                                Offset(0f, 0f),
                                                animationSpec = spring(
                                                    stiffness = Spring.StiffnessMedium,
                                                    dampingRatio = Spring.DampingRatioNoBouncy
                                                )
                                            )
                                        }
                                    } else {
                                        coroutineScope.launch {
                                            offsetAnimatable.animateTo(
                                                Offset(0f, 0f),
                                                animationSpec = spring(
                                                    stiffness = Spring.StiffnessMedium,
                                                    dampingRatio = Spring.DampingRatioNoBouncy
                                                )
                                            )
                                        }
                                    }


                                }, onDrag = { change, dragAmount ->
                                    change.consume()
                                    coroutineScope.launch {
                                        if (offsetAnimatable.value.y > thresholdCircle) {
                                            expansionBool.setBoolean(true)
                                            val targetOffset = Offset(
                                                offsetAnimatable.value.x + dragAmount.x * offsetAmpCircle,
                                                offsetAnimatable.value.y + dragAmount.y * offsetAmpCircle
                                            )
                                            offsetAnimatable.snapTo(targetOffset)
                                        } else {
                                            val targetOffset = Offset(
                                                offsetAnimatable.value.x + dragAmount.x * offsetAmpCircle,
                                                offsetAnimatable.value.y + dragAmount.y * offsetAmpCircle
                                            )
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
                                .clickable {
                                    expansionBool.setBoolean(true)
                                    haptic.performHapticFeedback(HapticFeedbackType.Confirm)
                                    Log.d(
                                        "Button",
                                        "Button clicked bool is ${expansionBool.globalBoolean.value}"
                                    )
                                },
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
fun notesGrid(
    myViewModel2: NoteViewModel?,
    showNoteBool: ShowNoteViewModel?,
    selectedNoteViewModel: SelectedNoteViewModel
) {
    val gridState = rememberLazyStaggeredGridState()
    val notes by myViewModel2?.allNotesObserved?.observeAsState(emptyList()) ?: mutableStateOf(
        emptyList()
    )
    var selectedNote by remember { mutableStateOf<Note?>(null) }


    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .background(Color.Transparent)
            .navigationBarsPadding()
            .statusBarsPadding()
            .fillMaxSize(),
        columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
        //verticalItemSpacing = (5.dp),
        state = gridState,

        //horizontalArrangement = Arrangement.spacedBy(5.dp),
        content = {

            items(notes) { note ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                    modifier = Modifier.animateItem()
                ) {
                    if (note != null) {
                        previewNote(
                            myViewModel2,
                            note,
                            showNoteBool,
                            onNoteClick = { selectedNote = it })
                    } else {
                        errorNote()
                    }

                    selectedNote?.let { selectedNoteViewModel.setNote(it) }
                }

            }

        }

    )
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
            )
    )
    {
        Text("ERROR", color = MaterialTheme.colorScheme.onError)
    }


}

@Composable
fun showNote(
    viewmodel: NoteViewModel?,
    showNoteBool: ShowNoteViewModel?,
    selectedNoteViewModel: SelectedNoteViewModel
) {

    var imeActive = rememberImeVisibility()

    var mTitle by remember { mutableStateOf("") }
    var mContent by remember { mutableStateOf("") }

    var offsetTable by remember { mutableStateOf(Offset.Zero)}


    val buttonAnimation by animateDpAsState(targetValue = if (!imeActive.value ?: false) 75.dp else 0.dp)

    Log.d("Update","I work bro")

    LaunchedEffect(selectedNoteViewModel.getNote()) {
        Log.d("Update","Launched Effect start")
        mTitle = selectedNoteViewModel.getNote().title ?: ""
        Log.d("Update", "Title set as ${mTitle}")
        mContent = selectedNoteViewModel.getNote().content ?: ""
        Log.d("Update", "Content set as ${mContent}")
    }



    val blurAmount by animateDpAsState(targetValue = if (showNoteBool?.getBoolean() == true) 10.dp else 0.dp)
    val blackAmount by animateFloatAsState(targetValue = if (showNoteBool?.getBoolean() == true) 0.2f else 0f)

    BackHandler(enabled = showNoteBool?.getBoolean() ?: false) {
        showNoteBool?.setBoolean(false)
    }

    val threshold = 300f
    val offsetAmp = 0.5f
    val density = LocalDensity.current
    var offsetAnimatable = remember {
        androidx.compose.animation.core.Animatable(
            Offset(0f, 0f),
            Offset.VectorConverter
        )
    }
    val alpha = (threshold + 50f) / offsetAnimatable.value.y * 0.7f

    LaunchedEffect(showNoteBool?.getBoolean()) {
        offsetAnimatable.snapTo(Offset.Zero)
    }

    if (showNoteBool?.getBoolean() ?: false) {
        Box(
            modifier = Modifier
                .blur(30.dp)
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = blackAmount))
        )
    }
    Log.d("Note", "showNote is ${showNoteBool?.getBoolean()}")
    AnimatedVisibility(
        visible = showNoteBool?.getBoolean() ?: false,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(animationSpec = spring(dampingRatio = DampingRatioLowBouncy)) { with(density) { 100.dp.roundToPx() } } +  expandVertically(expandFrom = Alignment.Top),
        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(animationSpec = spring(stiffness = StiffnessMedium)) {with(density){-400.dp.roundToPx()}  }
    ) {
        Card(
            modifier = Modifier
                .offset { IntOffset(0,offsetAnimatable.value.y.toInt()) }
                .fillMaxSize()
                .alpha(alpha)
                .navigationBarsPadding()
                .statusBarsPadding()
                .imePadding()
                .padding(15.dp)
                .border(
                    3.dp,
                    MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
                ), colors = CardColors(
                contentColor = Color.Transparent,
                disabledContentColor = Color.Gray,
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Gray
            ), elevation = CardDefaults.cardElevation(if(showNoteBool?.getBoolean()?:false) blurAmount else 0.dp)
            , shape = MaterialTheme.shapes.extraLarge

        ) {

            Column(){

                val coroutineScope = rememberCoroutineScope()
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 75.dp, max = 125.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(10.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(onDragEnd = {
                            if(offsetAnimatable.value.y > threshold){
                                coroutineScope.launch{
                                    offsetAnimatable.animateTo(targetValue = Offset(0f,0f))
                                }
                                showNoteBool?.setBoolean(false)

                            }
                            else{
                                coroutineScope.launch {
                                    offsetAnimatable.animateTo(Offset.Zero)
                                }
                            }
                        }
                            , onDrag = {
                                change, dragAmount ->
                            change.consume()
                            coroutineScope.launch {
                                if(offsetAnimatable.value.y < threshold) {
                                    val targetOffset = Offset(
                                        offsetAnimatable.value.x + dragAmount.x * offsetAmp,
                                        offsetAnimatable.value.y + dragAmount.y * offsetAmp
                                    )
                                    offsetAnimatable.snapTo(targetOffset)
                                }
                                else{
                                    val targetOffset = Offset(
                                        offsetAnimatable.value.x + dragAmount.x * offsetAmp * 0.15f,
                                        offsetAnimatable.value.y + dragAmount.y * offsetAmp * 0.15f
                                    )
                                    offsetAnimatable.snapTo(targetOffset)
                                }
                        }

                            })
                    }){
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
                        textStyle = MaterialTheme.typography.titleLarge,
                        value = mTitle,
                        onValueChange = { text ->
                            mTitle = text.replace("\n", " ")
                        },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Row(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background)){
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
                        ), textStyle = MaterialTheme.typography.bodyMedium
                        , value = mContent, onValueChange = { newText ->
                            mContent = newText
                        }, modifier = Modifier.fillMaxSize()
                    )
                }
                Row(modifier=Modifier
                    .fillMaxWidth()
                    .height(buttonAnimation)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(5.dp)
                    , horizontalArrangement = Arrangement.SpaceEvenly
                    , verticalAlignment = Alignment.CenterVertically){
                    Column(modifier=Modifier.weight(1f)){
                        ElevatedButton(modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            , enabled = (mTitle != selectedNoteViewModel.getNote().title || mContent != selectedNoteViewModel.getNote().content)
                            ,onClick = {
                                val updatedNote = selectedNoteViewModel.getNote().copy(title=mTitle, content = mContent)
                                viewmodel?.updateNote(updatedNote)
                                showNoteBool?.setBoolean(false)
                        }) { Text("Update", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                    Column(modifier=Modifier.weight(1f)){
                        ElevatedButton(modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp),onClick = {
                            viewmodel?.delete(selectedNoteViewModel.getNote())
                            showNoteBool?.setBoolean(false)
                        }) { Text("Delete", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberImeVisibility(): State<Boolean> {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    return rememberUpdatedState(imeBottom > 0)
}

@Composable
fun previewNote(
    viewmodel: NoteViewModel?,
    noteParameter: Note?,
    showNoteBool: ShowNoteViewModel?,
    onNoteClick: (Note?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val characterLimit = if (expanded) 300 else 60

    // Clamp text length to avoid index out of bounds
    val displayText = if ((noteParameter?.content?.length ?: 0) > characterLimit) {
        noteParameter?.content?.substring(0, characterLimit) + "..."
    } else {
        noteParameter?.content
    }
    val interactionSource = remember { MutableInteractionSource() }
    val animatable = remember {
        androidx.compose.animation.core.Animatable(1f)
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is HoverInteraction.Enter -> animatable.animateTo(1.1f)
                is HoverInteraction.Exit -> animatable.animateTo(1f)
            }
        }
    }

    Box(
        modifier = Modifier
            .padding(5.dp)
            .background(color = Color.Transparent)
            .hoverable(interactionSource = interactionSource)
            .then(Modifier.graphicsLayer {
                val scale = animatable.value
                this.scaleX = scale
                this.scaleY = scale
            })
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
            )
        ) {
            Card(
                colors = CardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primaryContainer,
                    disabledContentColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .animateContentSize(
                        spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    .bounceClick(
                        onClick = {
                            expanded = !expanded
                            onNoteClick(noteParameter)
                        },
                        onLongClick = {
                            showNoteBool?.setBoolean(true)
                            onNoteClick(noteParameter)
                        })

            ) {
                Column(
                    modifier = Modifier
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
                            .padding(6.dp)
                    ) {
                        Text(
                            noteParameter?.title ?: "Title Empty",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
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
    }
}

private fun Modifier.bounceClick(
    scaleDown: Float = 0.94f,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) = composed {

    val interactionSource = remember { MutableInteractionSource() }
    val haptic = LocalHapticFeedback.current

    val animatable = remember {
        androidx.compose.animation.core.Animatable(1f)
    }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> animatable.animateTo(scaleDown)
                is PressInteraction.Release -> animatable.animateTo(
                    1f,
                    animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
                )

                is PressInteraction.Cancel -> animatable.animateTo(
                    1f,
                    animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
                )
            }
        }
    }



    Modifier
        .graphicsLayer {
            val scale = animatable.value
            scaleX = scale
            scaleY = scale
        }
        .combinedClickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            },
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onLongClick()
            }


        )
}

@Composable
fun textBox(
    myViewModel3: NoteViewModel?,
    newNote: Boolean?,
    swipeDown: Modifier,
    expansionBool: GlobalViewModel
) {

    val context = LocalContext.current


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
                .padding(animatedTitleSpacing)
        ) {
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
                value = mTitle,
                onValueChange = { text ->
                    mTitle = text.replace("\n", " ")
                },
                label = { Text("Title", fontWeight = FontWeight.ExtraBold) },
                modifier = Modifier.fillMaxWidth()
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
                        } else {
                            Log.d("Empty", "New note is null")
                            Toast.makeText(context, "Title nor content cannot be empty", Toast.LENGTH_LONG).show()
                        }
                    }
                }, modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text("Save", fontWeight = FontWeight.ExtraBold)
            }

        }
    }
}
