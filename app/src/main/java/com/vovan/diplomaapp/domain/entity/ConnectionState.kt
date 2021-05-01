package com.vovan.diplomaapp.domain.entity

sealed class ConnectionState {
    object Connecting : ConnectionState()
    object Connected : ConnectionState()
    object Disconnect : ConnectionState()
}