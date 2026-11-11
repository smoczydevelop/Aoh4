# AoH4 Premium / Age of History 3 Android — instrukcja

## Twoja wersja
Otrzymany plik `classes.dex` pochodzi z oryginalnego AoH3 na Androidzie. Odczytano 8239 klas, w tym 5248 w pakiecie `aoc.kingdoms.lukasz`.

## Ścieżki na Twoim telefonie
Zgodnie ze zrzutem ekranu Twoje rozpakowane APK znajduje się w:

`/storage/emulated/0/Download/game/`

Istniejący kod gry jest w:

`/storage/emulated/0/Download/game/classes.dex`

To odpowiednik pliku `classes.dex` **w głównym katalogu APK**, a NIE folder `game/` wewnątrz archiwum.

## Co przygotowano w repozytorium
- `frontline/src/main/java/com/aoh4/frontline/FrontlineSystem.java` — algorytm planowania wielu frontów z tekstowymi kluczami armii.
- `frontline/src/main/java/com/aoh4/frontline/AoH3WorldAdapter.java` — odczyt prowincji, granic, armii, ruchu i stanu wojny z API wykrytego w DEX.
- `frontline/ANALIZA_CLASSES_DEX.md` — sprawdzone klasy oraz brakujące etapy.

**To są pliki źródłowe. NIE wklejaj ich do APK ani nie zmieniaj nazwy na classes.dex.**

## Co pozostaje do działającej wersji
1. Przeanalizować wywołania `Civilization.newMove(int,int,String,int,boolean)`, by bezpiecznie wydawać rozkazy marszu/ataku.
2. Dodać UI tworzenia/edycji frontów, przypisywania armii i kolory linii na mapie.
3. Dodać wywołanie silnika frontów w pętli tury, zapis i odczyt frontów.
4. Skompilować źródła do DEX, zintegrować z oryginalnym `classes.dex`.
5. Ponownie spakować i podpisać APK; przetestować na kopii i zabezpieczyć zapisy gry.

Podpis zmodyfikowanego APK musi być zgodny z zainstalowaną aplikacją, by nadpisać ją bez odinstalowania. W przeciwnym razie Android odmówi instalacji aktualizacji, a zmiana aplikacji może zagrozić danym zapisu.

## Stan
`PROTOTYP KODU` — nie jest to jeszcze instalowalny mod. Nie ma teraz bezpiecznej operacji „po prostu zamień plik” uruchamiającej fronty.
