package ru.netology.startsview

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.TextView
import androidx.activity.ComponentActivity
import org.w3c.dom.Text
import ru.netology.startsview.ui.theme.StatsView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val view = findViewById<StatsView>(R.id.statsView)
        view.data = listOf(
            1F,
            1F,
            1F,
            1F
        )

        /*val textView = findViewById<TextView>(R.id.label)


        view.animate()
            .rotation(720F)
            .setInterpolator(LinearInterpolator())
            .setStartDelay(1000)
            .setDuration(1000)
            .start()*/

        val animator = ValueAnimator.ofFloat(0f, 360f)
        animator.duration = 5000 // 2 секунды
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            view.progress = animation.animatedValue as Float
        }
        animator.start()



        /*   view.startAnimation(
               AnimationUtils.loadAnimation(this, R.anim.animation).apply {
                   setAnimationListener(object : Animation.AnimationListener {
                       override fun onAnimationEnd(p0: Animation?) {
                           textView.text = "onAnimationEnd"
                       }

                       override fun onAnimationRepeat(p0: Animation?) {
                           textView.text = "onAnimationRepeat"
                       }

                       override fun onAnimationStart(p0: Animation?) {
                           textView.text = "onAnimationStart"
                       }

                   })
               }
           )*/

    }
}