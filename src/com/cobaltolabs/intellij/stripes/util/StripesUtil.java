package com.cobaltolabs.intellij.stripes.util;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.jsp.JspDirectiveKind;
import com.intellij.psi.jsp.JspFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PropertyUtil;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiUtil;
import com.intellij.psi.xml.XmlTag;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.util.containers.WeakHashMap;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.text.MessageFormat;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 7/12/11
 *         Time: 9:00
 */

public final class StripesUtil {
// ------------------------------ FIELDS ------------------------------

    /**
     * Cache for resolving PsiClasses by FQN.
     */
    public static Map<String, PsiClass> PSI_CLASS_MAP = new WeakHashMap<String, PsiClass>(8);

    //  Methods for working with i18n and formatting.

    private static ResourceBundle stripesBundle = ResourceBundle.getBundle("resources.Stripes");

// -------------------------- STATIC METHODS --------------------------

    /**
     * Get a Module
     *
     * @param psiElement PsiElement
     * @return Module
     */
    public static Module getModule(PsiElement psiElement) {
        try {
            return ModuleUtil.findModuleForPsiElement(psiElement);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Create a HyperLink
     *
     * @param text Text
     * @param url  Url
     * @return a HyperLinkLaber, when the user Click on it IntelliJ Open a Navigator with the URL
     */
    public static HyperlinkLabel createLink(final String text, final @NonNls String url) {
        final HyperlinkLabel link = new HyperlinkLabel(text);
        link.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                BrowserUtil.launchBrowser(url);
            }
        });
        return link;
    }

    /**
     * This Jsp have a Stripes Taglib declared
     *
     * @param psiFile JspFile
     * @return true or false
     */

    public static boolean isStripesPage(PsiElement psiFile) {
        if (!(psiFile instanceof JspFile)) return false;

        for (XmlTag tag : ((JspFile) psiFile).getDirectiveTagsInContext(JspDirectiveKind.TAGLIB)) {
            if (tag.getAttributeValue(StripesConstants.URI_ATTR) != null
                    && tag.getAttributeValue(StripesConstants.URI_ATTR).startsWith(StripesConstants.TAGLIB_PREFIX)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Walks up XML tree to find and return parent element of XML tag matching criteria.
     *
     * @param childTag     start element of XML tree
     * @param stopFilter   filter triggering stop of walking up
     * @param returnFilter filter allowing return pf found XML tag
     * @return parent {@link com.intellij.psi.xml.XmlTag} or null
     */
    public static XmlTag findParent(XmlTag childTag, PsiElementFilter stopFilter, PsiElementFilter returnFilter) {
        for (XmlTag tag = childTag.getParentTag(); tag != null; tag = tag.getParentTag()) {
            if (stopFilter.isAccepted(tag)) {
                return returnFilter.isAccepted(tag) ? tag : null;
            }
        }
        return null;
    }

    /**
     * Processes XML tree and return child tag matching criteria.
     *
     * @param rootTag start element of XML treed
     * @param filter  filter triggering return of current tag
     * @return {@link com.intellij.psi.xml.XmlTag} or null
     */
    public static XmlTag findTag(XmlTag rootTag, PsiElementFilter filter) {
        if (filter.isAccepted(rootTag)) {
            return rootTag;
        } else {
            for (XmlTag tag : rootTag.getSubTags()) {
                XmlTag t = findTag(tag, filter);
                if (null != t) return t;
            }
        }
        return null;
    }

    /**
     * Processes XML tree and collects tags matching criteria.
     *
     * @param rootTag      root of XML tree
     * @param stopFilter   filter triggering stop current tag children processing
     * @param incudeFilter filter triggering collecting of current tag
     * @param container    container to collect tags that match criteria
     * @return container
     */
    public static <T> XmlTagContainer<T> collectTags(@NotNull XmlTag rootTag, @NotNull PsiElementFilter stopFilter,
                                                     @NotNull PsiElementFilter incudeFilter, @NotNull XmlTagContainer<T> container) {
        if (stopFilter.isAccepted(rootTag)) {
            if (incudeFilter.isAccepted(rootTag)) {
                container.add(rootTag);
            }
        } else {
            for (XmlTag tag : rootTag.getSubTags()) {
                collectTags(tag, stopFilter, incudeFilter, container);
            }
        }
        return container;
    }

    /**
     * Checks method to be valid Stripes Action Bean property setter.
     *
     * @param method {@link com.intellij.psi.PsiMethod} to be validated
     * @param full   flag indicating full (stripes-specific and JavaBean) or partial (only stripes-specific) validation
     * @return true if method is valid, false otherwise
     */
    public static Boolean isActionBeanPropertySetter(PsiMethod method, boolean full) {
        if (method == null
                || (full && !PropertyUtil.isSimplePropertySetter(method))) return false;

        PsiClass propertyClass = PsiUtil.resolveClassInType(method.getParameterList().getParameters()[0].getType());
        return method.hasModifierProperty(PsiModifier.PUBLIC)
                && !StripesUtil.isSubclass(method.getProject(), StripesConstants.ACTION_BEAN_CONTEXT, propertyClass)
                && !StripesUtil.isSubclass(method.getProject(), StripesConstants.FILE_BEAN, propertyClass);
    }

    /**
     * Checks if class presented by {@link PsiClass} instance of another class.
     *
     * @param project
     * @param baseClassName fully qualified name of parent class
     * @param cls           {@link com.intellij.psi.PsiClass} that will be checked for inheritance
     * @return true if subclass, false otherwise
     */
    public static Boolean isSubclass(Project project, String baseClassName, PsiClass cls) {
        if (cls == null) return false;

        PsiClass baseClass;
        try {
            baseClass = findPsiClassByName(baseClassName, project == null ? cls.getProject() : project);
        } catch (Exception e) {
            baseClass = null;
        }

        return null != baseClass && (cls.isInheritor(baseClass, true) || cls.equals(baseClass));
    }

    /**
     * Finds instance of {@link PsiClass} corresponding to FQN, passed as parameter.
     * <p/>
     * Method uses internal caching for speeding search up.
     *
     * @param className fully qualified class name to search for
     * @param project   current project
     * @return instance of {@link PsiClass} if search succesfull, null otherwise
     */
    public static PsiClass findPsiClassByName(String className, Project project) {
        if (className == null) return null;

        PsiClass retval = PSI_CLASS_MAP.get(className);
        if (null == retval) {
            retval = JavaPsiFacade.getInstance(project).findClass(className, GlobalSearchScope.allScope(project));
            if (null != retval) PSI_CLASS_MAP.put(className, retval);
        }

        try {
            retval.getContainingFile();
        } catch (Exception e) {
            PSI_CLASS_MAP.remove(className);
        }

        return retval;
    }

    public static String message(String template) {
        return stripesBundle.getString(template);
    }

    public static String message(String template, Object... params) {
        return MessageFormat.format(stripesBundle.getString(template), params);
    }

// --------------------------- CONSTRUCTORS ---------------------------

    private StripesUtil() {
    }
}
