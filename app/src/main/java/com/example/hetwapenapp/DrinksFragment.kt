package com.example.hetwapenapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class DrinksFragment : Fragment() {

    private lateinit var drinksAdapter: DrinksAdapter
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

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val searchView: SearchView = view.findViewById(R.id.search_view)

        drinksAdapter = DrinksAdapter(drinksList) { drink ->
            showDrinkDetails(drink)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = drinksAdapter

        requestQueue = Volley.newRequestQueue(context)
        fetchDrinks()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = drinksList.filter {
                    it.name.contains(newText ?: "", true) ||
                            it.type.contains(newText ?: "", true)
                }
                drinksAdapter.updateList(filteredList)
                return true
            }
        })
    }

    private fun fetchDrinks() {
        val url = "https://hetwapen.projects.adainforma.tk/api/v1/drinks"
        val params = JSONObject()

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, params,
            Response.Listener { response ->
                parseDrinks(response.getJSONArray("data"))
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(context, "Failed to fetch drinks: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonObjectRequest)
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
        drinksAdapter.updateList(drinksList)
    }

    private fun showDrinkDetails(drink: Drinks) {
        val dialogFragment = DrinkDetailDialogFragment.newInstance(drink)
        dialogFragment.show(childFragmentManager, "drinkDetail")
    }
}
