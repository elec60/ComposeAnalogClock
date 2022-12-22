package com.hashem.mousavi.ios6likeanalogclock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hashem.mousavi.ios6likeanalogclock.ui.Clock
import com.hashem.mousavi.ios6likeanalogclock.ui.backGroundColor
import com.hashem.mousavi.ios6likeanalogclock.ui.darkColor
import com.hashem.mousavi.ios6likeanalogclock.ui.theme.IOS6LikeAnalogClockTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IOS6LikeAnalogClockTheme {
                Box(
                    modifier = Modifier
                        .background(color = darkColor())
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(color = backGroundColor(), shape = CircleShape)
                            .padding(8.dp)
                    ) {
                        Clock(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    }

                }
            }
        }
    }
}
