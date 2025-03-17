package com.paudam.examenpaupulido.insertFirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.paudam.examenuf2paupulido.R
import com.paudam.examenuf2paupulido.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login, container, false
        )

        binding.buttonLogin.setOnClickListener(){
            if (binding.editTextEmail.text.isNotEmpty() && binding.editTextPasswd.text.isNotEmpty()) {
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(
                        binding.editTextEmail.text.toString(),
                        binding.editTextPasswd.text.toString()
                    ).addOnCompleteListener {
                        if (it.isSuccessful) {
                            findNavController().navigate(R.id.action_loginFragment_to_fetFragment)
                        } else {
                            showAlert("No te has logeado algo pasa")
                        }
                    }
            }
        }

        return binding.root


    }



    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showMssg(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("mensaje succesfullll")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}