package ngok3.fyp.backend.util

import org.springframework.stereotype.Component
import java.io.FileInputStream
import java.io.InputStreamReader
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

@Component
class RSAUtil {
    fun decryptMessage(encryptedText: String?):
            String {
        val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey())
        return String(
            cipher.doFinal(
                Base64.getDecoder().decode(encryptedText)
            )
        )
    }

    private fun loadPrivateKey(): PrivateKey {
        val keyText = InputStreamReader(
            FileInputStream("src/main/resources/rsa_private_key.pem")
        ).use {
            it.readText()
                .replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("\n", "")
                .replace("-----END PRIVATE KEY-----", "")
        }
        val encoded = Base64.getDecoder().decode(keyText)
        return KeyFactory.getInstance("RSA")
            .generatePrivate(PKCS8EncodedKeySpec(encoded))
    }
}