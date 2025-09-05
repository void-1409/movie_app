package di

import data.remote.ApiServiceImpl
import data.remote.httpClient
import data.repository.MovieRepositoryImpl
import domain.repository.MovieRepository
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import presentation.screens.home.HomeViewModel

val appModule = module {
    // provide a singleton instance of our HttpClient
    single { httpClient }
    // provide a singleton instance of ApiServiceImpl, giving it to HttpClient
    single<data.remote.ApiService> { ApiServiceImpl(get()) }
    // provide a singleton instance of MovieRepositoryImpl, giving it to ApiService
    single<MovieRepository> { MovieRepositoryImpl(get()) }

    // provide a new instance of HomeViewModel every time its requested
    factory { HomeViewModel(get()) }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(appModule)
    }