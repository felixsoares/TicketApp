package com.mobile.felix.ticketapp.core.domain

data class Event(
    val id: Long,
    val name: String,
    val date: String,
    val location: String,
    val poster: String = "https://www.jbl.com.br/on/demandware.static/-/Sites-masterCatalog_Harman/default/dwc1354bcd/pdp/JBL_PartyBox300_Black_Lifestyle01.png",
    val description: String,
)
