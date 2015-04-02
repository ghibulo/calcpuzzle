package cz.ghibulo.kalkulacka;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by ghibulo on 30.3.15.
 */

public class Priklad {
    NasButton[] tlacitka;
    int[] pocetStisku;
    double vysledek;
    public static final int pocetTl = 23;

    public Priklad(Activity akt) {
        tlacitka = new NasButton[pocetTl];
        pocetStisku = new int[pocetTl];
        vysledek=Double.MAX_VALUE;
        tlacitka[0] = (NasButton)akt.findViewById(R.id.b0);
        tlacitka[1] = (NasButton)akt.findViewById(R.id.b1);
        tlacitka[2] = (NasButton)akt.findViewById(R.id.b2);
        tlacitka[3] = (NasButton)akt.findViewById(R.id.b3);
        tlacitka[4] = (NasButton)akt.findViewById(R.id.b4);
        tlacitka[5] = (NasButton)akt.findViewById(R.id.b5);
        tlacitka[6] = (NasButton)akt.findViewById(R.id.b6);
        tlacitka[7] = (NasButton)akt.findViewById(R.id.b7);
        tlacitka[8] = (NasButton)akt.findViewById(R.id.b8);
        tlacitka[9] = (NasButton)akt.findViewById(R.id.b9);
        tlacitka[10] = (NasButton)akt.findViewById(R.id.brovnitko);
        tlacitka[11] = (NasButton)akt.findViewById(R.id.bplus);
        tlacitka[12] = (NasButton)akt.findViewById(R.id.bminus);
        tlacitka[13] = (NasButton)akt.findViewById(R.id.bkrat);
        tlacitka[14] = (NasButton)akt.findViewById(R.id.bdeleno);
        tlacitka[15] = (NasButton)akt.findViewById(R.id.blevazav);
        tlacitka[16] = (NasButton)akt.findViewById(R.id.bpravazav);
        tlacitka[17] = (NasButton)akt.findViewById(R.id.bcancel);
        tlacitka[18] = (NasButton)akt.findViewById(R.id.bmplus);
        tlacitka[19] = (NasButton)akt.findViewById(R.id.bmminus);
        tlacitka[20] = (NasButton)akt.findViewById(R.id.bmc);
        tlacitka[21] = (NasButton)akt.findViewById(R.id.bmr);
        tlacitka[22] = (NasButton)akt.findViewById(R.id.bodmocnina);
    }

    public void nastavPocetStisku(int pocet, String vyznambut) {
        int i=0;
        while(!(tlacitka[i++].vyznam.equals(vyznambut)));
        pocetStisku[--i]=pocet;
    }

    public void nastavVysledek(double jaky) {
        vysledek = jaky;
    }

    public void nastavPocetStiskuVsech(int[] pocet) {
        for (int i=0;i<tlacitka.length;i++) {
            pocetStisku[i]=pocet[i];
        }

    }

    public void vsechnyNaHodnotu(int jakou) { //i=0 ->rozbite, i=-1->nerozbitne
        for (int i=0;i<tlacitka.length;i++) {
            pocetStisku[i]=jakou;
        }
    }

    public void zaciname() {
        for (int i=0;i<tlacitka.length;i++) {
            tlacitka[i].nastavPocetStisku(pocetStisku[i]);
        }

    }

    public boolean kontrolaVysledku(double scim) {


        return (scim==vysledek);
    }




}
