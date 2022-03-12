package com.codepath.apps.restclienttemplate

import android.icu.text.SimpleDateFormat
import android.net.ParseException
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.apps.restclienttemplate.models.Tweet
import java.util.*
import kotlin.collections.ArrayList

class TweetsAdapter(val tweets: ArrayList<Tweet>) : RecyclerView.Adapter<TweetsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        //Inflate our item layout
        val view = inflater.inflate(R.layout.item_tweet,parent,false)
        return ViewHolder(view)
    }

    //Populating data into the item through holder
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: TweetsAdapter.ViewHolder, position: Int) {
        //Get the data model based on the position
        val tweet: Tweet = tweets.get(position)

        //set item views based on the views and data model

        holder.tvUserName.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body

        var newTime = TimeFormatter.getTimeDifference(tweet.createdAt)
        holder.tvTime.text = newTime

        Glide.with(holder.itemView).load(tweet.user?.publicImageUrl).into(holder.ivProfileImage)
    }

    override fun getItemCount(): Int {
        return tweets.size
    }

    fun clear() {
        tweets.clear()
        notifyDataSetChanged()
    }

    // Add a list of items -- change to type used
    fun addAll(tweetList: List<Tweet>) {
        tweets.addAll(tweetList)
        notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getTimeDifference(rawJsonDate: String?): String {
        var time = ""
        val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
        val format = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
        format.isLenient = true
        try {
            val diff = (System.currentTimeMillis() - format.parse(rawJsonDate).time) / 1000
            if (diff < 5) time = "Just now"
            else if (diff < 60) time = String.format(Locale.ENGLISH, "%ds", diff)
            else if (diff < 60 * 60) time = String.format(Locale.ENGLISH, "%dm", diff / 60)
            else if (diff < 60 * 60 * 24) time = String.format(Locale.ENGLISH, "%dh", diff / (60 * 60))
            else if (diff < 60 * 60 * 24 * 30) time = String.format(Locale.ENGLISH, "%dd", diff / (60 * 60 * 24))
            else {
                val now = Calendar.getInstance()
                val then = Calendar.getInstance()
                then.time = format.parse(rawJsonDate)
                time = if (now[Calendar.YEAR] == then[Calendar.YEAR]) {
                        (then[Calendar.DAY_OF_MONTH].toString() + " "
                                + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US))
                } else {
                        (then[Calendar.DAY_OF_MONTH].toString() + " "
                        + then.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.US)
                                + " " + (then[Calendar.YEAR] - 2000).toString())
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time    }
    /**     * Given a date String of the format given by the Twitter API, returns a display-formatted     * String of the absolute date of the form "30 Jun 16".     * This, as of 2016-06-30, matches the behavior of the official Twitter app.     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun getTimeStamp(rawJsonDate: String?): String {
        var time = ""
        val twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy"
        val format = SimpleDateFormat(twitterFormat, Locale.ENGLISH)
        format.isLenient = true
        try {
            val then = Calendar.getInstance()
            then.time = format.parse(rawJsonDate)
            val date = then.time
            val format1 = SimpleDateFormat("h:mm a \u00b7 dd MMM yy")
            time = format1.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return time    }
//


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUsername)
        val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
        val tvTime = itemView.findViewById<TextView>(R.id.tvTime)
    }
}