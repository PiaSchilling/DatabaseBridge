# 4.1 Lösungsstrategie

[TOC]

Die folgende Tabelle stellt die Qualitätsziele von DatabaseBridge (siehe Kapitel1) passenden Architekturansätzen gegenüber.

| Qualitätsziel             | Motivation und Erläuterung                                   |
| ------------------------- | ------------------------------------------------------------ |
| Flexibel/ Erweiterbarkeit | - Datenbank spezifische Implementierungen im code vermeiden<br />- Datenbank spezifische Attribute wie Datentypen, SQL Statements, etc. in file auslagern<br />- Lose Kopplung durch Dependency Injection und Interfaces |
| Integrität                | - auslesen aller constraints, user, tabellen und relationen<br />- Schreiben der Tabellen, User, ..., in eigenständig ausführbare Methoden auslagern -> ein Fehler führt nicht zum Abbruch der ganzen Anwendung bzw. Scheitern der Konvertierung |
| Verständlich              | - help command bereitstellen<br />- config file template bereitstellen<br />- angemessene Dokumentation zur Nutzung bereitstellen |

