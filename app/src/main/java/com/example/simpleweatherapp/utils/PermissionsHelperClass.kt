package com.example.simpleweatherapp.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class PermissionsHelperClass {

    interface OnPermissionListener {
        fun onPermissionGranted()
        fun onPermissionRequestNeeded()
        fun onPermissionDeniedWithDontShowAgain()
    }

    private var onPermissionListener: OnPermissionListener? = null

    fun checkPermissions(context: Context, permission: String, onPermissionListener: OnPermissionListener) {
        this.onPermissionListener = onPermissionListener
        if (shouldAskForPermission(context, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
                this.onPermissionListener?.onPermissionRequestNeeded()
            } else {
                this.onPermissionListener?.onPermissionDeniedWithDontShowAgain()
            }
        } else {
            this.onPermissionListener?.onPermissionGranted()
        }
    }

    private fun shouldAskForPermission(context: Context, permission: String): Boolean {
        val isPermissionAlreadyGranted = ActivityCompat.checkSelfPermission(context, permission)
        return isPermissionAlreadyGranted != PackageManager.PERMISSION_GRANTED
    }

}