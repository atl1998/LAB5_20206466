package com.example.lab5_20206466;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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


        ImageButton btnEliminar = holder.itemView.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Eliminar hábito")
                    .setMessage("¿Seguro que deseas eliminar \"" + h.getNombre() + "\"?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        // Eliminar de almacenamiento
                        StorageUtil.eliminarHabito(holder.itemView.getContext(), h.getNombre());

                        // Eliminar del adapter y actualizar
                        habitos.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, habitos.size());

                        Toast.makeText(holder.itemView.getContext(), "Hábito eliminado", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
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