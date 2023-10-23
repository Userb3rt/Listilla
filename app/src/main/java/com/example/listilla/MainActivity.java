package com.example.listilla;

import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // Model: Record (intents=puntuació, nom)
    class Record {
        public int intents;
        public String nom;

        public Record(int _intents, String _nom ) {
            intents = _intents;
            nom = _nom;
        }
    }
    // Model = Taula de records: utilitzem ArrayList
    ArrayList<Record> records;

    // ArrayAdapter serà l'intermediari amb la ListView
    ArrayAdapter<Record> adapter;

    // Array de nombres y cognoms

    String[] nombres = {
            "Sofía", "Mateo", "Valentina", "Alejandro", "Isabella",
            "Juan", "Camila", "Gabriel", "Mia", "Daniel",
            "Victoria", "Lucas", "Emma", "Carlos", "Olivia",
            "Andrés", "María", "Diego", "Antonia", "Javier"
    };

    String[] apellidos = {
            "Gómez", "Rodríguez", "López", "Fernández", "Pérez",
            "González", "Sánchez", "Ramírez", "Torres", "Vásquez",
            "Martínez", "Díaz", "Hernández", "Moreno", "Muñoz",
            "Álvarez", "Romero", "Jiménez", "Ruiz", "Silva"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialitzem model
        records = new ArrayList<Record>();
        // Afegim alguns exemples
        records.add( new Record(33,"Manolo") );
        records.add( new Record(12,"Pepe") );
        records.add( new Record(42,"Laura") );

        // Inicialitzem l'ArrayAdapter amb el layout pertinent
        adapter = new ArrayAdapter<Record>( this, R.layout.list_item, records )
        {
            @Override
            public View getView(int pos, View convertView, ViewGroup container)
            {
                // getView ens construeix el layout i hi "pinta" els valors de l'element en la posició pos
                if( convertView==null ) {
                    // inicialitzem l'element la View amb el seu layout
                    convertView = getLayoutInflater().inflate(R.layout.list_item, container, false);
                }
                Class<?> drawableClass = R.drawable.class;
                Field[] fields = drawableClass.getFields();
                Random random = new Random();
                int randomIndex = random.nextInt(fields.length);
                Field randomField = fields[randomIndex];

                // "Pintem" valors (també quan es refresca)
                ((TextView) convertView.findViewById(R.id.nom)).setText(getItem(pos).nom);
                ((TextView) convertView.findViewById(R.id.intents)).setText(Integer.toString(getItem(pos).intents));
                try {
                    ((ImageView) convertView.findViewById(R.id.imagenid)).setImageResource(randomField.getInt(null));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                return convertView;
            }

        };

        // busquem la ListView i li endollem el ArrayAdapter
        ListView lv = (ListView) findViewById(R.id.recordsView);
        lv.setAdapter(adapter);

        // botó per afegir entrades a la ListView
        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                records.add(new Record(random.nextInt(100)+1,nombres[random.nextInt(nombres.length)]));
                // notificar l'adapter dels canvis al model
                adapter.notifyDataSetChanged();
            }
        });
    }
}