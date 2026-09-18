package com.example.pulsepay12.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pulsepay12.R
import com.example.pulsepay12.databinding.FragmentDashboarAdminBinding
import com.example.pulsepay12.databinding.FragmentDashboardBinding
import com.example.pulsepay12.databinding.FragmentLoginBinding
import com.example.pulsepay12.model.User

class DashboardAdminFragment: Fragment(), OnClickListener {

    private lateinit var binding: FragmentDashboarAdminBinding
    private  var user: User? = null

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
        binding = FragmentDashboarAdminBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.tvNombreDash.setText("${user?.mail ?: "invitado"}!")
        binding.btnRegistrar.setOnClickListener(this)
        binding.btnPulseras.setOnClickListener(this)
        binding.btnDevolucion.setOnClickListener(this)

        }

    override fun onClick(v: View?) {
        when(v!!.id){
            binding.btnPulseras.id->{

            }
            binding.btnDevolucion.id->{
                findNavController().navigate(R.id.action_dashboardFragment_to_rechargeFragment)
            }
//            binding.btnRegistrar.id-> {
//                findNavController().navigate(R.id.action_dashboardFragment_to_registerFragment)
//            }
        }
    }

}

