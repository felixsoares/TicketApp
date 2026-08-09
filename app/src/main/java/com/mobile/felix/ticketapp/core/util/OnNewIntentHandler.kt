package com.mobile.felix.ticketapp.core.util

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.util.Consumer

@Composable
fun OnNewIntentHandler(onNewIntent: (Intent) -> Unit) {
    val context = LocalContext.current

    DisposableEffect(context) {
        val activity = context as? ComponentActivity
        val listener = Consumer<Intent> { intent ->
            onNewIntent(intent)
        }

        activity?.addOnNewIntentListener(listener)

        onDispose {
            activity?.removeOnNewIntentListener(listener)
        }
    }
}