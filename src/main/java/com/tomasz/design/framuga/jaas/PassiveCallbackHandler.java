package com.tomasz.design.framuga.jaas;

/* Java imports */
import java.io.*;
import javax.security.auth.callback.*;

/**
 * <p>
 * PassiveCallbackHandler has constructor that takes a username and password so
 * its handle() method does not have to prompt the user for input. Useful for
 * server-side applications.
 *
 * @author Paul Feuer and John Musser
 * @version 1.0
 */
public class PassiveCallbackHandler implements CallbackHandler {

    private final String username;
    char[] password;

    /**
     * <p>
     * Creates a callback handler with the give username and password.
     *
     * @param user
     * @param pass
     */
    public PassiveCallbackHandler(String user, String pass) {
        this.username = user;
        this.password = pass.toCharArray();
    }

    /**
     * Handles the specified set of Callbacks. Uses the username and password
     * that were supplied to our constructor to popluate the Callbacks.
     *
     * This class supports NameCallback and PasswordCallback.
     *
     * @param callbacks the callbacks to handle
     * @throws IOException if an input or output error occurs.
     * @throws UnsupportedCallbackException if the callback is not an instance
     * of NameCallback or PasswordCallback
     */
    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(username);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(password);
            } else {
                throw new UnsupportedCallbackException(callback, "Callback class not supported");
            }
        }
    }

    /**
     * Clears out password state.
     */
    public void clearPassword() {
        if (password != null) {
            for (int i = 0; i < password.length; i++) {
                password[i] = ' ';
            }
            password = null;
        }
    }

}
