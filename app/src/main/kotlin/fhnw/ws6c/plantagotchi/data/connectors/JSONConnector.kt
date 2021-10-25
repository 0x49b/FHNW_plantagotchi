package fhnw.ws6c.plantagotchi.data.connectors

import android.app.Activity
import android.Manifest
import android.annotation.SuppressLint
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import com.google.android.gms.location.LocationServices

class JSONConnector(val activity: Activity) {

    private val jsonString = "{}"
    private val PERMISSIONS = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE
    )

    init {
        requestPermissions()
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 10)
    }

    @SuppressLint("MissingPermission")
    fun getJSON(
        onSuccess: (jsonString: String) -> Unit,
        onFailure: (exception: Exception) -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        if(PERMISSIONS.oneOfGranted()){




        }

    }

    private fun getJSONString(){

    }

    private fun Array<String>.oneOfGranted(): Boolean {
        var any = false
        forEach{any = any || it.granted()}
        return any
    }

    private fun String.granted(): Boolean = ActivityCompat.checkSelfPermission(activity, this) == PackageManager.PERMISSION_GRANTED

}