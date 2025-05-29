package com.alberto.agendamedica;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alberto.agendamedica.adapters.CitaAdapter;
import com.alberto.agendamedica.database.DatabaseHelper;
import com.alberto.agendamedica.models.Cita;

import java.util.List;

public class AnularCitaActivity extends AppCompatActivity {
    private static final String TAG = "AnularCitaActivity";

    private ListView listViewCitasAnular;
    private EditText editTextIdCita;
    private Button btnAnular;
    private Button btnVolver;

    private DatabaseHelper databaseHelper;
    private CitaAdapter citaAdapter;
    private List<Cita> listaCitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anular_cita);

        Log.d(TAG, "AnularCitaActivity creada");

        initializeViews();
        initializeDatabase();
        setupClickListeners();
        loadCitas();
    }

    private void initializeViews() {
        listViewCitasAnular = findViewById(R.id.listViewCitasAnular);
        editTextIdCita = findViewById(R.id.editTextIdCita);
        btnAnular = findViewById(R.id.btnAnular);
        btnVolver = findViewById(R.id.btnVolver);

        Log.d(TAG, "Vistas inicializadas");
    }

    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
        Log.d(TAG, "Base de datos inicializada");
    }

    private void setupClickListeners() {
        btnAnular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anularCita();
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.d(TAG, "Volviendo a MainActivity");
            }
        });
    }

    private void loadCitas() {
        try {
            listaCitas = databaseHelper.obtenerTodasLasCitas();

            if (listaCitas != null && !listaCitas.isEmpty()) {
                citaAdapter = new CitaAdapter(this, listaCitas);
                listViewCitasAnular.setAdapter(citaAdapter);
                Log.d(TAG, "Mostrando " + listaCitas.size() + " citas para anular");
            } else {
                Log.d(TAG, "No hay citas para mostrar");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar citas: " + e.getMessage(), e);
            Toast.makeText(this, "Error al cargar las citas", Toast.LENGTH_SHORT).show();
        }
    }

    private void anularCita() {
        Log.d(TAG, "Intentando anular cita");

        String idTexto = editTextIdCita.getText().toString().trim();

        // Validar que el campo no esté vacío
        if (idTexto.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validación fallida: ID vacío");
            return;
        }

        try {
            int idCita = Integer.parseInt(idTexto);

            // Verificar que la cita existe
            Cita citaExistente = databaseHelper.obtenerCitaPorId(idCita);

            if (citaExistente == null) {
                Toast.makeText(this, getString(R.string.error_cita_no_encontrada), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Cita no encontrada con ID: " + idCita);
                return;
            }

            // Eliminar la cita
            boolean resultado = databaseHelper.eliminarCita(idCita);

            if (resultado) {
                // Éxito
                Toast.makeText(this, getString(R.string.cita_anulada), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Cita anulada exitosamente con ID: " + idCita);

                // Limpiar el campo de texto
                editTextIdCita.setText("");

                // Recargar la lista
                loadCitas();

            } else {
                Toast.makeText(this, "Error al anular la cita", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al eliminar la cita de la base de datos");
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, getString(R.string.error_id_invalido), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validación fallida: ID no es un número válido");
        } catch (Exception e) {
            Toast.makeText(this, "Error inesperado: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error inesperado al anular cita: " + e.getMessage(), e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        Log.d(TAG, "AnularCitaActivity destruida");
    }
}