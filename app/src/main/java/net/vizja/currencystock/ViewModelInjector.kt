package net.vizja.currencystock

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkClient::class])
interface ViewModelInjector {
    fun injectMainViewModel(mainViewModel: MainViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkClient(networkClient: NetworkClient): Builder
    }
}
