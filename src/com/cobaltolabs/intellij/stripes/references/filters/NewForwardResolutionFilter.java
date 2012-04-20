package com.cobaltolabs.intellij.stripes.references.filters;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.FORWARD_RESOLUTION;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:50
 */
public class NewForwardResolutionFilter extends NewOnwardResolutionFilter {
// -------------------------- OTHER METHODS --------------------------

    protected String getResolutionClassName() {
        return FORWARD_RESOLUTION;
    }
}