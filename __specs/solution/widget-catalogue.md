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
`frame`); split and border layouts resize through the browser's own
`resize` handle and, with script, a draggable divider; the sizes stay in
the browser (13 to 16, 23 to 30). Tab box wraps `TabbedPanel` with
closable tabs, icons, badges and an Ajax switch that falls back to the
link; the accordion is a `details` list without script (17). Group box and
card are borders with header, toolbar and footer slots (18, 19). Window,
drawer and message box are the native `dialog` element with its open state
rendered by the server, upgraded by script to `showModal` (backdrop, focus
trap, Escape), draggable and resizable by script, with a result model
(20, 21, 79). Portal layout is a panel whose order travels through an
Ajax behaviour into the model, with move links as the no-script path
(22).

### Navigation (D)

Menu bar and popup menu are one model (`MenuItem` tree with action, icon,
shortcut, checked, enabled) rendered as lists of links with the menu roles,
sub-menus opening by CSS on hover and focus, arrow keys and shortcuts by
script (31, 32). Navigation bar and breadcrumb are panels over page links;
the breadcrumb does not use Wicket's `BreadCrumbBar`, which models a panel
history where the catalogue's pages are bookmarkable (33, 34). Tree and
tree table wrap `NestedTree` / `DefaultTableTree` with lazy children,
tri-state checks and drag by HTML5 drag and drop (35, 62). Pager wraps
`PagingNavigator` and `AjaxPagingNavigator` and adds page size and total
count (36). The link family is the existing `Dm*Link` set completed by
anchor navigation over the `:target` rule (37, 38).

### Inputs and forms (E)

Text, number, date, choice, check, file upload and form widgets exist and
are completed: spinner, long and generic number fields, and the temporal
fields over the browser's own `date`, `time`, `datetime-local`, `month`,
`week` and number inputs with ISO on the wire whatever the locale, so the
calendar popup and the user's format are the browser's (39 to 41, 47, 51,
57). Combo wraps `AutoCompleteTextField` with a source, a minimum length
and a delay (43). Band box, chosen box, cascader, switch, tri-state box,
slider, range slider, rating, colour, input group, search box are
`FormComponentPanel`s whose no-script form is a native input (`select
multiple`, `input type=range`, `input type=color`, radio group, `details`
popup) and whose script adds the comfort (44 to 50, 54, 55). Signature is
a canvas with a hidden field carrying the image (52). Editor is the
toolbox's own: a `textarea` with the HTML source as the no-script form, a
tool bar and an editable area by script, and an allow-list sanitiser on the
server for every submitted value (53; Q1, decided below). Captcha offers
the slot with a simple self-made challenge whose answer lives in the
session (56; Q2). Validation wraps Wicket's validators with their German
and English messages; the toolbox's own rules carry messages next to the
class, and form-level rules through a bundle an application installs once
(`DmMessages.install`), with an English default when it is not (58).

### Collections (F)

Table is a panel over `DataView` and `ISortableDataProvider` with its own
markup, because `DataTable`'s fixed row markup leaves no room for detail
and group rows: every row is a `tbody` holding the group header, the row
and the detail. It has sorting, paging, column hiding through a menu,
frozen columns by CSS, resizing and reordering by script in the browser
(not persisted), a selection model, row detail, grouping, in-place editing
through `AjaxEditableLabel`, a total row over the page, an empty message
(59, 64, 65). List is the same over `DataView` with an item template (60).
Large list is a `DataView` whose "load more" link script clicks when it
scrolls into view (61). Tree table from D (62). The data source is
`ISortableDataProvider` (count, slice with sort), with an in-memory
implementation for lists (63).

### Display (G)

Label, image, image map, HTML, progress, badge, tag, avatar, icon, audio,
video, empty state are small components over the matching element (66 to
74). Organigram and fisheye are panels with one style sheet each (75, 76).

### Overlays and feedback (H)

Feedback panel exists and is dismissible by script (77). Notifications
are a region the base page places; the server adds a message to the Ajax
response, script removes it after its time or on its close (78). Message
box is a window in modal mode with a result and the answer reported (79).
Popup is a `details` element anchored to any widget by script (80);
tooltip is a behaviour with the `title` attribute as its no-script form
and a styled tooltip by script (81). Coach mark is a dialog stepping over
a list of widgets, highlighted by script (82). Loading indicator is a
component Wicket's own Ajax script shows while a request runs, one on the
base page and any number per region (83).

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

- Editor for 53: built in the toolbox over the browser's `contenteditable`
  with an allow-list sanitiser on the server, because the no-script form
  (the `textarea` with the source) and the sanitiser are the same whatever
  the editor, and a library binding can replace the script part later
  without touching the model; decided alone on 2026-10-29 during the build.
- Phase 6 in the first build: the map model with the Leaflet binding
  (`dm-wicket-map-leaflet`) and the chart model with the Chart.js binding
  (`dm-wicket-chart-chartjs`), each a module with its webjar served through
  `WebjarResourceReference`; the document viewer (100) is in the core. The
  other bindings follow the same cut.
- Spreadsheet project for 101: candidates in the plan; the first binding
  is chosen when a product needs it.
- Module cut: `components` keeps the core catalogue (C to I, K); embedded
  bindings (J) are modules of their own; the showcase is a sibling project.
- Test tiers in the toolbox: `*Test` with `WicketTester` per widget;
  `*IT` only where a widget talks to a server-side resource (file upload,
  push); golden HTML per widget state in the showcase, not in the toolbox.

## Deferred

- The MapLibre GL and OpenLayers map bindings, the ECharts, Plotly and D3
  chart bindings, the JointJS diagram canvas and the spreadsheet binding
  (97 to 99, 101): modules of their own with webjars, after the walk-through
  of the first build; the models exist.
- Split and border layout sizes reported to the model, table column widths
  and order persisted: the browser keeps them for now (16, 13, 59).
- Captcha beyond the simple self-made challenge (56): an application plugs
  in a stronger one.
- Theming and a design system: out of scope of the requirements.
