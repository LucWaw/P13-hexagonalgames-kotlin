package com.openclassrooms.hexagonal.games.di

import com.openclassrooms.hexagonal.games.data.manager.UserManager
import com.openclassrooms.hexagonal.games.data.repository.UserRepository
import com.openclassrooms.hexagonal.games.data.service.PostApi
import com.openclassrooms.hexagonal.games.data.service.PostFirebaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This class acts as a Dagger Hilt module, responsible for providing dependencies to other parts of the application.
 * It's installed in the SingletonComponent, ensuring that dependencies provided by this module are created only once
 * and remain available throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    /**
     * Provides a Singleton instance of PostApi using a PostFakeApi implementation for testing purposes.
     * This means that whenever a dependency on PostApi is requested, the same instance of PostFakeApi will be used
     * throughout the application, ensuring consistent data for testing scenarios.
     *
     * @return A Singleton instance of PostFakeApi.
     */
    @Provides
    @Singleton
    fun providePostApi(): PostApi {
        return PostFirebaseApi()
    }

    //Provide Manager
    @Provides
    @Singleton
    fun provideUserManager(userRepository: UserRepository): UserManager {
        return UserManager(userRepository)
    }

    //Provide Repository
    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository()
    }
}
