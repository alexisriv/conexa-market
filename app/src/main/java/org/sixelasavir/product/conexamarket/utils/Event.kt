package org.sixelasavir.product.conexamarket.utils

/**
 * fix (https://betterprogramming.pub/how-to-fix-a-serious-problem-in-livedata-android-594a3f18e981)
 */
class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent(): T = content
}
