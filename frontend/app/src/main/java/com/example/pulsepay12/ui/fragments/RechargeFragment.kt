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
import com.example.pulsepay12.databinding.FragmentRechargeBinding
import com.example.pulsepay12.databinding.FragmentRegisterBinding
import com.example.pulsepay12.service.AuthUtils
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class RechargeFragment: Fragment(), OnClickListener {

    private lateinit var binding: FragmentRechargeBinding
    private var jwt: String? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRechargeBinding.inflate(inflater,container,false)
        jwt = AuthUtils.getJwtToken(requireContext())
        binding.ivLimpiarCampo.setOnClickListener(this)
        binding.btnRecarga1.setOnClickListener(this)
        binding.btnRecarga5.setOnClickListener(this)
        binding.btnRecarga10.setOnClickListener(this)
        binding.btnRecarga20.setOnClickListener(this)
        binding.btnRecarga50.setOnClickListener(this)
        binding.btnRecarga100.setOnClickListener(this)
        binding.btnRecarga.setOnClickListener(this)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Recarga de saldo"
        return binding.root
    }

    override fun onStart() {
        super.onStart()
   }
    override fun onClick(v: View?) {
        when(v!!.id){
            binding.ivLimpiarCampo.id->{binding.etIntroducirCantidad.text.clear()}
            binding.btnRecarga1.id->{binding.etIntroducirCantidad.setText("1")}
            binding.btnRecarga5.id->{binding.etIntroducirCantidad.setText("5")}
            binding.btnRecarga10.id->{binding.etIntroducirCantidad.setText("10")}
            binding.btnRecarga20.id->{binding.etIntroducirCantidad.setText("20")}
            binding.btnRecarga50.id->{binding.etIntroducirCantidad.setText("50")}
            binding.btnRecarga100.id->{binding.etIntroducirCantidad.setText("100")}

            binding.btnRecarga.id->{
                if(!(binding.etIntroducirCantidad.text.isEmpty())){
                    binding.btnRecarga.text = getString(R.string.loading)
                    binding.btnRecarga.isEnabled = false
                    binding.progressBar.visibility = View.VISIBLE
                    recargarSaldo(binding.etIntroducirCantidad.text.toString())
                }else{
                    Toast.makeText(requireContext(),"Por favor, introduzca una cantidad",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun recargarSaldo(cantidad: String) {
        val url = "http://10.0.2.2:8080/api/v1/transactions"
        val jsonBody = JSONObject().apply {
            put("qty", cantidad)
            put("type", "RECHARGE")
        }
        val recargaRequest = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Snackbar.make(binding.root, "Recarga completada", Snackbar.LENGTH_SHORT).show()
                binding.etIntroducirCantidad.text.clear()
                binding.btnRecarga.text = getString(R.string.btnRecargar)
                binding.btnRecarga.isEnabled = true
                binding.progressBar.visibility = View.GONE
            },
            { error ->
                Snackbar.make(binding.root, "Error al recargar", Snackbar.LENGTH_SHORT).show()
                binding.btnRecarga.text = getString(R.string.btnRecargar)
                binding.btnRecarga.isEnabled = true
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