package com.example.newtry

import android.content.Context
import android.content.Intent
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
    private lateinit var splitBotton: Button
    private lateinit var unifiedButton: Button
    private lateinit var mylist : ListView
    private lateinit var movetoSecondAct: Button


    private lateinit var DataArrayList : ArrayList<LineCodeUnified>
    private lateinit var DataArrayListSplit : ArrayList<LineCodeSplit>

    private  var comitId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mylist = findViewById(R.id.listView)
        myCommit = findViewById(R.id.textView)
        nextButton = findViewById(R.id.buttonNext)
        splitBotton = findViewById(R.id.button_Split)
        unifiedButton = findViewById(R.id.button_unified)
        movetoSecondAct = findViewById(R.id.move_second_activity)



        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        gettingUnifiedResponse(comitId)

        nextButton.setOnClickListener{
            comitId ++
            gettingUnifiedResponse(comitId)
            if (comitId == 6 ){
                nextButton.isEnabled = false
                nextButton.setTextColor(ContextCompat.getColor(this, R.color.white))
                nextButton.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_700))

            }
        }
        splitBotton.setOnClickListener{
            gettingSplitResponse(comitId)

        }

        unifiedButton.setOnClickListener{
            gettingUnifiedResponse(comitId)

        }
        movetoSecondAct.setOnClickListener{

            val intent = Intent (this , Pulls::class.java)
            startActivity(intent)


        }




    }



    fun gettingUnifiedResponse(comitId : Int){
            viewModel.getPost()
            viewModel.myresp.observe(this, Observer {
                    response ->
                if(response.isSuccessful){
                    DataArrayList = ArrayList()

                    myCommit.text = response.body()?.files?.get(comitId)?.filename.toString()


                    // myPath receive the string from the response retrofit
                    val myPath = response.body()?.files?.get(comitId)?.patch.toString()



                    // Creating the dataArraylist object that will hold the data to send it to the recyclerview
                    DataArrayList = ArrayList()

                    var createOneLineArrayList : LineCodeUnified



                    var linesList = myPath.split("\n").toTypedArray()
                    /* create Int that track the number line of the script */

                    var lineLeftNumber = 0
                    var lineRightNumber = 0

                    // loop that goes through each line
                    for( i in linesList.indices){

                        // create a string form each array
                        var lineScriptByIndex = linesList[i]

                        var checkIfCommentsLine = lineScriptByIndex.startsWith("@@ -")

                        if(checkIfCommentsLine){


                            var arrayListComment = lineScriptByIndex.split(",").toTypedArray()

                            var theLnumber = arrayListComment[0].split("-").toTypedArray()
                            var theRnumber = arrayListComment[1].split("+").toTypedArray()

                            // setting the numberline from the comments
                            lineRightNumber = theRnumber[1].toInt()
                            lineLeftNumber = theLnumber[1].toInt()

                            createOneLineArrayList = LineCodeUnified(""," ",linesList[i], 0)
                            DataArrayList.add(createOneLineArrayList)


                        } else{

                            // checking if the line start with "+"
                            if ( lineScriptByIndex.startsWith("+") ) {
                                createOneLineArrayList = LineCodeUnified(
                                    " ",
                                    lineRightNumber.toString(),
                                    linesList[i],
                                    2
                                )
                                DataArrayList.add(createOneLineArrayList)
                                lineRightNumber++
                            }

                            // checking if the line start with "-"
                            if (lineScriptByIndex.startsWith("-")) {
                                createOneLineArrayList = LineCodeUnified(
                                    lineLeftNumber.toString(),
                                    " ",
                                    linesList[i],
                                    3
                                )
                                DataArrayList.add(createOneLineArrayList)
                                lineLeftNumber++
                            }

                            // checking if the line is a regular line of code
                          if(!lineScriptByIndex.startsWith("+") && !lineScriptByIndex.startsWith("-")){
                              createOneLineArrayList = LineCodeUnified(lineLeftNumber.toString(),lineRightNumber.toString(),linesList[i], 1)
                              DataArrayList.add(createOneLineArrayList)
                              lineRightNumber ++
                              lineLeftNumber ++
                          }
                                } //loop ends

               // DataArraylist object created

                        // sending the object to the adapter
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
                var myLoneofCode : LineCodeSplit

                var linesList = myPath.split("\n").toTypedArray()
                var lineLNumber = 0
                var lineRNumber = 0
                var lineArrayNumber = 0
                var skipthisloop = true


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
                        lineArrayNumber ++
                        DataArrayListSplit.add(myLoneofCode)

                    } else {

                        var gototheloop = true
                        if (lineArrayNumber < i){


                        var checkNegative = linesList[i - 1].startsWith("-")
                        var checkPositive = linesList[i - 1].startsWith("+")
                            var checkempty = linesList[i - 1].startsWith(" ")

                            if (checkempty) {
                                myLoneofCode = LineCodeSplit(
                                    lineLNumber.toString(),
                                    lineRNumber.toString(),
                                    linesList[i - 1],
                                    linesList[i - 1],
                                    1,
                                    1
                                )
                                DataArrayListSplit.add(myLoneofCode)
                                lineArrayNumber++
                                lineRNumber++
                                lineLNumber++
                                skipthisloop= true
                            }

                        var myIndex = i
                        var negativecounter = 0
                        var possitivecounter = 0
                        if (checkNegative) {
                            negativecounter++
                        }
                        if (checkPositive) {
                            possitivecounter++
                        }
                        while (checkNegative || checkPositive) {
                            var checkNextLineNegative = linesList[myIndex].startsWith("-")
                            var checkNextLineisPositive = linesList[myIndex].startsWith("+")
                            if (checkNextLineNegative) {
                                negativecounter++
                            }
                            if (checkNextLineisPositive) {
                                possitivecounter++
                            }
                            myIndex++
                            if (!checkNextLineNegative && !checkNextLineisPositive) {
                                break
                            }
                        }
                        if (possitivecounter == negativecounter && negativecounter != 0) {

                            myIndex = i - 1
                            for (a in 1..negativecounter) {
                                myLoneofCode = LineCodeSplit(
                                    lineLNumber.toString(),
                                    lineRNumber.toString(),
                                    linesList[myIndex + a - 1],
                                    linesList[myIndex + a - 1 + negativecounter],
                                    2,
                                    3
                                )
                                skipthisloop =false
                                lineLNumber++
                                lineRNumber++
                                DataArrayListSplit.add(myLoneofCode)
                                lineArrayNumber++
                            }

                        }
                        if (possitivecounter > 0 && negativecounter == 0 && skipthisloop ) {
                            myIndex = i - 1
                            for (a in 1..possitivecounter) {
                                myLoneofCode = LineCodeSplit(
                                    " ",
                                    lineRNumber.toString(),
                                    " ",
                                    linesList[myIndex + a - 1],
                                    5,
                                    3
                                )
                                lineRNumber++
                                DataArrayListSplit.add(myLoneofCode)
                                lineArrayNumber++
                                skipthisloop =false

                            }

                        }
                        if (negativecounter > 0 && possitivecounter == 0 && skipthisloop ) {
                            myIndex = i - 1
                            for (a in 1..negativecounter) {
                                myLoneofCode = LineCodeSplit(
                                    " ",
                                    lineRNumber.toString(),
                                    linesList[myIndex + a - 1],
                                    " ",
                                    2,
                                    5
                                )
                                lineRNumber++
                                DataArrayListSplit.add(myLoneofCode)
                                lineArrayNumber++
                                skipthisloop =false
                            }

                        }

                    }
                }

                    mylist.adapter = AdapterSplit(this, DataArrayListSplit)

            }

        }

    })

}







    fun gettingSplitOldResponse(id : Int){

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






