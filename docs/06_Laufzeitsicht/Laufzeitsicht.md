# 6.0 Laufzeitsicht

### Command-line-interface Ablauf

1. start der Anwendung 

   1. Entweder über zwei config files (source und destination) oder über Schritt für Schritt Abfrage der Parameter

2. Abfragen für source JBDC url werden nacheinander ausgeführt:

   1. Um welches source Datenbank-System handelt es sich? -> Protocol/sub protocol defintion
      1. Wird das Datenbank-System von uns unterstützt?
   2. Wie lautet die Server DNS oder IP-Adresse?
      1. Evlt. Kontrolle
   3. Wie lautet die Portnummer?
      1. Evlt. Kontrolle
   4. Wie lautet der Name von der Datenbank?
      1. Evlt. Kontrolle

   --> Daten für source Datenbank sind nun vollständig.

3. Abfrage für destination JBDC url werden nacheinander ausgeführt:

   1. Um welches source Datenbank-System handelt es sich? -> Protocol/sub protocol defintion
      1. Wird das Datenbank-System von uns unterstützt?
   2. Wie lautet die Server DNS oder IP-Adresse?
      1. Evlt. Kontrolle
   3. Wie lautet die Portnummer?
      1. Evlt. Kontrolle
   4. Wie lautet der Name von der Datenbank?
      1. Evlt. Kontrolle

4. Eventuelle constraint nachfragen

5. Verarbeitung der Daten

6. Feedback, ob alles funktioniert hat
