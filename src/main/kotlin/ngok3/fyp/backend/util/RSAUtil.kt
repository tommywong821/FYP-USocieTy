package ngok3.fyp.backend.util

import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import javax.crypto.Cipher

@Component
class RSAUtil {
    private val privateKey: String =
        "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMR/ZfjfylFQBXBTGyQHzFD4Q6F3vGkAJndrcdnmpa5jGVkDswikRIXR8df4QuMMhB8JZOdhWebqj8NakFH/blXMubGa3lyKE8CvcWpPQoe2pK87A6KfB8CwQ8FD9yWsr62JaLDGWBolrmXn3X+Fy5it0Kh5qT0vAlYsGRAF5dSvAgMBAAECgYAZJLwlpm07DmRDxT6Z062fRsVQqgOf4/cIHOWKnmrg76lUrJaCKpLiFMU/f3L/nqBhYFBbEyfv1l/i/XmuJGM3uf4FKVeomltGLFD+ArBCQPEO5TWIVyfW54BYyvnXk5Lqe+q1w6/5JwHLCEfjYsvXgb9rhPMzCXB3deaD+vhTkQJBAOIfVqleBHVxSwRvJENk7bieu01/u53MttbJD22c5aFCiv38l8rjvV5/vZZ2qGMA44nm52p67DQR/XNdxc42ZGUCQQDedf4pGLyi5P4TUJiZ7i3za7wtXWQVbx2mtIEDwec6i4nDAZFebmoedfevpCYeewXOKjBJ5J/Xucw5jnrVEtGDAkArnJ+6SsfRXuh5EnaMCQtQcEzvxZMJ/FHHWz/+hCRF1e+4zN754rLDZx/JxVj0v1cjYcWAY/Tqlg2sEon0G8mBAkBp6b9iDZbx7xhQVdTxpIZYGHj1GDuoQMjbL6ElCtJt+zVRlJODZAo+CmgsRXiolmXDLC7lX2YCrUDgPkeY3mbFAkANdGnc+w1puuChD81sGo7A3uWXzycWoYd9kwMEYcae/sm+NuPlsw9tePQ37cVLQW7+lLvC10CyBXATWKUGS8NT"

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

    fun loadPrivateKey(): PrivateKey {
        val clear: ByteArray = Base64.getDecoder().decode(privateKey.toByteArray())
        val keySpec = PKCS8EncodedKeySpec(clear)
        val fact = KeyFactory.getInstance("RSA")
        val priv = fact.generatePrivate(keySpec)
        Arrays.fill(clear, 0.toByte())
        return priv
    }
}