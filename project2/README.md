Aby uruchomić program najlepiej jest utworzyć virtualenva, 
a następnie pobrać wymagane pakiety komendą.
```
pip install -r requirements.txt
```

Następnie należy uruchomić program z flagą -f nazwa_pliku. Dane wejściowe 
powinny znajdować się w folderze inputs, wystarczy podać samą nazwę pliku.
```
python main.py -f example_input_1
```
Następnie na ekranie powinny pojawić się otrzymane relacje zależności i niezależności
dla podanych w pliku transakcji, postać normalna Foaty dla podanego śladu, a także
uzyskany graf w formacie .dot. Ponadto w folderze graph_plots powinny pojawić się zapisane grafy.
Graf będzie zapisany w dwóch wersjach (przed i po usunięciem zbędnych krawędzi).\
Dodatkowo podając flagę -show można od razu wyrenderować grafy jako png.
```
python main.py -f example_input_1 -show
```
Jednak wymaga to pobrania programu graphviz. Na Windowsie dodatkowo należy potem 
dodać folder Graphviz\bin do zmiennych środowiskowych.