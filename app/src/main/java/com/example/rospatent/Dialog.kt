package com.example.rospatent

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment

class Dialog : DialogFragment() {

    private lateinit var dialogButton1 : Button
    private lateinit var dialogButton2 : Button
    private lateinit var dialogButton3 : Button
    private lateinit var dialogButton4 : Button
    private lateinit var dialogButton5 : Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v : View = inflater.inflate(R.layout.dialog, container, false)

        dialogButton1 = v.findViewById(R.id.dialogButton1)
        dialogButton2 = v.findViewById(R.id.dialogButton2)
        dialogButton3 = v.findViewById(R.id.dialogButton3)
        dialogButton4 = v.findViewById(R.id.dialogButton4)
        dialogButton5 = v.findViewById(R.id.dialogButton5)

        if (SearchRep.sort == "relevance"){
            dialogButton1.setTextColor(Color.BLUE)
        }else if (SearchRep.sort == "publication_date:desc") {
            dialogButton2.setTextColor(Color.BLUE)
        }else if (SearchRep.sort == "publication_date:asc") {
            dialogButton3.setTextColor(Color.BLUE)
        }else if (SearchRep.sort == "filing_date:desc") {
            dialogButton4.setTextColor(Color.BLUE)
        }else if (SearchRep.sort == "filing_date:asc") {
            dialogButton5.setTextColor(Color.BLUE)
        }

        dialogButton1.setOnClickListener(View.OnClickListener {
            SearchRep.sort = "relevance"
            dismiss()
        })
        dialogButton2.setOnClickListener(View.OnClickListener {
            SearchRep.sort = "publication_date:desc"
            dismiss()
        })
        dialogButton3.setOnClickListener(View.OnClickListener {
            SearchRep.sort = "publication_date:asc"
            dismiss()
        })
        dialogButton4.setOnClickListener(View.OnClickListener {
            SearchRep.sort = "filing_date:desc"
            dismiss()
        })
        dialogButton5.setOnClickListener(View.OnClickListener {
            SearchRep.sort = "filing_date:asc"
            dismiss()
        })

        return v
    }

}