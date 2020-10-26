package mi.app.intents_crono;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.BreakIterator;

public class MainActivity extends AppCompatActivity {
    Thread tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

    public void run() {
        TextView crono = (TextView) findViewById(R.id.crono);
        crono.setText("00:00:00");
        int horas, minutos, segundos;
        horas = minutos = segundos = 0;

        long msInicial = System.currentTimeMillis();
        long msActuales;

        while(true){
            msActuales = System.currentTimeMillis();

            if (msActuales - msInicial > 50)
            {
                ++segundos;

                if(segundos == 60)
                {
                    segundos = 0;
                    ++minutos;
                    if(minutos == 60)
                    {
                        minutos = 0;
                        ++horas;
                    }
                }
                msInicial = msActuales;
                crono.setText(String.format("%02d:%02d:%02d", horas, minutos, segundos));
            }
        }

    }

    public void botonIniciar(View view) {
        Button btn_iniciar = (Button) findViewById(R.id.btn_inicio);
        Button btn_reiniciar = (Button) findViewById(R.id.btn_reiniciar);
        String btn = btn_iniciar.getText().toString();

        if(btn.equals("Iniciar"))
        {
            tarea = new Thread(this::run);
            tarea.start(); //Ejecuta el metodo RUN. Esto es el hilo.
            btn_iniciar.setText("Parar");
        }
        else
        {
            btn_reiniciar.setEnabled(true);
            btn_iniciar.setEnabled(false);
            btn_iniciar.setText("Iniciar");
        }
    }

    public void botonReiniciar(View view) {
        Button btn_iniciar = (Button) findViewById(R.id.btn_inicio);
        Button btn_reiniciar = (Button) findViewById(R.id.btn_reiniciar);
        TextView crono = (TextView)findViewById(R.id.crono);
        crono.setText("00:00:00");

        //Al reiniciar el contador, reiniciamos el hilo
        tarea = new Thread();
        tarea.start();
        btn_iniciar.setEnabled(true);
        btn_iniciar.setText("Iniciar");
        btn_reiniciar.setEnabled(false);

    }
}