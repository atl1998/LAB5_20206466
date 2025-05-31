package com.example.lab5_20206466;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HabitosAdapter extends RecyclerView.Adapter<HabitosAdapter.HabitoViewHolder> {

    private ArrayList<Habito> habitos;

    public HabitosAdapter(ArrayList<Habito> habitos) {
        this.habitos = habitos;
    }

    @Override
    public HabitoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habito, parent, false);
        return new HabitoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(HabitoViewHolder holder, int position) {
        Habito h = habitos.get(position);
        holder.nombre.setText(h.getNombre());
        holder.categoria.setText("Categoría: " + h.getCategoria());
        holder.frecuencia.setText("Cada " + h.getFrecuenciaHoras() + " horas");
        holder.inicio.setText("Inicio: " + h.getFechaInicio() + " " + h.getHoraInicio());
    }

    @Override
    public int getItemCount() {
        return habitos.size();
    }

    static class HabitoViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, categoria, frecuencia, inicio;

        public HabitoViewHolder(View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombre);
            categoria = itemView.findViewById(R.id.tvCategoria);
            frecuencia = itemView.findViewById(R.id.tvFrecuencia);
            inicio = itemView.findViewById(R.id.tvInicio);
        }
    }
}