package com.lee.permissionrequestsample

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val ps = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE)
    private val rcPerm = 123
    private val rcSet = 124

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            checkPermissions()
        }
    }

    @TargetApi(23)
    private fun checkPermissions() {
        if (hasPermissions(ps)) {
            doWork()
        } else {
            requestPermissions(ps, rcPerm)
        }
    }

    private fun doWork() {
        Toast.makeText(this, "We got it!", Toast.LENGTH_SHORT).show()
    }

    @TargetApi(23)
    private fun showRelationDialog(permissions: Array<String>) {
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.rationale)
                .setMessage(permissions.toCustomString())
                .setPositiveButton(R.string.ok) { _, _ ->
                    requestPermissions(permissions, rcPerm)
                }
                .setNegativeButton(R.string.cancel_and_exit) { _, _ ->
                    finish()
                }
                .create()
                .show()
    }

    private fun showSettingDialog(permissions: Array<String>) {
        AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.warn)
                .setMessage(permissions.toCustomString())
                .setPositiveButton(R.string.set) { _, _ ->
                    // go to app settings page
                    val intent = Intent().apply {
                        action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                        data = Uri.fromParts("package", packageName, null)
                    }
                    startActivityForResult(intent, rcSet)
                }
                .setNegativeButton(R.string.cancel_and_exit) { _, _ ->
                    finish()
                }
                .create()
                .show()
    }

    @TargetApi(23)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == rcPerm) {
            if (hasPermissions(ps)) {
                doWork()
            } else {
                val rationalePerms = ArrayList<String>()
                for (p in ps) {
                    if (shouldShowRequestPermissionRationale(p)) {
                        rationalePerms.add(p)
                    }
                }
                // at least one permission was refused before
                // but hasn't clicked 'no longer notify' yet
                if (rationalePerms.isNotEmpty()) {
                    showRelationDialog(rationalePerms.toTypedArray())
                    return
                }

                val refusedPerms = ArrayList<String>()
                for (p in ps) {
                    if (!hasPermissions(arrayOf(p))) {
                        refusedPerms.add(p)
                    }
                }
                showSettingDialog(refusedPerms.toTypedArray())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == rcSet) {
            checkPermissions()
        }
    }
}
