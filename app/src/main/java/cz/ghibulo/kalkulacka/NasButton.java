package cz.ghibulo.kalkulacka;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.util.AttributeSet;
//import android.view.View;
import android.widget.Button;
//import android.widget.LinearLayout;


public class NasButton extends Button  {
    int  maxStisku;
    String vyznam;


    public NasButton(Context context){
        super(context);
        //this.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        init();
    }

    public NasButton (Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        //this.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        init();
    }

    public NasButton (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        //this.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        init();
        }

    public void init () {
        setWidth(70);
        setTextColor(Color.WHITE);


        maxStisku = -1; //nekonecno
        nastavPocetStisku(maxStisku);
        int myId = this.getId();
        switch(myId)  {
            case R.id.b1: vyznam = "1";break;
            case R.id.b2: vyznam = "2";break;
            case R.id.b3: vyznam = "3";break;
            case R.id.b4: vyznam = "4";break;
            case R.id.b5: vyznam = "5";break;
            case R.id.b6: vyznam = "6";break;
            case R.id.b7: vyznam = "7";break;
            case R.id.b8: vyznam = "8";break;
            case R.id.b9: vyznam = "9";break;
            case R.id.b0: vyznam = "0";break;
            case R.id.brovnitko: vyznam = "=";break;
            case R.id.bplus: vyznam = "+";break;
            case R.id.bminus: vyznam = "-";break;
            case R.id.bkrat: vyznam = "*";break;
            case R.id.bdeleno: vyznam = "/";break;
            case R.id.blevazav: vyznam = "(";break;
            case R.id.bpravazav: vyznam = ")";break;
            case R.id.bcancel: vyznam = "c";break;
            case R.id.bmc: vyznam = "mc";break;
            case R.id.bmminus: vyznam = "m-";break;
            case R.id.bmplus: vyznam = "m+";break;
            case R.id.bmr: vyznam = "mr";break;
            case R.id.bodmocnina: vyznam = "od";break;
            default: vyznam = "chyba";break;
        }
    }



    public void zaregistrujStisk() {
        if (maxStisku>0) {  //povolene a rozbite neregistrujeme
            maxStisku--;
            nastavPocetStisku(maxStisku);
        }
    }

    public void nastavPocetStisku(int kolik) {
        maxStisku = kolik;
        if(kolik==-1) {this.setText("");setEnabled(true);return;}
        if(kolik==0) {this.setText(getResources().getString(R.string.kaput));this.setEnabled(false);return;}
        this.setText(Integer.toString(kolik));
        this.setEnabled(true);

    }

}
