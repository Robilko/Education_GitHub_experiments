package com.example.githubtests

import com.example.githubtests.presenter.details.DetailsPresenter
import com.example.githubtests.view.details.ViewDetailsContract
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class DetailPresenterTest {

    private lateinit var presenter: DetailsPresenter

    @Mock
    private lateinit var viewContract: ViewDetailsContract

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        presenter = DetailsPresenter()
        presenter.onAttach(viewContract)
    }

    @Test
    fun onDetach_Test() {
        presenter.onDetach()
        presenter.onIncrement()
        verify(viewContract, times(0)).setCount(1)
    }

    @Test
    fun onAttach_Test() {
        presenter.onDetach()
        presenter.onAttach(viewContract)
        presenter.onIncrement()
        verify(viewContract, times(1)).setCount(1)
    }

    @Test
    fun setCounter_Test() {
        val count = 9
        presenter.setCounter(count)
        assertEquals(presenter.getCounter(), count)
    }

    @Test
    fun getCounter_Test() {
        assertEquals(presenter.getCounter(), 0)
    }

    @Test
    fun onIncrement_Test() {
        presenter.onIncrement()
        assertEquals(presenter.getCounter(), 1)
    }

    @Test
    fun onDecrement_Test() {
        presenter.onDecrement()
        assertEquals(presenter.getCounter(), -1)
    }
}