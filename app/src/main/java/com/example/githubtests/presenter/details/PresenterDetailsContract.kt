package com.example.githubtests.presenter.details

import com.example.githubtests.presenter.PresenterContract

internal interface PresenterDetailsContract : PresenterContract{

    fun setCounter(count: Int)
    fun getCounter(): Int
    fun onIncrement()
    fun onDecrement()
}