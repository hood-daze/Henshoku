package com.ne.hood.henshoku


import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.DialogFragment

class NoContinueDialog : DialogFragment() {
    //activityのNoContinueDialog用メソッドを使う
    inner class DialogButtonClickListener: DialogInterface.OnClickListener{
        override fun onClick(dialog: DialogInterface?, which: Int) {
            val a = activity
            if (a is TitleActivity){
                a.onClickNoContinueDialog(which)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity.let{
            val builder =AlertDialog.Builder(it)
            builder.setTitle("注意")
                .setMessage("セーブデータがありません。")
                .setPositiveButton("はい", DialogButtonClickListener())
            builder.create()
        }?:throw IllegalStateException("Activity can not null")
    }

    /*ナビゲーションバー出てくるのを防ぐ。*/
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

}
