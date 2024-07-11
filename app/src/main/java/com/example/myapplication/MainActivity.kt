package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.databinding.ItemBinding
import org.json.JSONArray

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val list = ArrayList<Model>()
    lateinit var adapter: RecyclerView.Adapter<Holder>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getData()
        adapter = object : RecyclerView.Adapter<Holder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
                return Holder(
                    ItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            override fun getItemCount(): Int {
                return list.size
            }

            override fun onBindViewHolder(holder: Holder, position: Int) {
                val model = list.get(position)
                holder.itemBinding.model = model
                Glide.with(this@MainActivity).load(model.url).into(holder.itemBinding.thumb)
                //holder.itemBinding.thumb.setImageURI(Uri.parse(model.thumbnailUrl))
            }
        }
        binding.recyclerView.adapter = adapter
    }

    private fun getData() {
        binding.progressbar.visibility = View.VISIBLE
        ApiClient().makeCall(
            this@MainActivity,
            ApiClient.BASE_URL + "/photos",
            object : ApiClient.OnApiResponse {
                override fun onResponse(isSuccessfull: Boolean, root: String) {
                    if (isSuccessfull) {
                        val jArray = JSONArray(root)
                        for (i in 0 until jArray.length()) {
                            var obj = jArray.optJSONObject(i)
                            var title = obj.optString("title")
                            var url = obj.optString("url")
                            var thumbnailUrl = obj.optString("thumbnailUrl")
                            var model = Model()
                            model.thumbnailUrl = thumbnailUrl
                            model.url = url
                            model.title = title
                            list.add(model)
                        }
                        adapter.notifyDataSetChanged()

                    } else Toast.makeText(
                        this@MainActivity,
                        "Failed to call the api",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressbar.visibility = View.GONE
                }

            })
    }

    class Holder(var itemBinding: ItemBinding) : RecyclerView.ViewHolder(itemBinding.root)
}