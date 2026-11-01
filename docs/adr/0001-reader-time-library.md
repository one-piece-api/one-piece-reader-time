# ADR-0001: Reader-Axis Resolution as a Shared Library

## Context

Workflow revision 17 (§3.7, §3.8) puts every revealable piece of content on the **reader axis**: a point is a chapter and/or an episode, read independently on each axis. The same questions must get the same answers in two places:

- `one-piece-content-service`: checks on save (point rules, periods, reveals) and the editor's "preview at N" of a draft.
- `one-piece-public-api`: the public spoiler filter `asOfChapter` / `asOfEpisode` over published content.

The rules are subtle (no order between axes, a missing part means "not seen", windows half-open for overlaps, ended paragraphs hidden, main portrait derived). Two implementations would drift.

## Decision

A new repository, `one-piece-reader-time`, published to GitHub Packages like `one-piece-exception`, holds the rules as **pure Java** (no Spring, no persistence):

- **Neutral model**: `Point`, `ReaderPosition`, `RevealWindow`, `RevealedField<V>` with `FieldReveal<V>`, and the `Windowed` / `GalleryImage` interfaces. Services adapt their own data to it: ids become chapter and episode numbers.
- **Resolution at N**: window state, field value, visible paragraphs, main portrait, item and entry visibility.
- **Checks on save**: pure functions returning violations (`PointRules`, `PeriodRules`, `RevealRules`). Each service maps them to its own error codes and field paths.
- **Field reveals are generic**: one `RevealedField<V>` per field. Fields revealed together become one record `V`. Which fields go together is the entity's rule and stays in the service.
- **Reference cases** of the reveals plan (Nami, Usopp, Sabo, Ace, Rocks, Gomu Gomu) are its tests, on both axes.

## Alternatives considered

- **SQL functions in the `published` views**: one implementation for public-api, but content-service would need the same rules in Java for drafts. Also harder to test than plain unit tests.
- **Duplicate the rules in both services**: no release cycle, but two copies of subtle rules drift. The bug only shows as a spoiler or as a refused save.
- **Public-api calls content-service**: one implementation, but it adds a runtime dependency to the read path, the most loaded one. It also breaks public-api's read-only, database-only design.
- **One reveal type per entity** (as `DevilFruitReveal` on the `timeline` branch): simpler for one entity, but the same resolution would be written three times (Devil Fruit, Bounty, Character).

## Consequences

- Both services give one answer, tested once.
- A rule change needs a release cycle: change, tag `vX.Y.Z`, bump the version in the consumers. CI publishes **only on a tag** whose name matches `version`. GitHub Packages refuses to overwrite a version, so publishing on every push to `main` would fail after the first one.
- Consumers need GitHub Packages read credentials, as for `one-piece-exception`.
- The adapters (ids to numbers, rows to records) stay in each service. They are thin but are not shared.
