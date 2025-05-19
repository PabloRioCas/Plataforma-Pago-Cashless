package com.example.pulsepay12.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pulsepay12.R
import com.example.pulsepay12.model.Transaction
import com.example.pulsepay12.model.TransactionJSON


class MovementsAdapter(var transacionlist: ArrayList<TransactionJSON>, var context: Context): RecyclerView.Adapter<MovementsAdapter.MyHolder>(){

    private lateinit var listener: OnTransactionListener

    init {
        listener= context as OnTransactionListener
    }

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tipoTransaccion : TextView =itemView.findViewById(R.id.tvRecyclerTransaction)
        val fecha : TextView =itemView.findViewById(R.id.tvrecyclerFecha)
        val tituloImporte : TextView =itemView.findViewById(R.id.tvRecyclerImporte)
        val importeEuros : TextView =itemView.findViewById(R.id.tvrecyclerImporteEuros)
        val tituloSaldo : TextView =itemView.findViewById(R.id.tvRecyclerSaldo)
        val saldoEuros : TextView =itemView.findViewById(R.id.tvrecyclerSaldoEuros)

    }

    companion object {
        private const val TYPE_POSITIVE = 1
        private const val TYPE_NEGATIVE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val tipo = transacionlist[position].type
        return if (tipo.equals("RECHARGE")) TYPE_POSITIVE else TYPE_NEGATIVE

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val layout = when (viewType) {
            TYPE_NEGATIVE -> R.layout.item_movements_negativo
            TYPE_POSITIVE -> R.layout.item_movements
            else -> R.layout.item_movements
        }
        val vista: View =LayoutInflater.from(context).inflate(layout,parent, false)
        return MyHolder(vista)
    }

    override fun getItemCount(): Int {
        return transacionlist.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
         val transaction: TransactionJSON = transacionlist[position]
        holder.tipoTransaccion.text = transaction.type
        //holder.fecha.text = transaction.fecha.toString()
        holder.importeEuros.text = transaction.qty
        holder.tipoTransaccion.setOnClickListener{
            listener.onTransactionSelected(transaction)
        }
    }

    fun addTransaction(transaction: TransactionJSON){
        this.transacionlist.add(transaction)
        notifyItemInserted(transacionlist.size-1)
    }

    interface OnTransactionListener{
        fun onTransactionSelected(transaction: TransactionJSON){

        }
    }

}