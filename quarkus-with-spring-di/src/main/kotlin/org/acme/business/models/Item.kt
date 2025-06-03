package org.acme.business.models

import java.util.*

data class Item(
    val id: UUID,
    val name: String,
    val price: Float
)