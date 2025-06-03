package com.example.business.repo

import com.example.business.utility.QR
import java.util.*

interface QRRepository {
    suspend fun save(orderId: UUID, qr: QR)
    suspend fun getByOrderId(orderId: UUID): QR
}

class DefaultQRRepository(
    val dbConnection: DBConnection
) : QRRepository {
    override suspend fun save(orderId: UUID, qr: QR) {
        // do an insert with the connection
    }

    override suspend fun getByOrderId(orderId: UUID): QR {
        // query with the connection
        return QR()
    }
}