# Widget catalogue: status of the first build

Written at the end of the autonomous build on 2026-10-29 (branch
`feature/widget-catalogue`, version 0.2.0-SNAPSHOT) and the sibling project
`de.denktmit.wicket-showcase`. Every rule of the requirements is listed with
the class that answers it or the reason it is not built. Nothing is pushed,
tagged or released.

## Builds

- `de.denktmit.wicket-utils`: `mvn clean install` green, no build warning;
  601 tests (components 539 `*Test` and 7 `*IT`, model 30, kotlin 7, spring
  14, map-leaflet 2, chart-chartjs 2). The 7 ITs are the pre-existing border
  render ITs; every new widget is proven by `*Test` with `WicketTester`
  (render, behaviour, hooks, no-script submit or navigation, accessibility
  attributes).
- `de.denktmit.wicket-showcase`: `mvn clean verify` green, no build warning;
  87 `*Test` (every widget page renders at its address with all examples
  and hooks, and is reachable from the navigation) and 1 `ApplicationIT`
  (Boot starts, home page and a widget page answer over HTTP). 43 widget
  pages in 9 groups at `/widgets/<group>/<name>`.

## Rules implemented (A to K)

- A.1 to A.9: `WidgetHooks` (3), `DmWidgetPanel` / `DmWidgetBorder` / `DmInputPanel`
  (5, 7, 9), `VisibleRule`, `EnabledRule`, `CssClassRule` (89, 90), `DmResources`,
  `@JvmOverloads` on every widget (4), `WidgetTestBase` (6); no-script paths per
  widget (2); labels, roles and states per widget (8)
- B.10 to B.12: showcase `WidgetPage`, `WidgetCatalogue`, `ShowcasePage` navigation
  by group and name, `@MountPage("/widgets/<group>/<name>")`
- C.13: `DmBorderLayout` (collapsible `details`, resizable by the browser)
- C.14: `DmHBox`, `DmVBox` with `grow`
- C.15: `DmGrid` with `span` and `minColumn`
- C.16: `DmSplitLayout` (browser resize handle, script divider, collapse controls)
- C.17: `DmTabBox` over `TabbedPanel` (icon, badge, closable, side, Ajax with
  fallback), `DmAccordion`
- C.18: `DmGroupBox`
- C.19: `DmCard` (actions, footer, collapsible, maximisable, closable)
- C.20: `DmWindow` (embedded, floating, modal over the native `dialog`, result)
- C.21: `DmDrawer`
- C.22: `DmPortalLayout` (drag by script, move links without, order in the model)
- C.23: `DmAnchorLayout`
- C.24: `DmCenter`, `DmFit`
- C.25: `DmSeparator`, `DmSpace`
- C.26: `DmInclude` (component or page in an inline frame)
- C.27: `DmFrame`
- C.28: `DmToolbar` (groups, spacer, overflow menu by script)
- C.29: `DmStepBar`
- C.30: `DmTimeline`
- D.31, D.32: `MenuItem`, `MenuPanel`, `DmMenuBar`, `DmPopupMenu`
- D.33: `DmNavBar`
- D.34: `DmBreadcrumb`
- D.35: `DmTree` over `NestedTree` (lazy, selection, tri-state checks, icons, drag)
- D.36: `DmPager` over `PagingNavigator` / `AjaxPagingNavigator`
- D.37: the existing `DmPageLink`, `DmExternalLink`, `DmActionLink`,
  `DmDownloadLink`, `LazyPageLink`, `ReturnablePageLink` with `visibleIf` /
  `enabledIf`
- D.38: `DmAnchorLink`
- E.39: existing `DmTextField`, `DmTextArea`, `DmPasswordTextfield` plus
  `placeholder`, `maxLength`, `pattern`, `required`, `readOnlyIf`, `liveUpdate`
- E.40: existing `DmIntField`, `DmDoubleField`, `DmBigDecimalField`; `DmLongField`,
  `DmNumberField`, `DmSpinner`
- E.41: `DmDateField`, `DmTimeField`, `DmDateTimeField` (zone shown), `DmMonthField`,
  `DmYearField`, `DmWeekField` with `IsoWeek`, bounds and `DisabledDatesValidator`
- E.42: existing `DmDropDownChoice`, `DmRadioGroup`; `DmMultiSelect`, `DmCheckGroup`,
  `DmRadioChoice` with empty choice
- E.43: `DmCombo` over `AutoCompleteTextField`
- E.44: `DmBandBox`
- E.45: `DmChosenBox`
- E.46: `DmCascader`
- E.47: existing `DmCheckbox`; `DmSwitch`, `DmTriStateCheckbox`
- E.48: `DmSlider`, `DmRangeSlider`
- E.49: `DmRating`
- E.50: `DmColorField`
- E.51: `DmFileDrop` (dialog or drop, accept, size, progress over
  `UploadProgressBar`, lists)
- E.52: `DmSignature`
- E.53: `DmEditor` with `HtmlSanitizer`
- E.54: `DmInputGroup`
- E.55: `DmSearchBox`
- E.56: `DmCaptcha` (simple sum, answer in the session, pluggable)
- E.57: existing `DmForm`, `DmStatelessForm`, `DmButton`; `DmFormField`
- E.58: `DmValidators`, `CustomValidator`, `DateOrderValidator`, `DmMessages`
- F.59: `DmTable`, `DmColumn`
- F.60: `DmList`
- F.61: `DmLargeList`
- F.62: `DmTreeTable` over `DefaultTableTree`
- F.63: `ListDataSource` over `ISortableDataProvider`
- F.64: `DmSelectionModel`
- F.65: `DmColumn.cell`, `DmList` template, `DmTable.groupBy`
- G.66: `DmText`
- G.67: existing `DmImage`; `DmImageMap`
- G.68: `DmHtml`
- G.69: `DmProgress`
- G.70: `DmBadge`, `DmTag`
- G.71: `DmAvatar`
- G.72: `DmIcon` with the sprite `dm-icons.svg`
- G.73: `DmAudio`, `DmVideo`
- G.74: `DmEmptyState`
- G.75: `DmOrganigram`
- G.76: `DmFisheye`
- H.77: `DmFeedbackPanel` (dismissible)
- H.78: `DmNotifications` on `DmBasePage`
- H.79: `DmMessageBox`
- H.80: `DmPopup`
- H.81: `TooltipBehavior`
- H.82: `DmCoachMark`
- H.83: `DmLoadingIndicator`, `LoadingIndicatorBehavior`
- I.84: existing `DmAjax*`, `DmAjaxFormUpdatingBehavior`; `liveUpdate`
- I.85: existing `DmAjaxTimeBehavior`; `RefreshTimer`
- I.86: existing `DmWebSocketBehavior`
- I.87: `KeyboardShortcutBehavior`
- I.88: `DragSourceBehavior`, `DropTargetBehavior`
- I.89: `VisibleRule`, `EnabledRule`, `ReadonlyBehavior`
- I.90: `CssClassRule`
- I.91: `Focus.scrollTo`, `Focus.focus`
- I.92: `ConfirmBehavior`
- J.97: `MapModel`, `DmMap` (Leaflet 1.9.4 from its webjar)
- J.98: `ChartModel`, `DmChart` (Chart.js 4.6.1 from its webjar)
- J.100: `DmDocumentViewer`
- K.102: existing `@MountPage`, `PageParams`, `mountAnnotatedPages()`
- K.103: `DmBasePage`
- K.104: `DmSession`, `DmUser`, `SimpleUser`
- K.105: `DmResources`, `WebjarResourceReference`, header contributions per widget

## Rules not built, and why

- J.97 MapLibre GL and OpenLayers bindings, J.98 ECharts, Plotly and D3
  bindings: the models and the first binding of each kind exist; the other
  bindings are the same cut (one module, one webjar, one script) and wait
  for the walk-through, where the owner decides which ones a product needs.
- J.99 diagram canvas (JointJS) and J.101 spreadsheet: not started; both
  need a library choice the owner wanted to make (plan, "Open for the
  walk-through").

## Decisions taken alone during the build

Recorded in `widget-catalogue.md` (Decisions, Proposals, Deferred):

- Window, drawer and message box are the native `dialog` element, not
  `ModalDialog`: the open state renders server-side, so they work without
  script, and script upgrades them to a real modal.
- The table is a panel over `DataView`, not `DataTable`, because detail and
  group rows need their own markup.
- The breadcrumb is a list of page links, not `BreadCrumbBar`.
- The editor is built in (contenteditable plus an allow-list sanitiser);
  a library binding can replace the script part later.
- Form-level validation messages resolve through a bundle an application
  installs (`DmMessages.install`) with an English default.
- Split and border layout sizes and table column widths and order stay in
  the browser; nothing is reported to the model yet.
- `DmContainer` gained member `+` operators and the layout containers type
  their `init` lambda to their own class, so `grow`, `span` and `anchor`
  resolve inside the lambda even inside another panel's scope.

## Open points for the owner

- Walk through the showcase in a browser: the script parts (drag, resize,
  tooltips, menus, charts, maps) are proven by their server contracts and
  by hand-written vanilla script, not by a browser test; Playwright golden
  screenshots per widget page are the next step (plan, phase 7).
- Keycloak or any sign-in is not part of the showcase; `DmSession` is
  shown with a sample sign-in.
- The skillmatrix stays on 0.1.3-SNAPSHOT; it picks up 0.2.0 through its
  BOM when the owner decides.
- `ReturnablePageLink` still lives in package
  `de.denktmit.rechnomat.webui.components` (pre-existing).
