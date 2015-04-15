package com.tomasz.design.framuga.jaas;

import java.util.Iterator;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author toks
 */
public class RdbmsLoginModuleTest {

    public RdbmsLoginModuleTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSomeMethod() {
        System.setProperty("java.security.auth.login.config", "jaas.config"); // alternative to command line param setting
//        -Djava.security.auth.login.config=file:./jaas.config --for VM arguments
//        login.config.url.1=file:C:/Users/path/to/jaas.config --modify java.security to specify login configuration file
        boolean loginSuccess = false;
        Subject subject = null;

        try {
            UsernamePasswordCallbackHandler cbh = new UsernamePasswordCallbackHandler("", "");

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

        } catch (LoginException ex) {
            System.out.println(ex.getClass().getName() + ": " + ex.getMessage());
        }

    }

}
