package com.example.datasetapp.di

import com.example.datasetapp.data.repository.DatasetRepository
import com.example.datasetapp.data.source.network.service.ApiConfig
import com.example.datasetapp.data.source.network.service.DatasetApiService
import com.example.datasetapp.view.VerifikasiDataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.get
import org.koin.dsl.module

object AppModules {
    // Module for networking components
    val networkModule = module {
        single<DatasetApiService> { ApiConfig.apiService }
    }

    // Module for repository components
    val repositoryModule = module {
        single { DatasetRepository(get()) }
    }

    // Module for ViewModel components
    val viewModelModule = module {
        viewModel { VerifikasiDataViewModel(get()) } // Pass the repository to the ViewModel
    }

    // Combine all modules into one list
    val modules = listOf(
        networkModule,
        repositoryModule,
        viewModelModule
    )
}
