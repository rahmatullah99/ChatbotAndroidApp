package com.project.chatbot

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val USER_KEY = "user"
val BOT_KEY = "bot"

class MainActivity : AppCompatActivity() {

    private lateinit var chatsRV: RecyclerView
    private lateinit var sendMsgIB: FloatingActionButton
    private lateinit var userMsgEdt: EditText

    private var messageList = ArrayList<Chat>()
    private var messageRVAdapter: MessageRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chatsRV = findViewById(R.id.rv)
        sendMsgIB = findViewById(R.id.sendButton)
        userMsgEdt = findViewById(R.id.messageTextField)

        sendMsgIB.setOnClickListener {

            if (userMsgEdt.text.toString().isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please enter your message..",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // calling a method to send message
            // to our bot to get response.
            sendMessage(userMsgEdt.text.toString())
            // below line we are setting text in our edit text as empty
            userMsgEdt.setText("")

        }

        messageRVAdapter = MessageRVAdapter(messageList, this)

        val linearLayoutManager = LinearLayoutManager(this@MainActivity,
            RecyclerView.VERTICAL,
            false)
        chatsRV.layoutManager = linearLayoutManager
        chatsRV.adapter = messageRVAdapter

    }

    private fun sendMessage(userMessage: String) {
        messageList.add(Chat(userMessage,USER_KEY))
        messageRVAdapter?.notifyDataSetChanged()
        val url = "http://api.brainshop.ai/get?bid=170421&key=vyropTXelky92hHx&uid=[uid]&msg=$userMessage"
        val BASE_URL = "http://api.brainshop.ai"

        val retrofitApi = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api::class.java)

        GlobalScope.launch {
            val call = retrofitApi.getMessage(url)

            call.enqueue(object : Callback<Message>{
                override fun onResponse(call: Call<Message>, response: Response<Message>) {
                    Toast.makeText(applicationContext,
                        "success, Message is ${response.body()},",
                        Toast.LENGTH_SHORT).show()
                    val chat = response.body()
                    if(response.isSuccessful){
                        messageList.add(Chat(chat!!.cnt,BOT_KEY))
                        messageRVAdapter?.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<Message>, t: Throwable) {
                    Toast.makeText(applicationContext,
                        "Failed,",
                        Toast.LENGTH_SHORT).show()
                    messageList.add(Chat("Please, revert your question",BOT_KEY))
                    messageRVAdapter?.notifyDataSetChanged()
                }

            })
        }

    }
}
