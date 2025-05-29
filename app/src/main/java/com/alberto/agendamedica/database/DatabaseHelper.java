package com.alberto.agendamedica.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alberto.agendamedica.models.Cita;
import com.alberto.agendamedica.database.CitaContract.CitaEntry;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    // Información de la base de datos
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AgendaMedica.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CitaContract.SQL_CREATE_ENTRIES);
        Log.d(TAG, "Base de datos creada");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CitaContract.SQL_DELETE_ENTRIES);
        onCreate(db);
        Log.d(TAG, "Base de datos actualizada");
    }

    // Insertar una nueva cita
    public long insertarCita(Cita cita) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CitaEntry.COLUMN_NAME_PACIENTE, cita.getPaciente());
        values.put(CitaEntry.COLUMN_NAME_DOCTOR, cita.getDoctor());
        values.put(CitaEntry.COLUMN_NAME_FECHA, cita.getFecha());
        values.put(CitaEntry.COLUMN_NAME_HORA, cita.getHora());
        values.put(CitaEntry.COLUMN_NAME_MOTIVO, cita.getMotivo());

        long newRowId = db.insert(CitaEntry.TABLE_NAME, null, values);
        db.close();

        Log.d(TAG, "Cita insertada con ID: " + newRowId);
        return newRowId;
    }

    // Obtener todas las citas
    public List<Cita> obtenerTodasLasCitas() {
        List<Cita> citas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                CitaEntry._ID,
                CitaEntry.COLUMN_NAME_PACIENTE,
                CitaEntry.COLUMN_NAME_DOCTOR,
                CitaEntry.COLUMN_NAME_FECHA,
                CitaEntry.COLUMN_NAME_HORA,
                CitaEntry.COLUMN_NAME_MOTIVO
        };

        String sortOrder = CitaEntry.COLUMN_NAME_FECHA + " ASC, " + CitaEntry.COLUMN_NAME_HORA + " ASC";

        Cursor cursor = db.query(
                CitaEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(CitaEntry._ID));
            String paciente = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_PACIENTE));
            String doctor = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_DOCTOR));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_FECHA));
            String hora = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_HORA));
            String motivo = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_MOTIVO));

            Cita cita = new Cita(id, paciente, doctor, fecha, hora, motivo);
            citas.add(cita);
        }

        cursor.close();
        db.close();

        Log.d(TAG, "Obtenidas " + citas.size() + " citas");
        return citas;
    }

    // Obtener una cita por ID
    public Cita obtenerCitaPorId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                CitaEntry._ID,
                CitaEntry.COLUMN_NAME_PACIENTE,
                CitaEntry.COLUMN_NAME_DOCTOR,
                CitaEntry.COLUMN_NAME_FECHA,
                CitaEntry.COLUMN_NAME_HORA,
                CitaEntry.COLUMN_NAME_MOTIVO
        };

        String selection = CitaEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                CitaEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Cita cita = null;
        if (cursor.moveToFirst()) {
            String paciente = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_PACIENTE));
            String doctor = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_DOCTOR));
            String fecha = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_FECHA));
            String hora = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_HORA));
            String motivo = cursor.getString(cursor.getColumnIndexOrThrow(CitaEntry.COLUMN_NAME_MOTIVO));

            cita = new Cita(id, paciente, doctor, fecha, hora, motivo);
        }

        cursor.close();
        db.close();

        Log.d(TAG, "Cita obtenida por ID " + id + ": " + (cita != null ? "encontrada" : "no encontrada"));
        return cita;
    }

    // Eliminar una cita por ID
    public boolean eliminarCita(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = CitaEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int deletedRows = db.delete(CitaEntry.TABLE_NAME, selection, selectionArgs);
        db.close();

        boolean success = deletedRows > 0;
        Log.d(TAG, "Eliminación de cita ID " + id + ": " + (success ? "exitosa" : "fallida"));
        return success;
    }

    // Actualizar una cita
    public boolean actualizarCita(Cita cita) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CitaEntry.COLUMN_NAME_PACIENTE, cita.getPaciente());
        values.put(CitaEntry.COLUMN_NAME_DOCTOR, cita.getDoctor());
        values.put(CitaEntry.COLUMN_NAME_FECHA, cita.getFecha());
        values.put(CitaEntry.COLUMN_NAME_HORA, cita.getHora());
        values.put(CitaEntry.COLUMN_NAME_MOTIVO, cita.getMotivo());

        String selection = CitaEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(cita.getId())};

        int count = db.update(CitaEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();

        boolean success = count > 0;
        Log.d(TAG, "Actualización de cita ID " + cita.getId() + ": " + (success ? "exitosa" : "fallida"));
        return success;
    }
}