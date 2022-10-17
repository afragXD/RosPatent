package com.example.rospatent

import android.content.Context
import android.content.Intent
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val list = ArrayList<SearchClass>()
    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = LayoutInflater.from(parent.context).inflate(
            R.layout.item_serch_alement,
            parent,
            false
        )
        context = parent.context
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position]

        holder.textTitle.text = Html.fromHtml(currentItem.title, Html.FROM_HTML_MODE_LEGACY)
        holder.textFullName.text = Html.fromHtml("МПК ${currentItem.fullname}", Html.FROM_HTML_MODE_LEGACY)
        holder.textDate.text = Html.fromHtml("${currentItem.kind} ${currentItem.publication_date}", Html.FROM_HTML_MODE_LEGACY)
        holder.textDocument.text = Html.fromHtml("Документ ${currentItem.publishing_office} ${currentItem.document_number}", Html.FROM_HTML_MODE_LEGACY)
        holder.textDescription.text = Html.fromHtml(currentItem.description, Html.FROM_HTML_MODE_LEGACY)

        holder.materialCardView.setOnClickListener(View.OnClickListener {
            SearchRep.item = currentItem

            val  i = Intent(context, PatentActivity::class.java)
            context.startActivity(i)
        })
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateLIst(list : List<SearchClass>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textTitle : TextView = itemView.findViewById(R.id.textTitle)
        val textFullName : TextView = itemView.findViewById(R.id.textFullName)
        val textDate : TextView = itemView.findViewById(R.id.textDate)
        val textDocument : TextView = itemView.findViewById(R.id.textDocument)
        val textDescription : TextView = itemView.findViewById(R.id.textDescription)
        val materialCardView : MaterialCardView = itemView.findViewById(R.id.materialCardView)
    }
}