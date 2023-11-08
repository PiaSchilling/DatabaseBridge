# 1.1 Aufgabenstellung 

[TOC]



### Was ist DatabaseBridge?

DatabaseBridge ist eine Anwendung, welche dabei hilft eine existierende Datenbank in ein anderes Datenbank-System umzuziehen. 
Beispielsweise ist es möglich eine Postgresql Datenbank in eine Mysql Datenbank umzuziehen. 

### Wesentliche Features 

- Alle Tabellen und deren Relationen sollen in das neue Datenbank-System übernommen werden
- Es sollen so viele integrity constraints übernommen werden, wie möglich. Dazu gehören:
  - Data types potentially differing on both systems.
  - `null` / `not null` constraints.                              
  - Column default values.
  - Primary / candidate key constraints.
  - Foreign key constraints.
  - Check constraints.

- Es handelt sich um eine Anwendung, welche über die command-line bedient wird
- Die Anwendung ist auf verschiedene Datenbank-Systeme anwendbar
