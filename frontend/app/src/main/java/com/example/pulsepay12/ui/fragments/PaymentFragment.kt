package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pulsepay12.R
import com.example.pulsepay12.databinding.FragmentLoginBinding
import com.example.pulsepay12.databinding.FragmentPaymentBinding
import com.example.pulsepay12.databinding.FragmentRechargeBinding
import com.example.pulsepay12.databinding.FragmentRegisterBinding
import com.example.pulsepay12.service.AuthUtils
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class PaymentFragment: Fragment(), OnClickListener {

    private lateinit var binding: FragmentPaymentBinding
    private var jwt: String? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentBinding.inflate(inflater,container,false)
        jwt = AuthUtils.getJwtToken(requireContext())
        binding.ivLimpiarCampo.setOnClickListener(this)
        binding.btnPagar.setOnClickListener(this)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Pagar consumición"
        return binding.root
    }

    override fun onStart() {
        super.onStart()
   }
    override fun onClick(v: View?) {
        when(v!!.id){
            binding.ivLimpiarCampo.id->{binding.etIntroducirCantidad.text.clear()}
            binding.btnPagar.id->{
                if(!(binding.etIntroducirCantidad.text.isEmpty())){
                    binding.btnPagar.text = getString(R.string.loading)
                    binding.btnPagar.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                   pagar(binding.etIntroducirCantidad.text.toString(), binding.spinnerConceptos.selectedItem.toString())
                }else{
                    Toast.makeText(requireContext(),"Por favor, introduzca una cantidad",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun pagar(cantidad: String, descripcion: String) {
        val url = "http://10.0.2.2:8080/api/v1/transactions"
        val jsonBody = JSONObject().apply {
            put("qty", cantidad)
            put("type", "PAYMENT")
            put("description",descripcion)
        }
        val recargaRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Snackbar.make(binding.root, "Recarga completada", Snackbar.LENGTH_SHORT).show()
                binding.etIntroducirCantidad.text.clear()
                binding.btnPagar.text = "Pagar"
                binding.btnPagar.isEnabled = true
                binding.progressBar.visibility = View.GONE
            },
            { error ->
                Snackbar.make(binding.root, "Error al recargar", Snackbar.LENGTH_SHORT).show()
                binding.btnPagar.text = "Pagar"
                binding.btnPagar.isEnabled = true
                binding.progressBar.visibility = View.GONE
            }
        )
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $jwt"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        requireContext().let { Volley.newRequestQueue(it).add(recargaRequest)}
    }
}