package com.ne.hood.henshoku

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

class EndrollActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_endroll)

        Handler().postDelayed({
            val intent = Intent(this, TitleActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition (R.anim.med_fadein,
                R.anim.med_fadeout)
        },5000)
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

