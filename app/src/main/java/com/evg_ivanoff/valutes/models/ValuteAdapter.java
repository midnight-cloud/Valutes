package com.evg_ivanoff.valutes.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.evg_ivanoff.valutes.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ValuteAdapter extends RecyclerView.Adapter<ValuteAdapter.ViewHolder>{
    private final List<Valute> valutes;
    private final LayoutInflater inflater;
    private final OnValuteClickListener onClickListener;


    public ValuteAdapter(Context context, List<Valute> valutes, OnValuteClickListener onClickListener) {
        this.valutes = valutes;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    public interface OnValuteClickListener {
        void onValuteClick(Valute valute, int position);
    }

    @NonNull
    @Override
    public ValuteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_valutes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValuteAdapter.ViewHolder holder, int position) {
        Valute valute = valutes.get(position);
        holder.textValuteShort.setText(valute.getCharCode());
        holder.textValuteFull.setText(valute.getName());
        holder.textValuteValue.setText(Double.toString(valute.getValue()));
        holder.textValuteConvert.setText(new DecimalFormat("#.####").format(valute.getConvertValue())+" RUB");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onValuteClick(valute, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textValuteShort, textValuteFull, textValuteValue, textValuteConvert;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textValuteFull = itemView.findViewById(R.id.textValuteFull);
            textValuteShort = itemView.findViewById(R.id.textValuteShort);
            textValuteValue = itemView.findViewById(R.id.textValuteValue);
            textValuteConvert = itemView.findViewById(R.id.textConvertValue);
        }
    }

}
