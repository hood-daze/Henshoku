package com.ne.hood.henshoku

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : Activity() {
    private var countfadeout = 0
    private var countfadein = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fadein = AnimationUtils.loadAnimation(this,R.anim.short_fadein)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.short_fadeout)

        fadein.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation) {
                countfadein++
                if (countfadein <= 2) {
                    try {
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {

                    }
                    group_tl.startAnimation(fadeout)
                }
            }
        })

        fadeout.setAnimationListener(object : SimpleAnimationListener() {
            override fun onAnimationEnd(animation: Animation) {
                countfadeout++
                when(countfadeout){
                    1->{
                        text_caution.visibility = View.INVISIBLE
                        image_logo.setImageResource(R.drawable.logo)
                        group_tl.startAnimation(fadein)
                    }
                    2->{
                        try {
                            Thread.sleep(500)
                        } catch (e: InterruptedException) {

                        }
                        val intent = Intent(this@MainActivity, TitleActivity::class.java)
                        intent.putExtra("isSplash", true)
                        startActivity(intent)
                    }

                }
            }
        })
        group_tl.startAnimation(fadein)
    }

    private fun hideSystemUI() {
        //アクションバーを含めたアクティビティ全体をビューとして取得
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    //戻るキー無効。
    override fun onBackPressed() {
    }

    //ナビゲーションバーが出ないように。
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

}
