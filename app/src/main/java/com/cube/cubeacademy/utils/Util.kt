package com.cube.cubeacademy.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


/***
 * Extension function that checks if internet connectivity is active
 * @return  true if device has an active internet connection or
 * false otherwise
 */
fun Context.isOnline(): Boolean {
    val connectivityManager: ConnectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
    return when {
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        else -> false
    }
}
