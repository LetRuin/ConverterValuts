package com.example.converter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class ValuteAdapter extends RecyclerView.Adapter<ValuteAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private double vunitRate;
    private double valueValut;
    private List<Valute> valutes;
    private final OnValuteClickListener onClickListener;

    ValuteAdapter(Context context, List<Valute> valutes, OnValuteClickListener onClickListener) {
        vunitRate = 10;
        valueValut = 0;
        this.valutes = valutes;
        this.inflater = LayoutInflater.from(context);
        this.onClickListener = onClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void load(List<Valute> valutes){
        this.valutes = valutes;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVunitRate(double vunitRate){
        this.vunitRate = vunitRate;
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setValueValut(double valueValut){
        this.valueValut = valueValut;
        notifyDataSetChanged();
    }

    interface OnValuteClickListener{
        void onValuteClick(Valute valute, int position);
    }

    @Override
    public ValuteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ValuteAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Valute valute = valutes.get(position);
        double value = valueValut*(vunitRate/valute.vunitRate);
        holder.value.setText(new DecimalFormat("#0.0").format(value));
        holder.charCode.setText(valute.charCode);

        View.OnClickListener ocl = v -> {
            // вызываем метод слушателя, передавая ему данные
            onClickListener.onValuteClick(valute, position);
        };

        holder.itemView.setOnClickListener(ocl);
        holder.value.setOnClickListener(ocl);
        holder.charCode.setOnClickListener(ocl);
    }

    @Override
    public int getItemCount() {
        return valutes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView value, charCode;
        ViewHolder(View view){
            super(view);
            value = view.findViewById(R.id.value);
            charCode = view.findViewById(R.id.charCode);
        }
    }
}