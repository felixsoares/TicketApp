package com.mobile.felix.ticketapp.core.util

import android.content.Context
import android.content.Intent
import android.util.Base64
import androidx.core.net.toUri
import com.mobile.felix.ticketapp.core.service.DeepLinkService

fun startForegroundServiceAndLaunchDeepLink(context: Context, deepLink: String) {
    val serviceIntent = Intent(context, DeepLinkService::class.java)
    context.startService(serviceIntent)

    try {
        val intent = Intent(Intent.ACTION_VIEW, deepLink.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun callToStopService(context: Context) {
    val stopServiceIntent = Intent(context, DeepLinkService::class.java)
    stopServiceIntent.action = "STOP_SERVICE"
    context.startService(stopServiceIntent)
}

fun getBase64(json: String): String {
    val data = json.toByteArray(Charsets.UTF_8)
    return Base64.encodeToString(data, Base64.DEFAULT)
}

fun Intent.queryParameter(name: String) = data?.getQueryParameter(name)

fun String.decodeBase64() = Base64.decode(this, Base64.DEFAULT).let(::String)

fun Intent.deserializeQueryParameter(name: String, block: (String) -> Unit) =
    queryParameter(name)
        ?.decodeBase64()
        ?.let { block(it) }