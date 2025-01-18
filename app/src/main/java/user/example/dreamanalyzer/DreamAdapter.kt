package user.example.dreamanalyzer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DreamAdapter(private val dreams: List<Dream>, private val onClick: (Dream) -> Unit) :
    RecyclerView.Adapter<DreamAdapter.DreamViewHolder>() {

    class DreamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTextView: TextView = view.findViewById(R.id.dreamDate)
        val dreamTextView: TextView = view.findViewById(R.id.dreamText)
        val interpretationTextView: TextView = view.findViewById(R.id.dreamInterpretation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DreamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dream_item, parent, false)
        return DreamViewHolder(view)
    }

    override fun onBindViewHolder(holder: DreamViewHolder, position: Int) {
        val dream = dreams[position]
        holder.dateTextView.text = dream.date
        holder.dreamTextView.text = dream.dreamText
        holder.interpretationTextView.text = dream.interpretation

        holder.itemView.setOnClickListener {
            onClick(dream)
        }
    }

    override fun getItemCount(): Int = dreams.size
}

