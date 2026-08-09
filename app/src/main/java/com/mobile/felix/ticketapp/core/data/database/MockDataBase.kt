package com.mobile.felix.ticketapp.core.data.database

import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity
import com.mobile.felix.ticketapp.core.data.local.entity.OrderEntity
import com.mobile.felix.ticketapp.core.domain.model.OrderStatus

object MockDataBase {
    val events = listOf(
        EventEntity(
            id = 2L,
            name = "Festival de Verão 2026",
            date = "15/02/2026",
            location = "Arena Anhembi - São Paulo, SP",
            price = 250.00,
            poster = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500",
            description = "Grande festival com mais de 10 atrações nacionais e internacionais no palco principal."
        ),

        EventEntity(
            id = 3L,
            name = "Rock Night In Concert",
            date = "20/03/2026",
            location = "Audio Club - SP",
            price = 100.00,
            poster = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=500",
            description = "Uma noite inesquecível com os maiores clássicos do rock dos anos 80 e 90."
        ),

        EventEntity(
            id = 4L,
            name = "Conferência Internacional de Tecnologia, Inovação e Inteligência Artificial 2026",
            date = "10/05/2026",
            price = 50.00,
            location = "Centro de Convenções Pro Magno - São Paulo, SP",
            poster = "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?w=500",
            description = "Evento focado nas novas tendências do mercado de desenvolvimento, arquitetura de software, computação em nuvem e novos modelos de IA."
        ),

        EventEntity(
            id = 5L,
            name = "Noite do Humour - Stand-up Comedy",
            date = "05/06/2026",
            price = 75.00,
            location = "Teatro Bradesco",
            poster = "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?w=500",
            description = "Apresentação única dos melhores comediantes da cena nacional."
        )
    )

    /*val orders = listOf(
        OrderEntity(
            id = 1L,
            eventId = 2L,
            eventName = "Festival de Verão 2026",
            eventDate = "15/02/2026",
            amount = 240.0,
            purchaseDate = "10/01/2026",
            ticketQuantity = 2,
            status = OrderStatus.APPROVED.name
        ),
        OrderEntity(
            id = 2L,
            eventId = 3L,
            eventName = "Rock Night In Concert",
            eventDate = "20/03/2026",
            amount = 180.0,
            purchaseDate = "15/02/2026",
            ticketQuantity = 1,
            status = OrderStatus.DENIED.name
        ),
        OrderEntity(
            id = 3L,
            eventId = 5L,
            eventName = "Noite do Humour - Stand-up Comedy",
            eventDate = "05/06/2026",
            amount = 150.0,
            purchaseDate = "01/03/2026",
            ticketQuantity = 3,
            status = OrderStatus.CANCELLED.name
        )
    )*/
}