package com.cobaltolabs.intellij.stripes.util;

import com.intellij.psi.xml.XmlTag;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 23:18
 */
public abstract class XmlTagContainer<T> {
// ------------------------------ FIELDS ------------------------------

    protected T container;

// --------------------------- CONSTRUCTORS ---------------------------

    public XmlTagContainer(T container) {
        this.container = container;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public T getContainer() {
        return container;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Override this method to implement container-dependent storage.
     *
     * @param tag tag to be stored in internal container.
     */
    public abstract void add(XmlTag tag);
}