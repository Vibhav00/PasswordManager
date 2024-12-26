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
import com.potentialServices.passwordmanager.R

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
                    alertDialogBuilder.setMessage(context.getString(R.string.this_message_requires_you_to_grant_image_access_permissions_to_work_correctly))
                    alertDialogBuilder.setTitle(context.getString(R.string.permission_required))
                        .setCancelable(false)
                        .setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                            // Request Permissions when clicked OK
                            ActivityCompat.requestPermissions(
                                activity,
                                permissionsToRequest.toTypedArray(),
                                PERMISSION_REQUEST_CODE_MEDIA_IMAGES
                            )
                            dialog.dismiss()
                        }
                        .setNegativeButton(context.getString(R.string.cancel)) { dialog, which ->
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
                        context.getString(R.string.permissions_granted_now_you_can_proceed_with_document_access),
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
                        context.getString(R.string.permissions_denied_gracefully_degrade_the_feature),
                        Toast.LENGTH_SHORT
                    ).show()

                    // If the feature is heavily dependent on the permissions,
                    // then the user should be directed to the settings page to allow the feature from there

                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(
                        context.getString(R.string.this_feature_is_unavailable_since_it_requires_you_to_grant_image_access_permissions_to_work_correctly_please_go_to_settings_and_grant_the_image_access_permissions)
                    )
                    builder.setTitle(context.getString(R.string.permissions_required))
                        .setCancelable(false)
                        .setNegativeButton(context.getString(R.string.cancel)) { dialog, which ->
                            dialog.dismiss()
                        }
                        .setPositiveButton(context.getString(R.string.settings)) { dialog, which ->
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