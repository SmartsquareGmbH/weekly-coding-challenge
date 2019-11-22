# Lösungen in 64-bit asm für MacOS

Der grundliegende Ansatz ist bei beiden Lösungen identisch:  
Der Quellcode der Anwendung wird per Präprozessor beim kompilieren eingelesen und als statische daten in die erzeugte Applikation gebundled. Beim Ausführen werden diese Daten dann per `write` syscall auf stdout ausgegeben.

## simple.asm
Diese Anwendung tut das geforderte - sie gibt ihren Quellcode aus.

Verwendung: `make run-simple`

## meta.asm
Etwas spaßiger: Wenn `meta` ausgeführt wird, gibt es nicht den eigenen Quellcode auf stdout aus, sondern den eines Shellscriptes.  
Speichert man dieses Script in einer Datei und führt es aus, passiert folgendes:

* Das skript erzeugt ein Verzeichnis `tmp` im aktuellen Arbeitsverzeichnis
* Es erzeugt darin die Quellcodedateien `simple.asm`, `meta.asm` und eine `Makefile`
* Es führt im neu angelegten Verzeichnis `make run-meta` aus. 

Das make-Target `run-meta` kompiliert aus `meta.asm` ein neues `meta` binary, führt dieses aus, leitet den output in `meta.sh` um, und markiert dieses als ausführbar.

Verwendung:  
`make run-meta`, um das Shellscript zu erzeugen und auf stdout zu sehen.  
`make replicate` um das Shell auch auszuführen.
