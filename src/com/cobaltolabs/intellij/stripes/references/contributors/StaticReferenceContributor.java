package com.cobaltolabs.intellij.stripes.references.contributors;

import com.cobaltolabs.intellij.stripes.references.StaticReference;
import com.cobaltolabs.intellij.stripes.references.filters.NewStreamingResolutionFilter;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.*;
import com.intellij.psi.filters.*;
import com.intellij.psi.filters.position.FilterPattern;
import com.intellij.psi.filters.position.NamespaceFilter;
import com.intellij.psi.filters.position.ParentElementFilter;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import com.intellij.xml.util.XmlUtil;
import org.jetbrains.annotations.NotNull;

import static com.cobaltolabs.intellij.stripes.util.StripesConstants.*;
import static com.intellij.javaee.model.common.JavaeeCommonConstants.J2EE_NAMESPACE;
import static com.intellij.javaee.model.common.JavaeeCommonConstants.JAVAEE_NAMESPACE;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Mario Arias
 *         Date: 8/12/11
 *         Time: 21:02
 */
public class StaticReferenceContributor extends PsiReferenceContributor {
// ------------------------------ FIELDS ------------------------------

    private static final String[] STRIPES_FILTER_INIT_PARAMS = {
            ACTION_RESOLVER_PACKAGES,
            "ActionResolver.Class",
            "ActionBeanPropertyBinder.Class",
            "ActionBeanContextFactory.Class",
            "ExceptionHandler.Class",
            "FormatterFactory.Class",
            INTERCEPTOR_CLASSES,
            "LocalePicker.Class",
            "LocalizationBundleFactory.Class",
            "MultipartWrapperFactory.Class",
            "PopulationStrategy.Class",
            "TagErrorRendererFactory.Class",
            "TypeConverterFactory.Class",
            "ActionBeanContext.Class",
            "DelegatingExceptionHandler.Packages",
            "LocalePicker.Locales",
            "LocalizationBundleFactory.ErrorMessageBundle",
            "LocalizationBundleFactory.FieldNameBundle",
            "FileUpload.MaximumPostSize",
            "MultipartWrapper.Class",
            "Validation.InvokeValidateWhenErrorsExist",
            "Extension.Packages",
            "Configuration.Class",
            "FormatterFactory.Class",
            "TagErrorRenderer.Class",
            "ValidationMetadataProvider.Class",
            "Stripes.DebugMode",
            "Stripes.EncryptionKey",
            "CoreInterceptor.Classes",
            "MultipartWrapperFactory.Class",
    };

    private static final String[] MIME_TYPES = {
            "application/envoy",
            "application/fractals",
            "application/futuresplash",
            "application/hta",
            "application/internet-property-stream",
            "application/json",
            "application/mac-binhex40",
            "application/msword",
            "application/octet-stream",
            "application/oda",
            "application/olescript",
            "application/pdf",
            "application/pics-rules",
            "application/pkcs10",
            "application/pkix-crl",
            "application/postscript",
            "application/rtf",
            "application/set-payment-initiation",
            "application/set-registration-initiation",
            "application/vnd.ms-excel",
            "application/vnd.ms-outlook",
            "application/vnd.ms-pkicertstore",
            "application/vnd.ms-pkiseccat",
            "application/vnd.ms-pkistl",
            "application/vnd.ms-powerpoint",
            "application/vnd.ms-project",
            "application/vnd.ms-works",
            "application/winhlp",
            "application/x-bcpio",
            "application/x-cdf",
            "application/x-compress",
            "application/x-compressed",
            "application/x-cpio",
            "application/x-csh",
            "application/x-director",
            "application/x-dvi",
            "application/x-gtar",
            "application/x-gzip",
            "application/x-hdf",
            "application/x-internet-signup",
            "application/x-internet-signup",
            "application/x-iphone",
            "application/x-javascript",
            "application/x-latex",
            "application/x-msaccess",
            "application/x-mscardfile",
            "application/x-msclip",
            "application/x-msdownload",
            "application/x-msmediaview",
            "application/x-msmetafile",
            "application/x-msmoney",
            "application/x-mspublisher",
            "application/x-msschedule",
            "application/x-msterminal",
            "application/x-mswrite",
            "application/x-netcdf",
            "application/x-perfmon",
            "application/x-pkcs12",
            "application/x-pkcs7-certificates",
            "application/x-pkcs7-certreqresp",
            "application/x-pkcs7-mime",
            "application/x-pkcs7-signature",
            "application/x-sh",
            "application/x-shar",
            "application/x-shockwave-flash",
            "application/x-stuffit",
            "application/x-sv4cpio",
            "application/x-sv4crc",
            "application/x-tar",
            "application/x-tcl",
            "application/x-tex",
            "application/x-texinfo",
            "application/x-troff",
            "application/x-troff-man",
            "application/x-troff-me",
            "application/x-troff-ms",
            "application/x-ustar",
            "application/x-wais-source",
            "application/x-x509-ca-cert",
            "application/ynd.ms-pkipko",
            "application/zip",
            "audio/basic",
            "audio/mid",
            "audio/mpeg",
            "audio/x-aiff",
            "audio/x-mpegurl",
            "audio/x-pn-realaudio",
            "audio/x-wav",
            "image/bmp",
            "image/cis-cod",
            "image/gif",
            "image/ief",
            "image/jpeg",
            "image/pipeg",
            "image/png",
            "image/svg+xml",
            "image/tiff",
            "image/x-cmu-raster",
            "image/x-cmx",
            "image/x-icon",
            "image/x-portable-anymap",
            "image/x-portable-bitmap",
            "image/x-portable-graymap",
            "image/x-portable-pixmap",
            "image/x-rgb",
            "image/x-xbitmap",
            "image/x-xpixmap",
            "image/x-xwindowdump",
            "message/rfc822",
            "text/css",
            "text/h323",
            "text/html",
            "text/iuls",
            "text/plain",
            "text/richtext",
            "text/scriptlet",
            "text/tab-separated-values",
            "text/webviewhtml",
            "text/x-component",
            "text/x-setext",
            "text/x-vcard",
            "text/xml",
            "video/mpeg",
            "video/quicktime",
            "video/x-la-asf",
            "video/x-ms-asf",
            "video/x-msvideo",
            "video/x-sgi-movie",
            "x-world/x-vrml"};

    private static final ElementFilter INIT_PARAM_FILTER = new ScopeFilter(new ParentElementFilter(
            new AndFilter(
                    new NamespaceFilter(JAVAEE_NAMESPACE, J2EE_NAMESPACE),
                    new ClassFilter(XmlTag.class),
                    new TextFilter("filter"),
                    new ElementFilter() {
                        public boolean isAcceptable(Object element, PsiElement context) {
                            for (XmlTag xmlTag : ((XmlTag) element).getSubTags()) {
                                if ("filter-class".equals(xmlTag.getName()) && STRIPES_FILTER_CLASS.equals(xmlTag.getValue().getText())) {
                                    return true;
                                }
                            }
                            return false;
                        }

                        public boolean isClassAcceptable(Class hintClass) {
                            return XmlTag.class.isAssignableFrom(hintClass);
                        }
                    }
            ), 2));

// -------------------------- OTHER METHODS --------------------------

    public void registerReferenceProviders(PsiReferenceRegistrar registrar) {
        XmlUtil.registerXmlTagReferenceProvider(registrar, new String[]{"param-name"}, INIT_PARAM_FILTER, true, new PsiReferenceProvider() {
            @NotNull
            public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                return new PsiReference[]{new StaticReference(element, STRIPES_FILTER_INIT_PARAMS)};
            }
        });

        registrar.registerReferenceProvider(PsiJavaPatterns.literalExpression().and(new FilterPattern(
                new ParentElementFilter(new AndFilter(new ClassFilter(PsiExpressionList.class), new NewStreamingResolutionFilter()))
        )),
                new PsiReferenceProvider() {
                    @NotNull
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        return new PsiReference[]{new StaticReference(element, MIME_TYPES)};
                    }
                });
    }
}