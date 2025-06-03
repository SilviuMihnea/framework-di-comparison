package com.example.business.utility

fun interface QRGenerator {
    suspend fun generate(): QR
}

class QR
