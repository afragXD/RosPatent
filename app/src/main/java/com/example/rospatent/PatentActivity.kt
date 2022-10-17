package com.example.rospatent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PatentActivity : AppCompatActivity() {

    private lateinit var btnBack : ImageButton
    private lateinit var textTitle : TextView
    private lateinit var authors : TextView

    private lateinit var number : TextView
    private lateinit var publish : TextView
    private lateinit var date : TextView
    private lateinit var mpk : TextView
    private lateinit var priority : TextView
    private lateinit var patentHolders : TextView

    private lateinit var description : TextView
    private lateinit var textLink : TextView

    //private val key = "26a213594e7f4f6e8cd89064d885ea93"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patent)

        setPatent()

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener(View.OnClickListener {
            finish()
        })
    }
    private fun setPatent(){
        textTitle = findViewById(R.id.textTitle)
        authors = findViewById(R.id.authors)

        number = findViewById(R.id.number)
        publish = findViewById(R.id.publish)
        date = findViewById(R.id.date)
        mpk = findViewById(R.id.mpk)
        priority = findViewById(R.id.priority)
        patentHolders = findViewById(R.id.patentHolders)

        description = findViewById(R.id.description)
        textLink = findViewById(R.id.textLink)

        textTitle.text = Html.fromHtml(SearchRep.item.title, Html.FROM_HTML_MODE_LEGACY)
        authors.text = Html.fromHtml("Авторы: ${SearchRep.item.inventor}", Html.FROM_HTML_MODE_LEGACY)

        number.text = Html.fromHtml(SearchRep.item.number, Html.FROM_HTML_MODE_LEGACY)
        publish.text = Html.fromHtml(SearchRep.item.publication_date, Html.FROM_HTML_MODE_LEGACY)
        date.text = Html.fromHtml(SearchRep.item.filingDate, Html.FROM_HTML_MODE_LEGACY)
        mpk.text = Html.fromHtml(SearchRep.item.fullname, Html.FROM_HTML_MODE_LEGACY)
        priority.text = Html.fromHtml("${SearchRep.item.number} ${SearchRep.item.filingDate} ${SearchRep.item.publishing_office}", Html.FROM_HTML_MODE_LEGACY)
        patentHolders.text = Html.fromHtml(SearchRep.item.patentee, Html.FROM_HTML_MODE_LEGACY)

        description.text = Html.fromHtml(SearchRep.item.description, Html.FROM_HTML_MODE_LEGACY)
        //val requestQueue = Volley.newRequestQueue(this)
        //val stringRequest = object: JsonObjectRequest(Request.Method.POST, "https://searchplatform.rospatent.gov.ru/patsearch/v0.2/docs/${SearchRep.item.id}", null, { response ->
        //    try {
        //        val allText : String = response.getJSONObject("abstract").getString("ru").toString()
        //        var uitText : String = allText.replace("</p></pat:Abstract>.*".toRegex(), "")
        //        Log.d("MyLog", uitText)
        //        uitText = uitText.substring(uitText.lastIndexOf(">") + 1)
        //        referat.text = uitText
        //    } catch (e : JSONException){
        //        Log.d("MyLog", e.toString())
        //    }
        //}, {
        //        error ->
        //    Log.d("MyLog", error.toString())
        //}){
        //    override fun getHeaders(): MutableMap<String, String> {
        //        val headers = HashMap<String, String>()
        //        headers["Authorization"] = "Bearer $key"
        //        headers["Content-Type"] = "application/json"
        //        return headers
        //    }
        //}
        //requestQueue.add(stringRequest)

        textLink.text = Html.fromHtml(
            "<a href=\\\"https://searchplatform.rospatent.gov.ru/doc/" + SearchRep.item.id + "\\\">Полная версия документа" + "</a>", Html.FROM_HTML_MODE_LEGACY)
        textLink.setOnClickListener(View.OnClickListener {
            val  browserIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://searchplatform.rospatent.gov.ru/doc/" + SearchRep.item.id))
            startActivity(browserIntent)
        })
    }
}