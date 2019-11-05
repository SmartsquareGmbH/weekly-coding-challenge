## Willkommen zu den Weekly Code Challenges 

Zum Einstieg haben Ruben und Ich uns überlegt, dass wir mal aus unserer JVM ausbrechen. Zwar haben wir für diese Aufgabe eins der altbekannten Katas gewählt, jedoch möchten wir diesmal den Fokus auf die Performance legen.

Als Algorithmus würden wir gerne das Prime Factors Kata (http://butunclebob.com/ArticleS.UncleBob.ThePrimeFactorsKata) implementieren. Die beschriebene Methode soll als HTTP GET Endpunkt unter localhost:8080/generate/n exponiert werden. Das Ziel ist es den Endpunkt so performant wie möglich zu implementieren. Der technischen Fantasie sind dabei keine Grenzen gesetzt. Die einzige Regel ist, dass es auf einem Linux Rechner ausführbar sein muss. :bullettrain_side:
Um dem ganzen einen wettbewerbscharakter zu verleihen würden wir am Ende der Woche gerne alle Lösungen mit Locust unter Last testen und die Lösung nach der niedrigsten durchschnittlichen Antwortzeit und den wenigsten Ausfällen bewerten. :100:

Ich habe als Endtermin erstmal Freitag um 13:00 festgelegt, falls ich im Kalender was übersehen habe, gebt mir bitte Bescheid. :clock1:
Die Lösungen können wir in Form von Pull Requests im Projekt (https://github.com/SmartsquareGmbH/weekly-coding-challenge) zusammenführen.

## Anforderungen in Stichpunkten:
* Applikation per HTTP/1.1 unter :8080 erreichbar
* Der Service antwortet auf GET-Requests unter dem Pfad /generate/_n_, wobei _n_ die zu faktorierende Zahl ist
* Der Service muss mindestens in der Lage sein, 32-bit integer korrekt zu verarbeiten.
* Die Antwort des Service ist als JSON _array_ von _number_ zurück zu liefern, mit passendem Content-Type (z.B. application/json)
* Ein test-script, dass diese Anforderungen überprüft befindet sich in diesem Verzeichnis unter dem Namen test.sh. Zur Ausführung des Scriptes muss jq installiert sein.
* Die Abgabe sollte eine kurze README-Datei mit Informationen zum kompilieren und starten des Projekts enthalten.

## Beispielanfragen und -Antworten:

GET /generate/1  
[1]

GET /generate/254  
[2, 127]

GET /generate/4294967295  
[3, 5, 17, 257, 65537]