package com.example.study2025.hilt.userapp

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Named

@InstallIn(ActivityComponent::class)
@Module
class UserModule {

/*
    @Provides
    fun providesUserRepository() : UserRepository {
        return FirebaseRepository()
    }
*/

/*
    @Binds
    abstract fun bindsUserRepository(sqlRepository: SQLRepository): UserRepository

*/

    @Provides
    @Named("sql")
    fun providesSQLUserRepository(sqlRepository: SQLRepository) : UserRepository {
        return sqlRepository
    }

    @Provides
    @FirebaseQualifier
    fun provideFirebaseRepository(): UserRepository {
        return FirebaseRepository()
    }
}