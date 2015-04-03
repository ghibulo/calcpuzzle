package cz.ghibulo.kalkulacka;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;
import android.content.Context;

import java.util.LinkedList;

public class HlavniOkno extends Activity {

    boolean premazDisplej, stiskRovnitka, chybovyStav;
    ExpressionParser vyraz;
    double pametM;
    String[] obsahMalehoDispleje;
    TextView compMalyDisplej;

    //MediaPlayer mpklik; ... najit neotravnej zvuk je problem, navic je hlasitost svazana s mediama
    Vibrator vibe;  //vibrace


    //zasobnik vyrazu pro zobrazeni vysledku uzavorkovaneho mezivyrazu
    LinkedList<ExpressionParser> stackExpr = new LinkedList<ExpressionParser>();
    ExpressionParser exprForStack = null;

    Priklad p1,p2,normkalk,aktualni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_hlavni_okno);
        premazDisplej=true;
        stiskRovnitka=chybovyStav=false;
        vyraz=new ExpressionParser();

        //mpklik = MediaPlayer.create(this, R.raw.beep);
        vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        pametM=0;
        obsahMalehoDispleje = new String[2];
        obsahMalehoDispleje[0]=obsahMalehoDispleje[1]=" ";
        compMalyDisplej = (TextView)findViewById(R.id.malydisplej);
        obnovMalyDisplej();



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
            vysledek = String.format("%.6f", vysledekDouble).replace(",",".");


        } catch (Exception e) {
            toDisplej(getString(R.string.error));chybovyStav=true;
            return Double.MAX_VALUE;
        }

        toDisplej(vysledek);
        return vysledekDouble;

    }


    private void zPametinaDisplej() {
        String strCislo = String.format("%.6f", pametM).replace(",",".");
        toDisplej(strCislo);
        addTokenToExpr(strCislo);
    }


    private void obnovMalyDisplej() {
        if (pametM!=0) {
            obsahMalehoDispleje[0] = "M";
        } else {
            obsahMalehoDispleje[0] = " ";
        }

        if (obsahMalehoDispleje[1].equals("*")) obsahMalehoDispleje[1]="×";
        if (obsahMalehoDispleje[1].equals("/")) obsahMalehoDispleje[1]="÷";
        if (obsahMalehoDispleje[1].equals("od")) obsahMalehoDispleje[1]=" ";
        if (obsahMalehoDispleje[1].equals("cs")) obsahMalehoDispleje[1]=" ";
        String obsah = String.format("%s  %s ", obsahMalehoDispleje[0], obsahMalehoDispleje[1]);
        compMalyDisplej.setText(obsah);
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
    private void kontrolaStavu() {
        if (chybovyStav) {
            vymazVse();
            chybovyStav=false;
        }
    }

    public void onClickNum (View view) {
        kontrolaStavu();
        //mpklik.start();
        vibe.vibrate(80);
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
        kontrolaStavu();
        //mpklik.start();
        vibe.vibrate(80);
        NasButton tl = (NasButton)view;
        String zmacknuto = tl.vyznam;

        tl.zaregistrujStisk();

        if (zmacknuto.equals("c")) {
            vymazVse();
            return;
        }

        if (zmacknuto.equals("m+")) {
            pametM+=getDisplej();
            obnovMalyDisplej();
            return;
        }

        if (zmacknuto.equals("m-")) {
            pametM-=getDisplej();
            obnovMalyDisplej();
            return;
        }

        if (zmacknuto.equals("mc")) {
            pametM = 0;
            obnovMalyDisplej();
            return;
        }


        if (zmacknuto.equals("mr")) {
            zPametinaDisplej();
            return;
        }

        premazDisplej=true;

        if (zmacknuto.equals("=")) {


            obsahMalehoDispleje[1]=" ";
            obnovMalyDisplej();
            stiskRovnitka=true;

            double v = exprToDisplej(vyraz);

            if ((aktualni.kontrolaVysledku(v))&&(!chybovyStav)) {
                Toast.makeText(this.getApplicationContext(),getString(R.string.congrats), Toast.LENGTH_LONG).show();
                return;
            }
            vyraz.uzavorkuj();


        } else {
            if (zmacknuto.equals("(")) {
                toDisplej("0");premazDisplej=true;
                if (exprForStack!=null) stackExpr.push(exprForStack);
                exprForStack = new ExpressionParser();
                obsahMalehoDispleje[1]="("+(stackExpr.size()+1)+")";
                obnovMalyDisplej();
            } else

            if (zmacknuto.equals(")")) {

                exprToDisplej(exprForStack);
                if (stackExpr.isEmpty()) { //konci posledni zavorka
                    exprForStack = null;
                    obsahMalehoDispleje[1]=" ";
                    obnovMalyDisplej();
                } else {                    //vysledek vnitrni zavorky do vnejsi
                    try {
                        ExpressionParser ex = stackExpr.pop();
                        ex.addDoubleToExpr(exprForStack.dejVysledek());
                        exprForStack = ex;
                        obsahMalehoDispleje[1]="("+(stackExpr.size()+1)  +")";
                        obnovMalyDisplej();

                    } catch (Exception e ) {
                        toDisplej(getString(R.string.error));
                        chybovyStav=true;
                        return;
                    }

                }
            } else

            if (zmacknuto.equals("od")) {     //zatim jedina un-operace
                double zdisp = getDisplej();

                zdisp = Math.sqrt(zdisp);
                if (zdisp!=zdisp) {
                    toDisplej(getString(R.string.error));
                    chybovyStav=true;
                } else
                   toDisplej(String.format("%.6f", zdisp).replace(",", "."));
            } else

            if (zmacknuto.equals("cs")) {
                double zdisp = getDisplej();
                toDisplej(String.format("%.6f", -zdisp).replace(",", "."));
            } else {
                obsahMalehoDispleje[1] = zmacknuto;
                obnovMalyDisplej();
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
        return Double.parseDouble(disp.getText().toString().replace(",", "."));
    }


    private void vymazVse() {
        premazDisplej=true;
        stiskRovnitka=false;
        vyraz.smazVstup();
        exprForStack=null;
        stackExpr.clear();
        toDisplej("0");
        //pametM=0;obsahMalehoDispleje[0]=" "; ... pamet nechame
        obsahMalehoDispleje[1]=" ";
        obnovMalyDisplej();
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
