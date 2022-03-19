package com.codepath.apps.restclienttemplate.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject

@Parcelize
class Tweet(var body: String = "", var createdAt: String = "", var user: User? = null ):
    Parcelable {

    companion object{
        fun fromJason(jsonObject: JSONObject):Tweet{
            val tweet= Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJason(jsonObject.getJSONObject("user"))
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Tweet>{
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()){
                tweets.add(fromJason(jsonArray.getJSONObject(i)))
            }
            return tweets

        }

    }
}