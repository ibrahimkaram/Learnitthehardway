package com.example.newtry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text
import java.util.Observer

class Pulls : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mytext: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pulls)
        mytext = findViewById(R.id.text_view_T)
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        getmypulls()

    }

     fun getmypulls() {

         viewModel.getPull()
         viewModel.myseconresp.observe(this, androidx.lifecycle.Observer {
             response ->
             if(response.isSuccessful){


                 val mystringBuilder = StringBuilder()
                 for( i in 0..response.body()!!.size-1){
                     mystringBuilder.append("Pull Title :   ")
                     mystringBuilder.append(response.body()?.get(i)?.title)
                     mystringBuilder.append("\n")
                     mystringBuilder.append("Pull number:   ")
                     mystringBuilder.append(response.body()?.get(i)?.number)
                     mystringBuilder.append("\n")
                     mystringBuilder.append("Commit Sha id:    ")
                     mystringBuilder.append(response.body()?.get(i)?.merge_commit_sha)
                     mystringBuilder.append("\n")
                     mystringBuilder.append("use this API to Receive all the commits :   https://api.github.com/repos/square/okhttp/commits/"+response.body()?.get(i)?.merge_commit_sha )
                     mystringBuilder.append("\n")
                     mystringBuilder.append("\n")
                     mystringBuilder.append("***************************************")
                     mystringBuilder.append("\n")

                 }

                 mytext.text = mystringBuilder



             }




         })

         }
    }




