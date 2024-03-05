# 8.0 Architektur- und Entwurfsmuster 

## Dependency Injection

Um eine lose Kopplung zu gewährleisten und die Verantwortung der Instanz-Erzeugung auszulagern (single responsibility principle) wurde Dependency Injection mit Hilfe des Frameworks [Guice](https://github.com/google/guice) implementiert.

## Singleton

Die Klassen `DestinationDbSysConstsLoader` und  `SourceDbSysConstsLoader` wurden nach dem Singleton Muster implementiert und global je eine Instanz zugänglich zu machen. So haben die Klassen `DestinationConsts` und `SourceConsts`  einfachen Zugriff auf die jeweilige Klasse. Da  diese Klassen nur statische Methoden bzw. Konstanten definieren, wäre hier die Bereitstellung der ConstLoader-Klassen über DI keine Option gewesen. 