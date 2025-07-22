package com.example.yourapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.rxjava3.createObservable
import com.example.database.Note
import com.example.database.NoteViewModel
import com.example.yourapp.ui.theme.NotepadTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val myViewModel: NoteViewModel = viewModel()

            NotepadTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    if (myViewModel != null) {
                        notesGrid(myViewModel)
                    } else {
                        errorNote()
                    }
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

    val myViewModel: NoteViewModel = viewModel()


    val coroutineScope = rememberCoroutineScope()
    val threshold = 125f
    val offsetAmp = 0.6f
    val negativeFloat = -1.0f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(color = Color.Transparent), contentAlignment = Alignment.BottomEnd
    ) {
        val offsetX = remember { androidx.compose.animation.core.Animatable(0f) }
        val offsetY = remember { androidx.compose.animation.core.Animatable(0f) }

        LaunchedEffect(Unit) {
            offsetX.snapTo(0f)
            offsetY.snapTo(0f)
        }

        AnimatedContent(
            label = "addButton",
            targetState = expanded,
            transitionSpec = {
                fadeIn(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow, dampingRatio = DampingRatioMediumBouncy
                    )
                ) + scaleIn(
                    initialScale = 1.0f, transformOrigin = TransformOrigin(
                        0.0f + (offsetX.value * offsetAmp * negativeFloat),
                        0.0f + (offsetY.value * offsetAmp * negativeFloat)
                    )
                ) togetherWith fadeOut(
                    animationSpec = spring(
                        stiffness = Spring.StiffnessLow, dampingRatio = DampingRatioMediumBouncy
                    )
                ) + scaleOut()
            },
        ) { targetExpanded ->
            run {
                BackHandler(enabled = expanded) {
                    expanded = false
                }
                if (targetExpanded) {


                    val swipeDown = Modifier
                        .offset {
                            IntOffset(offsetX.value.toInt(), offsetY.value.toInt())

                        }
                        .pointerInput(Unit) {
                            detectDragGestures(onDragEnd = {
                                if (offsetY.value > threshold) {
                                    coroutineScope.launch {
                                        expanded = false
                                        offsetX.animateTo(0f, animationSpec = spring())
                                        offsetY.animateTo(0f, animationSpec = spring())
                                    }
                                } else {
                                    coroutineScope.launch {
                                        offsetX.animateTo(0f, animationSpec = spring())
                                        offsetY.animateTo(0f, animationSpec = spring())

                                    }
                                }


                            }, onDrag = { change, dragAmount ->
                                change.consume()
                                coroutineScope.launch {
                                    if (offsetY.value > threshold || offsetY.value < -threshold) {
                                        expanded = false
                                        offsetX.snapTo(offsetX.value + (dragAmount.x * offsetAmp))
                                        offsetY.snapTo(offsetY.value + (dragAmount.y * offsetAmp))
                                    } else {
                                        offsetX.snapTo(offsetX.value + (dragAmount.x * offsetAmp))
                                        offsetY.snapTo(offsetY.value + (dragAmount.y * offsetAmp))
                                    }

                                }
                            })
                        }
                    textBox(myViewModel, true, swipeDown)
                } else {
                    Box(modifier = Modifier.size(100.dp)) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.secondaryContainer,
                                    shape = CircleShape
                                )
                                .clickable { expanded = true }
                                .clip(shape = CircleShape),
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
fun notesGrid(myViewModel2: NoteViewModel?) {
    val gridState = rememberLazyStaggeredGridState()
    var expandedNote by remember { mutableStateOf<Note?>(null)}
    val notes by myViewModel2?.allNotesObserved?.observeAsState(emptyList()) ?: mutableStateOf(
        emptyList()
    )
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
            .padding(5.dp),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = (5.dp),
        state = gridState,

        horizontalArrangement = Arrangement.spacedBy(5.dp),
        content = {
            items(notes) { note ->
                Card(modifier = Modifier.size(100.dp)) {
                    if (note != null) {
                        previewNote(myViewModel2,note)
                    } else {
                        Text("Loading...")
                    }
                }
            }

        })
    if(expandedNote!=null){
        Box(modifier = Modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { expandedNote = null }){
            previewNote(myViewModel2,expandedNote)
        }
    }
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
fun previewNote(viewmodel: NoteViewModel?,noteParameter: Note?) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.background(Color.Red)){
        AnimatedContent(label="preview2editable",
            targetState = expanded,
            transitionSpec = {
                fadeIn()+ scaleIn() togetherWith fadeOut() + scaleOut()
            }
        ) { targetExpanded -> if(targetExpanded){
            if(viewmodel==null){
                textBox(null,true,Modifier)
            }
            else{
                textBox(viewmodel,false,Modifier)
            }
        }else{
            Box(modifier = Modifier
                .animateContentSize()
                .then(
                    if(expanded){
                        Modifier.fillMaxSize()
                    }else{
                        Modifier.fillMaxWidth()
                    }
                )) {


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
                        )
                ) {
                    Row(modifier = Modifier.weight(3.5f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .padding(5.dp)
                        ) {
                            Text(
                                noteParameter?.title ?: "Empty",
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Row(modifier = Modifier.weight(6.5f)) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.secondaryContainer)
                                .padding(5.dp)
                        ) {
                            Text(
                                noteParameter?.content ?: "Empty",
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

        }

        }

    }

}


@Composable
fun textBox(myViewModel3: NoteViewModel?, newNote: Boolean?, swipeDown: Modifier) {


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
                    onClick = { /*TODO*/ }, modifier = Modifier
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
        addButton()
    }
}