package uk.fernando.memory.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import uk.fernando.memory.R

enum class CardFace(val angle: Float) {
    Front(0f) {
        override val next: CardFace
            get() = Back
    },
    Back(180f) {
        override val next: CardFace
            get() = Front
    },
    Hidden(0f) {
        override val next: CardFace
            get() = Hidden
    };

    abstract val next: CardFace
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MyFlipCard(
    cardFace: CardFace,
    onClick: (CardFace) -> Unit,
    modifier: Modifier = Modifier,
    back: @Composable () -> Unit = {}
) {
    val rotation = animateFloatAsState(
        targetValue = cardFace.angle,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing,
        )
    )

    Box(
        Modifier
            .fillMaxSize()
            .aspectRatio(1f)
    ) {

        AnimatedVisibility(
            visible = cardFace != CardFace.Hidden,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            Card(
                onClick = { onClick(cardFace) },
                shape = MaterialTheme.shapes.small,
                modifier = modifier
                    .graphicsLayer {
                        rotationY = rotation.value
                        cameraDistance = 12f * density
                    }
            ) {

                Box(
                    Modifier
                        .fillMaxSize()
                        .graphicsLayer { if (rotation.value > 90f) rotationY = 180f },
                    contentAlignment = Alignment.Center
                ) {
                    if (rotation.value <= 90f) {
                        Icon(
                            painterResource(id = R.drawable.ic_question_mark),
                            modifier = Modifier.fillMaxSize(0.6f),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground.copy(0.6f)
                        )
                    } else {
                        back()
                    }
                }
            }
        }

    }
}