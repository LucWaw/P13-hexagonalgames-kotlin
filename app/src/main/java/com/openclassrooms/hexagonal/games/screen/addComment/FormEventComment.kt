package com.openclassrooms.hexagonal.games.screen.addComment

import androidx.annotation.StringRes
import com.openclassrooms.hexagonal.games.R

/**
 * A sealed class representing different events that can occur on a form.
 */
sealed class FormEventComment {

    /**
     * Event triggered when the content of the form is changed.
     *
     * @property content The new title of the form.
     */
    data class ContentChanged(val content: String) : FormEventComment()


}

/**
 * A sealed class representing different errors that can occur on a form.
 *
 * Each error holds a resource ID for the corresponding error message string.
 */
sealed class FormErrorComment(@StringRes val messageRes: Int) {

    /**
     * Error indicating an issue with the form content.
     *
     * The actual error message can be retrieved using the provided resource ID (`R.string.error_content_comment`).
     */
    data object ContentCommentError : FormErrorComment(R.string.error_content_comment)





}
