package com.alberto.agendamedica;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alberto.agendamedica.database.DatabaseHelper;
import com.alberto.agendamedica.models.Cita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class AltaCitaActivity extends AppCompatActivity {
    private static final String TAG = "AltaCitaActivity";

    private EditText editTextPaciente;
    private EditText editTextDoctor;
    private EditText editTextFecha;
    private EditText editTextHora;
    private EditText editTextMotivo;
    private Button btnGuardar;
    private Button btnCancelar;

    private DatabaseHelper databaseHelper;

    // Patrones para validación
    private static final Pattern FECHA_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
    private static final Pattern HORA_PATTERN = Pattern.compile("^\\d{2}:\\d{2}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_cita);

        Log.d(TAG, "AltaCitaActivity creada");

        initializeViews();
        initializeDatabase();
        setupClickListeners();
    }

    private void initializeViews() {
        editTextPaciente = findViewById(R.id.editTextPaciente);
        editTextDoctor = findViewById(R.id.editTextDoctor);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextHora = findViewById(R.id.editTextHora);
        editTextMotivo = findViewById(R.id.editTextMotivo);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        Log.d(TAG, "Vistas inicializadas");
    }

    private void initializeDatabase() {
        databaseHelper = new DatabaseHelper(this);
        Log.d(TAG, "Base de datos inicializada");
    }

    private void setupClickListeners() {
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCita();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.d(TAG, "Cancelado - volviendo a MainActivity");
            }
        });
    }

    private void guardarCita() {
        Log.d(TAG, "Intentando guardar cita");

        // Obtener valores de los campos
        String paciente = editTextPaciente.getText().toString().trim();
        String doctor = editTextDoctor.getText().toString().trim();
        String fecha = editTextFecha.getText().toString().trim();
        String hora = editTextHora.getText().toString().trim();
        String motivo = editTextMotivo.getText().toString().trim();

        // Validar campos
        if (!validarCampos(paciente, doctor, fecha, hora, motivo)) {
            return;
        }

        try {
            // Crear nueva cita
            Cita nuevaCita = new Cita(paciente, doctor, fecha, hora, motivo);

            // Insertar en la base de datos
            long resultado = databaseHelper.insertarCita(nuevaCita);

            if (resultado != -1) {
                // Éxito
                Toast.makeText(this, getString(R.string.cita_guardada), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Cita guardada exitosamente con ID: " + resultado);
                finish(); // Volver a MainActivity
            } else {
                // Error al guardar
                Toast.makeText(this, "Error al guardar la cita", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error al guardar la cita en la base de datos");
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error inesperado: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error inesperado al guardar cita: " + e.getMessage(), e);
        }
    }

    private boolean validarCampos(String paciente, String doctor, String fecha, String hora, String motivo) {
        // Validar que no estén vacíos
        if (paciente.isEmpty() || doctor.isEmpty() || fecha.isEmpty() || hora.isEmpty() || motivo.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_campos_vacios), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validación fallida: campos vacíos");
            return false;
        }

        // Validar formato de fecha
        if (!FECHA_PATTERN.matcher(fecha).matches()) {
            Toast.makeText(this, getString(R.string.error_fecha_invalida), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validación fallida: formato de fecha inválido");
            return false;
        }

        // Validar que la fecha sea válida
        if (!esFechaValida(fecha)) {
            Toast.makeText(this, getString(R.string.error_fecha_invalida), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validación fallida: fecha inválida");
            return false;
        }

        // Validar formato de hora
        if (!HORA_PATTERN.matcher(hora).matches()) {
            Toast.makeText(this, getString(R.string.error_hora_invalida), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validación fallida: formato de hora inválido");
            return false;
        }

        // Validar que la hora sea válida
        if (!esHoraValida(hora)) {
            Toast.makeText(this, getString(R.string.error_hora_invalida), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Validación fallida: hora inválida");
            return false;
        }

        Log.d(TAG, "Validación exitosa");
        return true;
    }

    private boolean esFechaValida(String fecha) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            sdf.setLenient(false); // No permitir fechas inválidas como 32/13/2023
            Date date = sdf.parse(fecha);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean esHoraValida(String hora) {
        try {
            String[] partes = hora.split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);

            return horas >= 0 && horas <= 23 && minutos >= 0 && minutos <= 59;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        Log.d(TAG, "AltaCitaActivity destruida");
    }
}