package com.example.githubtests.view.search


import com.example.githubtests.model.SearchResult
import com.example.githubtests.view.ViewContract

internal interface ViewSearchContract : ViewContract {
    fun displaySearchResults(searchResults: List<SearchResult>, totalCount: Int)
    fun displayError()
    fun displayError(error: String)
    fun displayLoading(show: Boolean)
}