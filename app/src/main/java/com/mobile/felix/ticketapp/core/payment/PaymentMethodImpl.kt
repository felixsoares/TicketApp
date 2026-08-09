package com.mobile.felix.ticketapp.core.payment

import android.content.Context
import com.google.gson.Gson
import com.mobile.felix.ticketapp.R
import com.mobile.felix.ticketapp.core.domain.model.Order
import com.mobile.felix.ticketapp.core.data.payment.request.Item
import com.mobile.felix.ticketapp.core.data.payment.request.OrderRequest
import com.mobile.felix.ticketapp.core.util.getBase64

class PaymentMethodImpl(
    private val context: Context
) : PaymentMethod {

    val reference by lazy { "uriapp #" + (System.currentTimeMillis() / 1000) }
    val callbackUrl by lazy {
        "${context.getString(R.string.intent_scheme)}://${
            context.getString(
                R.string.intent_host
            )
        }"
    }

    override fun paymentRequest(order: Order): String {
        val randomSku: Int = (1000..100000).random()
        val item = Item(
            sku = randomSku.toString(),
            name = order.eventName,
            unitPrice = order.price,
            quantity = order.ticketQuantity,
            unitOfMeasure = "unidade",
            description = "${order.id}",
            details = "${order.id}"
        )
        val items = mutableListOf(item)

        val request = OrderRequest(
            "xxxxxxxxxxxxxxxxx",
            "xxxxxxxxxxxxxxxxxxxxxx",
            order.price * order.ticketQuantity,
            null,
            1,
            "felix@email.br",
            null,
            reference,
            items,
        )

        val json = Gson().toJson(request).toString()
        val base64 = getBase64(json)
        return "lio://payment?request=$base64&urlCallback=$callbackUrl"
    }

}