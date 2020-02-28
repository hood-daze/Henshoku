package com.ne.hood.henshoku


import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_log.*


class LogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //タイトルを除く
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //画面いっぱいに広げる。多分こことcredit違う。
        val dialog: Dialog = dialog
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.setLayout(width, height)

        text_log_content.text = arguments!!.getString("log","")
        button_log_back.setOnClickListener {
            val a = activity
            if (a is StoryActivity) {
                a.logback()
                dismiss()
            }
        }

        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

}
