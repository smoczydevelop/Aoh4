# AoH4 — gdzie wkleić pliki?

## Aktualny status
To **kod źródłowy prototypu**, a nie gotowy plik do podmiany w grze.

Struktura repozytorium:
```
frontline/
  src/main/java/com/aoh4/frontline/FrontlineSystem.java
  README.md
  INSTALL_PL.md
```

## Aby zrobić działający mod
1. Prześlij strukturę katalogów oryginalnej gry (same nazwy folderów wystarczą na początek).
2. Ustal, czy masz źródła silnika, czy wyłącznie APK / DEX.
3. Dopasujemy adapter World do klas prowincji, armii i rozkazów AoH3/Bastille.
4. Dodamy GUI, zapis frontów i kompilację zgodną z Androidem.
5. Dopiero wtedy przygotujemy paczkę z **dokładnymi ścieżkami podmiany**.

Nie kopiuj pliku .java do instalacji gry — sam nie zadziała.
