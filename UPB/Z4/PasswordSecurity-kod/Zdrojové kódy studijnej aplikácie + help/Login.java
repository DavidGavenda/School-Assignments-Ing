//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha2: Upravte funkciu na prihlasovanie tak, aby porovnavala        //
//         heslo ulozene v databaze s heslom od uzivatela po            //
//         potrebnych upravach.                                         //
// Uloha3: Vlozte do prihlasovania nejaku formu oneskorenia.            //
//////////////////////////////////////////////////////////////////////////
package passwordsecurity2;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom; //PISKOT
import java.security.spec.KeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
// import javax.crypto.BCryptPasswordEncoder;
import java.time.Duration;
import java.time.Instant;

import passwordsecurity2.Database.MyResult;

public class Login {

    protected static MyResult prihlasovanie(String meno, String heslo) throws IOException, Exception{
        MyResult account = Database.find("hesla.txt", meno);
        if (!account.getFirst()){
            return new MyResult(false, "Nespravne meno.");
        }
        else {
            StringTokenizer st = new StringTokenizer(account.getSecond(), ":");
            st.nextToken();      //prvy token je prihlasovacie meno
            String hash512 = st.nextToken(); //HESLO
            String salt = st.nextToken(); //SALT
            boolean rightPassword = Hashing.toHex(salt, heslo).equals(hash512); // KONTROLA HESIEL
            if (!rightPassword) {
                Thread.sleep(100); //DELAY pred kazdym prihlasenim
                return new MyResult(false, "Nespravne heslo.");
            }
        }
        return new MyResult(true, "Uspesne prihlasenie.");
    }
}

