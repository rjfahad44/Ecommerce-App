package com.ft.ltd.ecommerceapp_mytask.data.apis

data class Response<out T, out E>(val status: Status, val data: T?, val error: E?) {

    companion object {

        fun <T, E> success(data: T?): Response<T, E> {
            return Response(Status.SUCCESS, data, null)
        }

        fun <T, E> error( data: T?,error: E): Response<T, E> {
            return Response(Status.ERROR, data, error)
        }

        fun <T, E> loading(data: T?): Response<T, E> {
            return Response(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
