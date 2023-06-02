//////////////////////////////////////////////////////////////////////////
// TODO:                                                                //
// Uloha1: Do suboru s heslami ulozit aj sal.                           //
// Uloha2: Pouzit vytvorenu funkciu na hashovanie a ulozit heslo        //
//         v zahashovanom tvare.                                        //
//////////////////////////////////////////////////////////////////////////
package passwordsecurity2;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import passwordsecurity2.Database.MyResult;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Registration {
    protected static MyResult registracia(String meno, String heslo) throws NoSuchAlgorithmException, Exception{
        if (Database.exist("hesla.txt", meno)){
            System.out.println("Meno je uz zabrate.");
            return new MyResult(false, "Meno je uz zabrate.");
        }
        else {

            System.out.println(heslo.length());
            if (heslo.length() < 8) {
                return new MyResult(false, "Heslo musí obsahovať aspoň 8 znakov");
            }
            // https://www.geeksforgeeks.org/compare-two-strings-in-java/
            String regex = "^(?=.*[a-z])(?=."
                    + "*[A-Z])(?=.*\\d)"
                    + "(?=.*[-+_!@#$%^&*., ?]).+$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(heslo);

            if (m.matches())
                System.out.println("Yes");
            else
                return new MyResult(false, "Heslo musí obsahovať malé a veľké písmeno, číslicu a špeciálny znak");



            //https://www.baeldung.com/java-file-to-arraylist
            String filename = "passwords.txt";
            Charset charset = Charset.forName("ISO-8859-1");
            List<String> result = Files.readAllLines(Paths.get(filename), charset);
            System.out.println(heslo);

            //https://github.com/danielmiessler/SecLists/blob/master/Passwords/Common-Credentials/10-million-password-list-top-100000.txt
            for(String str: result) {
                if(str. trim(). contains(heslo)){
                    return new MyResult(false, "Heslo sa nesmie nachádzať v zozname najviac používaných hesiel");
                }
            }


            Random random = new Random();
            //GENEROVANIE SALT
            int salt = random.nextInt(Integer.MAX_VALUE - 1) + 1;
            //HASHOVANIE
            Database.add("hesla.txt", meno + ":" + Hashing.toHex(Integer.toString(salt), heslo) + ":" + salt);
        }
        return new MyResult(true, "");
    }
}

