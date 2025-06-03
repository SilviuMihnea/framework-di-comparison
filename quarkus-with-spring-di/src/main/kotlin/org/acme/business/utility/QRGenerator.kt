package org.acme.business.utility

fun interface QRGenerator {
    suspend fun generate(): QR
}

class QR
