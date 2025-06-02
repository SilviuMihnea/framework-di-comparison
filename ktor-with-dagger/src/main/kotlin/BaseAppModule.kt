package com.example

import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BaseAppModule {

    @Provides
    @Singleton
    fun provideUserHandler(): UserHandler {
        return UserHandlerImpl()
    }
}

@Component(modules = [BaseAppModule::class])
interface UserHandlerComponent {
    fun getUserHandler(): UserHandler
}