@file:OptIn(ExperimentalSharedTransitionApi::class)

package ir.erfansn.sharedelementsanimationplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.BoundsTransform
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastJoinToString
import ir.erfansn.sharedelementsanimationplayground.ui.theme.SharedElementsAnimationPlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedElementsAnimationPlaygroundTheme {
                Scaffold { innerPadding ->
                    ListToDetailsSharedTransition(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
private fun ListToDetailsSharedTransition(modifier: Modifier = Modifier) {
    SharedTransitionLayout(
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        var extanded by remember { mutableStateOf(false) }
        AnimatedContent(
            targetState = extanded,
            label = "shared",
            transitionSpec = { fadeIn(animationSpec()) togetherWith fadeOut(animationSpec()) }
        ) {
            if (!it) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .sharedBoundsAnimation()
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
                        .clickable { extanded = true }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.caramel_sea_salt_cookie),
                        contentDescription = null,
                        modifier = Modifier
                            .sharedCookieImageAnimation()
                            .size(64.dp)
                    )
                    Text(
                        text = "Caramel sea slat cookie",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .sharedTitleAnimation()
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .sharedBoundsAnimation()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                        .clickable { extanded = false }
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.caramel_sea_salt_cookie),
                        contentDescription = null,
                        modifier = Modifier
                            .sharedCookieImageAnimation()
                            .aspectRatio(1f)
                    )
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurface) {
                        Text(
                            text = "Caramel sea slat cookie",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .sharedTitleAnimation(),
                        )
                        Text(
                            text = """
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer sodales
                            laoreet commodo. Phasellus a purus eu risus elementum consequat. Aenean eu
                            elit ut nunc convallis laoreet non ut libero. Suspendisse interdum placerat
                            risus vel ornare. Donec vehicula, turpis sed consectetur ullamcorper, ante
                            nunc egestas quam, ultricies adipiscing velit enim at nunc. Aenean id diam
                            neque. Praesent ut lacus sed justo viverra fermentum et ut sem.
                        """.trimIndent().split("\n").fastJoinToString(),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.skipToLookaheadSize()
                        )
                    }
                }
            }
        }
    }
}

private fun <T> animationSpec() = tween<T>(1000)

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
private fun Modifier.sharedBoundsAnimation(): Modifier {
    return this then Modifier.sharedBounds(
        rememberSharedContentState(key = "bounds"),
        animatedVisibilityScope = this@AnimatedVisibilityScope,
        enter = fadeIn(animationSpec()),
        exit = fadeOut(animationSpec()),
        resizeMode = SharedTransitionScope.ResizeMode.RemeasureToBounds,
        boundsTransform = { _, _ ->
            animationSpec()
        }
    )
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
private fun Modifier.sharedCookieImageAnimation(): Modifier {
    return this then Modifier.sharedElement(
        rememberSharedContentState(key = "cookie_image"),
        animatedVisibilityScope = this@AnimatedVisibilityScope,
        boundsTransform = { _, _ ->
            animationSpec()
        }
    )
}

context(SharedTransitionScope, AnimatedVisibilityScope)
@OptIn(ExperimentalAnimationSpecApi::class)
@Composable
private fun Modifier.sharedTitleAnimation(): Modifier {
    return this then Modifier.sharedBounds(
        rememberSharedContentState(key = "title"),
        animatedVisibilityScope = this@AnimatedVisibilityScope,
        enter = fadeIn(animationSpec()),
        exit = fadeOut(animationSpec()),
        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds(),
        boundsTransform = { initialBounds, targetBounds ->
            keyframes {
                durationMillis = 1000
                initialBounds at 0 using ArcMode.ArcBelow using FastOutSlowInEasing
                targetBounds at 1000
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SharedElementsAnimationPlaygroundTheme {
        ListToDetailsSharedTransition(Modifier.fillMaxSize())
    }
}