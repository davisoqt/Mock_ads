package com.example.mockads.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.blankj.utilcode.util.ActivityUtils
import com.example.mockads.MainActivity
import com.example.mockads.presentation.ui.theme.MockAdsTheme
import qtjambiii.ads.CheckVersionActivity

class SplashActivity : CheckVersionActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MockAdsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting2("Android")
                }
            }
        }
    }

    override fun nextScreen() {
        ActivityUtils.startActivity(MainActivity::class.java)
        finish()
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MockAdsTheme {
        Greeting2("Android")
    }
}