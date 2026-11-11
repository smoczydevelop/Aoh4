# Frontline System 1.0 — core prototype

This is **Java source**, not a ready-to-install Android mod.

Features implemented in the standalone planning layer:
- Multiple fronts, each with a name, color, opponent and assigned armies.
- Automatic border discovery via province neighbors.
- Optional manual selection of a subset of border provinces.
- Army assignment to exactly one front.
- HOLD / DEFEND / ATTACK / STOP orders.
- Basic strength-aware allocation to border provinces.
- Attack orders only when the two civilizations are at war.

Limitations:
- Requires an implementation of World for the actual game engine.
- Does not render lines or buttons in the game.
- Does not serialize fronts into saved games.
- Does not implement sophisticated pathfinding, supply, front segmentation, or safe attack evaluation.
- The engine must validate movements, ownership, accessibility and army IDs.

DO NOT paste the Java file into assets or replace classes.dex with it.
