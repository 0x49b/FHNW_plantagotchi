package fhnw.ws6c.plantagotchi.data.connectors

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import fhnw.ws6c.plantagotchi.data.GeoPosition


/**
 * Connector provided by D.Holz from emoba Module
 */
class GPSConnector(val activity: Activity) {

    private val aelggi = GeoPosition(latitude = 46.7976552, longitude = 8.236315, altitude = 1635.0)
    private val PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val locationProvider by lazy { LocationServices.getFusedLocationProviderClient(activity) }

    init {
        requestPermissions()
    }

    fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, 10)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(
        onSuccess: (geoPosition: GeoPosition) -> Unit,
        onFailure: (exception: Exception) -> Unit,
        onPermissionDenied: () -> Unit
    ) {
        if (PERMISSIONS.oneOfGranted()) {
            locationProvider.lastLocation
                .addOnSuccessListener(activity) {
                    // der Emulator liefert null zurueck. In diesem Fall nehmen wir einfach 'brugg'
                    onSuccess.invoke(
                        if (it == null) aelggi else GeoPosition(
                            it.longitude,
                            it.latitude,
                            it.altitude
                        )
                    )
                }
                .addOnFailureListener(activity) {
                    onFailure.invoke(it)
                }
        } else {
            onPermissionDenied.invoke()
        }
    }

    private fun Array<String>.oneOfGranted(): Boolean {
        var any = false
        forEach { any = any || it.granted() }

        return any
    }

    private fun String.granted(): Boolean =
        ActivityCompat.checkSelfPermission(activity, this) == PackageManager.PERMISSION_GRANTED
}