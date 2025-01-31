package com.openclassrooms.hexagonal.games.data.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.openclassrooms.hexagonal.games.domain.model.Comment
import com.openclassrooms.hexagonal.games.domain.model.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID


class PostFirebaseApi : PostApi {
    private val COLLECTION_NAME = "posts"

    override fun getPostsOrderByCreationDateDesc(): Flow<List<Post>> {
        return getPostCollection()
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(Post::class.java)
            }
    }


    override fun addPost(post: Post) {
        getPostCollection().document(post.id).set(post)
    }

    override fun uploadImage(imageUri: Uri, post: String): UploadTask {
        val uuid = UUID.randomUUID().toString() // GENERATE UNIQUE STRING
        val mImageRef = FirebaseStorage.getInstance().getReference("$COLLECTION_NAME/$uuid")
        return mImageRef.putFile(imageUri)
    }


    private fun getPostCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }


    /**
     * Check if the device is connected to the internet.
     */
    override fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            //for other device how are able to connect with Ethernet
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            //for check internet over Bluetooth
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
            else -> false
        }
    }

    override fun getPost(postId: String): Task<DocumentSnapshot> {
        return getPostCollection().document(postId).get()
    }


    override fun getCommentsByCreationDateAsc(postId: String): Flow<List<Comment>> {
        return getPostCollection()
            .document(postId)
            .collection("comments")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.toObjects(Comment::class.java)
            }
    }

    override fun addComment(postId: String,comment: Comment) {
        getPostCollection()
            .document(postId)
            .collection("comments")
            .document(comment.id)
            .set(comment)
    }


}