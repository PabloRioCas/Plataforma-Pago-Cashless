package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.pulsepay12.R
import com.example.pulsepay12.databinding.FragmentLoginBinding
import com.example.pulsepay12.databinding.FragmentRegisterBinding
import com.example.pulsepay12.databinding.FragmentSettingsBinding
import com.example.pulsepay12.model.TransactionJSON
import com.example.pulsepay12.model.User
import com.example.pulsepay12.model.UserJSON
import com.example.pulsepay12.service.AuthUtils
import com.example.pulsepay12.service.RetrofitClient.instance
import com.example.pulsepay12.service.UpdateUserRequest
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.json.JSONObject

class SettingsFragment: Fragment(), OnCheckedChangeListener, OnClickListener{

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userJSON: UserJSON
    private var jwt: String? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater,container,false)
        jwt = AuthUtils.getJwtToken(requireContext())
        if (jwt!=null){
            userJSON = decodeJWT(jwt)
            binding.etModificarNombre.hint = userJSON.name
            binding.etModificarApellidos.hint = userJSON.lastName
            binding.etModificarCorreo.hint = userJSON.mail
            binding.etModificarTelefono.hint = userJSON.phone
        }else
        binding.checkTerceros.setOnCheckedChangeListener(this)
        binding.checkNotificaciones.setOnCheckedChangeListener(this)
        binding.checkUbicacion.setOnCheckedChangeListener(this)
        binding.btnCancelar.setOnClickListener(this)
        binding.btnGuardar.isEnabled =false

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.btnGuardar.isEnabled =
                    binding.etModificarNombre.text.toString().isNotEmpty() &&
                    binding.etModificarApellidos.text.toString().isNotEmpty() &&
                    binding.etModificarCorreo.text.toString().isNotEmpty() &&
                    binding.etModificarTelefono.text.toString().isNotEmpty()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.etModificarNombre.addTextChangedListener(watcher)
        binding.etModificarApellidos.addTextChangedListener(watcher)
        binding.etModificarCorreo.addTextChangedListener(watcher)
        binding.etModificarTelefono.addTextChangedListener(watcher)
        binding.btnGuardar.isEnabled =false
        if(!binding.etModificarNombre.text.isNullOrEmpty() &&
            !binding.etModificarApellidos.text.isNullOrEmpty() &&
            !binding.etModificarCorreo.text.isNullOrEmpty() &&
            !binding.etModificarTelefono.text.isNullOrEmpty()){
            binding.btnGuardar.isEnabled =true
        }
        binding.btnGuardar.setOnClickListener(this)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Ajustes"
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<FloatingActionButton>(R.id.fab).hide()
    }
    override fun onStart() {
        super.onStart()
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when(buttonView!!.id){
            binding.checkTerceros.id->{
                var textoPermisoOn: String = "Permitido"
                var textoPermisoOf: String = "No permitido"
                if (binding.checkTerceros.isChecked){
                    binding.tvCheckTerceros.text = textoPermisoOn
                } else  binding.tvCheckTerceros.text = textoPermisoOf
            }
            binding.checkNotificaciones.id->{
                var textoPermisoOn: String = "Permitido"
                var textoPermisoOf: String = "No permitido"
                if (binding.checkNotificaciones.isChecked){
                    binding.tvCheckNotificaciones.text = textoPermisoOn
                } else  binding.tvCheckNotificaciones.text = textoPermisoOf
            }
            binding.checkUbicacion.id->{
                var textoPermisoOn: String = "Permitido"
                var textoPermisoOf: String = "No permitido"
                if (binding.checkUbicacion.isChecked){
                    binding.tvCheckUbicacion.text = textoPermisoOn
                } else  binding.tvCheckUbicacion.text = textoPermisoOf
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            binding.btnCancelar.id ->{findNavController().navigate(R.id.action_settingsFragment_to_dashboardFragment)}
            binding.btnGuardar.id->{
                if(jwt !=null){
                    val nombreNuevo = binding.etModificarNombre.text.toString()
                    val apellidosNuevo = binding.etModificarApellidos.text.toString()
                    val emailNuevo = binding.etModificarCorreo.text.toString()
                    val telefonoNuevo = binding.etModificarTelefono.text.toString()

                    val modificado = nombreNuevo != userJSON.name ||
                            apellidosNuevo != userJSON.lastName ||
                            emailNuevo != userJSON.mail ||
                            telefonoNuevo != userJSON.phone
                    if (modificado) {
                             actualizarUsuario(nombreNuevo, apellidosNuevo, emailNuevo,telefonoNuevo)
                    } else {
                        Snackbar.make(binding.root, "No hay cambios para guardar", Snackbar.LENGTH_SHORT).show()
                    }
                }else{ findNavController().navigate(R.id.action_global_loginFragment)
                    Snackbar.make(binding.root, "Tu sesión ha expirado. Por favor, inicia sesión de nuevo.", Snackbar.LENGTH_SHORT).show() }
            }
        }
    }

    fun actualizarUsuario(nombreNuevo: String, apellidosNuevo: String, emailNuevo: String, telefonoNuevo: String) {
        val url = "http://localhost:8080/api/v1/user/update"
        val jsonBody = JSONObject().apply {
            put("nombre", nombreNuevo)
            put("apellidos", apellidosNuevo)
            put("email", emailNuevo)
            put("phone",telefonoNuevo)
        }
        val request = object : JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Snackbar.make(binding.root, "Datos actualizados", Snackbar.LENGTH_SHORT).show()
            },
            { error ->
                Snackbar.make(binding.root, "Error al actualizar", Snackbar.LENGTH_SHORT).show()
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                // Añade el token si tu backend lo pide
                headers["Authorization"] = "Bearer $jwt"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        requireContext().let { Volley.newRequestQueue(it).add(request) }
    }

    fun decodeJWT(jwt: String?): UserJSON{
        val gson = Gson()
            val parts = jwt!!.split(".")
            val payload = parts[1]
            val padded = payload.padEnd((payload.length + 3) / 4 * 4, '=')
            val json = String(android.util.Base64.decode(padded, android.util.Base64.DEFAULT))
        return gson.fromJson(json, UserJSON::class.java)
    }
}