package com.example.flickrbrowserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var images: ArrayList<Image>
    lateinit var rcv: RecyclerView
    lateinit var layutSearch: LinearLayout
    lateinit var edSearch: EditText
    lateinit var btnSearch:Button
    lateinit var imgMain: ImageView
    lateinit var rvadaptar: rvAdaptar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        images = arrayListOf()
        rcv=findViewById(R.id.rcv)
        layutSearch = findViewById(R.id.layutSearch)
        edSearch = findViewById(R.id.edSearch)
        btnSearch = findViewById(R.id.btnSearch)
        imgMain = findViewById(R.id.imgMain)
        rvadaptar = rvAdaptar(this,images)
        rcv.adapter = rvadaptar
        rcv.layoutManager = LinearLayoutManager(this)

        btnSearch.setOnClickListener {
            if(edSearch.text.isNotEmpty()){
                requstAPI()
            }else{
                Toast.makeText(this, "Error Please Enter Some Keyword", Toast.LENGTH_LONG).show()
            }
        }


        imgMain.setOnClickListener { closeImage() }

    }
    fun requstAPI(){
        CoroutineScope(IO).launch {
            val data = async {getImages()}.await()
            if (data.isNotEmpty()){

                println(data)
                displayPhoto(data)
            }else{
                Toast.makeText(applicationContext, "Sorry no image found try other keyword", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getImages(): String {
        var respons = ""
        try {
            respons = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&per_page=10&api_key=cb0cbca5c50568f7e3189b08d8e6a89b&tags=${edSearch.text}&format=json&nojsoncallback=1")
                .readText(Charsets.UTF_8)
        }catch(e: Exception){
            println("Error: $e")
        }

        return respons
    }
  private suspend fun displayPhoto(data: String){
      withContext(Main){
          val json = JSONObject(data)
          val photo= json.getJSONObject("photos").getJSONArray("photo")
          println("photos")
          println(photo.getJSONObject(0))
          println(photo.getJSONObject(0).getString("farm"))
          for (i in 0 until photo.length()){
              val title = photo.getJSONObject(i).getString("title")
              val farmID = photo.getJSONObject(i).getString("farm")
              val serverID = photo.getJSONObject(i).getString("server")
              val id = photo.getJSONObject(i).getString("id")
              val secret = photo.getJSONObject(i).getString("secret")
              val photoLink = "https://farm$farmID.staticflickr.com/$serverID/${id}_$secret.jpg"
              images.add(Image(title, photoLink))
          }
          rvadaptar.notifyDataSetChanged()

      }

    }

    fun openImag(link: String) {
        Glide.with(this).load(link).into(imgMain)
        imgMain.isVisible=true
        rcv.isVisible = false
        layutSearch.isVisible = false

    }

    fun closeImage(){
        imgMain.isVisible= false
        rcv.isVisible = true
        layutSearch.isVisible = true
    }
}



