package com.example.key.di

import com.example.data.database.PassphraseRepository
import com.example.data.database.SiteDataBase
import com.example.data.dbrepositoryimpl.DbRepositoryImpl
import com.example.domain.repository.DBRepository
import com.example.domain.usecase.DeleteUseCase
import com.example.domain.usecase.GetItemUseCase
import com.example.domain.usecase.GetListUseCase
import com.example.domain.usecase.UpsertUseCase
import com.example.key.viewmodel.DbViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    single { PassphraseRepository(androidContext()) }

    single {
        val passRepo: PassphraseRepository = get()
        SiteDataBase.getDatabase(androidContext(), passRepo.getPassphrase())
    }

    single<DBRepository> { DbRepositoryImpl(get()) }

    factory { GetListUseCase(get()) }
    factory { UpsertUseCase(get()) }
    factory { DeleteUseCase(get()) }
    factory { GetItemUseCase(get()) }

    viewModel { DbViewModel(get(), get(), get(), get()) }

}