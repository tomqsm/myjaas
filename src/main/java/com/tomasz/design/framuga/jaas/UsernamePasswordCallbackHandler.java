package com.tomasz.design.framuga.jaas;

import java.io.*;
import javax.security.auth.callback.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 UsernamePasswordCallbackHandler prompts and reads from the command
 line console line for username and password. This can be used by a JAAS
 application to instantiate a CallbackHandler
 *
 * @see javax.security.auth.callback
 * @author Paul Feuer and John Musser
 * @version 1.0
 */
public class UsernamePasswordCallbackHandler implements CallbackHandler {

    private static final Logger logger = LoggerFactory.getLogger(UsernamePasswordCallbackHandler.class);
    private String username;
    private String password;

    /**
     * <p>
     * Creates a callback handler that prompts and reads from the command line
     * for answers to authentication questions. This can be used by JAAS
     * applications to instantiate a CallbackHandler.
     */
//    public UsernamePasswordCallbackHandler() {
//    }

    public UsernamePasswordCallbackHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * Handles the specified set of callbacks. This class supports NameCallback
     * and PasswordCallback.
     *
     * @param callbacks the callbacks to handle
     * @throws IOException if an input or output error occurs.
     * @throws UnsupportedCallbackException if the callback is not an instance
     * of NameCallback or PasswordCallback
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        logger.debug("Begining to set values in callbacks.");
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                NameCallback nameCallback = ((NameCallback) callback);
                nameCallback.setName(this.username);
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback passwordCallback = ((PasswordCallback) callback);
                passwordCallback.setPassword(this.password.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback, "Callback class not supported");
            }
        }
        logger.debug("Finished to set values in callbacks.");
    }
}
