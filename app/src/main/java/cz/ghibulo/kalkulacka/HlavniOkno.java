package cz.ghibulo.kalkulacka;

import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

public class HlavniOkno extends ActionBarActivity {

    boolean premazDisplej, stiskRovnitka;
    ExpressionParser vyraz;
    static private AudioManager klik;



    //zasobnik vyrazu pro zobrazeni vysledku uzavorkovaneho mezivyrazu
    LinkedList<ExpressionParser> stackExpr = new LinkedList<ExpressionParser>();
    ExpressionParser exprForStack = null;

    Priklad p1,p2,normkalk,aktualni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlavni_okno);
        premazDisplej=true;
        stiskRovnitka=false;
        vyraz=new ExpressionParser();
        klik = (AudioManager)getSystemService(AUDIO_SERVICE);


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
    private double exprToDisplej (ExpressionParser co) {

        String vysledek=" ";
        double vysledekDouble = 0;
        try {

            //Log.i("---", co.expr);
            vysledekDouble=co.dejVysledek();
            vysledek = String.format("%.6f", vysledekDouble);


        } catch (Exception e) {
            toDisplej("Error");return Double.MAX_VALUE;
        }

        toDisplej(vysledek);
        return vysledekDouble;

    }

    private void toMalyDisplej(String co) {
        TextView disp = (TextView)findViewById(R.id.malydisplej);
        if (co.equals("*")) disp.setText("x "); else disp.setText(co+" "); //krizek je hezci :-)

    }

    private void addTokenToExpr(String zmacknuto) {
        vyraz.addTokenToExpr(zmacknuto);
        boolean nenizavorka = !(zmacknuto.equals(")") | zmacknuto.equals("("));
        if ((exprForStack!=null)&(nenizavorka)) {
            exprForStack.addTokenToExpr(zmacknuto);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hlavni_okno, menu);
        return true;
    }

    public void onClickNum (View view) {
        klik.playSoundEffect(AudioManager.FX_KEY_CLICK);
        NasButton tl = (NasButton)view;
        String zmacknuto = tl.vyznam;
        tl.zaregistrujStisk();
        if (stiskRovnitka) {
            vyraz.smazVstup();
        }

        addTokenToExpr(zmacknuto); //vyraz.addTokenToExpr(zmacknuto) + vnitrni vyraz stacku
        toDisplej(zmacknuto);
        premazDisplej=stiskRovnitka=false;
    }


    public void onClickNONum (View view) {
        klik.playSoundEffect(AudioManager.FX_KEY_CLICK);
        NasButton tl = (NasButton)view;
        String zmacknuto = tl.vyznam;

        tl.zaregistrujStisk();

        if (zmacknuto.equals("c")) {
            vymazVse();
            return;
        }

        premazDisplej=true;

        if (zmacknuto.equals("=")) {

            toMalyDisplej("");
            stiskRovnitka=true;

            double v = exprToDisplej(vyraz);

            if (aktualni.kontrolaVysledku(v)) {
                Toast.makeText(this.getApplicationContext(),getString(R.string.congrats), Toast.LENGTH_LONG).show();
                return;
            }
            vyraz.uzavorkuj();


        } else {
            if (zmacknuto.equals("(")) {
                toDisplej("0");premazDisplej=true;
                if (exprForStack!=null) stackExpr.push(exprForStack);
                exprForStack = new ExpressionParser();
                toMalyDisplej("("+(stackExpr.size()+1)+")");
            } else

            if (zmacknuto.equals(")")) {

                exprToDisplej(exprForStack);
                if (stackExpr.isEmpty()) { //konci posledni zavorka
                    exprForStack = null;
                    toMalyDisplej("");
                } else {                    //vysledek vnitrni zavorky do vnejsi
                    try {
                        ExpressionParser ex = stackExpr.pop();
                        ex.addDoubleToExpr(exprForStack.dejVysledek());
                        exprForStack = ex;
                        toMalyDisplej("("+(stackExpr.size()+1)  +")");
                    } catch (Exception e ) {
                        toDisplej("Error");return;
                    }

                }
            } else {
                toMalyDisplej(zmacknuto);
            }

            addTokenToExpr(zmacknuto); //vyraz.addTokenToExpr(zmacknuto) + vnitrni vyraz stacku

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


    private void vymazVse() {
        premazDisplej=true;
        stiskRovnitka=false;
        vyraz.smazVstup();
        exprForStack=null;
        stackExpr.clear();
        toDisplej("0");
        toMalyDisplej("");
    }

    private void zacniPriklad(Priklad jaky) {

        vymazVse();
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
