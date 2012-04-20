package com.cobaltolabs.intellij.stripes.references.contributors;

import com.cobaltolabs.intellij.stripes.references.filters.OnwardResolutionConstructorFilter;
import com.cobaltolabs.intellij.stripes.references.providers.LayoutComponentReferenceProvider;
import com.intellij.javaee.web.WebRoot;
import com.intellij.javaee.web.WebUtil;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.css.impl.util.CssInHtmlClassOrIdReferenceProvider;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.impl.source.jsp.WebDirectoryUtil;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.WebPathReferenceProvider;
import com.intellij.util.Function;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static com.cobaltolabs.intellij.stripes.references.contributors.ContributorUtil.registerXmlAttributeReferenceProvider;
import static com.cobaltolabs.intellij.stripes.util.StripesConstants.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 22:22
 */
public class WebReferenceContributor extends PsiReferenceContributor {
// -------------------------- OTHER METHODS --------------------------

    @Override
    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        registerXmlAttributeReferenceProvider(registrar, new WebPathReferenceProvider(), NAME_ATTR, LAYOUT_RENDER_TAG);
        registerXmlAttributeReferenceProvider(registrar, new LayoutComponentReferenceProvider(), NAME_ATTR, LAYOUT_COMPONENT_TAG);
        registerXmlAttributeReferenceProvider(registrar, new CssInHtmlClassOrIdReferenceProvider(), CLASS_ATTR, CLASS_TAGS);
        registerXmlAttributeReferenceProvider(registrar, new WebPathReferenceProvider(), SRC_ATTR, IMAGE_TAG);

        registrar.registerReferenceProvider(PlatformPatterns.psiElement(PsiLiteralExpression.class).and(new FilterPattern(new OnwardResolutionConstructorFilter(1))),
                new WebPathReferenceProvider(true, false, false) {
                    @NotNull
                    public PsiReference[] getReferencesByElement(@NotNull final PsiElement element, @NotNull ProcessingContext context) {
                        FileReferenceSet set = FileReferenceSet.createSet(element, false, false, false);
                        set.addCustomization(FileReferenceSet.DEFAULT_PATH_EVALUATOR_OPTION, new Function<PsiFile, Collection<PsiFileSystemItem>>() {
                            public Collection<PsiFileSystemItem> fun(PsiFile psiFile) {
                                WebFacet webFacet = WebUtil.getWebFacet(element);
                                if (null == webFacet) return new ArrayList<PsiFileSystemItem>(0);

                                Collection<PsiFileSystemItem> retval = new ArrayList<PsiFileSystemItem>(8);
                                for (WebRoot webRoot : webFacet.getWebRoots(true)) {
                                    retval.add(WebDirectoryUtil.getWebDirectoryUtil(element.getProject()).findWebDirectoryElementByPath(webRoot.getRelativePath(),
                                            webFacet
                                    ));
                                }
                                return retval;
                            }
                        });

                        //TODO add references to servlets declared in web.xml
                        return set.getAllReferences();
                    }
                }
        );
    }
}
