package com.upco.paging.ui

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.upco.paging.R
import com.upco.paging.model.Repo
import kotlinx.android.synthetic.main.item_repo_view.view.*

/**
 * View Holder for [Repo] RecyclerView list item.
 */
class RepoViewHolder(private val view: View): RecyclerView.ViewHolder(view) {

    private val name = view.repo_name
    private val description = view.repo_description
    private val language = view.repo_language
    private val stars = view.repo_stars
    private val forks = view.repo_forks

    fun bind(repo: Repo?) {
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.tx_loading)
            description.visibility = View.GONE
            language.visibility = ViewGroup.GONE
            stars.text = resources.getString(R.string.tx_unknown)
            forks.text = resources.getString(R.string.tx_unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Repo) {
        name.text = repo.fullName

        // If the description is missing, hide the TextView
        var descriptionVisibility = View.GONE
        if (repo.description != null) {
            description.text = repo.description
            descriptionVisibility = View.VISIBLE
        }
        description.visibility = descriptionVisibility

        // If the language is missing, hide the label and the value
        var languageVisibility = View.GONE
        if (!repo.language.isNullOrEmpty()) {
            val resources = itemView.resources
            language.text = resources.getString(R.string.tx_language, repo.language)
            languageVisibility = View.VISIBLE
        }
        language.visibility = languageVisibility

        stars.text = repo.stars.toString()
        forks.text = repo.forks.toString()

        // Set the OnClickListener for view
        view.setOnClickListener {
            repo.url.let {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                view.context.startActivity(intent)
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): RepoViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_repo_view, parent, false)
            return RepoViewHolder(view)
        }
    }
}