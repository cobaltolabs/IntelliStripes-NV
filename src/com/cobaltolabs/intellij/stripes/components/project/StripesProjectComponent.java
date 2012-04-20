package com.cobaltolabs.intellij.stripes.components.project;

import com.cobaltolabs.intellij.stripes.references.StripesReferenceUtil;
import com.cobaltolabs.intellij.stripes.util.StripesUtil;
import com.intellij.openapi.components.ProjectComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 22:55
 */
public class StripesProjectComponent implements ProjectComponent {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface BaseComponent ---------------------


    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {
        StripesReferenceUtil.URL_BINDING_SEARCHER = null;
        StripesUtil.PSI_CLASS_MAP.clear();
    }

// --------------------- Interface NamedComponent ---------------------


    @NotNull
    @Override
    public String getComponentName() {
        return "Stripes Component Project";
    }

// --------------------- Interface ProjectComponent ---------------------

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }
}
