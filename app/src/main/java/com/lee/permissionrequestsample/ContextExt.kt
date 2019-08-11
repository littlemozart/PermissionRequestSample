package com.lee.permissionrequestsample

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

fun Context.hasPermissions(permissions: Array<String>) : Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        for (permission in permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
    }
    return true
}