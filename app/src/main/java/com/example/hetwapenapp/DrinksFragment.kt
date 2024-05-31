package com.example.hetwapenapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray


class DrinksFragment : Fragment() {

    // De variabelen die gebruikt worden
    private lateinit var drinksContainer: LinearLayout
    private var drinksList: List<Drinks> = listOf()
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_drinks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        drinksContainer = view.findViewById(R.id.drinks_container)
        val filterSpinner: Spinner = view.findViewById(R.id.filter_spinner)

        requestQueue = Volley.newRequestQueue(context)
        fetchDrinks()

        // Dropdown/spinner instellen voor het filteren van de drankjes
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            filterSpinner.adapter = adapter
        }

        // Filteren van de drankjes op basis van het geselecteerde categorie
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                val filteredList = when (selectedItem) {
                    "Beer" -> drinksList.filter { it.type.equals("beer", true) }
                    "Wine" -> drinksList.filter { it.type.equals("wine", true) }
                    else -> drinksList
                }
                updateDrinksList(filteredList)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    // OPhalen van de drankjes vanuit de API
    private fun fetchDrinks() {
        val url = "https://hetwapen.projects.adainforma.tk/api/v1/drink"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.POST, url, null,
            Response.Listener { response ->
                parseDrinks(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val errorMessage = error.message ?: "Onbekende fout"
                Toast.makeText(context, "Kan helaas de drankjes niet ophalen: $errorMessage", Toast.LENGTH_LONG).show()


                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val responseData = String(error.networkResponse.data)
                    Toast.makeText(context, "Error $statusCode: $responseData", Toast.LENGTH_LONG).show()
                }
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    // Json response verwerken en lijst van Drinks maken
    private fun parseDrinks(response: JSONArray) {
        val drinks = mutableListOf<Drinks>()
        for (i in 0 until response.length()) {
            val jsonObject = response.getJSONObject(i)
            val drink = Drinks(
                id = jsonObject.getInt("id"),
                name = jsonObject.getString("name"),
                description = jsonObject.getString("description"),
                type = jsonObject.getString("type"),
                abv = jsonObject.getDouble("abv").toFloat(),
                stars = jsonObject.getInt("stars"),
                image = jsonObject.getString("image"),
                company = jsonObject.getString("company")
            )
            drinks.add(drink)
        }
        drinksList = drinks
        updateDrinksList(drinksList)
    }

    // Groepeert de drankjes op basis van de categorie
    private fun updateDrinksList(drinks: List<Drinks>) {
        drinksContainer.removeAllViews()

        val groupedDrinks = drinks.groupBy { it.type }

        for ((type, drinks) in groupedDrinks) {
            val typeHeader = TextView(context).apply {
                text = when (type.toLowerCase()) {
                    "beer" -> "Bier"
                    "wine" -> "Wijn"
                    else -> type.capitalize()
                }
                textSize = 24f
                setBackgroundColor(Color.WHITE)
                setTextColor(Color.RED)
                setPadding(16, 16, 16, 16)
            }


            drinksContainer.addView(typeHeader)


            for (drink in drinks) {
                val itemView = layoutInflater.inflate(R.layout.item_drink, drinksContainer, false)
                val drinkImage: ImageView = itemView.findViewById(R.id.drink_image)
                val drinkName: TextView = itemView.findViewById(R.id.drink_name)
                val drinkDescription: TextView = itemView.findViewById(R.id.drink_description)

                drinkName.text = drink.name
                drinkDescription.text = drink.description
                Glide.with(itemView.context).load(drink.image).into(drinkImage)

                itemView.setOnClickListener {
                    showDrinkDetails(drink)
                }

                drinksContainer.addView(itemView)
            }
        }
    }

    private fun showDrinkDetails(drink: Drinks) {
        val dialogFragment = DrinkDetailDialogFragment.newInstance(drink)
        dialogFragment.show(childFragmentManager, "drinkDetail")
    }
}
