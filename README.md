
# Opis techniczny projektu

Projekt demonstracyjny Kotlin Multiplatform (Compose Multiplatform) prezentujący trzy podejścia do lokalnej persystencji danych dla wspólnego modelu `Note` na Android oraz iOS:

1. Shared Preferences / NSUserDefaults (biblioteka `multiplatform-settings`) – klucz‑wartość, rozwiązanie lekkie.
2. Plik JSON (kotlinx-serialization + własne API expect/actual do I/O) – zapis całej kolekcji w jednym pliku.
3. SQLite (SQLDelight) – relacyjna baza z indeksowaniem i mapowaniem typów.

## Moduły
- `shared` – logika domenowa + warstwa persystencji + UI Compose (wspólne). Zawiera `expect/actual` dla plików i ścieżek.
- `androidApp` – punkt startowy Android (Activity + inicjalizacja repozytoriów, sterowanie cyklem życia).
- `iosApp` – punkt startowy iOS (SwiftUI + mostek do `ComposeUIViewController`).

## Model danych
Aktualnie: `Note(id: Long, title: String, content: String, createdAt: Long, tags: List<String>, priority: Int)` serializowany przez `kotlinx.serialization`. W JSON/SQLite pole `tags` przechowywane jako lista (JSON) lub `tags_json` (string JSON) w tabeli. Planowane ulepszenia: UUID dla `id`, `Instant` dla czasu.

## Warstwa abstrakcji
`StorageRepository` dostarcza jednolite API: odczyt listy, insert/update/delete, czyszczenie oraz seed danych. Implementacje:
- `PrefsRepository` – serializacja poszczególnych pól pod klucze `note_{index}_*` + licznik `notes_count`.
- `FileRepository` – pełny snapshot listy w `notes.json` w katalogu dokumentów (platformowe ścieżki przez `FilePaths`).
- `SqlDelightRepository` – zapytania generowane ze schematu `notes.sq` (indeks na `created_at`).

## Inicjalizacja platformowa
- Android: `initializeRepositories(context)` oraz `initializeSqlDelightRepository(context)` ustawiają kontekst i tworzą drivery.
- iOS: fabryki korzystają z `NSUserDefaultsSettings` oraz `NativeSqliteDriver` bez dodatkowych parametrów.

## Wątki i coroutine
Operacje I/O i DB wykonywane w `withContext(Dispatchers.Default)` (do rozważenia migracja na `Dispatchers.IO`). UI komunikuje się przez prosty stan Compose (listy notatek + flagi ładowania).

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…