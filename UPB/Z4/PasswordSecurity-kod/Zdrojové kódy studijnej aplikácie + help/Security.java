//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Vytvorit funkciu na bezpecne generovanie saltu.              //
// Uloha2: Vytvorit funkciu na hashovanie.                              //
// Je vhodne vytvorit aj dalsie pomocne funkcie napr. na porovnavanie   //
// hesla ulozeneho v databaze so zadanym heslom.                        //
//////////////////////////////////////////////////////////////////////////
package passwordsecurity2;

import java.math.BigInteger;  
import java.security.NoSuchAlgorithmException;  
import java.security.MessageDigest;  

public class Security {
    
    private static String hash(String password) throws Exception{  
        /*
        *   Pred samotnym hashovanim si najskor musite ulozit instanciu hashovacieho algoritmu.
        *   Hash sa uklada ako bitovy retazec, takze ho nasledne treba skonvertovat na String (napr. cez BigInteger);
        
        String hash = "";
        return hash;
        */

        MessageDigest msgDst = MessageDigest.getInstance("MD5");  
  
        // the digest() method is invoked to compute the message digest  
        // from an input digest() and it returns an array of byte  
        byte[] msgArr = msgDst.digest(password.getBytes());  
        
        // getting signum representation from byte array msgArr  
        BigInteger bi = new BigInteger(1, msgArr);  
        
        // Converting into hex value  
        String hshtxt = bi.toString(16);  
        
        while (hshtxt.length() < 32)   
        {  
        hshtxt = "0" + hshtxt;  
        }  
        return hshtxt;  
    }
    
    protected static long getSalt(long min, long max) {
        /*
        *   Salt treba generovat cez secure funkciu.
        */
        long salt = 0;
        return salt;
    }
}

