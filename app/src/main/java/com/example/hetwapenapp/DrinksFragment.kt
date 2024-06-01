package com.example.hetwapenapp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray

class DrinksFragment : Fragment() {

    private lateinit var drinksContainer: LinearLayout
    private lateinit var searchView: SearchView
    private lateinit var filterSpinner: Spinner
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
        searchView = view.findViewById(R.id.search_view)
        filterSpinner = view.findViewById(R.id.filter_spinner)

        requestQueue = Volley.newRequestQueue(context)
        fetchDrinks()

        // Eigen layout en thema voor de spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.filter_options,
            R.layout.spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(R.layout.spinner_item)
            filterSpinner.adapter = adapter
        }

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                filterAndSearchDrinks()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Als er niks geselecteerd is, alles blijven tonen/ niks doen
            }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                filterAndSearchDrinks()
                return true
            }
        })
    }

    // Aanroepen van de API
    private fun fetchDrinks() {
        val url = "https://hetwapen.projects.adainforma.tk/api/v1/drink"

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.POST, url, null,
            Response.Listener { response ->
                parseDrinks(response)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                val errorMessage = error.message ?: "Unknown error"
                Toast.makeText(context, "Failed to fetch drinks: $errorMessage", Toast.LENGTH_LONG).show()
                // Log extra details about the error
                if (error.networkResponse != null) {
                    val statusCode = error.networkResponse.statusCode
                    val responseData = String(error.networkResponse.data)
                    Toast.makeText(context, "Error $statusCode: $responseData", Toast.LENGTH_LONG).show()
                }
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

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

    // Filteren op de drankjes
    private fun filterAndSearchDrinks() {
        val query = searchView.query.toString()
        val selectedType = filterSpinner.selectedItem.toString()

        val filteredList = drinksList.filter {
            (selectedType == "All" || it.type.equals(selectedType, true)) &&
                    (query.isEmpty() || it.name.contains(query, true))
        }

        updateDrinksList(filteredList)
    }

    // Categoriseren van de drankjes
    private fun updateDrinksList(drinks: List<Drinks>) {
        drinksContainer.removeAllViews()

        val groupedDrinks = drinks.groupBy { it.type }

        for ((type, drinks) in groupedDrinks) {
            Log.d("DrinksFragment", "Adding header for type: $type")

            // Textview voor de categorienaam
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

            // Voeg de header toe aan de container
            drinksContainer.addView(typeHeader)
            Log.d("DrinksFragment", "Header toegevoegd voor: $type")

            // Voeg elke drink in de categorie toe aan de container
            for (drink in drinks) {
                val itemView = layoutInflater.inflate(R.layout.item_drink, drinksContainer, false)
                val drinkImage: ImageView? = itemView.findViewById(R.id.drink_image)
                val drinkName: TextView? = itemView.findViewById(R.id.drink_name)
                val drinkDescription: TextView? = itemView.findViewById(R.id.drink_description)

                if (drinkImage == null || drinkName == null || drinkDescription == null) {
                    Log.e("DrinksFragment", "Een van de views is leeg: drinkImage=$drinkImage, drinkName=$drinkName, drinkDescription=$drinkDescription")
                    continue
                }

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
