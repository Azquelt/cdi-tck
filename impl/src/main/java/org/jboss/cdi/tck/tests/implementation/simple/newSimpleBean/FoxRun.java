package org.jboss.cdi.tck.tests.implementation.simple.newSimpleBean;

import jakarta.enterprise.inject.New;
import jakarta.inject.Inject;

public class FoxRun {

    @Inject
    @New
    private Fox newFox;

    public Fox getNewFox() {
        return newFox;
    }

    @Inject
    private Fox newFox2;

    public Fox getNewFox2() {
        return newFox2;
    }

    @Inject
    private Fox fox;

    public Fox getFox() {
        return fox;
    }

}
