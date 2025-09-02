package ru.netology.startsview

import android.os.Bundle
import androidx.activity.ComponentActivity
import ru.netology.startsview.ui.theme.StatsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<StatsView>(R.id.statsView).data = listOf(
            500F,
            500f,
            500F
        )
    }
}