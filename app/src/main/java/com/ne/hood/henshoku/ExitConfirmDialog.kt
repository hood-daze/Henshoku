package com.ne.hood.henshoku

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException


class ExitConfirmDialog : DialogFragment() {

    inner class DialogButtonClickListener: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val a = activity
            if (a is StoryActivity){
                a.onClickExitDialog(which)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("確認")
                .setMessage("データを上書きして物語を終わりにします。よろしいですか？")
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
