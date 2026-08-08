package com.mobile.felix.ticketapp.core.data.database

import com.mobile.felix.ticketapp.core.data.local.entity.EventEntity
import com.mobile.felix.ticketapp.core.domain.Event

object MockDataBase {
    val events = listOf(
        EventEntity(
            id = 2L,
            name = "Festival de Verão 2026",
            date = "15/02/2026",
            location = "Arena Anhembi - São Paulo, SP",
            poster = "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?w=500",
            description = "Grande festival com mais de 10 atrações nacionais e internacionais no palco principal."
        ),

        EventEntity(
            id = 3L,
            name = "Rock Night In Concert",
            date = "20/03/2026",
            location = "Audio Club - SP",
            poster = "https://images.unsplash.com/photo-1501386761578-eac5c94b800a?w=500",
            description = "Uma noite inesquecível com os maiores clássicos do rock dos anos 80 e 90."
        ),

        EventEntity(
            id = 4L,
            name = "Conferência Internacional de Tecnologia, Inovação e Inteligência Artificial 2026",
            date = "10/05/2026",
            location = "Centro de Convenções Pro Magno - São Paulo, SP",
            poster = "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?w=500",
            description = "Evento focado nas novas tendências do mercado de desenvolvimento, arquitetura de software, computação em nuvem e novos modelos de IA."
        ),

        EventEntity(
            id = 5L,
            name = "Noite do Humour - Stand-up Comedy",
            date = "05/06/2026",
            location = "Teatro Bradesco",
            poster = "https://images.unsplash.com/photo-1585699324551-f6c309eedeca?w=500",
            description = "Apresentação única dos melhores comediantes da cena nacional."
        )
    )
}