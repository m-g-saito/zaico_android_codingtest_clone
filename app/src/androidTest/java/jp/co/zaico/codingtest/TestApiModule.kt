package jp.co.zaico.codingtest

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import jp.co.zaico.codingtest.api.ZaicoApi
import jp.co.zaico.codingtest.di.ApiModule
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [ApiModule::class]
)
object TestApiModule {

    @Provides
    @Singleton
    fun provideTestZaicoApi(): ZaicoApi = TestZaicoApi()
}