package com.upco.paging

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.upco.paging.api.GithubService
import com.upco.paging.data.GithubRepository
import com.upco.paging.db.GithubLocalCache
import com.upco.paging.db.RepoDatabase
import com.upco.paging.ui.SearchRepositoriesViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [GithubLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): GithubLocalCache {
        val database = RepoDatabase.getInstance(context)
        return GithubLocalCache(database.repoDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [GithubRepository] based on the [GithubService] and a
     * [GithubLocalCache].
     */
    private fun provideGithubRepository(context: Context): GithubRepository {
        return GithubRepository(GithubService.create(), provideCache(context))
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return SearchRepositoriesViewModelFactory(provideGithubRepository(context))
    }
}