import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public class Hashing {

    public static String[] createNewHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //TODO better random and check for salt copies Random.org for random
        //TODO increase salt length and iterations
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[24];
        random.nextBytes(salt);
        String[] pwparts = createHash(password.toCharArray(), salt);
        return pwparts;
    }

    public static String[] createHash(char[] password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] hash = pbkdf2(password, salt, 1000, 24);
        String var10000 = toHex(salt);
        String[] pwparts = new String[]{var10000, toHex(hash)};
        return pwparts;
    }

    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        return skf.generateSecret(spec).getEncoded();
    }

    private static String toHex(byte[] array) {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = array.length * 2 - hex.length();
        if (paddingLength > 0) {
            String var10000 = String.format("%0" + paddingLength + "d", 0);
            return var10000 + hex;
        } else {
            return hex;
        }
    }
}
