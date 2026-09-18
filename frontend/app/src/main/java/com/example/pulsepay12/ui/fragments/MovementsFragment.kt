package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.Volley.*
import com.example.pulsepay12.R
import com.example.pulsepay12.adapter.MovementsAdapter
import com.example.pulsepay12.databinding.FragmentLoginBinding
import com.example.pulsepay12.databinding.FragmentMovementsBinding
import com.example.pulsepay12.databinding.FragmentRegisterBinding
import com.example.pulsepay12.model.Transaction
import com.example.pulsepay12.model.TransactionJSON
import com.example.pulsepay12.service.AuthUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import org.json.JSONArray
import java.lang.reflect.Method
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MovementsFragment: Fragment(), MovementsAdapter.OnTransactionListener{

    private lateinit var binding: FragmentMovementsBinding
    private lateinit var adapter: MovementsAdapter
    private lateinit var transactionList: ArrayList<TransactionJSON>
    private var jwt: String? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovementsBinding.inflate(inflater,container,false)
        jwt = AuthUtils.getJwtToken(requireContext())
        instancias()
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Tus movimientos"
        if(jwt.isNullOrEmpty()) {
            // Redirige al login o muestra mensaje de autenticación
            findNavController().navigate(R.id.action_global_loginFragment)
        } else{
            val url: String = "http://10.0.2.2:8080/api/v1/transactions"
            val peticion = object :JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                { response ->
                    val gson = Gson()
                    if (response.length() == 0) {
                        Snackbar.make(binding.root,"Sin movimientos registrados todavía",Snackbar.LENGTH_SHORT).show()
                    }else{
                        for (i in 0.. response.length()-1){


                            val transactionJSON = response.getJSONObject(i)
                            Log.v("tagdedatos", transactionJSON.toString())
                            val transaction = gson.fromJson(transactionJSON.toString(),TransactionJSON::class.java)
                            adapter.addTransaction(transaction)

                        }
                    }
                },
                {error->
                    if (error.networkResponse?.statusCode == 401 ||
                        error.networkResponse?.statusCode == 403) {
                        // El token ha expirado, es inválido, etc.
                        findNavController().navigate(R.id.action_global_loginFragment)
                        Snackbar.make(binding.root, "Tu sesión ha expirado. Por favor, inicia sesión de nuevo.", Snackbar.LENGTH_SHORT).show()
                    }
                    Snackbar.make(binding.root,"Error en la conexión",Snackbar.LENGTH_SHORT).show()}) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = mutableMapOf<String, String>()
                    headers["Authorization"] = "Bearer $jwt"
                    return headers
                }
            }
            context?.let{ Volley.newRequestQueue(requireContext()).add(peticion)}
        }
        return binding.root
    }
    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<FloatingActionButton>(R.id.fab).hide()
    }

    private fun instancias() {
        /*transactionList = arrayListOf(
            Transaction("Consumo", 100.00,stringToDate("15/10/2025"),100.00),
            Transaction("Consumo",-5.0,stringToDate("15/10/2025")),
            Transaction("Recarga Online",50.0,stringToDate("15/10/2025")),
            Transaction("Consumo",-20.0,stringToDate("15/10/2025")),
            Transaction("Consumo",-7.50,stringToDate("15/10/2025")),
            Transaction("Consumo",-1.0,stringToDate("15/10/2025")),
            Transaction("Recarga Online",100.00,stringToDate("15/10/2025")),
            Transaction("Consumo",-15.0,stringToDate("15/10/2025")),
            Transaction("Consumo",-5.0,stringToDate("15/10/2025")),
            Transaction("Recarga Online",50.0,stringToDate("15/10/2025")),
            Transaction("Consumo",-20.0,stringToDate("15/10/2025")),
            Transaction("Consumo",-7.50,stringToDate("15/10/2025")),
            Transaction("Consumo",-1.0,stringToDate("15/10/2025")),
            Transaction("Recarga Online",100.00,stringToDate("15/10/2025")))*/
        transactionList = ArrayList()
        adapter  = MovementsAdapter(transactionList, requireContext())
        binding.recyclerMovements.adapter = adapter
        binding.recyclerMovements.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    fun stringToDate(fecha: String): Date {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.parse(fecha)!!
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onTransactionSelected(transaction: TransactionJSON) {
        super.onTransactionSelected(transaction)
    }
}