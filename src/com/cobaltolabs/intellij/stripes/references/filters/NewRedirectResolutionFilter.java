package com.cobaltolabs.intellij.stripes.references.filters;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.REDIRECT_RESOLUTION;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 19:51
 */
public class NewRedirectResolutionFilter extends NewOnwardResolutionFilter {
// -------------------------- OTHER METHODS --------------------------

    protected String getResolutionClassName() {
        return REDIRECT_RESOLUTION;
    }
}