package com.tomasz.design.framuga;

import com.tomasz.design.framuga.jaas.ConsoleCallbackHandler;
import java.util.Iterator;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
//        System.setProperty("java.security.auth.login.config", "jaas.config"); // alternative to command line param setting
//        -Djava.security.auth.login.config=file:./jaas.config --for VM arguments
//        login.config.url.1=file:C:/Users/path/to/jaas.config --modify java.security to specify login configuration file
        boolean loginSuccess = false;
        Subject subject = null;

        try {
            ConsoleCallbackHandler cbh = new ConsoleCallbackHandler();

            LoginContext lc = new LoginContext("Example", cbh);

            try {
                lc.login();
                loginSuccess = true;

                subject = lc.getSubject();

                Iterator it = subject.getPrincipals().iterator();
                while (it.hasNext()) {
                    System.out.println("Authenticated: " + it.next().toString());
                }

                it = subject.getPublicCredentials(Properties.class).iterator();
                while (it.hasNext()) {
                    ((Properties) it.next()).list(System.out);
                }

                lc.logout();
            } catch (LoginException lex) {
                System.out.println(lex.getClass().getName() + ": " + lex.getMessage());
            }

        } catch (Exception ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
        }

        System.exit(0);
    }

//    public static void main(String[] args) {
//        LoginContext lc = null;
//        try {
//            lc = new LoginContext("Example");
//            lc.login();
//        } catch (LoginException e) {
//            // Authentication failed.
//            log.info("{}", e.getMessage());
//            System.out.println("Authentication failed.");
//        }
//        // Authentication successful, we can now continue.
//        // We can use the returned Subject if we like.
//        Subject sub = lc != null ? lc.getSubject() : new Subject();
//        Subject.doAs(sub, new MyPrivilegedAction<String>());
//    }
}
