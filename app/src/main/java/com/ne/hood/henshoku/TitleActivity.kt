package com.ne.hood.henshoku

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.android.synthetic.main.activity_title.*

class TitleActivity : FragmentActivity() {

    private var bgTitle: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var soundGo: Int = 0
    private var soundBack: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*splashからの遷移かどうか判断。真ならスタート画面、偽ならタイトル画面を見せる。*/
        val isSplash = intent.getBooleanExtra("isSplash", false)
        when(isSplash){
            true -> {
                setContentView(R.layout.activity_start)
                setbuttonStart()
            }
            else -> {
                setContentView(R.layout.activity_title)
                setbuttonTitle()
            }
        }

        /*音楽設定*/
        setmusic()
        /*se設定*/
        setse()

    }
    /*三つのボタンのenable*/
    fun titleButtonEnable(b: Boolean){
        button_newgame.isEnabled=b
        button_continue.isEnabled=b
        button_credit.isEnabled=b
    }

    //ゲームを始めるときの遷移。intentを引数に新しいか続きか判断。
    fun gamefadeout(i: Intent){
        //animの準備
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.med_fadeout)
        fadeout.setAnimationListener(object : SimpleAnimationListener() {
            //初めにタイトルのボタンを全て押せなくする。
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                titleButtonEnable(false)
            }

            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                startActivity(i)
                finish()
            }
        })
        //animの開始。
        (window.decorView as ViewGroup).getChildAt(0).startAnimation(fadeout)
    }

    //newgameの確認画面のボタン
    fun onClickNewGameConfirm(which: Int) {
        if(which == DialogInterface.BUTTON_POSITIVE){
            soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
            val i = Intent(this,StoryActivity::class.java)
            i.putExtra("stage", 0)
            i.putExtra("isNew", true)
            gamefadeout(i)
        }else if(which ==DialogInterface.BUTTON_NEGATIVE){
            soundPool!!.play(soundBack, 1.0f, 1.0f, 0, 0, 1f)
            titleButtonEnable(true)
        }
    }

    //続きからゲームを始めるの確認画面のボタン
    fun onClickContinueConfirmDialog(which: Int){
        if(which == DialogInterface.BUTTON_POSITIVE){
            soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
            val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val sNum = sharedPref.getInt("save",-1)

            val i = Intent(this, StoryActivity::class.java)
            i.putExtra("stage",sNum)
            i.putExtra("isNew",false)
            gamefadeout(i)
        }else if(which ==DialogInterface.BUTTON_NEGATIVE){
            soundPool!!.play(soundBack, 1.0f, 1.0f, 0, 0, 1f)
            titleButtonEnable(true)
        }
    }
    //セーブデータがないときの注意
    fun onClickNoContinueDialog(which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE){
            soundPool!!.play(soundBack, 1.0f, 1.0f, 0, 0, 1f)
            titleButtonEnable(true)
        }
    }

    /*クレジットの戻るボタン用のメソッド 戻る音を鳴らすだけ*/
    fun creback(){
        soundPool!!.play(soundBack, 1.0f, 1.0f, 0, 0, 1f)
    }

    /*tap to startのボタン*/
    private fun setbuttonStart() {
        button_start.setOnClickListener {
            soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
            setContentView(R.layout.activity_title)
            setbuttonTitle()
        }
    }

    private fun setbuttonTitle() {
        /*タイトル画面のボタン三個*/
        /*初めからボタン*/
        button_newgame.setOnClickListener {
            soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
            titleButtonEnable(false)
            val conf = NewGameConfirmDialog()
            conf.show(supportFragmentManager, "NewGameConf")
        }
        /*続きからボタン*/
        button_continue.setOnClickListener {
            soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
            titleButtonEnable(false)

            val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val sNum = sharedPref.getInt("save",-1)

            if(sNum!=-1){
                val conf = ContinueConfirmDialog()
                conf.show(supportFragmentManager, "ContinueConf")
            }else{
                val nocon = NoContinueDialog()
                nocon.show(supportFragmentManager, "NoCont")
            }
        }
        button_credit.setOnClickListener {
            soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
            val credit = CreditDialogFragment()
            credit.show(supportFragmentManager, "credit")
        }

    }


    private fun setmusic() {
        bgTitle = MediaPlayer.create(this, R.raw.bgm_title)
        bgTitle!!.setVolume(0.7f, 0.7f)
        bgTitle!!.isLooping = true
    }

    private fun setse() {
        //21以上。Builderはね
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(2)
            .build()
        soundGo = soundPool!!.load(this, R.raw.se_go, 1)//waveのロード
        soundBack = soundPool!!.load(this, R.raw.se_back, 1)
    }

    /*activityが止るときbgmを止める*/
    override fun onPause() {
        super.onPause()
        bgTitle!!.pause()
    }

    /*activityが始まるときまた再開されるときbgmを鳴らす*/
    override fun onResume() {
        super.onResume()
        bgTitle!!.start()
        hideSystemUI()
    }

    /*activityが終わるときbgmを止める*/
    override fun onDestroy() {
        super.onDestroy()
        bgTitle!!.release()// メモリの解放
        bgTitle = null // 音楽プレーヤーを破棄
    }

    //システムバーを無くすメソッド
    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    //戻るボタン無効
    override fun onBackPressed() {
    }

}
