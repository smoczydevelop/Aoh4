# AoH4 Premium — Frontline System (prototyp)

**Gra docelowa:** oryginalne Age of History 3 na Androida, kod w `classes.dex` (nie Bastille).

## Co już jest
- Algorytm wielu linii frontu (`FrontlineSystem.java`).
- Armie identyfikowane przez `String armyKey`, zgodnie z przesłanym DEX.
- Automatyczne wyszukiwanie prowincji granicznych.
- Wybór części granicy i podział wojsk między prowincje.
- Rozkazy HOLD / DEFEND / ATTACK / STOP.
- Adapter odczytu gry (`AoH3WorldAdapter.java`) bazujący na metodach potwierdzonych w DEX.

## Co nie działa jeszcze w samej grze
- Brak hooka wykonywanego co turę.
- Brak wykonania ruchu: `MovementHook` wymaga bezpiecznego podłączenia do `Civilization.newMove(...)`.
- Brak przycisków i rysowania frontów na mapie.
- Brak zapisu/odczytu frontów.
- Brak wynikowego, przetestowanego `classes.dex` lub APK.

## Instrukcja instalacji
Zobacz [INSTALL_PL.md](INSTALL_PL.md). **Nie kopiuj źródeł .java do APK**: nie są gotowym modem. Szczegółowa analiza przesłanego DEX jest w [ANALIZA_CLASSES_DEX.md](ANALIZA_CLASSES_DEX.md).
