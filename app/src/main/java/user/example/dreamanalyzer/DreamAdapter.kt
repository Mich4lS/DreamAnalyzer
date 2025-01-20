package user.example.dreamanalyzer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DreamAdapter(
    private var dreams: MutableList<Dream>,
    private val onItemClicked: (Dream) -> Unit
) : RecyclerView.Adapter<DreamAdapter.DreamViewHolder>() {

    class DreamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.dreamDate)
        val text: TextView = view.findViewById(R.id.dreamText)
        val interpretation: TextView = view.findViewById(R.id.dreamInterpretation)
        val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DreamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dream_item, parent, false)
        return DreamViewHolder(view)
    }

    override fun onBindViewHolder(holder: DreamViewHolder, position: Int) {
        val dream = dreams[position]
        holder.date.text = dream.date
        holder.text.text = dream.dreamText
        holder.interpretation.text = dream.interpretation

        holder.itemView.setOnClickListener {
            onItemClicked(dream)
        }

        holder.deleteButton.setOnClickListener {
            dreams.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)

            saveDreamsToPreferences(holder.itemView.context, dreams)
        }
    }

    override fun getItemCount(): Int = dreams.size
}

