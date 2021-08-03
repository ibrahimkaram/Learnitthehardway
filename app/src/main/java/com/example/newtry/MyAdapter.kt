package com.example.newtry

import android.app.Activity
import android.graphics.Color
import android.graphics.Color.red
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class MyAdapter(private val context: Activity, private val arrayList: ArrayList<LineCodeUnified>) : ArrayAdapter<LineCodeUnified>(context, R.layout.list_item, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.list_item, null)
        val lineNumberRight : TextView = view.findViewById(R.id.lineNumberRight)
        val lineNumberLeft : TextView = view.findViewById(R.id.lineNumberLeft)
        val lineCode: TextView = view.findViewById(R.id.lineCode)

            lineNumberLeft.text = arrayList[position].lineNumberLift
            lineNumberRight.text = arrayList[position].lineNumberRight
            lineCode.text = arrayList[position].lineCode
        if(arrayList[position].lineType == 0) {
            view.setBackgroundColor(Color.parseColor("#b4b4b4"))

        }
        if(arrayList[position].lineType == 2) {
            view.setBackgroundColor(Color.parseColor("#acffb2"))

        }
        if(arrayList[position].lineType == 3) {
            view.setBackgroundColor(Color.parseColor("#ffb3b3"))

        }

        return view
    }
}