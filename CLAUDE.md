# one-piece-reader-time

Pure Java library: the reader-axis rules of workflow revision 17 (§3.7, §3.8), shared by content-service (save checks, preview at N) and public-api (spoiler filter). Usage: `README.md`. Design: `docs/adr/0001`.

```bash
./gradlew check > "$TMP/check.log" 2>&1; echo "exit=$?"
```

- No Spring and no persistence: neutral records and pure functions only. Services adapt their data and map violations to their own errors.
- Tests use the reference cases in `ReferenceCases` (real chapter and episode numbers). Check both axes.
- Release: bump `version` in `build.gradle.kts`, then push tag `v<version>`. CI publishes only on that tag. Then bump the version in the consumers.
