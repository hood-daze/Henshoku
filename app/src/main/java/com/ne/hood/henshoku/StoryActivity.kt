package com.ne.hood.henshoku

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_story.*


class StoryActivity : FragmentActivity() {

    /*ストーリーを回すための変数関係*/
    private var storyCount: Int= 0
    private var isNew: Boolean=false
    private var tapCount: Int=0

    /*se関係*/
    private var soundPool: SoundPool?=null
    private var soundGo: Int=0
    private var soundBack: Int=0
    private var soundBreak: Int=0

    /*bgm*/
    private var bgm: MediaPlayer? = null

    /*カウントで変化するリスト関係 name:body:me:kuchi:bg:bgm:se:item:こんな調子です。*/
    private val name_list: MutableList<String> = mutableListOf()
    private val body_list: MutableList<String> = mutableListOf()
    private val me_list: MutableList<String> = mutableListOf()
    private val kuchi_list: MutableList<String> = mutableListOf()
    private val bg_list: MutableList<String> = mutableListOf()
    private val bgm_list: MutableList<String> = mutableListOf()
    private val se_list: MutableList<String> = mutableListOf()
    private val item_list: MutableList<String> = mutableListOf()
    private val str_list: MutableList<String> = mutableListOf()

    /*ログで使うリスト*/
    private val log_list: MutableList<String> = mutableListOf()

    private var isFirst: Boolean =true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story)

        //titleActivityからの値を取り出す。
        storyCount=intent.getIntExtra("stage", 0)
        isNew = intent.getBooleanExtra("isNew", false)
        /*ゲームで使うサウンドエフェクトの設定*/
        setSE()
        /*ゲーム用テキストを読み込み、storyCountを添え字にゲームの変数をリストとしてセット*/
        readtext()
        /*続きからならbgとbgmとitemをいじる*/
        if (!isNew) continueInit()
        /*進めるメソッドで開始*/
        advance()
        isFirst=false
    }

    private fun readtext(){
        val whole_text=assets.open("henshoku.txt").reader(charset = Charsets.UTF_8).readText()
        val sentences= whole_text.split(":")
        //正規表現を記述
        val regex_name= Regex("<n>(.*)</n>")
        val regex_body= Regex("<body>(.*)</body>")
        val regex_me= Regex("<me>(.*)</me>")
        val regex_kuchi= Regex("<kuchi>(.*)</kuchi>")
        val regex_bg= Regex("<bg>(.*)</bg>")
        val regex_bgm= Regex("<bgm>(.*)</bgm>")
        val regex_se= Regex("<se>(.*)</se>")
        val regex_item= Regex("<item>(.*)</item>")
        val regex_str= Regex("<s>(.*)</s>")


        for(sentence in sentences){
            var match_name = regex_name.find(sentence)
            var match_body = regex_body.find(sentence)
            var match_me = regex_me.find(sentence)
            var match_kuchi = regex_kuchi.find(sentence)
            var match_bg = regex_bg.find(sentence)
            var match_bgm = regex_bgm.find(sentence)
            var match_se = regex_se.find(sentence)
            var match_item = regex_item.find(sentence)
            var match_str = regex_str.find(sentence)

            if (match_name == null){
                name_list.add("no_name")
            } else{
                name_list.add(match_name.groupValues[1])
            }

            if (match_body == null){
                body_list.add("no_body")
            }else{
                body_list.add(match_body.groupValues[1])
            }

            if (match_me == null){
                me_list.add("no_me")
            }else{
                me_list.add(match_me.groupValues[1])
            }

            if (match_kuchi == null){
                kuchi_list.add("no_me")
            }else{
                kuchi_list.add(match_kuchi.groupValues[1])
            }

            if (match_bg == null){
                bg_list.add("no_bg")
            }else{
                bg_list.add(match_bg.groupValues[1])
            }

            if (match_bgm == null){
                bgm_list.add("no_bgm")
            }else{
                bgm_list.add(match_bgm.groupValues[1])
            }

            if (match_se == null){
                se_list.add("no_se")
            }else{
                se_list.add(match_se.groupValues[1])
            }

            if (match_item == null){
                item_list.add("no_item")
            }else{
                item_list.add(match_item.groupValues[1])
            }

            if (match_str == null){
                str_list.add("no_se")
            }else{
                str_list.add(match_str.groupValues[1])
            }
        }
    }


    fun tap_sentence(v: View){
        storyCount++
        advance()
    }

    private fun makelog(){
        val s: String
        s = if (name_list[storyCount] == "no_name")"---" else "---"+name_list[storyCount]

        if (log_list.size < 5){
            log_list.add(s+"\n"+str_list[storyCount])
        }else{
            log_list.add(s+"\n"+str_list[storyCount])
            log_list.removeAt(0)
        }
    }

    private fun advance() {
        if (bg_list[storyCount]=="no_bg"){
            no_bg_advance()
        }else{
            bg()
        }
    }

    private fun no_bg_advance(){
        sentence()
        name()
        chara()
        se()
        bgm()
    }

    /*no_bg_advance関係のメソッド*/
    private fun sentence(){
        makelog()
        text_sentence.text=str_list[storyCount]
    }

    private fun name(){
        if(name_list[storyCount] == "no_name"){
            text_name.text = null
        }else {
            text_name.text = name_list[storyCount]
        }
    }

    private fun chara(){
        if (body_list[storyCount]=="no_body"){
            image_chara.setImageDrawable(null)
        }else{
            val body: Drawable = resources.getDrawable(resources.getIdentifier(body_list[storyCount], "drawable", packageName))
            val me: Drawable = resources.getDrawable(resources.getIdentifier(me_list[storyCount], "drawable", packageName))
            val kuchi: Drawable = resources.getDrawable(resources.getIdentifier(kuchi_list[storyCount], "drawable", packageName))

            val layers: Array<Drawable> = arrayOf(body, me, kuchi)
            val layerDrawable = LayerDrawable(layers)
            image_chara.setImageDrawable(layerDrawable)
        }
    }

    private fun se(){
        if(se_list[storyCount] =="no_se"){
        }else if(se_list[storyCount] == "se_break"){
            soundPool!!.play(soundBreak,1.0f,1.0f,0,0,1f)
        }
    }

    private fun bgm(){
       if(bgm_list[storyCount] == "no_bgm"){
       }else if(bgm_list[storyCount] == "end"){
           bgm!!.stop()
       }else{
           if (bgm != null) {
               bgm!!.release()
               bgm = null
           }
           bgm = MediaPlayer.create(applicationContext, resources.getIdentifier(bgm_list[storyCount],"raw",packageName))
           bgm!!.isLooping = true
           bgm!!.start()
       }
    }

    /*bg*/
    private fun bg(){
        /*画面をfadein,outさせるアニメーション関係*/

        if (bg_list[storyCount] == "end"){//endの場合
            set_story_fadeout()
        }else if(isFirst){//最初の場合
            set_story_fadein()
        }else{//普通の場合
            set_fadeout_in()
        }
    }

    /*フェード関係のセット*/
    private fun set_fadeout_in(){
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.med_fadeout)
        val fadein = AnimationUtils.loadAnimation(this, R.anim.med_fadein)

        fadeout.setAnimationListener(object : SimpleAnimationListener(){
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                storyButtonEnable(false)
                pictureInvisible()
            }

            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                val bg= resources.getDrawable(resources.getIdentifier(bg_list[storyCount],"drawable",packageName), theme)
                image_bg.setImageDrawable(bg)
                (window.decorView as ViewGroup).getChildAt(0).startAnimation(fadein)//遷移開始
            }
        })

        fadein.setAnimationListener(object : SimpleAnimationListener(){
            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                storyButtonEnable(true)
                no_bg_advance()
            }
        })

        (window.decorView as ViewGroup).getChildAt(0).startAnimation(fadeout)
    }

    private fun set_story_fadeout(){
        val story_fadeout = AnimationUtils.loadAnimation(this, R.anim.long_fadeout)
        story_fadeout.setAnimationListener(object : SimpleAnimationListener(){
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                storyButtonEnable(false)
                pictureInvisible()
            }

            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                val intent = Intent(this@StoryActivity, EndrollActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        (window.decorView as ViewGroup).getChildAt(0).startAnimation(story_fadeout)
    }

    private fun set_Exit_fadeout(){
        val exit_fadeout = AnimationUtils.loadAnimation(this, R.anim.long_fadeout)
        exit_fadeout.setAnimationListener(object : SimpleAnimationListener(){
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                storyButtonEnable(false)
                pictureInvisible()
            }

            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                val intent = Intent(this@StoryActivity, TitleActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        (window.decorView as ViewGroup).getChildAt(0).startAnimation(exit_fadeout)
    }

    private fun set_story_fadein(){
        val story_fadein = AnimationUtils.loadAnimation(this, R.anim.long_fadein)
        story_fadein.setAnimationListener(object : SimpleAnimationListener(){
            override fun onAnimationStart(animation: Animation) {
                super.onAnimationStart(animation)
                storyButtonEnable(false)
                val bg= resources.getDrawable(resources.getIdentifier(bg_list[storyCount],"drawable",packageName), theme)
                image_bg.setImageDrawable(bg)
            }

            override fun onAnimationEnd(animation: Animation) {
                super.onAnimationEnd(animation)
                storyButtonEnable(true)
                no_bg_advance()
            }
        })
        (window.decorView as ViewGroup).getChildAt(0).startAnimation(story_fadein)
    }

    /*ダイアログを表示したり、フェードアウト、インしたり用。キャラ、名前、メッセージの可視を決める*/
    private fun storyButtonEnable(b: Boolean){
        button_exit.isEnabled = b
        button_log.isEnabled = b
        text_sentence.isEnabled = b
    }
    /*fadeout用。キャラ、名前、メッセージに空のデータを与える。*/
    private fun pictureInvisible(){
        image_chara.setImageDrawable(null)
        text_name.text = null
        text_sentence.text = null
    }

    private fun continueInit() {
        /*それぞれ中身がなければ、あったときまで遡り、それを格納*/
        if (bg_list[storyCount]=="no_bg"){
            for (i in storyCount downTo 0){
                if (bg_list[i]!="no_bg"){
                    bg_list[storyCount] = bg_list[i]
                    break
                }
            }
        }

        if (bgm_list[storyCount]=="no_bgm"){
            for (i in storyCount downTo 0){
                if (bgm_list[i]!="no_bgm"){
                    bgm_list[storyCount] = bgm_list[i]
                    break
                }
            }
        }

        if (item_list[storyCount]=="no_item"){
            for (i in storyCount downTo 0){
                if (item_list[i]!="no_item"){
                    item_list[storyCount] = item_list[i]
                    break
                }
            }
        }

    }

    private fun setSE() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        soundPool = SoundPool.Builder()
            .setAudioAttributes(audioAttributes)
            .setMaxStreams(3)
            .build()
        soundGo = soundPool!!.load(this, R.raw.se_go,1)
        soundBack = soundPool!!.load(this, R.raw.se_back,1)
        soundBreak = soundPool!!.load(this,R.raw.se_break,1)
    }

    fun goLog(view: View){
        soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
        //log用の文字列を作る。
        val bf = StringBuffer()
        for(item in log_list){
            bf.append(item)
            bf.append("\n")
        }
        var s = String(bf)

        val logFragment = LogFragment()
        var args = Bundle()
        args.putString("log",s)
        logFragment.arguments = args
        logFragment.show(supportFragmentManager, "backlog")
    }

    fun logback(){
        soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
    }

    fun goExit(view: View){
        soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
        storyButtonEnable(false)
        val exitConf = ExitConfirmDialog()
        exitConf.show(supportFragmentManager, "Exit Confirm")
    }

    fun onClickExitDialog(witch: Int){
        if(witch==DialogInterface.BUTTON_POSITIVE){
            soundPool!!.play(soundGo, 1.0f, 1.0f, 0, 0, 1f)
            save()
            set_Exit_fadeout()
        }else if(witch==DialogInterface.BUTTON_NEGATIVE){
            soundPool!!.play(soundBack, 1.0f, 1.0f, 0, 0, 1f)
            storyButtonEnable(true)
        }
    }

    private fun save(){
        val sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        with(sharedPref.edit()){
            putInt("save", storyCount)
            apply()
        }
    }

    /*bgm関係を含む。ライフサイクル*/
    /*activityが止るときbgmを止める*/
    override fun onPause() {
        super.onPause()
        bgm!!.pause()
    }

    /*activityが始まるときまた再開されるときbgmを鳴らす*/
    override fun onResume() {
        super.onResume()
        if (bgm != null) bgm!!.start()
        hideSystemUI()
    }

    /*activityが終わるときbgmを止める*/
    override fun onDestroy() {
        super.onDestroy()
        bgm!!.release()
        bgm = null
        soundPool!!.release()
        soundPool = null

        tapCount = 0
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
