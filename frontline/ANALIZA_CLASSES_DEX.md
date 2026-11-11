# Wyniki analizy oryginalnego classes.dex (Age of History 3 Android)

Przeanalizowany plik: `classes.dex` użytkownika, nagłówek `dex\n035`, rozmiar 9 989 576 B.
W pliku znaleziono 8239 definicji klas, około 5248 w pakiecie `aoc.kingdoms.lukasz`.
To ustalenia na podstawie **rzeczywistego pliku**, nie ogólnych poradników.

## Klasy i API potwierdzone w DEX

- `aoc.kingdoms.lukasz.jakowski.Game`:
  - `static getProvince(int): Province`
  - `static getProvincesSize(): int`
  - `static getCiv(int): Civilization`
  - `static getDistanceFromProvinceToProvince_Simpler(int,int): float`
  - pola `gameThread`, `gameThreadTurns`, `activeArmy`, `lProvinces`, `lCivs`
  - metoda `regroupArmy_Move()` — gra ma już ręczne przegrupowywanie armii.
- `aoc.kingdoms.lukasz.map.province.Province`:
  - `getCivID(): int`
  - `getNeighboringProvincesSize(): int`
  - `getNeighboringProvinces(int): int`
  - `getArmy(String): ArmyDivision`
  - `getArmySize(): int`
- `aoc.kingdoms.lukasz.map.civilization.Civilization`:
  - `getNumOfProvinces(): int`
  - `getProvinceID(int): int`
  - `getArmyPositionKey(int): String`
  - `getArmyPosition(int): int`
  - `isInMoveUnits_ArmyKey(String): boolean`
  - `newMove(int,int,String,int,boolean): boolean` — **znaczenie parametrów niezweryfikowane**
  - publiczne pole `iArmyPositionSize`
- `aoc.kingdoms.lukasz.map.army.ArmyDivision`:
  - publiczne pola `key: String`, `civID: int`, `provinceID: int`, `iArmyRegimentSize: int`
- `aoc.kingdoms.lukasz.map.diplomacy.DiplomacyManager`:
  - `static isAtWar(int,int): boolean`
- `aoc.kingdoms.lukasz.jakowski.GameThreads.GameThread`:
  - `updateMoveUnits()`, `run()`
- `aoc.kingdoms.lukasz.map.moveUnits.MoveUnits`:
  - `buildRoute(int,int,int,boolean)`, `update()`

## Proponowane punkty integracji

1. Silnik frontów uruchamiać w kontrolowanym miejscu cyklu tury, a nie w każdej klatce.
2. UI frontów w klasach mapy i menu gry; dodać gest wyboru granicy i przyciski.
3. Rysowanie linii tylko w fazie renderowania, najlepiej z użyciem istniejącego renderera LibGDX.
4. Przed wydaniem rozkazu marszu potwierdzić znaczenie pięciu argumentów `Civilization.newMove` i zasady dostępu / wojny.
5. Zapis i odczyt frontów wymaga osobnego formatu lub rozszerzenia istniejącego SaveLoad.

## Istotne ograniczenia

**Nie istnieje osobny folder `game/frontline/` obsługiwany przez oryginalne APK.**
Kod Java w repozytorium nie jest automatycznie ładowany przez AoH3. Aby włączyć nowe klasy, trzeba skompilować i zmienić DEX, a potem spakować i podpisać APK.

Źródła w repozytorium mają status prototypu: część odczytowa API jest powiązana z klasami z DEX, ale połączenie z wywołaniami ruchu, interfejsem i zapisem nie zostało wykonane ani przetestowane na urządzeniu.
