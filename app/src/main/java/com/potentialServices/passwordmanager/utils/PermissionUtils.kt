package com.potentialServices.passwordmanager.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import android.net.Uri
import android.provider.Settings

class PermissionUtils {
    companion object {
        const val PERMISSION_READ_IMAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        const val PERMISSION_READ_MEDIA_IMAGES = Manifest.permission.READ_MEDIA_IMAGES
        const val PERMISSION_REQUEST_CODE_MEDIA_IMAGES = 101

        fun requestRuntimePermissionForReadAndWriteFiles(context: Context, activity: Activity) {
            val permissionsToRequest = mutableListOf<String>()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // For Android 13 and above, request the granular READ_MEDIA_IMAGES permission
                if (ActivityCompat.checkSelfPermission(
                        context,
                        PERMISSION_READ_MEDIA_IMAGES
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(PERMISSION_READ_MEDIA_IMAGES)
                }

            } else {
                // For Android 12 and below, request the READ_EXTERNAL_STORAGE permission
                if (ActivityCompat.checkSelfPermission(
                        context,
                        PERMISSION_READ_IMAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsToRequest.add(PERMISSION_READ_IMAGE)
                }
            }


            if (permissionsToRequest.isNotEmpty()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        permissionsToRequest.first()
                    )
                ) {
                    // Handling permission rationale
                    val alertDialogBuilder = AlertDialog.Builder(context)
                    alertDialogBuilder.setMessage("This message requires you to grant Image Access Permissions to work correctly")
                    alertDialogBuilder.setTitle("Permission Required")
                        .setCancelable(false)
                        .setPositiveButton("OK") { dialog, which ->
                            // Request Permissions when clicked OK
                            ActivityCompat.requestPermissions(
                                activity,
                                permissionsToRequest.toTypedArray(),
                                PERMISSION_REQUEST_CODE_MEDIA_IMAGES
                            )
                            dialog.dismiss()
                        }
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                } else {
                    // If not granted, request the permissions
                    ActivityCompat.requestPermissions(
                        activity,
                        permissionsToRequest.toTypedArray(),
                        PERMISSION_REQUEST_CODE_MEDIA_IMAGES
                    )
                }
            }
        }

        @JvmStatic
        fun checkPermissionsMedia(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.checkSelfPermission(
                    context,
                    PERMISSION_READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                ActivityCompat.checkSelfPermission(
                    context,
                    PERMISSION_READ_IMAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        }

        @JvmStatic
        fun onRequestPermissionsResultMedia(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
            context: Context,
            activity: Activity,
            packageName: String
        ): Boolean {
            if (requestCode == PERMISSION_REQUEST_CODE_MEDIA_IMAGES) {
                var allPermissionsGranted = true
                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                        break
                    }
                }

                if (allPermissionsGranted) {
                    //If permissions granted
                    Toast.makeText(
                        context,
                        "Permissions Granted. Now you can proceed with document access ",
                        Toast.LENGTH_SHORT
                    ).show()
                    return true;
                } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        PERMISSION_READ_IMAGE
                    ) || !ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        PERMISSION_READ_MEDIA_IMAGES
                    )
                ) {
                    //If permissions denied
                    Toast.makeText(
                        context,
                        "Permissions Denied. Gracefully degrade the feature",
                        Toast.LENGTH_SHORT
                    ).show()

                    // If the feature is heavily dependent on the permissions,
                    // then the user should be directed to the settings page to allow the feature from there

                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(
                        "This feature is unavailable since it requires you to grant Image Access Permissions to work correctly" +
                                "Please go to settings and grant the Image Access permissions"
                    )
                    builder.setTitle("Permissions Required")
                        .setCancelable(false)
                        .setNegativeButton("Cancel") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Settings") { dialog, which ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", packageName, null)
                            intent.data = uri
                            activity.startActivity(intent)
                            dialog.dismiss()

                        }
                    builder.show()
                } else {
                    requestRuntimePermissionForReadAndWriteFiles(context, activity)
                }
            }

            return false
        }




    }

}