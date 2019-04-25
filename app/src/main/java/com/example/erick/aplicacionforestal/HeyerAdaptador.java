package com.example.erick.aplicacionforestal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HeyerAdaptador extends RecyclerView.Adapter<HeyerAdaptador.ViewHolder>{
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView diams, longs, vol, unidad;

        public ViewHolder(View itemView) {
            super(itemView);
            diams = (TextView)itemView.findViewById(R.id.textViewDH);
            longs = (TextView)itemView.findViewById(R.id.textViewLoH);
            vol = (TextView)itemView.findViewById(R.id.textViewVolH);
            unidad = (TextView)itemView.findViewById(R.id.textViewUnH);
        }
    }

    public List<HeyerModel> heyerModelList;

    public HeyerAdaptador(List<HeyerModel> heyerModelList) {
        this.heyerModelList = heyerModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.heyer_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.vol.setText("Volumen: "+ heyerModelList.get(position).getVol());
        holder.diams.setText("Diámetro(s): "+heyerModelList.get(position).getDias());
        holder.longs.setText("Longitudes: "+ heyerModelList.get(position).getLongs());
        holder.unidad.setText("Unidad de medida: "+heyerModelList.get(position).getUnidad());
    }

    @Override
    public int getItemCount() {
        return heyerModelList.size();
    }
}
