# Widget catalogue

## Purpose

The cooperative builds its applications with one server-side UI toolbox,
`de.denktmit.wicket-utils`. Today the toolbox covers forms, links, tables and
a few borders; every new application page that needs a tab strip, a dialog,
a tree or a date picker writes it again. The owner wants the toolbox to hold
the complete set of building blocks a business application needs, so that a
page is composed from the catalogue and never starts from raw HTML. The
catalogue below is the inventory the owner wants (Marius, 2026-10-29); it is
written as what the cooperative needs, not as a copy of any other library.

## Terms

- **Widget** (Baustein): one reusable building block with its own markup,
  styling and behaviour, used by id from a page or another widget.
- **Catalogue** (Baukasten): the complete set of widgets the toolbox offers,
  grouped by what a page author is trying to do.
- **Container**: a widget that holds other widgets and arranges them.
- **Input**: a widget that reads a value from the user into a model.
- **Collection widget**: a widget that shows many rows of data (table,
  list, tree, grid) from a data source, with the behaviours a collection
  needs (paging, sorting, selection, grouping).
- **Overlay**: a widget that appears above the page (dialog, popup,
  tooltip, notification, menu) and goes away again.
- **Showcase**: a separate application that presents every widget of the
  catalogue on its own page, as living documentation.
- **Stable hook**: the marker a widget carries so that tests and styles can
  find it regardless of its inner structure.

## Rules

### A. Catalogue as a whole

1. Every widget in the inventory (sections C to K) exists in the toolbox
   with the behaviours listed for it; an application never needs to copy
   markup from another application to get one of them.
2. Every widget works without script in the browser: it renders, reads
   and submits its value, navigates. Script adds comfort (live update,
   keyboard, drag) and never structure.
3. Every widget carries a stable hook for its id and its kind, so a test or
   a style reaches it without knowing its inner markup.
4. Every widget is usable from Kotlin and from Java with the same features.
5. Every widget is documented where it is declared: what it is for, which
   behaviours it has, which markup it expects or brings.
6. Every widget has its own tests in the toolbox: it renders, its
   behaviours work, its stable hooks are present. An application never has
   to test the toolbox.
7. A widget never brings a styling framework or an icon font; it brings the
   least markup its meaning needs and one style sheet per widget that an
   application may include or replace.
8. A widget is accessible by construction: labels are bound to inputs,
   keyboard reaches every action, focus is visible, roles and states are
   announced where the browser does not do it on its own.
9. A widget with a model never holds the application's objects; it holds
   what the page author gives it, and the page author stays responsible
   for keeping that small.

### B. Showcase

10. After the widgets exist, a showcase application presents each widget on
    its own page: what it is for, every behaviour in use, the markup the
    page author writes, the result.
11. The showcase runs on the same application setup the cooperative uses
    for its products, so that it proves the toolbox in that setup and not
    only in isolation.
12. The showcase is navigable by group and by name; a widget's page is
    reachable by a stable address.

### C. Layout containers

13. **Border layout**: five regions (top, bottom, left, right, centre), each
    optional, each collapsible and resizable where the author allows it;
    the centre fills what the others leave.
14. **Horizontal and vertical box**: children in a row or a column with a
    gap, alignment and wrapping; a child may take the remaining space.
15. **Grid layout**: children in rows and columns with explicit spans and a
    minimum column width below which columns wrap.
16. **Split layout**: two panes with a draggable divider, horizontal or
    vertical, with a collapse control per pane.
17. **Tab box**: tabs with one visible panel; tabs may be closable, have an
    icon and a badge, sit on any side, and may show as an accordion.
18. **Group box**: a titled, optionally collapsible section around its
    children.
19. **Card** (panel with header, toolbar and footer): a titled block with
    an action area, collapsible, maximisable, closable where allowed.
20. **Window**: a titled block that may be embedded in the page, floated
    over it, or shown as a modal dialog that blocks the page until closed;
    draggable, resizable, closable; returns a result to the opener.
21. **Drawer**: a panel that slides in from an edge over the page and
    closes on request or on clicking outside.
22. **Portal layout**: columns of panels the user may drag between columns
    and reorder; the order is reported to the page.
23. **Anchor layout**: children positioned relative to the container's
    edges and corners, kept in place when the container resizes.
24. **Centre and fit layout**: a child centred in its container or
    stretched to fill it.
25. **Separator and space**: a visible or invisible gap, horizontal or
    vertical, with a size.
26. **Include**: a region whose content is another page or panel chosen at
    runtime.
27. **Frame** (external document): an external page embedded at a size.
28. **Toolbar**: a row of actions with overflow when space runs out,
    alignment left, centre or right, and separators between groups.
29. **Step bar**: the steps of a process, with the current, the finished
    and the pending ones, and an optional link per finished step.
30. **Timeline**: events in order along a line with a title, a time and a
    body per event.

### D. Navigation

31. **Menu bar** with menus, items, sub-menus, separators, check and radio
    items, icons, keyboard shortcuts shown and bound, disabled items.
32. **Popup menu** opened from any widget by click or right click, same
    content model as the menu bar.
33. **Navigation bar** (vertical or horizontal) with items, sub-items,
    icons, badges, collapsed mode showing icons only, the current item
    marked.
34. **Breadcrumb**: the path to the current page with links on every step
    but the last.
35. **Tree**: nodes with children loaded eagerly or on open, expand and
    collapse, selection of one or many, check boxes with tri-state, icons
    per node, columns per node (tree table), drag of nodes where allowed.
36. **Pager**: page navigation for any collection widget: first, previous,
    numbered pages, next, last, page size, total count; works by link and
    by live update.
37. **Link family**: page link, external link, action link, download link,
    lazy link (target resolved when clicked), returnable link (comes back
    to the current page), each with disabled and visible states.
38. **Anchor navigation**: jump to a region of the same page with the
    region highlighted.

### E. Inputs

39. **Text**: single line, multi line, password, with placeholder, maximum
    length, pattern, read-only and disabled states, and live update on
    change where asked.
40. **Numbers**: integer, long, decimal, double, with format, minimum,
    maximum, step; a spinner variant with up and down controls.
41. **Date and time**: date, time, date and time, month, year, week; a
    calendar popup, keyboard input in the user's format, minimum and
    maximum, disabled dates, time zone shown where it matters.
42. **Choice**: drop-down of one, list of one or many, radio group, check
    box group, with a renderer for the shown text and an optional empty
    choice.
43. **Combo**: text with suggestions from a source, free text allowed or
    not, remote source with a minimum of typed characters and a delay.
44. **Band box**: text whose popup is any panel the author builds (a
    search form, a table) and that puts the chosen value back.
45. **Chosen box**: many values from a source shown as removable chips,
    with suggestions while typing.
46. **Cascader**: a value chosen through levels (country, region, city)
    with one list per level.
47. **Check box and switch**: a boolean as a box or as a toggle switch,
    with a label on either side and an indeterminate state for the box.
48. **Slider and range slider**: one or two values on a scale with step,
    marks and the value shown; vertical where asked.
49. **Rating**: a value as a row of symbols, with half steps where asked,
    read-only variant.
50. **Colour**: a colour value with a picker and a text input.
51. **File upload**: one or many files, by dialog or by drop, with
    accepted types, maximum size, progress and a list of chosen files
    before and after upload.
52. **Signature**: a drawn signature captured as an image.
53. **Editor**: formatted text with a tool bar whose output is sanitised
    HTML; the plain text area is the fallback.
54. **Input group**: an input with a prefix, a suffix, a button or a unit
    attached to it.
55. **Search box**: a text with a search action, a clear control and
    recent values.
56. **Captcha**: a challenge the user has to pass before a form submits.
57. **Form**: groups inputs, validates them, shows errors next to the
    input and in a summary, submits and either stays with the errors or
    carries on; works stateless where nothing but the submit happens.
58. **Validation**: required, length, range, pattern, e-mail, date order,
    and a custom rule; each message in the user's language and placed at
    the input it belongs to.

### F. Collections

59. **Table** (grid): columns with header, footer and auxiliary header
    rows, sorting by column, paging, column resizing, column reordering,
    column hiding, frozen columns, row selection of one or many, row
    detail (an expandable region per row), grouping with group headers
    and footers, in-place editing of cells, row styling by rule, empty
    message, a total row.
60. **List** (list box): the same behaviours as the table for lists whose
    rows are not tabular: items with a template, selection, paging,
    sorting, grouping, check mark mode.
61. **Large list**: a table or list over a source too large to render at
    once; rows load as the user scrolls.
62. **Tree table**: see Tree, with columns.
63. **Data source**: every collection widget reads from a source that
    answers "how many" and "rows from x to y with this sort", so the
    database does the paging; a list in memory is a valid source.
64. **Selection model**: selection is a model the page can read and set,
    for one row, many rows, or none; a selection change can trigger a
    live update.
65. **Templates**: a row, a cell, a group header may be a template the
    author writes once per collection.

### G. Display

66. **Label and text**: a value as text, with format, multi-line, pre-
    formatted, hidden when empty, hyperlinks in the text detected where
    asked.
67. **Image and image map**: an image from a resource or a URL, with areas
    that act as links.
68. **HTML**: trusted markup inserted as it is; untrusted markup escaped.
69. **Progress**: a value as a bar, determinate or indeterminate, with the
    value shown.
70. **Badge and tag**: a short mark on another widget (a count, a state).
71. **Avatar**: a picture or initials in a frame.
72. **Icon**: an inline symbol from the toolbox's set, with a label for
    assistive technology.
73. **Audio and video**: a media file with the browser's controls.
74. **Empty state**: a block that explains that there is nothing to show
    and offers the next action.
75. **Organigram**: nodes in a hierarchy drawn top-down or left-right.
76. **Fisheye and dock**: a row of icons that enlarge on hover.

### H. Overlays and feedback

77. **Feedback panel**: messages of the page (info, success, warning,
    error), dismissible, placed once per page or per form.
78. **Notification**: a message that appears at an edge for a time and
    goes away, triggered by the server after an action.
79. **Message box**: a modal question with buttons (OK, Cancel, Yes, No,
    custom) that returns the answer to the page.
80. **Popup**: a floating panel anchored to a widget, opened on click or
    hover, closed on click outside or on request.
81. **Tooltip**: a short text on hover and on focus, with a delay.
82. **Coach mark**: a highlighted widget with an explanation, in a sequence
    the user steps through.
83. **Loading indicator**: a mark that the page or a region is busy, shown
    during a live update.

### I. Behaviours

84. **Live update**: any widget can be refreshed after an action without a
    full page load; a form field can update the page on change.
85. **Timer**: a region refreshes every n seconds while visible.
86. **Push**: the server sends an update to the page when something
    happens (a job finished).
87. **Keyboard shortcuts**: a page or widget binds keys to actions.
88. **Drag and drop**: a widget can be dragged onto another; the page is
    told what was dropped where; the server decides what happens.
89. **Visibility and state by rule**: visible, enabled, read-only from a
    function the page author gives, evaluated on every render.
90. **CSS class by rule**: a class added or removed by a function.
91. **Scroll into view and focus**: the page brings a widget into view or
    gives it focus after an action.
92. **Confirm before action**: an action asks a question first and runs
    only on yes.

### J. Embedded best-of-breed widgets

The cooperative does not rebuild what excellent open-source projects
already solve; it embeds them so that they play well with the rest of the
page (the owner's words, 2026-10-29: "top-notch Open-Source-Projekte ...
gut integriert, sodass das Zusammenspiel mit den anderen Komponenten
hervorragend läuft"). Each embedded widget has a server-side model the page
author works with; the page never speaks the embedded library's language.

97. **Map**: a map with markers, shapes and layers from the page's model,
    the viewport reported back, a click or a drawn shape reported to the
    page. The rendering library is replaceable behind one model; the
    candidates are the leading open-source browser map libraries, and the
    toolbox may ship one binding per supported library instead of one
    abstraction if the abstraction costs more than it saves.
98. **Chart**: bar, line, area, pie, scatter and gauge charts from the
    page's data, with legend, axes, tooltips, a selection reported to the
    page, and an export as image. Same rule as for maps: one model, one
    binding per supported library (the owner's candidates: a simple canvas
    library for dashboards, a large-data library, a scientific library,
    and a low-level toolkit for free forms).
99. **Diagram canvas**: a canvas of nodes and links the user can arrange,
    connect and edit, with the graph as the page's model, selection and
    changes reported to the page, and the surrounding toolbar, property
    panel and lists built from the ordinary widgets of this catalogue. The
    owner names one library he knows well for it.
100. **Document viewer**: a document (PDF first) shown with the browser's
    own viewer in a region of the page or in a new tab, with a download
    link; the toolbox builds no viewer of its own.
101. **Spreadsheet**: where a page needs a spreadsheet, an existing
    collaborative spreadsheet project is embedded and its data moved in
    and out of the page's model in both directions; the toolbox builds no
    spreadsheet of its own.

### K. Page level

102. **Mounting**: a page is reachable at a fixed address declared at the
    page, with typed parameters read from and written to the address.
103. **Base page**: title, language, the catalogue's style sheets, the
    feedback panel, the loading indicator, and a region for the content.
104. **Session access**: the signed-in user and the user's locale available
    to every widget.
105. **Resource delivery**: a widget's style sheet and script are delivered
    once per page, cached, versioned.

## Examples

- A member list page: a table from a source with paging of 25, sorting by
  family name, a row selection that fills a card on the right in the split
  layout, and a toolbar with "New" opening a window as a modal dialog.
- A profile form: a form with text, date, combo with remote suggestions,
  chosen box for skills, rating for the level, file upload for the photo;
  the submit stays on the page with errors at the fields or redirects.
- A settings page: tab box with four tabs, the second an accordion of
  group boxes, each with switches that update live.
- An admin dashboard: border layout with the navigation bar on the left in
  collapsed mode, a breadcrumb on top, cards in a portal layout the user
  reorders, a timer that refreshes the "jobs" card every 10 seconds, a
  notification when a job finishes.
- Rejected: a widget that needs a styling framework on the page; a widget
  that only works with script on; a collection widget that loads every
  row to show page 1.

## Out of scope

- A viewer, map, chart, diagram or spreadsheet engine of the cooperative's
  own; section J embeds existing projects.
- Theming beyond the tokens the style sheets expose; a design system is a
  separate piece of work.
- Mobile-specific widgets (swipe views, pull to refresh).

## Open questions

- Q1 **Rich text editor** (rule 53): a usable editor needs a script
  library. Does the owner want one in the toolbox, with the plain text
  area as the no-script fallback, or is formatted text out of scope for
  now? Proposal: in scope with the fallback; the library is chosen in the
  solution specification.
- Q2 **Captcha** (rule 56): a challenge the toolbox generates itself is
  weak against bots and strong against users; a third-party challenge
  sends data out. Proposal: the toolbox offers the slot and a simple
  self-made challenge; applications plug in a stronger one.
- Q3 **Organigram, fisheye, coach mark, signature** (rules 52, 75, 76, 82):
  rarely needed in the cooperative's products. Keep them in the catalogue
  for completeness or defer them to a second round? Proposal: keep them
  in the catalogue, build them last.
- Q4 **Showcase as a published artefact** (rules 10 to 12): internal tool
  only, or published next to the toolbox's documentation? Proposal:
  internal first, published once the catalogue is complete.
- Q5 **One abstraction or one binding per library** (rules 97, 98): the
  owner accepts either. Proposal: one server-side model per widget kind
  (map, chart) and one binding per library behind it; where a library's
  strength does not fit the common model (3D, scientific plots), the
  binding exposes it as its own option and the model stays small.
- Q6 **Script delivery** (section J): the embedded libraries are script
  packages. Does the owner want them delivered from the toolbox's own
  resources, from packaged web archives, or from the application's own
  bundle? Proposal in the solution specification.
