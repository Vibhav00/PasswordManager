package com.potentialServices.passwordmanager.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptionDecryption {
    companion object{

        private fun generateSecretKey(): SecretKeySpec {
            /*** The customSecretKey can be 16, 24 or 32 depending on algorithm you are using* i.e AES-128, AES-192 or AES-256 ***/
            val secretKey = SecretKeySpec(getKey().toByteArray(), "AES")
            return secretKey
        }
        fun generateIV(): IvParameterSpec {
            return IvParameterSpec(getKey().toByteArray())
        }

        fun encrypt(
            textToEncrypt: String,
        ): String {
            val secretKey = generateSecretKey()
            val iv = generateIV()
            val plainText = textToEncrypt.toByteArray()

            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)

            val encrypt = cipher.doFinal(plainText)
            return Base64.encodeToString(encrypt, Base64.DEFAULT)
        }

        fun decrypt(
            encryptedText: String
        ): String {
            val secretKey = generateSecretKey()
            val iv = generateIV()
            val textToDecrypt = Base64.decode(encryptedText, Base64.DEFAULT)

            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")

            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv)

            val decrypt = cipher.doFinal(textToDecrypt)
            return String(decrypt)
        }

        @JvmStatic
        external  fun getKey():String
    }

}
