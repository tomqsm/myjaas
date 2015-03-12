package com.tomasz.design.framuga;

import java.security.PrivilegedAction;

/**
 *
 * @author toks
 */
class MyPrivilegedAction <String> implements PrivilegedAction<String> {

    public MyPrivilegedAction() {
    }

    public String run() {
        return (String) "proviledged";
    }
    
}
