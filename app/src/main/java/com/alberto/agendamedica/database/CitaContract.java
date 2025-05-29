package com.alberto.agendamedica.database;

import android.provider.BaseColumns;

public final class CitaContract {

    // Constructor privado para evitar instanciación
    private CitaContract() {}

    /* Inner class que define el contenido de la tabla */
    public static class CitaEntry implements BaseColumns {
        public static final String TABLE_NAME = "citas";
        public static final String COLUMN_NAME_PACIENTE = "paciente";
        public static final String COLUMN_NAME_DOCTOR = "doctor";
        public static final String COLUMN_NAME_FECHA = "fecha";
        public static final String COLUMN_NAME_HORA = "hora";
        public static final String COLUMN_NAME_MOTIVO = "motivo";
    }

    // SQL para crear la tabla
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CitaEntry.TABLE_NAME + " (" +
                    CitaEntry._ID + " INTEGER PRIMARY KEY," +
                    CitaEntry.COLUMN_NAME_PACIENTE + " TEXT," +
                    CitaEntry.COLUMN_NAME_DOCTOR + " TEXT," +
                    CitaEntry.COLUMN_NAME_FECHA + " TEXT," +
                    CitaEntry.COLUMN_NAME_HORA + " TEXT," +
                    CitaEntry.COLUMN_NAME_MOTIVO + " TEXT)";

    // SQL para eliminar la tabla
    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CitaEntry.TABLE_NAME;
}
