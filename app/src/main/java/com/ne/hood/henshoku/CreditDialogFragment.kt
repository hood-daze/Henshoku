package com.ne.hood.henshoku


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_credit_dialog.*

class CreditDialogFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_credit_dialog,container,false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //タイトルを除く
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //画面いっぱいに広げる。
        val dialog: Dialog=dialog
        val width =ViewGroup.LayoutParams.MATCH_PARENT
        val height =ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window.setLayout(width,height)

        dialog.findViewById<Button>(R.id.button_credit_back).setOnClickListener{
            val a = activity
            if(a is TitleActivity){
                a.creback()
                dismiss()
            }
        }
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }
}
