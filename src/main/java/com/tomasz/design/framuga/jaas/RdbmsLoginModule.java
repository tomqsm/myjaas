package com.tomasz.design.framuga.jaas;

/* Java imports */
import java.util.*;
import java.sql.*;

/* Security & JAAS imports */
import javax.security.auth.spi.LoginModule;
import javax.security.auth.login.LoginException;
import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.FailedLoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * RdbmsLoginModule is a LoginModule that authenticates a given
 * username/password credential against a JDBC datasource.
 *
 * <p>
 * This <code>LoginModule</code> interoperates with any conformant JDBC
 * datasource. To direct this <code>LoginModule</code> to use a specific JNDI
 * datasource, two options must be specified in the login
 * <code>Configuration</code> for this <code>LoginModule</code>.
 * <pre>
 *    url=<b>jdbc_url</b>
 *    driverb>jdbc driver class</b>
 * </pre>
 *
 * <p>
 * For the purposed of this example, the format in which the user's information
 * must be stored in the database is in a table named "USER_AUTH" with the
 * following columns
 * <pre>
 *     USERID
 *     PASSWORD
 *     FIRST_NAME
 *     LAST_NAME
 *     DELETE_PERM
 *     UPDATE_PERM
 * </pre>
 *
 * @see javax.security.auth.spi.LoginModule
 * @author Paul Feuer and John Musser
 * @version 1.0
 */
public class RdbmsLoginModule implements LoginModule {

    private static final Logger logger = LoggerFactory.getLogger(RdbmsLoginModule.class);

    // initial state
    private CallbackHandler callbackHandler;
    private Subject subject;
    private Map sharedState;
    private Map options;

    // temporary state
    private final List tempCredentials;
    private final List <String> roles;

    // the authentication status
    private boolean success;

    // configurable options
    private boolean debug;
    private String url;
    private String driverClass;

    private UserPrincipal userPrincipal;
    private RolePrincipal rolePrincipal;

    /**
     * <p>
     * Creates a login module that can authenticate against a JDBC datasource.
     */
    public RdbmsLoginModule() {
        tempCredentials = new ArrayList();
        roles = new ArrayList<>();
        success = false;
        debug = false;
    }

    /**
     * Initialize this <code>LoginModule</code>.
     *
     * <p>
     *
     * @param subject the <code>Subject</code> to be authenticated.
     * <p>
     *
     * @param callbackHandler a <code>CallbackHandler</code> for communicating
     * with the end user (prompting for usernames and passwords, for example).
     * <p>
     *
     * @param sharedState shared <code>LoginModule</code> state.
     * <p>
     *
     * @param options options specified in the login <code>Configuration</code>
     * for this particular <code>LoginModule</code>.
     */
    @Override
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options) {
        boolean inst = callbackHandler instanceof UsernamePasswordCallbackHandler;
        logger.debug("callback handler type of ConsoleCredentialSetterCallbackHandler: {}, actual: {}", inst, callbackHandler.getClass().getName());
        // save the initial state
        this.callbackHandler = callbackHandler; //SecureCallbackHandler inner of LoginContext
        this.subject = subject;
        this.sharedState = sharedState;
        this.options = options;

        // initialize any configured options
        if (options.containsKey("debug")) {
            debug = "true".equalsIgnoreCase((String) options.get("debug"));
        }

        url = (String) options.get("url");
        driverClass = (String) options.get("driver");
        logger.debug("Found options - url:{}, driverClass:{}", url, driverClass);
    }

    /**
     * <p>
     * Verify the password against the relevant JDBC datasource.
     *
     * @return true always, since this <code>LoginModule</code> should not be
     * ignored.
     *
     * @exception FailedLoginException if the authentication fails.
     * <p>
     *
     * @exception LoginException if this <code>LoginModule</code> is unable to
     * perform the authentication.
     */
    @Override
    public boolean login() throws LoginException {
        logger.debug("Starting login.");
        try {
            // Setup default callback handlers.
            Callback[] callbacks = new Callback[]{
                new NameCallback(""),
                new PasswordCallback("", false)
            };
            callbackHandler.handle(callbacks);
            final NameCallback nameCallback = (NameCallback) callbacks[0];
            final PasswordCallback passwordCcallback = (PasswordCallback) callbacks[1];

            String username = nameCallback.getName();
            String password = new String(passwordCcallback.getPassword());

            passwordCcallback.clearPassword();

//            success = rdbmsValidate(username, password);
            success = username.equals("t@h.com") && password.equals("pass");
            userPrincipal = new UserPrincipal(username);
// store username and roles to be used in commit()
            roles.add("admin");
            callbacks[0] = null;
            callbacks[1] = null;

            if (!success) {
                throw new LoginException("Authentication failed: Password does not match");
            }

            return (true);
        } catch (LoginException ex) {
            logger.error("{}", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            success = false;
            throw new LoginException(ex.getMessage());
        }
    }

    /**
     * Abstract method to commit the authentication process (phase 2).
     *
     * <p>
     * This method is called if the LoginContext's overall authentication
     * succeeded (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
     * LoginModules succeeded).
     *
     * <p>
     * If this LoginModule's own authentication attempt succeeded (checked by
     * retrieving the private state saved by the <code>login</code> method),
     * then this method associates a <code>RolePrincipal</code> with the
     * <code>Subject</code> located in the <code>LoginModule</code>. If this
     * LoginModule's own authentication attempted failed, then this method
     * removes any state that was originally saved.
     *
     * <p>
     *
     * @exception LoginException if the commit fails
     *
     * @return true if this LoginModule's own login and commit attempts
     * succeeded, or false otherwise.
     */
    @Override
    public boolean commit() throws LoginException {
        logger.debug("Begins commit.");
        if (success) {

            if (subject.isReadOnly()) {
                throw new LoginException("Subject is Readonly");
            }
            subject.getPrincipals().add(userPrincipal);
            try {
                for(String role : roles){
                    logger.debug("principal: {}", role);
                    rolePrincipal = new RolePrincipal(role);
                    subject.getPrincipals().add(rolePrincipal);
                }
                roles.clear();
                if (callbackHandler instanceof PassiveCallbackHandler) {
                    ((PassiveCallbackHandler) callbackHandler).clearPassword();
                }
                return (true);
            } catch (Exception ex) {
                ex.printStackTrace(System.out);
                logger.error("Exception 12: {}", ex.getMessage());
                throw new LoginException(ex.getMessage());
            }
        } else {
            roles.clear();
            tempCredentials.clear();
            return (true);
        }
    }

    /**
     * <p>
     * This method is called if the LoginContext's overall authentication
     * failed. (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL
     * LoginModules did not succeed).
     *
     * <p>
     * If this LoginModule's own authentication attempt succeeded (checked by
     * retrieving the private state saved by the <code>login</code> and
     * <code>commit</code> methods), then this method cleans up any state that
     * was originally saved.
     *
     * <p>
     *
     * @exception LoginException if the abort fails.
     *
     * @return false if this LoginModule's own login and/or commit attempts
     * failed, and true otherwise.
     */
    @Override
    public boolean abort() throws javax.security.auth.login.LoginException {
//        logger.debug("abort");
//        // Clean out state
//        success = false;
//
//        roles.clear();
//        tempCredentials.clear();
//
//        if (callbackHandler instanceof PassiveCallbackHandler) {
//            ((PassiveCallbackHandler) callbackHandler).clearPassword();
//        }
//
//        logout();

        return false;
    }

    /**
     * Logout a user.
     *
     * <p>
     * This method removes the Principals that were added by the
     * <code>commit</code> method.
     *
     * <p>
     *
     * @exception LoginException if the logout fails.
     *
     * @return true in all cases since this <code>LoginModule</code> should not
     * be ignored.
     */
    @Override
    public boolean logout() throws javax.security.auth.login.LoginException {
        logger.debug("Starting logout.");
//        roles.clear();
//        tempCredentials.clear();
//
//        if (callbackHandler instanceof PassiveCallbackHandler) {
//            ((PassiveCallbackHandler) callbackHandler).clearPassword();
//        }
//
//        // remove the principals the login module added
//        Iterator it = subject.getPrincipals(RolePrincipal.class).iterator();
//        while (it.hasNext()) {
//            RolePrincipal p = (RolePrincipal) it.next();
//            logger.debug("removing principal: {}", p.toString());
//            subject.getPrincipals().remove(p);
//        }
//
//        // remove the credentials the login module added
//        it = subject.getPublicCredentials(RdbmsCredential.class).iterator();
//        while (it.hasNext()) {
//            RdbmsCredential c = (RdbmsCredential) it.next();
//            logger.debug("removing credential: {}", c.toString());
//            subject.getPrincipals().remove(c);
//        }

        return (true);
    }

    /**
     * Validate the given user and password against the JDBC datasource.
     * <p>
     *
     * @param user the username to be authenticated.
     * <p>
     * @param pass the password to be authenticated.
     * <p>
     * @exception Exception if the validation fails.
     */
    private boolean rdbmsValidate(String user, String pass) throws Exception {

        Connection con;
        String query = "SELECT * FROM login.USER_AUTH where userid='" + user + "'";
        Statement stmt;
        RolePrincipal p = null;
        RdbmsCredential c = null;
        boolean passwordMatch = false;
//        final ClientDataSource ds = new ClientDataSource();
//        ds.setDatabaseName("/Users/toks/Documents/NetBeansProjects/kroniker/" + dbname);
//        ds.setServerName("localhost");
//        ds.setCreateDatabase("create");
//        ds.setPortNumber(1527);

        try {
            Class.forName(driverClass);
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
            throw new LoginException("Database driver class not found: " + driverClass);
        }

        try {
            logger.debug("Trying to connect");
            con = DriverManager.getConnection(url, "tumcyk", "Drzewko74");
            logger.debug("Connected.");
            stmt = con.createStatement();
            logger.debug("Query: {}", query);

            ResultSet result = stmt.executeQuery(query);
            String dbPassword = null, dbFname = null, dbLname = null;
            String updatePerm = null, deletePerm = null;
            boolean isEqual = false;

            while (result.next()) {
                if (!result.isFirst()) {
                    throw new LoginException("Ambiguous user (located more than once): " + user);
                }
                dbPassword = result.getString(result.findColumn("password"));
                dbFname = result.getString(result.findColumn("first_name"));
                dbLname = result.getString(result.findColumn("last_name"));
                deletePerm = result.getString(result.findColumn("delete_perm"));
                updatePerm = result.getString(result.findColumn("update_perm"));
            }

            if (dbPassword == null) {
                throw new LoginException("User " + user + " not found");
            }
            passwordMatch = pass.equals(dbPassword);
            if (passwordMatch) {
                logger.debug("passwords match");
                c = new RdbmsCredential();
                c.setProperty("delete_perm", deletePerm);
                c.setProperty("update_perm", updatePerm);
                this.tempCredentials.add(c);
//                this.roles.add(new RolePrincipal(dbFname + " " + dbLname));
            } else {
                logger.debug("passwords dont match");
            }
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            logger.error("SQLException: {}", ex.getMessage());
            throw new LoginException("SQLException: " + ex.getMessage());
        }
        return (passwordMatch);
    }
}
