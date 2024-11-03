package com.potentialServices.passwordmanager.utils

import com.potentialServices.passwordmanager.models.PasswordItem

class PasswordList {
    companion object{

        private fun generatePasswordItems(count: Int): List<PasswordItem> {
            val passwordItems = mutableListOf<PasswordItem>()
            for (i in 1..count) {
                passwordItems.add(
                    PasswordItem(
                        id = i,
                        title = "Title $i",
                        userName = "User$i",
                        website = "https://example$i.com",
                        password = "password$i",
                        description = "Description for password item $i",
                        liked = i % 2 == 0,
                        recent = i % 3 == 0,
                        lastTime = System.currentTimeMillis(),
                        passwordUsed = i * 2
                    )
                )
            }
            return passwordItems
        }

        fun getTenPasswordItems(): List<PasswordItem> {
            return generatePasswordItems(10)
        }

        fun getTwentyPasswordItems(): List<PasswordItem> {
            return generatePasswordItems(20)
        }

        fun getFortyPasswordItems(): List<PasswordItem> {
            return generatePasswordItems(40)
        }
    }

}
