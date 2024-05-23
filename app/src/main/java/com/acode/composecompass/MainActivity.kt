package com.acode.composecompass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.acode.composecompass.compassViewModel.CompassViewModel
import com.acode.composecompass.ui.theme.ComposeCompassTheme
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCompassTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val compassViewModel: CompassViewModel by viewModels()
                    CompassApp(compassViewModel)
                    //CustomShape(modifier = Modifier)

                }
            }
        }

    }


}

@Composable
fun CompassApp(compassViewModel: CompassViewModel) {

    val angle = compassViewModel.angleV.collectAsState().value
    val degrees: Double = compassViewModel.degreesV.collectAsState().value
    val direction = compassViewModel.directionV.collectAsState().value

    var currentRotation by remember { mutableStateOf(angle.toFloat()) }

    val rotation = remember { Animatable(currentRotation) }

    val animateDegrees: Float by animateFloatAsState(
        targetValue = degrees.roundToInt().toFloat(),
        animationSpec = tween(
            easing = LinearOutSlowInEasing
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White),
        contentAlignment = Alignment.Center
    ) {

        Column(Modifier.background(color = Color.White)) {
            Image(
                modifier = Modifier.rotate(-animateDegrees),
                painter = painterResource(id = R.drawable.compass_png),
                contentDescription = "Compass",
            )
            Spacer(modifier = Modifier.size(50.dp))
            Text(
                text = "Degrees: ${degrees.roundToInt().toFloat()}",
                style = TextStyle(color = Color.Black)
            )
        }
    }
}


@Composable
fun CustomShape(modifier: Modifier) {
    val i = 1
    repeat(4) {
        SingleLine(modifier = Modifier, rotate = 30f)
    }
}

@Composable
fun SingleLine(modifier: Modifier, rotate: Float) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = Color.Blue)
            .rotate(rotate), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "|",
            style = TextStyle(color = Color.Black),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold
        )

    }
}

























