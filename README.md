# wicket-utils

Utils for [Apache Wicket](https://wicket.apache.org/) development with [Kotlin](https://kotlinlang.org/)

## Features

* Annotation based mounts
  ```kotlin
  @MountPage("some/path")
  class MyPage: WebPage { ... }
  
  class MyApplication: WebApplication {
     override fun init() {
      // ...
      mountAnnotatedPages()
    }
  }
  ```

* DSL-like components
  ```kotlin
  class MyPage: Page {
    override fun onInitialize() {
      +SomeComponent("id") {
        // configure component ...
      }         
    }
  }
  ```

* Models for Lambdas and properties
  ```kotlin
  modelOf { "getter" }
  modelOf( { getter() }, { setter(it) })
  modelOf( MyObject::name )
  modelOf( MyObject::birthdate, LocalDate::year )
  ```

* PageParameters by delegates
  ```kotlin
  data class MyParams(
    val pp: PageParameters
  ) {
    var name: String by pp
  }
  ```
* Typesafe PageParameters
  ```kotlin
  bookmarkablePageLink.apply {
    param(MyParams::name, "value")
  }
  component.setResponsePage(MyPage::class.java, myParams.copy()) {
    name = "value"
  }
  ```

* Spring beans by delegate
  ```kotlin
  class MyComponent() {
    val bean: BeanType by bean()
    val bean: BeanType by bean("name")
  }
  ```

* easy CSS class handling
  ```kotlin
  component.addCssClass("a", "b", classNameAsCssClass())
  component.removeCssClass("c")
  ```

## Widget catalogue (0.2.0)

Every widget is a `Dm*` class with the id, the model and an `init` lambda,
carries the stable hooks `dm-id-<wicketId>` and `dm-<kind>`, brings one
style sheet (and, where comfort needs it, one script) through Wicket's
header contribution, and works without script: it renders, submits and
navigates. The base sheet `DmResources.BASE_CSS` holds the tokens and the
Every Layout primitives. See `__specs/requirements/widget-catalogue.md`
and `__specs/solution/widget-catalogue.md`; the sibling project
`de.denktmit.wicket-showcase` shows every widget live.

| Group | Widgets |
|---|---|
| Foundation | `DmBasePage`, `DmSession`, `DmIcon`, `DmLoadingIndicator`, `visibleIf`/`enabledIf`/`readOnlyIf`/`cssClassIf` |
| Layout | `DmHBox`, `DmVBox`, `DmGrid`, `DmCenter`, `DmFit`, `DmAnchorLayout`, `DmSeparator`, `DmSpace`, `DmFrame`, `DmInclude`, `DmGroupBox`, `DmCard`, `DmToolbar`, `DmStepBar`, `DmTimeline`, `DmTabBox`, `DmAccordion`, `DmSplitLayout`, `DmBorderLayout`, `DmWindow`, `DmDrawer`, `DmPortalLayout` |
| Navigation | `MenuItem` with `DmMenuBar` and `DmPopupMenu`, `DmNavBar`, `DmBreadcrumb`, `DmPager`, `DmAnchorLink`, `DmTree`, `DmTreeTable` |
| Inputs | `DmLongField`, `DmNumberField`, `DmSpinner`, `DmDateField`, `DmTimeField`, `DmDateTimeField`, `DmMonthField`, `DmYearField`, `DmWeekField`, `DmMultiSelect`, `DmCheckGroup`, `DmRadioChoice`, `DmCombo`, `DmBandBox`, `DmChosenBox`, `DmCascader`, `DmSwitch`, `DmTriStateCheckbox`, `DmSlider`, `DmRangeSlider`, `DmRating`, `DmColorField`, `DmFileDrop`, `DmSignature`, `DmEditor` with `HtmlSanitizer`, `DmInputGroup`, `DmSearchBox`, `DmCaptcha`, `DmFormField`, `DmValidators`, `placeholder`/`maxLength`/`pattern`/`required`/`liveUpdate` |
| Collections | `ListDataSource`, `DmColumn`, `DmTable`, `DmList`, `DmLargeList`, `DmSelectionModel` |
| Display | `DmText`, `DmHtml`, `DmImageMap`, `DmProgress`, `DmBadge`, `DmTag`, `DmAvatar`, `DmAudio`, `DmVideo`, `DmEmptyState`, `DmOrganigram`, `DmFisheye`, `DmDocumentViewer` |
| Overlays | `DmFeedbackPanel`, `DmNotifications`, `DmMessageBox`, `DmPopup`, `TooltipBehavior`, `DmCoachMark` |
| Behaviours | `RefreshTimer`, `DmWebSocketBehavior`, `KeyboardShortcutBehavior`, `DragSourceBehavior`/`DropTargetBehavior`, `Focus.scrollTo`/`focus`, `ConfirmBehavior` |

An application returns `DmSession` from `newSession` and calls
`DmMessages.install(this)` in `init` to get the toolbox's translated
messages for form-level rules.
