package com.mobile.felix.ticketapp.core.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobile.felix.ticketapp.R

@Composable
fun ErrorView(
    modifier: Modifier = Modifier
) {
    ErrorContentView(
        modifier = modifier,
        message = stringResource(R.string.generic_error),
    )
}

@Composable
fun ErrorContentView(
    modifier: Modifier = Modifier,
    message: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(end = 16.dp, start = 16.dp)
        )
    }
}