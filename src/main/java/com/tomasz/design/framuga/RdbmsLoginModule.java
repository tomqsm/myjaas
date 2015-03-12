package com.tomasz.design.framuga;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * java -Djavax.security.suth.login.config=jaas.config
 *
 * @author toks
 */
public class RdbmsLoginModule implements LoginModule {

    final Logger logger = LoggerFactory.getLogger(RdbmsLoginModule.class);

    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;
    private String url;
    private String driverClass;
    private boolean debug;

    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
        url = (String) options.get("url");
        driverClass = (String) options.get("driver");
        debug = "true".equalsIgnoreCase((String) options.get("debug"));
    }

    public boolean login() throws LoginException {
        boolean logged = false;
        logger.info("running :)");
        return logged;
    }

    public boolean commit() throws LoginException {
        boolean commited = false;
        return commited;
    }

    public boolean abort() throws LoginException {
        boolean aborted = false;
        return aborted;
    }

    public boolean logout() throws LoginException {
        boolean loggedout = false;
        return loggedout;
    }

}
