package com.example.hetwapenapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DrinksAdapter(
    private var drinks: List<Drinks>,
    private val onItemClick: (Drinks) -> Unit
) : RecyclerView.Adapter<DrinksAdapter.DrinkViewHolder>() {

    class DrinkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val drinkImage: ImageView = itemView.findViewById(R.id.drink_image)
        val drinkName: TextView = itemView.findViewById(R.id.drink_name)
        val drinkCompany: TextView = itemView.findViewById(R.id.drink_company)
        val drinkAbv: TextView = itemView.findViewById(R.id.drink_abv)
        val drinkStars: TextView = itemView.findViewById(R.id.drink_stars)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drink, parent, false)
        return DrinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrinkViewHolder, position: Int) {
        val drink = drinks[position]
        holder.drinkName.text = drink.name
        holder.drinkCompany.text = drink.company
        holder.drinkAbv.text = "ABV: ${drink.abv}%"
        holder.drinkStars.text = "Stars: ${drink.stars}"
        Glide.with(holder.itemView.context).load(drink.image).into(holder.drinkImage)

        holder.itemView.setOnClickListener { onItemClick(drink) }
    }

    override fun getItemCount(): Int = drinks.size

    fun updateList(newDrinks: List<Drinks>) {
        drinks = newDrinks
        notifyDataSetChanged()
    }
}
