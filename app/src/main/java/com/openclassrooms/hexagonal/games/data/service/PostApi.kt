package com.openclassrooms.hexagonal.games.data.service

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.UploadTask
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines the contract for interacting with Post data from a data source.
 * It outlines the methods for retrieving and adding Posts, abstracting the underlying
 * implementation details of fetching and persisting data.
 */
interface PostApi {
    /**
     * Retrieves a list of Posts ordered by their creation date in descending order.
     *
     * @return A list of Posts sorted by creation date (newest first).
     */
    fun getPostsOrderByCreationDateDesc(): Flow<List<Post>>

    /**
     * Adds a new Post to the data source.
     *
     * @param post The Post object to be added.
     */
    fun addPost(post: Post)

    /**
     * Uploads an image to the data source.
     *
     * @param imageUri The URI of the image to be uploaded.
     * @param post The ID of the post associated with the image.
     * @return An UploadTask representing the image upload operation.
     */
    fun uploadImage(imageUri: Uri, post: String) : UploadTask


    /**
     * Check if the device is connected to the internet.
     */
    fun isNetworkAvailable(context: Context): Boolean
}
