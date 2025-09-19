package ru.netology.startsview

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val root = findViewById<ViewGroup>(R.id.root)
        root.layoutTransition = LayoutTransition().apply {
            setDuration(2_000)
            setInterpolator(LayoutTransition.CHANGE_APPEARING, BounceInterpolator())
        }
        findViewById<View>(R.id.buttonGo).setOnClickListener {

            val view = layoutInflater.inflate(R.layout.stats_view, root, false)
            root.addView(view, 0)

        }
    }
}
