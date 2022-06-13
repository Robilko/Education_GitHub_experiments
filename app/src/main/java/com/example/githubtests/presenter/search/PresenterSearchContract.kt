package com.example.githubtests.presenter.search

import com.example.githubtests.presenter.PresenterContract

internal interface PresenterSearchContract : PresenterContract{
    fun searchGitHub(searchQuery: String)
}