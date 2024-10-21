package com.jay30.retrofit2demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.jay30.retrofit2demo.ui.theme.RetrofitTestProject2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitTestProject2Theme {
                Main()
            }
        }
    }
}

@Composable
fun Main(){
    MainScreen()
}