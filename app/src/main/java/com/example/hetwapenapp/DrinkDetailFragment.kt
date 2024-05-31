package com.example.hetwapenapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide

class DrinkDetailDialogFragment : DialogFragment() {

    //Weergeven van het detailscherm van de drankjes
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drink_detail, container, false)
    }

    //Weergeven en instellen van de drankjesdata
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val drinkName: TextView = view.findViewById(R.id.drink_name)
        val drinkDescription: TextView = view.findViewById(R.id.drink_description)
        val drinkType: TextView = view.findViewById(R.id.drink_type)
        val drinkAbv: TextView = view.findViewById(R.id.drink_abv)
        val drinkStars: TextView = view.findViewById(R.id.drink_stars)
        val drinkImage: ImageView = view.findViewById(R.id.drink_image)
        val drinkCompany: TextView = view.findViewById(R.id.drink_company)

        val drink = arguments?.getParcelable<Drinks>("drink")

        // data uit het object ophalen en tonen
        drink?.let {
            drinkName.text = it.name
            drinkDescription.text = it.description
            drinkType.text = "Type: ${it.type}"
            drinkAbv.text = "ABV: ${it.abv}%"
            drinkStars.text = "Sterren: ${it.stars}"
            drinkCompany.text = it.company
            Glide.with(this).load(it.image).into(drinkImage)
        }
    }

    companion object {
        fun newInstance(drink: Drinks): DrinkDetailDialogFragment {
            val args = Bundle()
            args.putParcelable("drink", drink)
            val fragment = DrinkDetailDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
