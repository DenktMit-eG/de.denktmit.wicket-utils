# Widget catalogue: solution

## Purpose

Answers `__specs/requirements/widget-catalogue.md`: how the toolbox
`de.denktmit.wicket-utils` grows from its forms, links, tables and borders
into the complete catalogue of sections C to K, and how the showcase (B)
proves it. Written as a proposal on 2026-10-29; the owner approves it in
the walk-through of the plan.

## Approach

### Catalogue as a whole (A)

Every widget is a Kotlin class in the `components` module on Apache Wicket
10, in the `Dm*` style the toolbox already has: a constructor with the id,
the model and an `init` lambda, `+widget` to add it to the surrounding
container, `visible`, `enabled` and `cssClass` by rule (89, 90). Markup
comes with the class (`.html` next to it, `<wicket:panel>` or
`<wicket:border>`), as little of it as the meaning needs (7). Every widget
carries `dm-id-<wicketId>` and `dm-<Class>` as its stable hooks (3) and
documents itself in KDoc, which Dokka publishes (5). Java callers get the
same constructors through `@JvmOverloads` and lambda-free overloads (4).
Each widget has a `*Test` with `WicketTester`: it renders, its behaviours
work, its hooks are present, its no-script path submits or navigates
(2, 6). Accessibility (8) is part of the markup and of every test: label
bound, role and state attributes, keyboard path. Models hold what the page
author gives (9); the toolbox never loads an application object itself.

Wicket already ships much of the behaviour; the toolbox wraps it rather
than rewriting it: `TabbedPanel`, `ModalDialog`, `DataTable` with
`IDataProvider`, `PagingNavigator`, `NestedTree` and `TableTree`,
`AutoCompleteTextField`, `DateTextField`, `FileUploadField`,
`AjaxLazyLoadPanel`, `AjaxSelfUpdatingTimerBehavior`, the native WebSocket
behaviour, the breadcrumb and wizard of `wicket-extensions`. Where Wicket
has nothing (switch, slider, rating, chosen box, cascader, drawer, portal
layout, step bar, timeline, toolbar overflow, notification, coach mark),
the toolbox builds the widget from markup, one style sheet and, where
comfort needs it, a few lines of plain script that enhance and never
replace (2).

### Styling and script delivery (7, 105)

One style sheet per widget under `components/src/main/resources/.../css/`,
built on the Every Layout primitives and tokens of `stacks/html-css.md`;
the toolbox ships the primitives and tokens as its base sheet, so an
application starts styled and overrides tokens, not rules. Each widget
contributes its sheet and script through Wicket's header contribution
with a `CssResourceReference` / `JavaScriptResourceReference`, versioned
by the toolbox's version and cached by the browser (105). No CSS
framework, no icon font; icons are inline SVG symbols from one sprite the
toolbox ships (72). Script is vanilla, no build step in the toolbox.

### Layout containers (C)

Border, box, grid, split, centre, fit, anchor, separator, include, frame,
toolbar, step bar, timeline are panels or borders over the Every Layout
primitives (`sidebar`, `cluster`, `stack`, `grid`, `cover`, `imposter`,
`frame`); split and border layouts get a resize handle by script with the
sizes kept in the model (13 to 16, 23 to 30). Tab box wraps `TabbedPanel`
and its Ajax variant with closable tabs, icons, badges and an accordion
mode (17). Group box and card are borders with header, toolbar and footer
slots (18, 19). Window wraps `ModalDialog` for the modal case and a floating
panel for the embedded and overlapped cases, with drag and resize by
script and a result model (20). Drawer and portal layout are new panels
(21, 22); portal order travels through an Ajax behaviour into the model.

### Navigation (D)

Menu bar and popup menu are one model (`MenuItem` tree with action, icon,
shortcut, checked, enabled) rendered as `nav` and `menu` elements with
roles and keyboard handling by script (31, 32). Navigation bar and
breadcrumb are panels over that model and Wicket's breadcrumb support
(33, 34). Tree and tree table wrap `NestedTree` / `TableTree` with
lazy children, tri-state checks and drag by HTML5 drag and drop (35).
Pager wraps `PagingNavigator` for links and Ajax (36). The link family is
the existing `Dm*Link` set completed by anchor navigation (37, 38).

### Inputs and forms (E)

Text, number, date, choice, check, file upload and form widgets exist and
are completed: spinner, long and double variants, date-time, month, year,
week, time (39 to 41, 47, 51, 57). Combo wraps `AutoCompleteTextField`
with a remote source and a minimum length (43). Band box, chosen box,
cascader, switch, slider, range slider, rating, colour, input group,
search box are new form components, each a `FormComponentPanel` whose
no-script form is a native input (`select multiple`, `input type=range`,
`input type=color`, radio group) and whose script adds the comfort (44 to
50, 54, 55). Signature is a canvas with a hidden field carrying the image
(52). Editor wraps a chosen editor library over a `textarea` fallback
(53; Q1). Captcha offers the slot with a simple self-made challenge (56;
Q2). Validation wraps Wicket's validators and `wicket-bean-validation`
with messages from the toolbox's resource bundles, German and English
(58).

### Collections (F)

Table wraps `DataTable` with the toolbox's `TableColumn` model: sorting,
paging, resizing, reordering, hiding, frozen columns by CSS, selection
model, row detail, grouping, in-place editing, total row, empty message
(59, 64, 65). List is the same over `DataView` with an item template (60).
Large list is a `DataView` with a scroll-triggered Ajax page load (61).
Tree table from D (62). The data source is `IDataProvider` (count, slice
with sort), with an in-memory implementation for lists (63).

### Display (G)

Label, image, image map, HTML, progress, badge, tag, avatar, icon, audio,
video, empty state are small components over the matching element (66 to
74). Organigram and fisheye are panels with one style sheet each (75, 76).

### Overlays and feedback (H)

Feedback panel exists (77). Notification is a panel the server adds to
the Ajax response, placed by the base page, dismissed by time or click
(78). Message box wraps `ModalDialog` with a result model (79). Popup and
tooltip are behaviours that attach a panel or a text to any component,
positioned by script, with the content rendered server-side (80, 81).
Coach mark is a sequence of popups over a list of hooks (82). Loading
indicator is a behaviour on the base page that shows during Ajax calls
(83).

### Behaviours (I)

Live update, timer, push, keyboard shortcuts, drag and drop, visibility
and class by rule, scroll and focus, confirm are behaviours, most already
present in the toolbox (`DmAjax*`, `DmAjaxTimeBehavior`,
`DmWebSocketBehavior`, `DisabledBehavior`, `CssClassRemover`) and
completed by `KeyboardShortcutBehavior`, `DragSourceBehavior` /
`DropTargetBehavior`, `ScrollIntoViewBehavior`, `ConfirmBehavior` (84 to
92).

### Embedded best-of-breed widgets (J)

One server-side model per kind and one binding per library behind it
(Q5): `MapPanel<M>` with `MapModel` (viewport, markers, shapes, layers,
events to the page) and bindings for Leaflet, MapLibre GL and OpenLayers
(97); `ChartPanel` with `ChartModel` (series, axes, legend, selection) and
bindings for Chart.js, Apache ECharts, Plotly.js and D3 (98; D3 as a
binding that takes a render function, since it has no chart model of its
own); `DiagramPanel` with a graph model and the JointJS binding, events
and changes travelling through Ajax behaviours so that the surrounding
toolbar and property panels are ordinary widgets (99); `DocumentViewer`
as an `object`/`iframe` with the browser's own PDF viewer and a download
link, nothing else (100); `SpreadsheetPanel` embedding an existing
collaborative spreadsheet project with its data moved in and out through
its own API, chosen in the plan (101). Each binding is its own Maven
module (`dm-wicket-map-leaflet`, `dm-wicket-chart-echarts`, ...) so that
an application depends only on what it uses, and the library's script
comes from a packaged web archive (webjar) pinned by version, served
through Wicket's resource references (Q6).

### Page level (K)

`@MountPage` with typed `PageParams` exists (102). `DmBasePage` adds title,
language, the base sheet, feedback panel and loading indicator with a
`<wicket:child/>` region (103). Session access offers the signed-in user
and locale through `DmSession` (104). Resource delivery as in "Styling and
script delivery" (105).

### Showcase (B)

A sibling project `de.denktmit.wicket-showcase/` on Spring Boot and Wicket
through the toolbox, exactly the product setup, one page per widget mounted
at `/widgets/<group>/<name>` with purpose, live examples of every
behaviour, the Kotlin the page author writes and the rendered result; a
navigation by group and name; page tests per widget page (10 to 12). The
showcase is internal first and published with the documentation once the
catalogue is complete (Q4).

## Decisions

- **Wrap Wicket where it has the behaviour, build only what it lacks**
  over a toolbox that reimplements everything: Wicket's components are
  tested and accessible, and the toolbox stays a thin, readable layer
  (reference `de.denktmit.grundbuch.md`, Wicket UI; `de.denktmit.webapp.md`
  for the CSS file layout).
- **No script build in the toolbox**: vanilla script per widget delivered
  as a resource; a bundler stays a product decision (stack `html-css.md`).
- **Embedded libraries behind one model, one binding module per library**
  over one abstraction that hides the libraries completely: the owner
  accepts bindings, and a binding may expose a library's strength as its
  own option (Q5, the owner 2026-10-29: "wir bauen einfach einen Binder für
  jede von den Varianten").
- **Document viewer is the browser's own** (the owner 2026-10-29: "einen
  PDF-Viewer selber bauen ... unnötig").
- **No spreadsheet engine of our own** (the owner 2026-10-29: "sowas neu zu
  erfinden, würde ich für wahnsinnig halten").

## Proposals

- Editor library for 53: a maintained, framework-free editor with sanitised
  HTML output (candidates in the plan), delivered as a webjar.
- Spreadsheet project for 101: candidates in the plan; the first binding
  is chosen when a product needs it.
- Module cut: `components` keeps the core catalogue (C to I, K); embedded
  bindings (J) are modules of their own; the showcase is a sibling project.
- Test tiers in the toolbox: `*Test` with `WicketTester` per widget;
  `*IT` only where a widget talks to a server-side resource (file upload,
  push); golden HTML per widget state in the showcase, not in the toolbox.

## Deferred

- Organigram, fisheye, coach mark, signature (52, 75, 76, 82): built last
  (Q3).
- Captcha beyond the simple self-made challenge (56): an application plugs
  in a stronger one.
- Theming and a design system: out of scope of the requirements.
