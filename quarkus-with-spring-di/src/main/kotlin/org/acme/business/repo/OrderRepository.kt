package org.acme.business.repo

import org.acme.business.models.Order
import java.util.*

interface OrderRepository {
    suspend fun save(order: Order)
    suspend fun getById(id: UUID): Order?
}

class DefaultOrderRepository(
    val dbConnection: DBConnection
) : OrderRepository {
    override suspend fun save(order: Order) {
        // do an insert with the connection
    }

    override suspend fun getById(id: UUID): Order? {
        // query with the connection
        return null
    }

}