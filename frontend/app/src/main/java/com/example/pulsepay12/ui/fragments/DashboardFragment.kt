package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.pulsepay12.MainActivity
import com.example.pulsepay12.R
import com.example.pulsepay12.databinding.FragmentDashboardBinding
import com.example.pulsepay12.databinding.FragmentLoginBinding
import com.example.pulsepay12.model.User
import com.example.pulsepay12.service.AuthUtils
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import org.json.JSONObject
import kotlin.math.log2

class DashboardFragment: Fragment(), OnClickListener {

    private lateinit var binding: FragmentDashboardBinding
    private  var user: User? = null
    private var jwt: String? =null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(arguments?.getSerializable("user") != null){
            this.user =arguments?.getSerializable("user") as User
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        jwt = AuthUtils.getJwtToken(requireContext())
        binding = FragmentDashboardBinding.inflate(inflater,container,false)
        binding.btnMovimientos.setOnClickListener(this)
        binding.btnRecarga.setOnClickListener(this)
        binding.btnAjustes.setOnClickListener(this)
        binding.btnPagar.setOnClickListener(this)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Inicio"
        (activity as? MainActivity)?.showFab(true)
        val qrScanner = registerForActivityResult(
            ScanContract()
        ) { result: ScanIntentResult ->
            result.contents?.let { qrResult ->
                asignarPulsera(qrResult)
                // Aquí tienes el código QR escaneado.
                // Puedes mostrarlo, guardarlo o mandarlo al backend.
            }
        }
        (activity as? MainActivity)?.setFabClickListener {
            qrScanner.launch(ScanOptions().apply {
                setPrompt("Escanea un código QR")
                setBeepEnabled(true)
                setBarcodeImageEnabled(true)
            })
        }
        requestBalance()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.tvNombreDash.setText("${user?.mail ?: "invitado"}!")

        }

    override fun onClick(v: View?) {
        when(v!!.id){
            binding.btnRecarga.id->{
                findNavController().navigate(R.id.action_dashboardFragment_to_rechargeFragment)
            }
            binding.btnPagar.id->{
                findNavController().navigate(R.id.action_dashboardFragment_to_paymentFragment)
            }
            binding.btnMovimientos.id->{
              findNavController().navigate(R.id.action_dashboardFragment_to_movementsFragment)
            }
            binding.btnAjustes.id-> {
              findNavController().navigate(R.id.action_dashboardFragment_to_settingsFragment)
            }

        }
    }

    fun requestBalance() {
        val url = "http://10.0.2.2:8080/api/v1/balance" // Cambia por tu endpoint real
        Log.v("JWT", jwt.toString())
        val request = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                // Suponiendo que la respuesta es algo así: {"pulsera":"ASd5168s74"} o {"pulsera":null}
                val balance = response.optString("balance"?: "error", null)
                if (!balance.equals("error") && balance != "null") {
                    binding.tvSaldoActual.text = balance + "€"
                    binding.tvPulsera.text= null;
                } else {
                    binding.tvPulsera.text = getString(R.string.sin_pulsera)
                }
            },
            Response.ErrorListener {
                binding.tvPulsera.text = getString(R.string.sin_pulsera)
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $jwt" // Si usas JWT o Auth
                return headers
            }
        }
        Volley.newRequestQueue(requireContext()).add(request)
    }

    fun asignarPulsera(codigoPulseraEscaneado: String) {
        val url = "http://10.0.2.2:8080/api/v1/sync"
        val params = JSONObject()
        params.put("nfcUid", codigoPulseraEscaneado)

        val request = JsonObjectRequest(
            Request.Method.POST, url, params,
            { response ->
                val success = response.optString("sync_status", null)
                if (success.equals("sync_status")) {
                    binding.tvPulsera.text = getString(R.string.pulsera_asignada, codigoPulseraEscaneado)
                    Toast.makeText(requireContext(), "Pulsera asignada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "No se pudo asignar la pulsera", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(requireContext(), "Error de red", Toast.LENGTH_SHORT).show()
            }
        )
        request.headers["Authorization"] = "Bearer $jwt"
        request.headers["Content-Type"] = "application/json"

        Volley.newRequestQueue(requireContext()).add(request)
    }
}

