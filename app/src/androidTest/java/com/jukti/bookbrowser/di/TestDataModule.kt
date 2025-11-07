package com.jukti.bookbrowser.di

import com.jukti.bookbrowser.domain.repository.BookRepository
import com.jukti.bookbrowser.fakes.FakeBookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
object TestDataModule {

    @Provides
    @Singleton
    fun provideFakeRepository(): FakeBookRepository {
        val fake = FakeBookRepository()
        fake.holdEmission()
        return fake
    }

    @Provides
    fun bindFakeRepository(fake: FakeBookRepository): BookRepository = fake
}