package mi.app.intents_crono;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Thread tarea;
    Handler h = new Handler();
    Button btn_iniciar, btn_reiniciar;
    TextView crono;

    boolean encendido = false;
    int  minutos = 0, seg = 0;
    long mili = System.currentTimeMillis();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        crono = (TextView) findViewById(R.id.crono);
        crono.setText("00:00:00");
        btn_iniciar = (Button)findViewById(R.id.btn_inicio);
        btn_reiniciar = (Button)findViewById(R.id.btn_reiniciar);
        btn_reiniciar.setOnClickListener(this);
        btn_reiniciar.setEnabled(false);
        btn_iniciar.setOnClickListener(this);


    }

    public void botonWeb(View view) {
        String url="https://palomatica.info/";
        Uri uri = Uri.parse(url);
        Intent web = new Intent(Intent.ACTION_VIEW, uri);

        startActivity(web);
    }

    public void botonLlamar(View view) {
        String tlf = "tel: +34 913 98 03 00";
        Intent llamar = new Intent (Intent.ACTION_DIAL);
        llamar.setData(Uri.parse(tlf));

        startActivity(llamar);
    }

    public void botonMaps(View view) {
        Intent maps = new Intent(Intent.ACTION_VIEW);

        maps.setData(Uri.parse("geo: 40.459864, -3.717866"));

        startActivity(maps);
    }

    public void botonMail(View view) {
        Intent mail = new Intent(Intent.ACTION_SEND);

        mail.putExtra(Intent.EXTRA_TEXT, "Correo de prubea");

        //Indicamos el tipo de cifrado
        mail.setType("text/plain");


        //Se establecen el Asunto del email y la direccion de destino
        mail.putExtra(Intent.EXTRA_SUBJECT, "Texto de prueba");
        mail.putExtra(Intent.EXTRA_EMAIL, new String[] {" palomafp@palomafp.org"});

        startActivity(mail);

    }

    public void botonAgenda(View view) {
        Intent agenda = new Intent(Intent.ACTION_PICK);
        agenda.setType(ContactsContract.Contacts.CONTENT_TYPE);

        startActivity(agenda);
    }


    public void botonCamara(View view) {
        Intent cam = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivity(cam);
    }

    //METODOS PARA EL CRONOMETRO


    private void iniciarHilo() {
        tarea = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (encendido) { // se activa la variable encendido si se presiona el boton iniciar
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mili++;
                        if (mili >= 999) {
                            seg++;
                            mili = 0;
                        }
                        if (seg >= 59) {
                            minutos++;
                            seg = 0;
                        }
                        h.post(new Runnable() {
                            @Override
                            public void run() {
                                String m = "", s = "", mi = "";
                                if (mili < 10) { //Modificar la variacion de los 0
                                    m = "00" + mili;
                                } else if (mili <= 100) {
                                    m = "0" + mili;
                                } else {
                                    m = "" + mili;
                                }
                                if (seg <= 10) {
                                    s = "0" + seg;
                                } else {
                                    s = "" + seg;
                                }
                                if (minutos <= 10) {
                                    mi = "0" + minutos;
                                } else {
                                    mi = "" + minutos;
                                }
                                crono.setText(mi + ":" + s + ":" + m);
                            }
                        });
                    }
                }
            }
        });
        tarea.start();
    }

    public void onClick (View view){
        switch (view.getId()){
            case R.id.btn_inicio:
                if(encendido == false){
                    btn_iniciar.setText("Parar");
                    btn_reiniciar.setEnabled(false);
                    encendido = true;
                    iniciarHilo();
                }
                else{
                    btn_iniciar.setText("Iniciar");
                    btn_reiniciar.setEnabled(true);
                    encendido = false;
                    iniciarHilo();
                }

                break;
            case R.id.btn_reiniciar:
                encendido = false;
                mili = 0;
                seg = 0;
                minutos = 0;
                crono.setText("00:00:00");
                break;
        }
    }



}