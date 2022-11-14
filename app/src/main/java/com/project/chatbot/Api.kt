package com.project.chatbot

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface Api {

 @GET
 fun getMessage (@Url url:String):Call<Message>

}