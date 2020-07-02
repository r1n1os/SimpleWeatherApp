package com.example.simpleweatherapp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class PermissionsHelperClass {

    interface OnPermissionListener {
        fun onPermissionGranted()
        fun onPermissionRequestNeeded()
        fun onPermissionDenied()
    }

    private var onPermissionListener: OnPermissionListener? = null

    fun checkPermissions(context: Context, permission: String, onPermissionListener: OnPermissionListener) {
        this.onPermissionListener = onPermissionListener
        when{
            shouldAskForPermission(context, permission) -> this.onPermissionListener?.onPermissionRequestNeeded()
            ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission) -> this.onPermissionListener?.onPermissionDenied()
            else -> this.onPermissionListener?.onPermissionGranted()
        }
    }

    private fun shouldAskForPermission(context: Context, permission: String): Boolean {
        val isPermissionAlreadyGranted = ActivityCompat.checkSelfPermission(context, permission)
        return isPermissionAlreadyGranted != PackageManager.PERMISSION_GRANTED
    }

}