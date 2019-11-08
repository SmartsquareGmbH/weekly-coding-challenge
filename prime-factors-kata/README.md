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
* Ein test-script, dass diese Anforderungen überprüft befindet sich im Verzeichnis [testing](testing) unter dem Namen test.sh. Zur Ausführung des Scriptes muss jq installiert sein.
* Die Metriken zur Geschwindigkeit und damit Bewertung werden mit dem Locust-Container im [testing](testing) Ordner gesammelt.
* Die Abgabe sollte eine kurze README-Datei mit Informationen zum kompilieren und starten des Projekts enthalten.

## Beispielanfragen und -Antworten:

GET /generate/1  
[1]

GET /generate/254  
[2, 127]

GET /generate/4294967295  
[3, 5, 17, 257, 65537]

## Leaderbord

Gatling Tests ausgeführt auf Arch Linux (Linux 5.3.8-arch1-1) mit Intel® Core™ i7-7700T CPU @ 2.90GHz × 8 und 15,6 GiB RAM.

|                   | Autor      | Technologie                                              | Requests/s                               |
|-------------------|------------|----------------------------------------------------------|------------------------------------------|
| :1st_place_medal: | rubengees  | go ([fasthttp](https://github.com/valyala/fasthttp))     | 56916.917                                |
| :2nd_place_medal: | jkrafczyk  | rust ([hyper](https://github.com/hyperium/hyper))        | 49474.367                                |
| :3rd_place_medal: | danielr    | rust ([hyper](https://github.com/hyperium/hyper))        | 46841.767                                |
|                   | deen13     | rust ([nickel](https://github.com/nickel-org/nickel.rs)) | 37908.125 (24 failed / 4548975 requests) |
|                   | jkrafczyk  | c                                                        | 24250.5                                  |
|                   | danielr    | java ([quarkus native](https://quarkus.io/))             | 20412.417                                |
|                   | darivs     | java ([spring boot](https://spring.io/))                 | 20376.575                                |
|                   | karstenamf | javascript ([express](https://expressjs.com/))           | 13028.958                                |
