package com.example.rospatent

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SearchResultsActivity : AppCompatActivity() {

    private lateinit var editSearch : EditText
    private lateinit var patentList : RecyclerView
    private lateinit var btnSearch : ImageButton
    private lateinit var list:ArrayList<SearchClass>
    private lateinit var searchAdapter : SearchAdapter
    private lateinit var progressBar : ProgressBar
    private lateinit var btnBack : ImageButton
    private lateinit var btnSort : ImageButton

    private val url = "https://searchplatform.rospatent.gov.ru/patsearch/v0.2/search"
    private val key = "26a213594e7f4f6e8cd89064d885ea93"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        createRecyclerView()
        editSearch = findViewById<EditText>(R.id.editSearch)
        editSearch.setText(SearchRep.inp)
        search()
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        btnSort = findViewById<ImageButton>(R.id.btnSort)
        btnSort.setOnClickListener(View.OnClickListener {
            var dialog = Dialog()

            dialog.show(supportFragmentManager, "dialog")
        })
    }
    private fun createRecyclerView(){
        patentList = findViewById(R.id.patentList)
        patentList.setHasFixedSize(true)
        patentList.layoutManager = LinearLayoutManager(this)
        list = ArrayList()
        list = SearchRep.list
        searchAdapter = SearchAdapter()
        searchAdapter.updateLIst(list)
        patentList.adapter = searchAdapter
        searchAdapter.notifyDataSetChanged()
    }
    private fun search(){
        btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        btnSearch.setOnClickListener{
            if(editSearch.text.isNullOrBlank()){
                Toast.makeText(this, "Введите запрос", Toast.LENGTH_SHORT).show()
            }else{
                list.clear()
                progressBar = findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.VISIBLE
                post(editSearch.text.toString())
            }
        }
    }
    private fun post(input:String){
        val postData = JSONObject()
        try {
            postData.put("qn", input)
            postData.put("limit", 10)
            postData.put("sort", SearchRep.sort)
            postData.put("pre_tag", "<font color=\"#E30613\">")
            postData.put("post_tag", "</font>")
        } catch (e: JSONException){
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
                        try {
                            buf.filingDate = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getJSONObject("application").getString("filing_date")
                        }catch (e:JSONException){
                            buf.filingDate = "-"
                        }
                        buf.number = response.getJSONArray("hits").getJSONObject(index).getJSONObject("common").getJSONObject("application").getString("number")
                    }
                    try {
                        val ja: JSONArray = response.getJSONArray("hits").getJSONObject(index)
                            .getJSONObject("biblio").getJSONObject("ru").getJSONArray("patentee")
                        for (i in 0 until ja.length()){
                            buf.patentee += "${ja.getJSONObject(i).getString("name")}, "
                        }
                    }catch (e:JSONException){
                        buf.patentee = "-"
                    }

                    buf.title = response.getJSONArray("hits").getJSONObject(index).getJSONObject("snippet").getString("title")
                    buf.description = response.getJSONArray("hits").getJSONObject(index).getJSONObject("snippet").getString("description")
                    try {
                        buf.fullname = response.getJSONArray("hits").getJSONObject(index)
                            .getJSONObject("snippet").getJSONObject("classification")
                            .getString("ipc")
                    }catch (e:JSONException){
                        buf.fullname = "-"
                    }
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
                searchAdapter.updateLIst(list)
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