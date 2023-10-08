package com.aryan.a2048.di

import android.app.Application
import com.aryan.a2048.data.model.Board
import com.aryan.a2048.data.model.Game
import com.aryan.a2048.data.repository.GameRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesGameRepository(app:Application): GameRepository {
        return GameRepository(app)
    }

    @Provides
    @Singleton
    fun providesBoard(): Board {
        return Board()
    }

    @Provides
    @Singleton
    fun providesGame(board: Board): Game {
        return Game(board)
    }
}