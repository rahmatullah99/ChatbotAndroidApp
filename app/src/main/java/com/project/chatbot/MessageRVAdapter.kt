package com.project.chatbot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


public class MessageRVAdapter(val messageList: ArrayList<Chat>?, val context: Context?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        // below code is to switch our
        // layout type along with view holder.
        when (viewType) {
            0 -> {
                // below line we are inflating user message layout.
                view = LayoutInflater.from(parent.context).inflate(R.layout.user_message,parent, false)
                return UserViewHolder(view)
            }
            1 -> {
                // below line we are inflating bot message layout.
                view = LayoutInflater.from(parent.context).inflate(R.layout.bot_message,parent, false)
                return BotViewHolder(view)
            }
        }
     return null!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // this method is use to set data to our layout file.
        val modal: Chat = messageList!![position]
        when (modal.sender) {
            "user" ->
                (holder as UserViewHolder).userTV.setText(modal.message)
            "bot" ->
                (holder as BotViewHolder).botTV.setText(modal.message)
        }
    }

    override fun getItemCount(): Int {
        // return the size of array list
        return messageList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        // below line of code is to set position.
        return when (messageList?.get(position)?.sender) {
            "user" -> 0
            "bot" -> 1
            else -> -1
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userTV: TextView

        init {
            userTV = itemView.findViewById(R.id.idTVUser)
        }
    }

    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var botTV: TextView

        init {
            botTV = itemView.findViewById(R.id.idTVBot)
        }
    }

}