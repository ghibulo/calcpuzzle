package cz.ghibulo.kalkulacka;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HlavniOkno extends ActionBarActivity {

    boolean premazDisplej, stiskRovnitka;
    ExpressionParser vyraz;

    Priklad p1,p2,normkalk,aktualni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlavni_okno);
        premazDisplej=true;
        stiskRovnitka=false;
        vyraz=new ExpressionParser();
        p1 = new Priklad(this);
        p1.vsechnyNaHodnotu (0); //vsechny rozbite
        p1.nastavPocetStisku(2,"1");
        p1.nastavPocetStisku(1,"5");
        p1.nastavPocetStisku(1,"-");
        p1.nastavPocetStisku(2,"3");
        p1.nastavPocetStisku(1,"*");
        p1.nastavPocetStisku(1,"=");
        p1.nastavPocetStisku(1,"(");
        p1.nastavPocetStisku(1,")");
        p1.nastavVysledek(12*13);

        p2 = new Priklad(this);
        p2.vsechnyNaHodnotu(0); //vsechny rozbite
        p2.nastavPocetStisku(2,"9");
        p2.nastavPocetStisku(2,"*");
        p2.nastavPocetStisku(1,"2");
        p2.nastavPocetStisku(1,"+");
        p2.nastavPocetStisku(1,"8");
        p2.nastavPocetStisku(2,"=");
        p2.nastavVysledek(18*17);


        normkalk = new Priklad(this);
        normkalk.vsechnyNaHodnotu(-1); //vse neomezene
        aktualni=normkalk;



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hlavni_okno, menu);
        return true;
    }

    public void onClickNum (View view) {
        NasButton tl = (NasButton)view;
        String zmacknuto = tl.vyznam;
        tl.zaregistrujStisk();
        if (stiskRovnitka) {
            vyraz.smazVstup();
        }
        vyraz.addTokenToExpr(zmacknuto);
        toDisplej(zmacknuto);
        premazDisplej=stiskRovnitka=false;
    }


    public void onClickNONum (View view) {
        NasButton tl = (NasButton)view;
        String zmacknuto = tl.vyznam;
        tl.zaregistrujStisk();


        premazDisplej=true;

        if (zmacknuto.equals("=")) {

            stiskRovnitka=true;
            String vysledek=" ";
            double vysledekDouble = 0;
            try {

                vysledekDouble=vyraz.dejVysledek();
                vysledek = String.format("%.6f", vysledekDouble);


            } catch (Exception e) {
                toDisplej("Error");return;
            }

            toDisplej(vysledek);
            if (aktualni.kontrolaVysledku(vysledekDouble)) {
                Toast.makeText(this.getApplicationContext(),getString(R.string.congrats), Toast.LENGTH_LONG).show();
                return;
            }
            vyraz.uzavorkuj();


        } else {
            vyraz.addTokenToExpr(zmacknuto);
            stiskRovnitka=false;

        }


    }






    private void toDisplej(String co) {
        TextView disp = (TextView)findViewById(R.id.displej);
        if (premazDisplej) {
            disp.setText(co);
        } else {
            disp.setText(disp.getText().toString() + co);
        }
    }

    private double getDisplej() {
        TextView disp = (TextView)findViewById(R.id.displej);
        return Double.parseDouble(disp.getText().toString());
    }

    private void zacniPriklad(Priklad jaky) {
        premazDisplej=true;
        stiskRovnitka=false;
        vyraz.smazVstup();
        toDisplej("0");
        aktualni=jaky;
        jaky.zaciname();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.akce_priklad1) {



            zacniPriklad(p1);

            return true;
        }
        if (id == R.id.akce_priklad2) {

            zacniPriklad(p2);
            return true;
        }

        if (id == R.id.akce_normkalkulacka) {

            zacniPriklad(normkalk);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
