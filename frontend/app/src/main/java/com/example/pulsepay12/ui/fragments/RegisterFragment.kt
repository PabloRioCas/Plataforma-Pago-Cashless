package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.pulsepay12.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONObject

class RegisterFragment: Fragment(), OnClickListener {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Registro de usuarios"
        binding.btnRegistrar.isEnabled = false
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.btnRegistrar.isEnabled =
                    binding.etPass.text.toString() == binding.etConfirmPass.text.toString() &&
                            binding.etName.text.toString().isNotEmpty() &&
                            binding.etLastName.text.toString().isNotEmpty() &&
                            binding.etMail.text.toString().isNotEmpty() &&
                            binding.etPhone.text.toString().isNotEmpty() &&
                            binding.etPass.text.toString().isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.etMail.addTextChangedListener(watcher)
        binding.etPass.addTextChangedListener(watcher)
        binding.etConfirmPass.addTextChangedListener(watcher)
        binding.etName.addTextChangedListener(watcher)
        binding.etLastName.addTextChangedListener(watcher)
        binding.etPhone.addTextChangedListener(watcher)
        binding.btnRegistrar.setOnClickListener(this)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnRegistrar.setOnClickListener{registrarUsuario(

            binding.etMail.text.toString(),
            binding.etPass.text.toString(),
            binding.etName.text.toString(),
            binding.etLastName.text.toString(),
            binding.etPhone.text.toString())
        }
    }


    fun registrarUsuario(nombre: String, apellidos: String,phone: String, email: String, contrasenia: String) {
        val url = "http://10.0.2.2:8080/api/v1/auth/register"
        val jsonBody = JSONObject().apply {
            put("email", email)
            put("password",contrasenia)
            put("name", nombre)
            put("lastname", apellidos)
            put("phone",phone)
        }

        val request = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Snackbar.make(binding.root, "Usuario registrado correctamente", Snackbar.LENGTH_SHORT).show()
            },
            { error ->
                Snackbar.make(binding.root, "Error al registrar, por favor, intentelo de nuevo", Snackbar.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Añade el token si tu backend lo pide
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        requireContext().let { Volley.newRequestQueue(it).add(request) }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnRegistrar-> { registrarUsuario(

                binding.etMail.text.toString(),
                binding.etPass.text.toString(),
                binding.etName.text.toString(),
                binding.etLastName.text.toString(),
                binding.etPhone.text.toString())


            }
        }
    }
}