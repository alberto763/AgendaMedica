package com.alberto.agendamedica.models;

public class Cita {
    private int id;
    private String paciente;
    private String doctor;
    private String fecha;
    private String hora;
    private String motivo;

    // Constructor vacío
    public Cita() {}

    // Constructor completo
    public Cita(int id, String paciente, String doctor, String fecha, String hora, String motivo) {
        this.id = id;
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    // Constructor sin ID (para nuevas citas)
    public Cita(String paciente, String doctor, String fecha, String hora, String motivo) {
        this.paciente = paciente;
        this.doctor = doctor;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "Cita{" +
                "id=" + id +
                ", paciente='" + paciente + '\'' +
                ", doctor='" + doctor + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}