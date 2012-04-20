Welcome to the IntelliStripes' site on GitHub.

IntelliStripes NV add support for [Stripes Framework](http://stripesframework.com) to [IntelliJ Ultimante 11](http://www.jetbrains.com/idea/) and up

## Features ##

### AUTOCOMPLETE ALL THE THINGS ###

One of the main features in IntelliStripes is the autocomplete in almost every place that could help the developer to use Stripes more quickly and with less errors

#### In web.xml ####

IntelliStripes provide autocomplete for all the parameters `<param-name>` that could be configured for the `net.sourceforge.stripes.controller.StripesFilter` like `Interceptor.Classes` and others. Ex:

```xml
<filter>
    <filter-name>StripesFilter</filter-name>
    <filter-class>net.sourceforge.stripes.controller.StripesFilter</filter-class>
    <init-param>
        <param-name>Interceptor.Classes</param-name>
        <param-value>net.sourceforge.stripes.integration.spring.SpringInterceptor</param-value>
    </init-param>
```

__Warning__:IntelliStripes doesn't provide autocomplete for `<param-value>` values. Altough IntelliJ itself could autodetect class names and could be provide autocomplete in some cases, this values aren't validated

#### In Stripes Related Classes ####

IntelliStripes provide autocomplete for:

##### Annotations #####

Almost every Stripes' annotation is supported on IntelliStripes

* `net.sourceforge.stripes.integration.spring.SpringBean`

IntelliStripes provide autocomplete for Spring beans names

```java
@SpringBean("userService")
public void injectUserService(UserService userService) {
    this.userService = userService;
}
```

* `net.sourceforge.stripes.validation.Validate`

Autocomplete for event methods in the `on` attribute and setter methods in the `field` attribute

```java
@Validate(on = "create",field = "lastName", required=true)
public void setUser(User user){
    this.user = user;
}
```

* `net.sourceforge.stripes.validation.ValidationMethod`

Autocomplete for event methods in the `on` attribute

* `net.sourceforge.stripes.action.After`

Autocomplete for event methods in the `on` attribute

* `net.sourceforge.stripes.action.Before`

Autocomplete for event methods in the `on` attribute

* `net.sourceforge.stripes.action.Wizard`

Autocomplete for event methods in the `startEvent` attribute

* `net.sourceforge.stripes.action.StrictBinding`

Autocomplete for setter methods in the `deny` and `allow` attributes

* `net.sourceforge.stripes.action.UrlBinding`

Autocomplte for setter methods inside `{}`

```java
@UrlBinding("/admin/user/{$event}/{user.id}")
public class UserActionBean implements ActionBean {
```

##### Resolutions #####

* `net.sourceforge.stripes.action.ForwardResolution` and `net.sourceforge.stripes.action.RedirectResolution`

IntelliStripes provide autocomplete for JSP paths on `new` call

```java
public Resolution createForm() {
    return new ForwardResolution("/WEB-INF/jsp/views/admin/user/create.jsp");
}
```

Also for event methods on the second `String` parameter in the two argument constructor

```java
return new RedirectResolution(RoleActionBean.class, "detail")
```

* `net.sourceforge.stripes.action.StreamingResolution`

Autocomplete for MIME types on the first parameter

```java
new StreamingResolution("text/xml","<message>Hi!</message>");
```

__Warning__:Altough this list is extense is far from be a complete MIME type reference

##### Other places #####

* `net.sourceforge.stripes.validation.ValidationErrors`

Autocomplete for setter methods on the first parameter of `put`, `putAll`,`add` and `addAll`

### In JSP ###

#### CSS classes ####

IntelliStripes provide autocomplete for CSS classes on `class` attribute for this tags:

* `s:button`
* `s:checkbox`
* `s:file`
* `s:form`
* `s:image`
* `s:label`
* `s:link`
* `s:hidden`
* `s:option`
* `s:options-collection`
* `s:options-enumeration`
* `s:options-map`
* `s:password`
* `s:radio`
* `s:reset`
* `s:select`
* `s:submit`
* `s:text`
* `s:textarea`

#### ActionBean classes ####

Autocomplete for fully qualified `net.sourceforge.stripes.action.ActionBean` implementations class name on `beanclass` attribute for this tags:

* `s:errors`
* `s:field-metadata`
* `s:form`
* `s:link`
* `s:url`
* `s:useActionBean`

#### Event methods ####

Autocomplete for event methods on `event` attribute on tags:

* `s:link`
* `s:url`
* `s:useActionBean`

And in `name` attribute on tags:

* `s:button`
* `s:image`
* `s:submit`

This also works on `_eventName` special case in the `value` attribute on tags

* `s:hidden`
* `s:param`

#### Setter methods ####

Autocomplete for setter methods on `name` attribure on tags:

* `s:checkbox`
* `s:file` *This will only complete setter with type `net.sourceforge.stripes.action.FileBean` and arrays and List of the same type*
* `s:hidden`
* `s:param`
* `s:password`
* `s:radio`
* `s:select`
* `s:text`
* `s:textarea`

And in `field` attribute on tag:

* `s:errors`

#### Others ####

Autocomplete of JSP paths on the `name` attribute of `s:layout-render` tag. Also autocomplete of `s:layout-component` defined inside a tag `s:layout-definition` on the `name` attribute on `s:layout-component` tag inside of a `s:layout-render` tag

```jsp
<s:layout-definition>
  <s:layout-component name="header"/>
  <s:layout-component name="body"/>
  <s:layout-component name="footer"/>
</s:layout-definition>
```

```jsp
<s:layout-render name="/WEB-INF/path/to/my/layout/definition.jsp">
  <s:layout-component name="header">
    My Header
  </s:layout-component>
  <s:layout-component name="body">
    My Body
  </s:layout-component>
  <s:layout-component name="footer">
    My Footer
  </s:layout-render>
</s:layout-definition>
```


## FAQ ##

### What is IntelliStripes NV? ###

IntelliStripes NV is the Next Version of [IntelliStripes](http://intellistripes.googlecode.com) aimed to to [IntelliJ Ultimante 11](http://www.jetbrains.com/idea/) and up

### Why start a new project? ###

IntelliStripes is a great project, but the IDEA Open API changes a lot in the latests versions, So we was using a lot of deprecated methods that now don't exists.

With this new project we can make a fresh start and ensure that we are using the IDEA Open API to the max.

### IntelliStripes NV will be Open Source and Free as IntelliStripes? ###

Yes, IntelliStripes NV is Open Source with Apache License 2.0

### Why changing from Google Code to GitHub? ###

GitHub have a better way to forking the project, and Git is way cooler than SVN. And GitHub have a better UI:) (Also all the gool guys and rockstars use GitHub)

### Are you rewriting the Whole Enchilada? ###

Yes and No

Yes, the idea is to polish the code and have a better project for all, with better documentation.

But No, we expect to have the same features (and new ones) but with better implementation and overall quality.

### And the future of IntelliStripes? ###

IntelliStripes is no longer active, now all our efforts are dedicated to IntelliStripes NV

### Who is behind this? ###

IntelliStripes NV now is backed for a company [Cobalto Labs SAS](http://cobaltolabs.com) that will ensure a healthy future for the project

### What is Cobalto Labs SAS? ###

[Cobalto Labs SAS](http://cobaltolabs.com) is a company based in Bogot&aacute;, Colombia, with a broad experience in Spring and Stripes development, we deliver SpringSource certified training and consulting across the Americas.

### Hey, I miss that old IntelliStripes feature  ###

Please fill an [Issue](https://github.com/cobaltolabs/IntelliStripes-NV/issues)

Right now IntelliStripes NV is a work in progress. Some features that the Ol' IntelliStripes have aren't ready yet and some others never will be migrated to IntelliStripes NV



