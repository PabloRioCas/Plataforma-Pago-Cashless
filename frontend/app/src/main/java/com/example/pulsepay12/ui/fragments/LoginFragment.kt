package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup.OnCheckedChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pulsepay12.MainActivity
import com.example.pulsepay12.R
import com.example.pulsepay12.databinding.FragmentLoginBinding
import com.example.pulsepay12.model.User
import com.example.pulsepay12.service.LoginRequest
import com.example.pulsepay12.service.RetrofitClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginFragment: Fragment(), CompoundButton.OnCheckedChangeListener {

    private lateinit var binding: FragmentLoginBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.iconoMostrarPass.setOnCheckedChangeListener(this)
        binding.checkRecordarPass.setOnCheckedChangeListener(this)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        binding.botonLogueo.setOnClickListener{
            if (binding.etCorreo.text.toString().isEmpty() || binding.etCorreo.text.toString().isEmpty()) {
                Snackbar.make(binding.root, "Completa todos los campos", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                binding.botonLogueo.text = getString(R.string.loading)
                binding.botonLogueo.isEnabled = false
                binding.progressBar.visibility = View.VISIBLE
                login(binding.etCorreo.text.toString(),binding.etPass.text.toString())
            }
        }
    }
    fun login(email: String, password: String) {
        val url = "http://10.0.2.2:8080/api/v1/auth/login"
        val jsonBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        val loginRequest = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                    val token = response.getString("access_token")
                    val prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                    prefs.edit().putString("access_token", token).apply()
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                        if (!binding.checkRecordarPass.isChecked) {
                            binding.etCorreo.text?.clear()
                            binding.etPass.text?.clear()
                        }
                        binding.botonLogueo.text = getString(R.string.btnLogin)
                        binding.botonLogueo.isEnabled = true
                        binding.progressBar.visibility = View.GONE
            },
            { error ->
                if(error.networkResponse?.statusCode == 403){
                    binding.botonLogueo.text = getString(R.string.btnLogin)
                    binding.botonLogueo.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, "Usuario o contraseña incorrecta", Snackbar.LENGTH_SHORT)
                        .setAction("¿Quieres registrarte?"){findNavController().navigate(R.id.action_dashboardAdminFragment_to_registerFragment)}
                }
                if(error.networkResponse == null){
                    Snackbar.make(binding.root, "Sin conexion a internet", Snackbar.LENGTH_SHORT).show()
                }
           }
        )
        requireContext().let { Volley.newRequestQueue(it).add(loginRequest) }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView!!.id){
            binding.iconoMostrarPass.id->{
                if (isChecked) {
                    // Mostrar contraseña
                    binding.etPass.inputType =  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                }else {
                // Ocultar contraseña
                    binding.etPass.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                // Cambia el icono si tienes
                binding.etPass.setSelection(binding.etPass.text?.length ?: 0)
            }
            binding.checkRecordarPass.id->{
                if (isChecked) {
                    var preferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    with(preferences.edit()){
                        putString("email", binding.etCorreo.text.toString())
                        apply()
                    }
                }else { var preferences = requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE)
                    with(preferences.edit()){
                        remove("email")
                        apply()
                    }

                }
            }
        }
    }


}