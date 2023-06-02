package passwordsecurity2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;



public class Hashing {
    public static String toHex(String salt, String heslo) throws NoSuchAlgorithmException {
        String password = salt + heslo + salt;

        MessageDigest digest = MessageDigest.getInstance("SHA-512"); //HASHOVANIE SHA-512
        byte[] encodedhash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder(); 

        for (byte b : encodedhash) {
            sb.append(String.format("%02X", b)); //PREVOD NA HEXA
        }
        
        return sb.toString();
    }
}


