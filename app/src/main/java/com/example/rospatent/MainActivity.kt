package com.example.rospatent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val url = "https://searchplatform.rospatent.gov.ru/patsearch/v0.2/search"
    private val key = "26a213594e7f4f6e8cd89064d885ea93"
    private lateinit var progressBar : ProgressBar
    private lateinit var editSearch : EditText
    private lateinit var btnSearch : ImageButton
    private lateinit var list:ArrayList<SearchClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list = ArrayList()

        search()
    }
    private fun search(){
        editSearch = findViewById<EditText>(R.id.editSearch)
        btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        btnSearch.setOnClickListener{
            if(editSearch.text.isNullOrBlank()){
                Toast.makeText(this, "Введите запрос", Toast.LENGTH_SHORT).show()
            }else{
                progressBar = findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.VISIBLE
                SearchRep.inp = editSearch.text.toString()
                post(editSearch.text.toString())
            }
        }
    }
    private fun post(input:String){
        val postData = JSONObject()
        Log.d("MyLog", SearchRep.sort)
        try {
            postData.put("qn", input)
            postData.put("limit", 10)
            postData.put("sort", SearchRep.sort)
            postData.put("pre_tag", "<font color=\"#E30613\">")
            postData.put("post_tag", "</font>")
        } catch (e:JSONException){
            Log.d("MyLog", e.toString())
        }
        val requestQueue = Volley.newRequestQueue(this)
        val stringRequest = object: JsonObjectRequest(Request.Method.POST, url, postData, { response ->
            try {
                for (index in 0 until response.getJSONArray("hits").length()){
                    val buf = SearchClass()

                    try {
                        buf.number = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getJSONArray("priority").getJSONObject(0).getString("number")
                        buf.filingDate = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getJSONArray("priority").getJSONObject(0).getString("filing_date")
                    } catch (e:JSONException){
                        buf.number = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getJSONObject("application").getString("number")
                        buf.filingDate = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getJSONObject("application").getString("filing_date")
                    }
                    try {
                        var ja: JSONArray = response.getJSONArray("hits").getJSONObject(index)
                            .getJSONObject("biblio").getJSONObject("ru").getJSONArray("patentee")
                        for (i in 0 until ja.length()){
                            buf.patentee += "${ja.getJSONObject(i).getString("name")}, "
                        }
                    }catch (e:JSONException){
                        buf.patentee = "-"
                    }

                    buf.title = response.getJSONArray("hits").getJSONObject(index).getJSONObject("snippet").getString("title")
                    buf.description = response.getJSONArray("hits").getJSONObject(index).getJSONObject("snippet").getString("description")
                    buf.fullname = response.getJSONArray("hits").getJSONObject(index).getJSONObject("snippet").getJSONObject("classification").getString("ipc")
                    buf.kind = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getString("kind")
                    buf.publication_date = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getString("publication_date")
                    buf.publishing_office = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getString("publishing_office")
                    buf.document_number = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getString("document_number")
                    buf.id = response.getJSONArray("hits").getJSONObject(index).getString("id")
                    try {
                        buf.inventor = response.getJSONArray("hits").getJSONObject(index).getJSONObject("snippet").getString("inventor")
                    } catch (e : JSONException){
                        buf.inventor = "-"
                    }
                    list.add(index, buf)
                }
                SearchRep.list = list
                progressBar.visibility = View.GONE
                val  i = Intent(this, SearchResultsActivity::class.java)
                startActivity(i)
            } catch (e : JSONException){
                Log.d("MyLog", e.toString())
                progressBar.visibility = View.GONE
            }
        }, {
            error ->
            Log.d("MyLog", error.toString())
            progressBar.visibility = View.GONE
        }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $key"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }
}