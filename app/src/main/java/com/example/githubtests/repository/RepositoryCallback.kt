package com.example.githubtests.repository

import com.example.githubtests.model.SearchResponse
import retrofit2.Response

internal interface RepositoryCallback {
    fun handleGitHubResponse(response: Response<SearchResponse?>?)
    fun handleGitHubError()
}