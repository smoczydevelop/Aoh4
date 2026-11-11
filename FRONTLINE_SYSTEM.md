# AoH4 Premium — Frontline System 1.0 (projekt)

Status: specyfikacja, NIE działający mod. Integracja wymaga dostępu do logiki ruchu armii i mapy w AoH3/Bastille.

## Cel
- Gracz tworzy wiele niezależnych frontów na granicy z wybranym państwem.
- Przypisuje armie do frontu, bez automatycznego ataku.
- Rozkazy: HOLD, DEFEND, ATTACK, STOP; fronty mają własne kolory i nazwy.
- Po zmianie granicy system przelicza docelowe prowincje i przemieszcza armie.

## Model
Front {id, ownerCivilizationId, targetCivilizationId, name, color, provinceIds[], assignedArmyIds[], stance, enabled}

## Algorytm (szkic)
1. Wyznacz prowincje właściciela sąsiadujące z prowincjami celu.
2. Podziel granicę na spójne segmenty; gracz wybiera segment lub całą granicę.
3. Rozdziel przypisane armie między prowincje graniczne z uwzględnieniem pojemności, odległości i zagrożenia.
4. Dla HOLD/DEFEND: wydaj wyłącznie rozkazy przemarszu i utrzymania pozycji.
5. Dla ATTACK: wybierz osiągalne prowincje przeciwnika i respektuj istniejące zasady wojny/ruchu.
6. Po zmianie właściciela prowincji lub utracie armii przelicz przydziały; nie wydawaj rozkazów co klatkę.

## Integracja — wymagane punkty
- odczyt sąsiedztwa i właścicieli prowincji
- identyfikacja armii i rozkaz przemarszu/ataku
- cykl tury lub tick AI
- rysowanie linii na mapie i obsługa dotyku
- serializacja frontów do zapisów gry

## Bezpieczeństwo
Zachować kopię oryginałów. Nie podmieniać losowych plików .dex/.so: błędna wersja uniemożliwi uruchomienie gry.
