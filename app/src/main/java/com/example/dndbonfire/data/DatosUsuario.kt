package com.example.dndbonfire.data

import android.content.SharedPreferences
import android.util.Log
import com.example.dndbonfire.modelo.Usuario

class DatosUsuario {
    companion object {
        fun guardarUsuarioLogeado(pref: SharedPreferences, usuario: Usuario?) {
            val editor = pref.edit()
            if (usuario == null) {
                editor.remove("id")
                editor.remove("nombre")
                editor.remove("username")
                editor.remove("rango")
                editor.remove("token")
                editor.remove("imagen")
            } else {
                editor.putInt("id", usuario.id)
                editor.putString("nombre", usuario.nombre)
                editor.putString("username", usuario.username)
                editor.putInt("rango", usuario.rango)
                editor.putString("token", usuario.token)
                editor.putString("imagen", usuario.imagen)
            }
            usuario?.token?.let { Log.i("TOKEN", it) }
            editor.commit()
        }

        fun cargarUsuarioLogeado(pref: SharedPreferences): Usuario? {
            val id = pref.getInt("id", 0)
            val nombre = pref.getString("nombre", null)
            val username = pref.getString("username", null)
            val rango = pref.getInt("rango", 0)
            val token = pref.getString("token", null)
            val imagen = pref.getString("imagen", null)

            if (username == null) {
                return null
            } else {
                Log.i("TOKEN", token!!)
                return Usuario(id, nombre!!, username, rango, token!!, imagen)
            }
        }

        fun guardarTema(pref: SharedPreferences, tema: String) {
            val editor = pref.edit()
            editor.putString("tema", tema)
            editor.commit()
        }

        fun cargarTema(pref: SharedPreferences): String {
            return pref.getString("tema", "sistema") ?: "sistema"
        }
    }
}