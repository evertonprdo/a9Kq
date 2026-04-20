package dev.evertonprdo.a9kq.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.evertonprdo.a9kq.R
import kotlin.math.absoluteValue

@Composable
fun SwipeToRemoveCard(
    onSwipe: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    val positionalThreshold: (totalDistance: Float) -> Float =
        SwipeToDismissBoxDefaults.positionalThreshold

    val dismissState: SwipeToDismissBoxState =
        remember { SwipeToDismissBoxState(SwipeToDismissBoxValue.Settled, positionalThreshold) }
    // rememberSavable restores the dismissState when it comes back to composition

    BoxWithConstraints {
        val width = constraints.maxWidth.toFloat()
        val offset = runCatching { dismissState.requireOffset() }.getOrNull() ?: 0f

        val colorTransitionThreshold = 0.5f
        val fraction = (offset.absoluteValue / (width * colorTransitionThreshold)).coerceIn(0f, 1f)

        val dynamicColor = lerp(
            start = Color.Transparent,
            stop = MaterialTheme.colorScheme.error,
            fraction = fraction
        )

        val dynamicTint = lerp(
            start = Color.Transparent,
            stop = MaterialTheme.colorScheme.onError,
            fraction = fraction
        )

        SwipeToDismissBox(
            state = dismissState,
            enableDismissFromStartToEnd = false,
            onDismiss = {
                if (it == SwipeToDismissBoxValue.EndToStart)
                    onSwipe()
            },
            backgroundContent = {
                Icon(
                    painter = painterResource(R.drawable.trash),
                    contentDescription = null,
                    tint = dynamicTint,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(dynamicColor, shape)
                        .wrapContentSize(Alignment.CenterEnd)
                        .padding(24.dp, 16.dp)
                )
            },
        ) {
            Card(
                modifier = modifier,
                shape = shape,
                colors = colors,
                elevation = elevation,
                border = border,
                content = content
            )
        }
    }
}