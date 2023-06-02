//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Do suboru s heslami ulozit aj sal.                           //
// Uloha2: Pouzit vytvorenu funkciu na hashovanie a ulozit heslo        //
//         v zahashovanom tvare.                                        //
//////////////////////////////////////////////////////////////////////////
package passwordsecurity2;

import java.security.NoSuchAlgorithmException;
import passwordsecurity2.Database.MyResult;
import java.security.SecureRandom; //PISKOT
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Random;



public class Registration {
    protected static MyResult registracia(String meno, String heslo) throws NoSuchAlgorithmException, Exception{
        if (Database.exist("hesla.txt", meno)){
            System.out.println("Meno je uz zabrate.");
            return new MyResult(false, "Meno je uz zabrate.");
        }
        else {
            Random random = new Random();
			//GENEROVANIE SALT
            int salt = random.nextInt(Integer.MAX_VALUE - 1) + 1;
			//HASHOVANIE
            Database.add("hesla.txt", meno + ":" + Hashing.toHex(Integer.toString(salt), heslo) + ":" + salt);
        }
        return new MyResult(true, "");
    }
    }
    
