package com.tomasz.design.framuga.jaas;

/* Java imports */
import java.util.*;

/* JAAS imports */
import javax.security.auth.*;
import javax.security.auth.login.*;

/**
 * <p>
 JaasTest uses JAAS and our custom RdbmsLoginModule 
 and ConsoleCredentialSetterCallbackHandler. Prompts and reads from the 
 command line console line for username/password and
 then authenticates using a JDBC database.
 * 
 * @author  Paul Feuer and John Musser
 * @version 1.0
 */

public class JaasTest {

    public static void main(String[] args) {
//        System.setProperty("java.security.auth.login.config", "jaas.config"); // alternative to command line param setting
//        -Djava.security.auth.login.config=file:./jaas.config --for VM arguments
//        login.config.url.1=file:C:/Users/path/to/jaas.config --modify java.security to specify login configuration file
        boolean loginSuccess = false;
        Subject subject = null;

        try {
            ConsoleCredentialSetterCallbackHandler cbh = new ConsoleCredentialSetterCallbackHandler();

            LoginContext lc = new LoginContext("Example", cbh);

            try {
                lc.login();
                loginSuccess = true;

                subject = lc.getSubject();

                Iterator it = subject.getPrincipals().iterator();
                while (it.hasNext()) 
                    System.out.println("Authenticated: " + it.next().toString());

                it = subject.getPublicCredentials(Properties.class).iterator();
                while (it.hasNext()) 
                    ((Properties)it.next()).list(System.out);

                lc.logout();
            } catch (LoginException lex) {
                System.out.println(lex.getClass().getName() + ": " + lex.getMessage());
            }

        } catch (LoginException ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
        }

        System.exit(0);
    }
}

