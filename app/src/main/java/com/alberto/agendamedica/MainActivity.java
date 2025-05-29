package com.alberto.agendamedica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alberto.agendamedica.adapters.CitaAdapter;
import com.alberto.agendamedica.database.DatabaseHelper;
import com.alberto.agendamedica.models.Cita;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ListView listViewCitas;
    private TextView textViewNoCitas;
    private Button btnNuevaCita;
    private Button btnAnularCita;

    private DatabaseHelper databaseHelper;
    private CitaAdapter citaAdapter;
    private List<Cita> listaCitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity creada");

        initializeViews();
        initializeDatabase();
        setupClickListeners();
        loadCitas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar las citas cuando volvemos a esta actividad
        loadCitas();
        Log.d(TAG, "MainActivity resumida - citas recargadas");
    }

    private void initializeViews() {
        listViewCitas = findViewById(R.id.listViewCitas);
        textViewNoCitas = findViewById(R.id.textViewNoCitas);
        btnNuevaCita = findViewById(R.id.btnNuevaCita);
        btnAnularCita = findViewById(R.id.btnAnularCita);

        Log.d(TAG, "Vistas inicializadas");
    }

    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
        Log.d(TAG, "Base de datos inicializada");
    }

    private void setupClickListeners() {
        btnNuevaCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AltaCitaActivity.class);
                startActivity(intent);
                Log.d(TAG, "Navegando a AltaCitaActivity");
            }
        });

        btnAnularCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AnularCitaActivity.class);
                startActivity(intent);
                Log.d(TAG, "Navegando a AnularCitaActivity");
            }
        });
    }

    private void loadCitas() {
        try {
            listaCitas = databaseHelper.obtenerTodasLasCitas();

            if (listaCitas != null && !listaCitas.isEmpty()) {
                // Hay citas - mostrar ListView
                citaAdapter = new CitaAdapter(this, listaCitas);
                listViewCitas.setAdapter(citaAdapter);
                listViewCitas.setVisibility(View.VISIBLE);
                textViewNoCitas.setVisibility(View.GONE);

                Log.d(TAG, "Mostrando " + listaCitas.size() + " citas");
            } else {
                // No hay citas - mostrar mensaje
                listViewCitas.setVisibility(View.GONE);
                textViewNoCitas.setVisibility(View.VISIBLE);

                Log.d(TAG, "No hay citas para mostrar");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar citas: " + e.getMessage(), e);
            // En caso de error, mostrar mensaje de no citas
            listViewCitas.setVisibility(View.GONE);
            textViewNoCitas.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        Log.d(TAG, "MainActivity destruida");
    }
}