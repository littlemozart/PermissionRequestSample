package com.lee.permissionrequestsample

fun Array<String>.toCustomString() : String {
    if (isNullOrEmpty()) {
        return "[]"
    }
    val sb = StringBuilder()
    forEach {
        sb.append(it)
        sb.append(",")
    }
    sb.deleteCharAt(sb.length - 1)
    return "[$sb]"
}