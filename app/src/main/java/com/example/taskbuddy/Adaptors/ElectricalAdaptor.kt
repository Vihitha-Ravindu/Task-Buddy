package com.example.taskbuddy.Adaptors

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskbuddy.Modals.ServiceProviderModal
import com.example.taskbuddy.R
import com.example.taskbuddy.SelectServices.ElectricalServiceSelect
import com.example.taskbuddy.SelectServices.PlumberServiceSelect

class ElectricalAdaptor(private val context: Context, private val data: List<ServiceProviderModal>) :
    RecyclerView.Adapter<ElectricalAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)

        fun bind(item: ServiceProviderModal) {
            textView1.text = item.name
            textView2.text = item.rating.toString()

            // Handle item click to navigate to another activity
            itemView.setOnClickListener {
                val intent = Intent(context, ElectricalServiceSelect::class.java)
                intent.putExtra("name", item.name)
                intent.putExtra("id",item.sid)
                intent.putExtra("number",item.number)
                intent.putExtra("location",item.location)
                intent.putExtra("rating",item.rating)
                context.startActivity(intent)
            }
        }
    }
}