package net.vizja.currencystock

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkClient(NetworkClient)
        .build()

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is MainViewModel -> injector.injectMainViewModel(this)
        }
    }
}