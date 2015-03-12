package com.tomasz.design.framuga;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {
    public static final Logger log = LoggerFactory.getLogger(App.class);

    

    public static void main(String[] args) {
        LoginContext lc = null;
        try {
            lc = new LoginContext("Example");
            lc.login();
        } catch (LoginException e) {
            // Authentication failed.
            log.info("{}", e.getMessage());
            System.out.println("Authentication failed.");
        }
        // Authentication successful, we can now continue.
        // We can use the returned Subject if we like.
        Subject sub = lc != null ? lc.getSubject() : new Subject();
        Subject.doAs(sub, new MyPrivilegedAction<String>());
    }
}
