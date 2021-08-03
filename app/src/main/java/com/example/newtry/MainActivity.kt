package com.example.newtry

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel


    private lateinit var myCommit: TextView
    private lateinit var nextButton: Button
    private lateinit var mylist : ListView


    private lateinit var DataArrayList : ArrayList<LineCodeUnified>
    private lateinit var DataArrayListSplit : ArrayList<LineCodeSplit>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mylist = findViewById(R.id.listView)
        myCommit = findViewById(R.id.textView)
        nextButton = findViewById(R.id.buttonNext)


        var comitId = 0
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        nextButton.setOnClickListener{
            gettingSplitResponse(comitId)
            //gettingUnifiedResponse(comitId)
            comitId ++
            if (comitId == 6 ){
                nextButton.isEnabled = false
                nextButton.setTextColor(ContextCompat.getColor(this, R.color.white))
                nextButton.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))


            }
        }
        //gettingUnifiedResponse(comitId)
        gettingSplitResponse(comitId)


    }



    fun gettingUnifiedResponse(id : Int){
            viewModel.getPost()
            viewModel.myresp.observe(this, Observer {
                    response ->
                if(response.isSuccessful){
                    DataArrayList = ArrayList()
                    val myPath = response.body()?.files?.get(id)?.patch.toString()
                    myCommit.text = response.body()?.files?.get(id)?.filename.toString()

                    DataArrayList = ArrayList()
                    var myLoneofCode = LineCodeUnified("1","1","this this my first code", 0)



                    var linesList = myPath.split("\n").toTypedArray()
                    var lineLNumber = 0
                    var lineRNumber = 0


                    for( i in 1..linesList.count()){


                        var myline = linesList[i-1]

                        var findindex = myline.startsWith("@@ -")

                        if(findindex){


                            var Firstline = myline.split(",").toTypedArray()

                            var theLnumber = Firstline[0].split("-").toTypedArray()
                            var theRnumber = Firstline[1].split("+").toTypedArray()
                            lineRNumber = theRnumber[1].toInt()
                            lineLNumber = theLnumber[1].toInt()
                            myLoneofCode = LineCodeUnified(""," ",linesList[i-1], 0) //myLoneofCode = LineCodeUnified("  ","  ",linesList[i-1], 0)
                            DataArrayList.add(myLoneofCode)


                        } else{

                       var checkPossitive = myline.startsWith("+")
                            if (checkPossitive) {
                                myLoneofCode = LineCodeUnified(
                                    " ",
                                    lineRNumber.toString(),
                                    linesList[i - 1],
                                    2
                                )
                                DataArrayList.add(myLoneofCode)
                                lineRNumber++
                            }else {
                            }
                            var checkNegative = myline.startsWith("-")
                            if (checkNegative) {
                                    myLoneofCode = LineCodeUnified(
                                        lineLNumber.toString(),
                                        " ",
                                        linesList[i - 1],
                                        3
                                    )
                                    DataArrayList.add(myLoneofCode)
                                lineLNumber++
                                } else{}

                          if(!checkNegative && !checkPossitive){

                              myLoneofCode = LineCodeUnified(lineLNumber.toString(),lineRNumber.toString(),linesList[i-1], 1)
                              DataArrayList.add(myLoneofCode)
                              lineRNumber ++
                              lineLNumber ++

                          }
                                }


                    mylist.adapter = MyAdapter(this,DataArrayList)

                }

            }

            })

    }


    fun gettingSplitResponse(id : Int){

        viewModel.getPost()
        viewModel.myresp.observe(this, Observer { response ->
            if (response.isSuccessful) {
                DataArrayListSplit = ArrayList()
                val myPath = response.body()?.files?.get(id)?.patch.toString()
                myCommit.text = response.body()?.files?.get(id)?.filename.toString()


                DataArrayListSplit = ArrayList()
                var myLoneofCode = LineCodeSplit(
                    "1",
                    "1",
                    "this this my first code",
                    " this is the code of the second",
                    0,
                    0
                )



                var linesList = myPath.split("\n").toTypedArray()
                var lineLNumber = 0
                var lineRNumber = 0


                for (i in 1..linesList.count()) {

                    var myline = linesList[i - 1]
                    var findindex = myline.startsWith("@@ -")

                    if (findindex) {

                        var Firstline = myline.split(",").toTypedArray()

                        var theLnumber = Firstline[0].split("-").toTypedArray()
                        var theRnumber = Firstline[1].split("+").toTypedArray()
                        lineRNumber = theRnumber[1].toInt()
                        lineLNumber = theLnumber[1].toInt()
                        myLoneofCode = LineCodeSplit(" ", " ", linesList[i - 1], " ", 0, 0)

                        DataArrayListSplit.add(myLoneofCode)

                    } else {


                        var checkNegative = linesList[i-1].startsWith("-")
                        var myIndex = i
                        var negativecounter = 1
                        var possitivecounter = 1
                         while (checkNegative) {

                            var checkPositivComing = linesList[myIndex].startsWith("+")
                            var checkNegativeComing = linesList[myIndex].startsWith("-")
                            var checkEmptyComing = linesList[myIndex].startsWith(" ")
                            if (checkNegativeComing) {
                                myIndex++
                                negativecounter++
                                var checkPositivComing = linesList[myIndex].startsWith("+")

                                var checkEmptyComing = linesList[myIndex].startsWith(" ")

                                if(checkPositivComing){

                                    myLoneofCode = LineCodeSplit(
                                        lineLNumber.toString(),
                                        lineRNumber.toString(),
                                        linesList[i-1],
                                        linesList[i+1],
                                        2,
                                        3
                                    )
                                    lineLNumber++
                                    lineRNumber++
                                    DataArrayListSplit.add(myLoneofCode)
                                    myLoneofCode = LineCodeSplit(
                                        lineLNumber.toString(),
                                        lineRNumber.toString(),
                                        linesList[i],
                                        linesList[i+2],
                                        2,
                                        3
                                    )
                                    lineLNumber++
                                    lineRNumber++
                                    DataArrayListSplit.add(myLoneofCode)

                                }else{

                                }
                                if(checkEmptyComing){

                                    myLoneofCode = LineCodeSplit(
                                        lineLNumber.toString(),
                                        lineRNumber.toString(),
                                        linesList[i-1],
                                        " ",
                                        2,
                                        5
                                    )
                                    lineLNumber++
                                    lineRNumber++
                                    DataArrayListSplit.add(myLoneofCode)
                                    myLoneofCode = LineCodeSplit(
                                        lineLNumber.toString(),
                                        lineRNumber.toString(),
                                        linesList[i],
                                        " ",
                                        2,
                                        5
                                    )
                                    lineLNumber++
                                    lineRNumber++
                                    DataArrayListSplit.add(myLoneofCode)


                                }

                                break

                            }else{}
                            if (checkPositivComing) {


                                var startcountingthepossitive = false
                                possitivecounter++
                                while (startcountingthepossitive)
                                {

                                    var checkPositivComingaft =
                                        linesList[myIndex + negativecounter + possitivecounter].startsWith("+")
                                    var checkEmptyComing =
                                        linesList[myIndex + negativecounter + possitivecounter].startsWith(" ")
                                    if (checkEmptyComing) {

                                        break
                                    }
                                    else{}
                                    if (checkPositivComingaft) {
                                        possitivecounter++
                                    }else{}

                                    for (b in 0..maxOf(negativecounter, possitivecounter)) {
                                        var negativisbiggar = false
                                        if (negativecounter == possitivecounter) {
                                            myLoneofCode = LineCodeSplit(
                                                lineLNumber.toString(),
                                                lineRNumber.toString(),
                                                linesList[i - 1 + b],
                                                linesList[i - 1 + negativecounter + b],
                                                2,
                                                3
                                            )
                                            lineLNumber++
                                            lineRNumber++
                                            DataArrayListSplit.add(myLoneofCode)
                                        } else {
                                        }
                                        if (negativecounter > possitivecounter) {
                                            if (b > possitivecounter) {
                                                myLoneofCode = LineCodeSplit(
                                                    lineLNumber.toString(),
                                                    " ",
                                                    linesList[i - 1 + b],
                                                    " ",
                                                    2,
                                                    5
                                                )
                                                lineLNumber++

                                                DataArrayListSplit.add(myLoneofCode)
                                            } else {
                                                myLoneofCode = LineCodeSplit(
                                                    lineLNumber.toString(),
                                                    lineRNumber.toString(),
                                                    linesList[i - 1 + b],
                                                    linesList[i - 1 + negativecounter + b],
                                                    2,
                                                    3
                                                )
                                                lineLNumber++
                                                lineRNumber++
                                                DataArrayListSplit.add(myLoneofCode)
                                            }
                                        }else{ }
                                        if (possitivecounter > negativecounter) {
                                            if (b > negativecounter) {
                                                myLoneofCode = LineCodeSplit(
                                                    " ",
                                                    lineRNumber.toString(),
                                                    " ",
                                                    linesList[i - 1 + negativecounter + b],
                                                    5,
                                                    2
                                                )

                                                lineRNumber++
                                                DataArrayListSplit.add(myLoneofCode)

                                            } else {
                                                myLoneofCode = LineCodeSplit(
                                                    lineLNumber.toString(),
                                                    lineRNumber.toString(),
                                                    linesList[i - 1 + b],
                                                    linesList[i - 1 + negativecounter + b],
                                                    2,
                                                    3
                                                )
                                                lineLNumber++
                                                lineRNumber++
                                                DataArrayListSplit.add(myLoneofCode)
                                            }

                                        }else{}

                                    }
                                }


                                myLoneofCode = LineCodeSplit(
                                    lineLNumber.toString(),
                                    lineRNumber.toString(),
                                    linesList[i - 1 ],
                                    linesList[i],
                                    2,
                                    3
                                )
                                lineLNumber++
                                lineRNumber++
                                DataArrayListSplit.add(myLoneofCode)


                                break


                            }else{}
                            if (checkEmptyComing) {
                                negativecounter++
                                for (a in 0..negativecounter) {
                                    myLoneofCode = LineCodeSplit(
                                        lineLNumber.toString(),
                                        " ",
                                        linesList[i - 1 + a],
                                        " ",
                                        2,
                                        5
                                    )
                                    lineLNumber++
                                    DataArrayListSplit.add(myLoneofCode)

                                }
                                break
                            }else{}
                        }
                        var checkPreviousLine = true
                        if (i>1){
                         checkPreviousLine = linesList[i-2].startsWith(" ")
                        }else{}
                        var checkPossitive = myline.startsWith("+")

                        if (checkPossitive && checkPreviousLine) {
                            myLoneofCode = LineCodeSplit(
                                " ",
                                lineRNumber.toString(), "",
                                linesList[i - 1],
                                5, 3
                            )
                            DataArrayListSplit.add(myLoneofCode)
                            lineRNumber++
                        } else {
                        }


                        if (!checkNegative && !checkPossitive) {

                            myLoneofCode = LineCodeSplit(
                                lineLNumber.toString(),
                                lineRNumber.toString(),
                                linesList[i - 1],
                                linesList[i - 1],
                                1,
                                1
                            )
                            DataArrayListSplit.add(myLoneofCode)
                            lineRNumber++
                            lineLNumber++

                        }else{}
                    }


                    //val myResponce = test?.let { MyData(it," ") }
                    //  DataArrayList.add(myResponce)
                    // println(test)

                    mylist.adapter = AdapterSplit(this, DataArrayListSplit)

                }


            }

        })

    }

}





