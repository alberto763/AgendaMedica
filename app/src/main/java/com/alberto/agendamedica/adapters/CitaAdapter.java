package com.alberto.agendamedica.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alberto.agendamedica.R;
import com.alberto.agendamedica.models.Cita;

import java.util.List;

public class CitaAdapter extends ArrayAdapter<Cita> {
    private Context context;
    private List<Cita> citas;

    public CitaAdapter(Context context, List<Cita> citas) {
        super(context, R.layout.item_cita, citas);
        this.context = context;
        this.citas = citas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_cita, parent, false);
        }

        Cita citaActual = citas.get(position);

        TextView textViewId = listItem.findViewById(R.id.textViewId);
        TextView textViewPaciente = listItem.findViewById(R.id.textViewPaciente);
        TextView textViewDoctor = listItem.findViewById(R.id.textViewDoctor);
        TextView textViewFechaHora = listItem.findViewById(R.id.textViewFechaHora);
        TextView textViewMotivo = listItem.findViewById(R.id.textViewMotivo);

        textViewId.setText("ID: " + citaActual.getId());
        textViewPaciente.setText("Paciente: " + citaActual.getPaciente());
        textViewDoctor.setText("Doctor: " + citaActual.getDoctor());
        textViewFechaHora.setText(citaActual.getFecha() + " - " + citaActual.getHora());
        textViewMotivo.setText("Motivo: " + citaActual.getMotivo());

        return listItem;
    }
}