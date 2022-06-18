package com.example.githubtests.view.search

import com.example.githubtests.repository.GitHubApi
import com.example.githubtests.repository.GitHubRepository
import com.example.githubtests.repository.RepositoryContract
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : DefaultMainActivity(){

    override fun createRepository(): RepositoryContract = GitHubRepository(createRetrofit().create(GitHubApi::class.java))

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}