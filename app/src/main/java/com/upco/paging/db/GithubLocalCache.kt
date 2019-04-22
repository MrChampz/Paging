package com.upco.paging.db

import android.util.Log
import androidx.lifecycle.LiveData
import com.upco.paging.model.Repo
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class GithubLocalCache(private val repoDao: RepoDao, private val ioExecutor: Executor) {

    /**
     * Insert a list of repos in the database, on a background thread.
     */
    fun insert(repos: List<Repo>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("GithubLocalCache", "Inserting ${repos.size} repos")
            repoDao.insert(repos)
            insertFinished()
        }
    }

    /**
     * Request a LiveData<List<Repo>> from the Dao, based on a repo name. If the name contains
     * multiple words separated by spaces, then we're emulating the Github API behavior and allow
     * any characters between the words.
     *
     * @param name Repository name.
     */
    fun reposByName(name: String): LiveData<List<Repo>> {
        // Appending '%' so we can allow other characters to be before and after the query string
        val query = "%${name.replace(' ', '%')}%"
        return repoDao.reposByName(query)
    }

}