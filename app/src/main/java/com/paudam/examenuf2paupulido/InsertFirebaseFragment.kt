package com.paudam.examenpaupulido.insertFirebase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.paudam.examenuf2paupulido.InsertFirebaseVM
import com.paudam.examenuf2paupulido.R
import com.paudam.examenuf2paupulido.databinding.FragmentInsertFirebaseBinding

class InsertFirebaseFragment : Fragment() {
    private lateinit var binding: FragmentInsertFirebaseBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val viewModelInsertFirebase: InsertFirebaseVM by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_insert_firebase, container, false
        )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.buttonInsert.setOnClickListener() {
            if (binding.editTextEmail.text.isNotEmpty() && binding.editTextPasswd.text.isNotEmpty()) {
                //coger campos necesarios para bdd y para comprovaciones
                val email = binding.editTextEmail.text.toString().trim()
                val password = binding.editTextPasswd.text.toString().trim()
                val nombreUser = binding.editTextNom.text.toString().trim()
                val edadUser = binding.editTextEdat.text.toString().trim()

                // comporvaciones
                if (email.isEmpty() || password.isEmpty() || nombreUser.isEmpty() || edadUser.isEmpty()) {
                    viewModelInsertFirebase.showAlert("Todos los campos son obligatorios", requireContext())
                    return@setOnClickListener
                }

                val edad = edadUser.toIntOrNull()
                if (edad == null || edad < 0) {
                    viewModelInsertFirebase.showAlert("La edad debe ser un número válido", requireContext())
                    return@setOnClickListener
                }

                //crear el user en el auth
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //si el auth es correcto busca si existe el usuario
                        task.result?.user?.let { userActual ->
                            //si entra bien hace el insert
                            viewModelInsertFirebase.createUser(db, userActual, nombreUser, edad, requireContext())
                        } ?: run {
                            viewModelInsertFirebase.showAlert("Error: No se pudo obtener el usuario después del registro", requireContext())
                        }
                    } else {
                        viewModelInsertFirebase.showAlert("Error al registrar usuario: ${task.exception?.message}", requireContext())
                    }
                }



            }


        }
        binding.buttonLogin.setOnClickListener(){
            findNavController().navigate(R.id.action_insertFirebaseFragment_to_loginFragment)
        }
        return binding.root
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
