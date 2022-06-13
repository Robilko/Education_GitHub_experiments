package com.example.githubtests.view.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.githubtests.R
import com.example.githubtests.model.SearchResult

internal class SearchResultAdapter : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>(){

    private var results: List<SearchResult> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, null))
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun updateResults(results: List<SearchResult>) {
        this.results = results
        notifyDataSetChanged()
    }

    internal class SearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(searchResult: SearchResult) {
            itemView.findViewById<TextView>(R.id.repositoryName).text = searchResult.fullName
        }
    }
}