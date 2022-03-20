package com.codepath.apps.restclienttemplate

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.R.color.Red
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    lateinit var tvCharCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        tvCharCount = findViewById(R.id.tvCharCount)
        client = TwitterApplication.getRestClient(this)


        btnTweet.setOnClickListener {
            //Grab the content of the edittext(etCompose)
            val tweetContent = etCompose.text.toString()
            //1. Make sure the tweet isn't empty

            if (tweetContent.isEmpty()){
                Toast.makeText(this, "Empty tweets are not allowed", Toast.LENGTH_SHORT).show()
                //Look into displaying a Snackbar message
            }
            //2. Make sure the tweet is under the  character count
            if (tweetContent.length > 280){
                Toast.makeText(this, "Tweet is too long!!, Limit is 280 characters", Toast.LENGTH_SHORT).show()
            }else{
                client.publishTweet(tweetContent, object : JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG,"Failed to publish tweet", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully published tweet!!")

                        val tweet = Tweet.fromJason(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK,intent)
                        finish()

                    }

                })

            }

        }

        etCompose.addTextChangedListener(object : TextWatcher {
            @SuppressLint("ResourceAsColor")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
                val character = 280 - etCompose.length()

                if(character > 0){
                    tvCharCount.setText(character.toString())
                    tvCharCount.setTextColor(Color.parseColor("#000000"))
                }
                else{
                    Log.i("here", "CharacterCount is less than 0")
                    tvCharCount.setText("above limit")
                    tvCharCount.setTextColor(Color.parseColor("#FF0000"))


                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed

            }
        })


    }


    companion object{
        val TAG = "ComposeActivity"
    }
}