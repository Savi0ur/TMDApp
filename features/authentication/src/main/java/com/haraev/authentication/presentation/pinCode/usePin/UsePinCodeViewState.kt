package com.haraev.authentication.presentation.pinCode.usePin

data class UsePinCodeViewState(
    val showProgressBar: Boolean = false,
    val pinCode: String = "",
    val userName: String = ""
)