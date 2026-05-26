# orchestration_comparison

## AI Agent Setup

This repository is connected to shared Java agent guidance as a git submodule at `.agent/shared`.

Detected project type: Spring Boot. The sparse checkout for `.agent/shared` is configured with these paths:

- `java`
- `gradle`
- `spring-boot`

The root agent instruction files are symlinks to the Spring Boot guidance:

- `AGENTS.md` -> `.agent/shared/spring-boot/AGENTS.md`
- `CLAUDE.md` -> `.agent/shared/spring-boot/AGENTS.md`

When pulling this repository, include submodules:

```bash
git pull --rebase --recurse-submodules
```

To update the shared agent guidance to the latest upstream commit:

```bash
git submodule update --remote --recursive
```

Recommended git aliases:

```bash
git config alias.pullr "pull --rebase --recurse-submodules"
git config alias.sub-update "submodule update --remote --recursive"
```
