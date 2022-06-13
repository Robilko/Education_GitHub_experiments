package com.example.githubtests.view.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import com.example.githubtests.R
import com.example.githubtests.databinding.ActivityMainBinding
import com.example.githubtests.model.SearchResult
import com.example.githubtests.presenter.search.PresenterSearchContract
import com.example.githubtests.presenter.search.SearchPresenter
import com.example.githubtests.repository.GitHubApi
import com.example.githubtests.repository.GitHubRepository
import com.example.githubtests.view.details.DetailsActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), ViewSearchContract {

    lateinit var binding: ActivityMainBinding
    private val adapter = SearchResultAdapter()
    private val presenter: PresenterSearchContract = SearchPresenter(this, createRepository())
    private var totalCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUI()
    }

    private fun setUI() {
        binding.toDetailsActivityButton.setOnClickListener {
            startActivity(DetailsActivity.getIntent(this, totalCount))
        }
        setQueryListener()
        setRecyclerView()

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
                    Toast.makeText(this@MainActivity, getString(R.string.enter_search_word), Toast.LENGTH_LONG).show()
                    return@OnEditorActionListener false
                }
            }
            false
        })
    }

    private fun createRepository(): GitHubRepository {
        return GitHubRepository(createRetrofit().create(GitHubApi::class.java))
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun displaySearchResults(searchResults: List<SearchResult>, totalCount: Int) {
        this.totalCount = totalCount
        adapter.updateResults(searchResults)
    }

    override fun displayError() {
        TODO("Not yet implemented")
    }

    override fun displayError(error: String) {
        TODO("Not yet implemented")
    }

    override fun displayLoading(show: Boolean) {
        TODO("Not yet implemented")
    }

    companion object {
        const val BASE_URL = "https://api.github.com"
    }
}