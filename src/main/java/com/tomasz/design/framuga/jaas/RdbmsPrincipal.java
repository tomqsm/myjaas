package com.tomasz.design.framuga.jaas;

import java.io.Serializable;
import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Basic implementation of the Principal class. By implementing our own
 * Principal for our application, we can more easily add and remove instances of
 * our principals in the authenticated Subject during the login and logout
 * process.
 *
 * @see java.security.Principal
 * @author Paul Feuer and John Musser
 * @version 1.0
 */
public class RdbmsPrincipal implements Principal, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(RdbmsPrincipal.class);

    private final String name;

    /**
     * Create a <code>RdbmsPrincipal</code> with no user name.
     *
     */
    public RdbmsPrincipal() {
        name = "";
    }

    /**
     * Create a <code>RdbmsPrincipal</code> using a <code>String</code>
     * representation of the user name.
     *
     * <p>
     *
     * @param newName
     *
     */
    public RdbmsPrincipal(String newName) {
        name = newName;
    }

    /**
     * Compares the specified Object with this <code>RdbmsPrincipal</code> for
     * equality. Returns true if the given object is also a
     * <code>RdbmsPrincipal</code> and the two RdbmsPrincipals have the same
     * name.
     *
     * <p>
     *
     * @param o Object to be compared for equality with this
     * <code>RdbmsPrincipal</code>.
     *
     * @return true if the specified Object is equal equal to this
     * <code>RdbmsPrincipal</code>.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof RdbmsPrincipal) {
            return ((RdbmsPrincipal) o).getName().equals(name);
        } else {
            return false;
        }
    }

    /**
     * Return a hash code for this <code>RdbmsPrincipal</code>.
     *
     * <p>
     *
     * @return a hash code for this <code>RdbmsPrincipal</code>.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Return a string representation of this <code>RdbmsPrincipal</code>.
     *
     * <p>
     *
     * @return a string representation of this <code>RdbmsPrincipal</code>.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Return the user name for this <code>RdbmsPrincipal</code>.
     *
     * <p>
     *
     * @return the user name for this <code>RdbmsPrincipal</code>
     */
    @Override
    public String getName() {
        return name;
    }
}
