package com.example.erick.aplicacionforestal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SimpsonAdaptador extends RecyclerView.Adapter<SimpsonAdaptador.ViewHolder> {
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView dias, lon, vol, uni;

        public ViewHolder(View itemView) {
            super(itemView);

            dias = (TextView)itemView.findViewById(R.id.textViewDS);
            lon = (TextView)itemView.findViewById(R.id.textViewLoS);
            uni = (TextView)itemView.findViewById(R.id.textViewUnS);
            vol = (TextView)itemView.findViewById(R.id.textViewVolS);
        }
    }

    public List<SimpsonModel> simpsonModelList;

    public SimpsonAdaptador(List<SimpsonModel> simpsonModelList) {
        this.simpsonModelList = simpsonModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpson_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.vol.setText("Volumen: "+ simpsonModelList.get(position).getVol());
        holder.dias.setText("Diametros de C/Seccion: " + simpsonModelList.get(position).getDias());
        holder.lon.setText("Longitud: " + simpsonModelList.get(position).getLon());
        holder.uni.setText("Unidad de medida: " + simpsonModelList.get(position).getUnidad());
    }

    @Override
    public int getItemCount() {
        return simpsonModelList.size();
    }
}
