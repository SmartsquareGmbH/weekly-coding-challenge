# Lasttests

Dieser Ordner enhält Konfigurationen und Scripte Dockerfile um die Abgaben zu testen und die Metriken zu vergleichen.

## Gatling

### Ausführung unter Docker (notlösung!)
```bash
./run-gatling.sh
```

### Ausführung nativ (empfohlen!)
* Gatling herunterladen und entpacken: https://gatling.io/open-source
* `/path/to/gatling-home/bin/gatling.sh -sf gatling -rf "gatling-results" -rsf gatling -rd "Load test"`  

### Auswertung
HTML reports und logfiles liegen in ./gatling-results/

## Locust

### Ausführung
```bash
./run-locust.sh [name]
```
_name_ wird als Präfix für die exportierten CSV dateien verwendet. Falls _name_ nicht angegeben wurde, wird ein Timestamp verwendet.


### Auswertung
```bash
./run-jupyter.sh
```
Jupyter sollte beim Start eine Ausgabe in dieser Form präsentieren:
```
[C 17:02:14.886 LabApp] 
    
    To access the notebook, open this file in a browser:
        file:///home/jovyan/.local/share/jupyter/runtime/nbserver-6-open.html
    Or copy and paste one of these URLs:
        http://bca279e32d4d:8888/?token=50516bdab86cd45af209e82c54651ef8dff83a822b82ca41
     or http://127.0.0.1:8888/?token=50516bdab86cd45af209e82c54651ef8dff83a822b82ca41
```
Hier bitte einfach den letzten angezeigten Link öffnen.  
Innerhalb von Jupyter im Browser dann links das _work_-Verzeichnis öffnen und darin das notebook _Compare Results.ipynb_ auswählen. 
Der Übersicht halber empfiehlt es sich, im Menü unter _View_ einmmal _Collapse All Code_ zu betätigen. 
Nach Ausführung des Notebooks (_Run_ / _Run All Cells_) können verschiedene Graphen angezeigt werden.

