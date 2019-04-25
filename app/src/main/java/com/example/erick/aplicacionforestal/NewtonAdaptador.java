package com.example.erick.aplicacionforestal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class NewtonAdaptador extends RecyclerView.Adapter<NewtonAdaptador.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView di1, di2, lon, vol, uni;

        public ViewHolder(View itemView) {
            super(itemView);

            di1 = (TextView)itemView.findViewById(R.id.textViewD1);
            di2 = (TextView)itemView.findViewById(R.id.textViewD2);
            lon = (TextView)itemView.findViewById(R.id.textViewLo);
            vol = (TextView)itemView.findViewById(R.id.textViewVol);
            uni = (TextView)itemView.findViewById(R.id.textViewUn);
        }
    }

    public List<NewtonModel> newtonModelList;

    public NewtonAdaptador(List<NewtonModel> newtonModelList) {
        this.newtonModelList = newtonModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newton_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.vol.setText("Volumen: " +newtonModelList.get(position).getVol());
        holder.di1.setText("Diámetro 1: " + newtonModelList.get(position).getDia1());
        holder.di2.setText("Diámetro 2: " + newtonModelList.get(position).getDia2());
        holder.lon.setText("Longitud: " + newtonModelList.get(position).getLon());
        holder.uni.setText("Unidad de medida: "+ newtonModelList.get(position).getUni());
    }

    @Override
    public int getItemCount() {
        return newtonModelList.size();
    }
}
