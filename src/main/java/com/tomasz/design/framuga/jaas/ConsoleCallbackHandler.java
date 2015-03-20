package com.tomasz.design.framuga.jaas;

import java.io.*;
import javax.security.auth.callback.*;

/**
 * <p>
 * ConsoleCallbackHandler prompts and reads from the 
 * command line console line for username and password.
 * This can be used by a JAAS application to instantiate a
 * CallbackHandler
 *
 * @see     javax.security.auth.callback
 * @author  Paul Feuer and John Musser
 * @version 1.0
 */

public class ConsoleCallbackHandler implements CallbackHandler {

    /**
     * <p>Creates a callback handler that prompts and reads from the
     * command line for answers to authentication questions.
     * This can be used by JAAS applications to instantiate a
     * CallbackHandler.
     */
    public ConsoleCallbackHandler() {
    }

    /**
     * Handles the specified set of callbacks.
     * This class supports NameCallback and PasswordCallback.
     *
     * @param   callbacks the callbacks to handle
     * @throws  IOException if an input or output error occurs.
     * @throws  UnsupportedCallbackException if the callback is not an
     * instance of NameCallback or PasswordCallback
     */
    @Override
    public void handle(Callback[] callbacks) 
        throws java.io.IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                System.out.print(((NameCallback) callback).getPrompt());
                String user=(new BufferedReader(new InputStreamReader(System.in))).readLine();
                ((NameCallback) callback).setName(user);
            } else if (callback instanceof PasswordCallback) {
                System.out.print(((PasswordCallback) callback).getPrompt());
                String pass=(new BufferedReader(new InputStreamReader(System.in))).readLine();
                ((PasswordCallback) callback).setPassword(pass.toCharArray());
            } else {
                throw new UnsupportedCallbackException(callback, "Callback class not supported");
            }
        }
    }
}

