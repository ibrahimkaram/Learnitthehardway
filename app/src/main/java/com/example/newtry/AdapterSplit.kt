package com.example.newtry

import android.app.Activity
import android.graphics.Color
import android.graphics.Color.red
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class AdapterSplit(private val context: Activity, private val arrayList: ArrayList<LineCodeSplit>) : ArrayAdapter<LineCodeSplit>(context, R.layout.list_item_split, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item_split, null)
        val lineNumberRight : TextView = view.findViewById(R.id.lineNumberRightS)
        val lineNumberLeft : TextView = view.findViewById(R.id.lineNumberLeftS)
        val lineCodeR: TextView = view.findViewById(R.id.lineCodeRightS)
        val lineCodeL: TextView = view.findViewById(R.id.lineCodeLeftS)

        lineNumberLeft.text = arrayList[position].lineNumberLift
        lineNumberRight.text = arrayList[position].lineNumberRight
        lineCodeR.text = arrayList[position].lineRightCode
        lineCodeL.text = arrayList[position].lineLeftCode

        if(arrayList[position].lineTypeR == 0) {
            view.setBackgroundColor(Color.parseColor("#b4b4b4"))

        }
        if(arrayList[position].lineTypeL == 5){
            lineNumberLeft.setBackgroundColor(Color.parseColor("#b4b4b4"))
            lineCodeL.setBackgroundColor(Color.parseColor("#b4b4b4"))
        }
        if(arrayList[position].lineTypeR == 5){
            lineNumberRight.setBackgroundColor(Color.parseColor("#b4b4b4"))
            lineCodeR.setBackgroundColor(Color.parseColor("#b4b4b4"))
        }


        if(arrayList[position].lineTypeL== 3) {
            lineNumberLeft.setBackgroundColor(Color.parseColor("#acffb2"))
            lineCodeL.setBackgroundColor(Color.parseColor("#acffb2"))
        }
        if(arrayList[position].lineTypeR== 3) {
            lineNumberRight.setBackgroundColor(Color.parseColor("#acffb2"))
            lineCodeR.setBackgroundColor(Color.parseColor("#acffb2"))
        }
        if(arrayList[position].lineTypeR == 2) {

            lineNumberRight.setBackgroundColor(Color.parseColor("#ffb3b3"))
            lineCodeR.setBackgroundColor(Color.parseColor("#ffb3b3"))

        }
        if(arrayList[position].lineTypeL == 2) {

            lineNumberLeft.setBackgroundColor(Color.parseColor("#ffb3b3"))
            lineCodeL.setBackgroundColor(Color.parseColor("#ffb3b3"))

        }

        return view
    }
}