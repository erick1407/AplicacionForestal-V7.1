package com.example.erick.aplicacionforestal;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HuberAdaptador extends RecyclerView.Adapter<HuberAdaptador.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView di1, di2, lon, vol, uni;

        public ViewHolder(View itemView) {
            super(itemView);

            di1 = (TextView)itemView.findViewById(R.id.textViewD1Hu);
            di2 = (TextView)itemView.findViewById(R.id.textViewD2Hu);
            lon = (TextView)itemView.findViewById(R.id.textViewLoHu);
            vol = (TextView)itemView.findViewById(R.id.textViewVolHu);
            uni = (TextView)itemView.findViewById(R.id.textViewUnHu);
        }
    }

    public List<HuberModel> huberModelList;

    public HuberAdaptador(List<HuberModel> huberModelList) {
        this.huberModelList = huberModelList;
    }

    @Override
    public HuberAdaptador.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.huber_item, parent, false);
        HuberAdaptador.ViewHolder viewHolder = new HuberAdaptador.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HuberAdaptador.ViewHolder holder, int position) {
        holder.vol.setText("Volumen: " +huberModelList.get(position).getVol());
        holder.di1.setText("Diámetro 1: " + huberModelList.get(position).getDia1());
        holder.di2.setText("Diámetro 2: " + huberModelList.get(position).getDia2());
        holder.lon.setText("Longitud: " + huberModelList.get(position).getLon());
        holder.uni.setText("Unidad de medida: "+ huberModelList.get(position).getUni());
    }

    @Override
    public int getItemCount() {
        return huberModelList.size();
    }
}
