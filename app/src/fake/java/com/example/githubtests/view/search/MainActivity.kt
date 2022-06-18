package com.example.githubtests.view.search

import com.example.githubtests.repository.FakeGitHubRepository
import com.example.githubtests.repository.RepositoryContract

class MainActivity : DefaultMainActivity() {

    override fun createRepository(): RepositoryContract = FakeGitHubRepository()
}