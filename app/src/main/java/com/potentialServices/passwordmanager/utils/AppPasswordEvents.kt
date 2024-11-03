package com.potentialServices.passwordmanager.utils

import java.io.Serializable

enum class AppPasswordEvents : Serializable{
    CREATE_PASSWORD,
    CHECK_PASSWORD,
    CHECK_PIN,
    CREATE_PIN,
    CHECK_FINGERPRINT,
    DEFAULT
}