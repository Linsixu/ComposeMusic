package com.music.classroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.music.classroom.util.StatusBarController

class MainActivity : ComponentActivity() {
    private val mStatusBarControlledComposition = StatusBarController()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        mStatusBarControlledComposition.init(this)
        setContent {
            App(statusBarController = mStatusBarControlledComposition)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}