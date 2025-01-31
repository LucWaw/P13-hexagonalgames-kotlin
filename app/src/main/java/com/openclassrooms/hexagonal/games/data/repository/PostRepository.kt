package com.openclassrooms.hexagonal.games.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.openclassrooms.hexagonal.games.data.service.PostApi
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


/**
 * This class provides a repository for accessing and managing Post data.
 * It utilizes dependency injection to retrieve a PostApi instance for interacting
 * with the data source. The class is marked as a Singleton using @Singleton annotation,
 * ensuring there's only one instance throughout the application.
 */
@Singleton
class PostRepository @Inject constructor(private val postApi: PostApi) {

    /**
     * Retrieves a Flow object containing a list of Posts ordered by creation date
     * in descending order.
     *
     * @return Flow containing a list of Posts.
     */
    val posts: Flow<List<Post>> = postApi.getPostsOrderByCreationDateDesc()

    /**
     * Retrieves a Flow object containing a list of Comments associated with a Post
     * ordered by creation date in ascending order.
     *
     * @param idPost The ID of the Post to retrieve comments for.
     * @return Flow containing a list of Comments.
     */
    fun comments(idPost: String): Flow<List<Comment>> = postApi.getCommentsByCreationDateAsc(idPost)

    /**
     * Adds a new Post to the data source using the injected PostApi.
     *
     * @param post The Post object to be added.
     * @param uri The URI of the image associated with the post.
     */
    fun addPost(post: Post, uri: Uri?) {
        if (uri == null) {
            postApi.addPost(post)
            return
        }
        uploadImage(uri, post.id).addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                val postUpdatedURL = post.copy(photoUrl = uri.toString())
                postApi.addPost(postUpdatedURL)
            }
        }
    }

    /**
     * Uploads an image to the data source using the injected PostApi.
     *
     * @param imageUri The URI of the image to be uploaded.
     * @param post The ID of the post associated with the image.
     * @return An UploadTask representing the image upload operation.
     */
    private fun uploadImage(imageUri: Uri, post: String) =
        postApi.uploadImage(imageUri, post)


    /**
     * Check if the device is connected to the internet.
     */
    fun isNetworkAvailable(context: Context): Boolean {
        return postApi.isNetworkAvailable(context)
    }

    fun getPost(postId: String): Task<Post>? {
        return postApi.getPost(postId)?.continueWith({ task ->
            Log.d("PostRepository", "getPost: ${task.result.toObject(Post::class.java)}")

            task.result.toObject(Post::class.java)
        })
    }

    /**
     * Adds a new Comment to the data source using the injected PostApi.
     *
     * @param idPost The ID of the Post to add the comment to.
     * @param value The Comment object to be added.
     */
    fun addComment(value: Comment, idPost: String) {
        return postApi.addComment(idPost, value)
    }
}
