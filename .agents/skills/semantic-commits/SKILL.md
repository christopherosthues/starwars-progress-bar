---
name: Semantic Commits
description: Writes Conventional Commits–style commit messages for this repository. Use when committing changes, titling PRs (squash merges turn the title into the commit), or reviewing commit message quality — covers type selection, breaking-change markers, and changelog coupling.
---

# Semantic commits

This repo follows [Conventional Commits](https://www.conventionalcommits.org) for hand-written commits: `type: subject`. There is no commit-lint tooling; the convention keeps history readable and makes drafting CHANGELOG.md entries straightforward. Merges are squashed, so **PR titles must follow the same format** — they become the commit message (e.g., `docs: Changelog and Readme update - 3.0.1 (#295)`).

## 1. Format

```
<type>[optional scope]: <subject>
[optional body]
```

- Type is lowercase, followed by a colon + space.
- Subject in imperative mood ("Add" not "Added"), no trailing period.
- Keep the subject ≤ 72 characters. Hand-written commits are header-only; add a body only when the *why* isn't obvious from the diff (wrap at 72).

## 2. Types

| Type | Use for |
|---|---|
| `feat` | New user-visible functionality: new vehicles/lightsabers/factions, new settings, new languages |
| `fix` | Bug fixes (including wrong or missing translations) |
| `docs` | README, CHANGELOG, AGENTS.md, skills, code comments — no runtime behavior change |
| `refactor` | Behavior-preserving code changes; formatting-only changes also go here |
| `test` | Adding or updating tests and test helpers |
| `chore` | Housekeeping: version bumps in `gradle.properties`, dependency updates, config that doesn't affect the plugin at runtime |
| `ci` | Changes to `.github/workflows/` |
| `build` | Build system changes (Gradle, toolchains, `libs.versions.toml`) |

- Scope is optional and omitted by default; only dependabot auto-commits use scopes (`chore(deps): ...`, `ci(deps): ...`).
- `style`, `perf`, and `revert` are valid conventional types but unused in this repo — fold formatting into `refactor`.
- When a change mixes concerns (e.g., feature code plus docs), prefer splitting it into separate commits so each has one type.

## 3. Breaking changes

1. Mark the commit with `!`: `<type>!: subject` (e.g., `feat!: Remove lightsaber-only mode`) or add a `BREAKING CHANGE:` footer.
2. Add a changelog entry under `[Unreleased]` in `CHANGELOG.md`, marked with a leading `BREAKING:` — this is what release tooling and users see:

   ```markdown
   ### Changed

   - BREAKING: Raised pluginSinceBuild to 262 (plugin now requires IDE 2026.2+)
   ```

3. Bump the version in `gradle.properties` per SemVer (breaking → major).

## 4. Examples from this repo's history

```
feat: Add labels to the selection options
fix: Track previousVersion to detect plugin updates
refactor: Extract migration methods
test: Use FactionCreationHelper
ci: Rename changelog update branch on release
docs: Update AGENTS.md
chore: Increase version
```

## 5. Checklist before committing

- Header matches `<type>: <subject>` with one of the types above.
- Imperative mood, no trailing period, ≤ 72 characters.
- The type fits the dominant change; mixed concerns split into separate commits where practical.
- Breaking change → `!` marker **and** a leading-`BREAKING:` entry under `[Unreleased]` in CHANGELOG.md.
