package com.example.core.reified

sealed class Video {
    data class Programming(val title: String, val duration: String): Video()
    data class Cooking(val title: String, val duration: String): Video()
    data class Travel(val title: String, val duration: String): Video()
}

inline fun <reified T> filter(videos: List<Video>): List<T> {
    return videos.filterIsInstance<T>()
}
/**
 - wants to filter on the basis of types(type T)
 - But type T is not available, inside the function body
 - So, generic types are not available on runtime, inside the function body
 - so to make it accessible, we use special keyword called reified
 - Now the T is accessible inside the function body

 when to use reified??
 - when you require type inside the function body
 - reified can only be used with inline
 */

inline fun applyTransformation(
    videos: List<Video>,
    noinline transformation: (Video) -> Video,
    crossinline onComplete: (List<Video>) -> Unit
) {
    val v = videos.map{ transformation(it) }
    onComplete(v)
}

fun main() {
    val videos = listOf(
        Video.Programming("Programming 1", "1"),
        Video.Cooking("Cooking", "1"),
        Video.Programming("Programming 2", "2"),
        Video.Travel("Travel", "1")
    )

    val result = filter<Video.Programming>(videos)
    println(result.toString())

    println("Transformation -----")
    applyTransformation(videos,
        transformation = { it ->
            when(it) {
                is Video.Programming -> {
                    it.copy(it.title + "transformed")
                }
                is Video.Cooking -> {
                    it.copy(it.title + "transformed")
                }
                is Video.Travel -> {
                    it.copy(it.title + "transformed")
                }
            }
        },
        onComplete = {
            it.forEach {
                println(it.toString())
            }
        }
    )
}
