package com.example.githubtests.presenter

import com.example.githubtests.view.ViewContract

internal interface PresenterContract {
    fun onAttach(view: ViewContract)
    fun onDetach()
}