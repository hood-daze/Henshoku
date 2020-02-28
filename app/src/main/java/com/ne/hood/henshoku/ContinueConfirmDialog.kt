package com.ne.hood.henshoku


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.content.DialogInterface
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class ContinueConfirmDialog : DialogFragment() {
    inner class DialogButtonClickListener: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val a = activity
            if (a is TitleActivity){
                a.onClickContinueConfirmDialog(which)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("確認")
                .setMessage("続きから物語を開始します。よろしいですか？")
                .setPositiveButton("はい",DialogButtonClickListener())
                .setNegativeButton("いいえ",DialogButtonClickListener())
            builder.create()
        }?:throw IllegalStateException("Activity can not null")
    }
    /*ナビゲーションバー出てくるのを防ぐ。*/
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
}
