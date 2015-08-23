package com.example.thomas.voyage.ContainerClasses;


public class ItemCapability {

    /*

    NOCH NIRGENDS IN VERWENDUNG

     */


    public boolean formulaResolver(String var) {

        // 1. Gehe bis zu Symbol '->'
        //    falls nicht vorhanden, gehe zu Symbol '&&' od. '||'
        //     falls nicht vorhanden, gehe zu Symobl '==' od. '<=' od. '>=' od. '!=' od. '<' od. '>'
        //      falls nicht vorhandne, gehe zu Symbol '*' od. '/'
        //       falls nicht vorhanden, gehe zu Symbol '+' od. '-'
        //        falls nicht vorhanden, rufe 'return' auf

        // 2. Nimm den Abschnitt vom Anfang des Strings bis zum Symobl aus dem String heraus
        // 3. Rufe Funktion mit neuen Parametern rekursiv auf

        switch (var) {

            case "->":
                // if(left){ right };

                // left = formulaResolver( var von Stelle 0 bis letzte Stelle vor '->'
                // right = formulaResolver( var von Stelle '->' bis letzte Stelle
                break;

            case "&&":
                //
        }

        return false;
    }
}
