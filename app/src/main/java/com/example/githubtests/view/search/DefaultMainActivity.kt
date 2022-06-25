package com.example.githubtests.view.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.githubtests.R
import com.example.githubtests.databinding.ActivityMainBinding
import com.example.githubtests.model.SearchResult
import com.example.githubtests.presenter.search.PresenterSearchContract
import com.example.githubtests.presenter.search.SearchPresenter
import com.example.githubtests.repository.RepositoryContract
import com.example.githubtests.view.details.DetailsActivity
import java.util.*

abstract class DefaultMainActivity : AppCompatActivity(), ViewSearchContract {

    private lateinit var binding: ActivityMainBinding
    private val adapter = SearchResultAdapter()
    private lateinit var presenter: PresenterSearchContract
    private var totalCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
    }

    private fun setUI() {
        presenter = SearchPresenter(createRepository())
        presenter.onAttach(this)
        binding.toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setQueryListener()
        setRecyclerView()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (adapter.itemCount > 0) {
            val searchQueryFromEditText = binding.searchEditText.text.toString()
            outState.putString(SEARCH_QUERY, searchQueryFromEditText)
            presenter.onDetach()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        presenter.onAttach(this)
        val query = savedInstanceState.getString(SEARCH_QUERY)
        query?.let { presenter.searchGitHub(it) }
    }

    private fun setRecyclerView() {
        with(binding) {
            recyclerView.setHasFixedSize(true)
            recyclerView.adapter = adapter
        }
    }

    private fun setQueryListener() {
        binding.searchEditText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.searchEditText.text.toString()
                if (query.isNotBlank()) {
                    presenter.searchGitHub(query)
                    return@OnEditorActionListener true
                } else {
                    Toast.makeText(this, getString(R.string.enter_search_word), Toast.LENGTH_LONG).show()
                    return@OnEditorActionListener false
                }
            }
            false
        })

        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotBlank()) {
                presenter.searchGitHub(query)
            } else {
                Toast.makeText(this, getString(R.string.enter_search_word), Toast.LENGTH_LONG).show()
            }
        }
    }

    internal abstract fun createRepository(): RepositoryContract

    override fun displaySearchResults(searchResults: List<SearchResult>, totalCount: Int) {
        with(binding.totalCountTextView) {
            this.visibility = View.VISIBLE
            this.text = String.format(Locale.getDefault(), getString(R.string.results_count), totalCount)
        }
        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    override fun displayError() {

    }

    override fun displayError(error: String) {

    }

    override fun displayLoading(show: Boolean) {

    }

    companion object {
        const val BASE_URL = "https://api.github.com"
        const val SEARCH_QUERY = "SEARCH_QUERY"
    }
}