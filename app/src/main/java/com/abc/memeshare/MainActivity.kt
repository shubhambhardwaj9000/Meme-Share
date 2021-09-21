package com.abc.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

   private var currentImageUrl:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**
         * method to load meme.
         * */
        loadMeme()
    }

    private fun loadMeme(){

        // Instantiate the RequestQueue.
        progressBar.visibility=View.VISIBLE
       /**
        * url to call API.
        * */
        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            { response ->
               currentImageUrl = response.getString("url")

                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                         progressBar.visibility= View.GONE
                         return false
                    }
                    /**
                    * Shows Progress Bar when meme is loading.
                     * */
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility=View.GONE
                        return false
                    }
                }).into(imageView)
            },
            /**
             * Toast if something goes wrong.
             * */
            {
                Toast.makeText(this, "something went wrong",Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    /**
     * Method to share meme to contacts.
     * */
    fun shareMeme(view: View) {

        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Hey checkout this meme $currentImageUrl")
        val chooser = Intent.createChooser(intent, "share this meme using...")

    }
    /**
     * Method to call meme when next button is clicked.
     * */
    fun nextMeme(view: View) {
        loadMeme()
    }
}
