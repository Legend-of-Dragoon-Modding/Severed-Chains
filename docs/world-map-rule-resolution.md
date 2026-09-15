# World-map rule resolution

## Legacy compatibility

Existing unscoped portal, capability, policy, departure, and arrival registrations remain supported. Their attribution identifies `lod:wmap_legacy_rules`, with `legacy() == true` and maximum integer priority. Portal/capability composition and legacy replacement ordering remain unchanged.

A builder logs one diagnostic per affected target when a replacement shares that target with an unscoped replacement. This reports a potentially order-dependent override without executing callbacks during configuration. Attribution is available through the existing inspection methods.

## Departure and arrival decisions

Previously, the last selected departure/arrival callback supplied the whole decision. New attributed decision APIs allow callbacks to abstain by returning `null`:

```java
// Before: an unconditional replacement competes by registration order
builder.departure((origin, progression) -> WorldMapTravel.Departure.NONE);

// After: this source participates only for its own endpoint
builder.departureDecision(new RegistryId("example", "harbour"), 20,
  (origin, progression) -> origin.cut() == 900
    ? WorldMapTravel.Departure.FIRST_QUEEN_FURY
    : null);

builder.arrivalDecision(new RegistryId("example", "teleporter"), 20,
  (origin, progression, definition) -> origin.cut() == 901
    ? WorldMapTravel.Arrival.TELEPORT
    : null);
```

Resolution visits priority groups from highest to lowest. Within a group, attributed callbacks may agree or abstain. Incompatible applicable results throw an exception naming the departure/arrival context, priority, sources, and results. Lower-priority callbacks run only when the entire higher-priority group abstains. Complete abstention produces `Departure.NONE` or `Arrival.NORMAL`.

Existing `departure(...)` and `arrival(...)` calls inside a source scope participate in strict decision resolution and must return a non-null result. To retain conditional participation, migrate to the new decision methods. Equal-priority registrations from the same source must also agree; source identity does not excuse ambiguous outcomes.

Unscoped legacy decisions remain a compatibility layer at maximum priority. Their last applicable registration wins by order, with diagnostics. They do not suppress conflicts between attributed decisions in that same priority group. Migrate every competing unscoped callback to enable priority-based cooperation below that layer. Callbacks must be pure because multiple attributed callbacks can be evaluated for one decision.

Policy and portal/capability replacement conflicts between different attributed sources at the same priority remain configuration errors. Boolean access composition is unchanged.

## Validation and migration

These changes were reviewed from source only; no builds, tests, lint, or runtime checks were run. Attribution now includes a `legacy` field while retaining the existing three-argument constructor for source compatibility. Mods inspecting attribution should use `legacy()` instead of checking for a null source.
