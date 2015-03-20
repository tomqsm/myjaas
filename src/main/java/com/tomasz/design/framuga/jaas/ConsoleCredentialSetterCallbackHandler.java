package com.tomasz.design.framuga.jaas;

import java.io.*;
import javax.security.auth.callback.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * ConsoleCredentialSetterCallbackHandler prompts and reads from the command
 * line console line for username and password. This can be used by a JAAS
 * application to instantiate a CallbackHandler
 *
 * @see javax.security.auth.callback
 * @author Paul Feuer and John Musser
 * @version 1.0
 */
public class ConsoleCredentialSetterCallbackHandler implements CallbackHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleCredentialSetterCallbackHandler.class);

    /**
     * <p>
     * Creates a callback handler that prompts and reads from the command line
     * for answers to authentication questions. This can be used by JAAS
     * applications to instantiate a CallbackHandler.
     */
    public ConsoleCredentialSetterCallbackHandler() {
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
                logger.debug(nameCallback.getPrompt());
                String user = (new BufferedReader(new InputStreamReader(System.in))).readLine();
                nameCallback.setName(user);
            } else if (callback instanceof PasswordCallback) {
                PasswordCallback passwordCallback = ((PasswordCallback) callback);
                logger.debug(passwordCallback.getPrompt());
                String pass = (new BufferedReader(new InputStreamReader(System.in))).readLine();
                passwordCallback.setPassword(pass.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback, "Callback class not supported");
            }
        }
        logger.debug("Finished to set values in callbacks.");
    }
}
