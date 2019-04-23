package com.upco.paging.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.upco.paging.api.GithubService
import com.upco.paging.api.searchRepos
import com.upco.paging.db.GithubLocalCache
import com.upco.paging.model.Repo

class RepoBoundaryCallback(
    private val query: String,
    private val service: GithubService,
    private val cache: GithubLocalCache
): PagedList.BoundaryCallback<Repo>() {

    // LiveData of network errors
    val networkErrors: LiveData<String>
        get() = _networkErrors

    private val _networkErrors = MutableLiveData<String>()

    // Keep the last requested page.
    // When the request is successful, increment the page number
    private var lastRequestedPage = 1

    // Avoid triggering multiple requests in the same time
    private var isRequestInProgress = false

    override fun onZeroItemsLoaded() {
        requestAndSaveData(query)
    }

    override fun onItemAtEndLoaded(itemAtEnd: Repo) {
        requestAndSaveData(query)
    }

    private fun requestAndSaveData(query: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true
        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
            cache.insert(repos) {
                lastRequestedPage++
                isRequestInProgress = false
            }
        }, { error ->
            _networkErrors.postValue(error)
            isRequestInProgress = false
        })
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }
}