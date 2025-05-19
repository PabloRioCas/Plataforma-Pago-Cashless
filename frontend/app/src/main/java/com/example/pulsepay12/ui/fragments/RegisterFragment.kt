package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pulsepay12.R
import com.example.pulsepay12.databinding.FragmentLoginBinding
import com.example.pulsepay12.databinding.FragmentRegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment: Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onAttach(context: Context) {
        super.onAttach(context)
        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Registro de usuarios"
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.btnRegistrar.setOnClickListener{
            auth.createUserWithEmailAndPassword(
                binding.etMail.text.toString(),
                binding.etPass.text.toString()
            ).addOnCompleteListener{
                if(it.isSuccessful){
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }else{
                    Snackbar.make(binding.root,"Error en el registro",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}