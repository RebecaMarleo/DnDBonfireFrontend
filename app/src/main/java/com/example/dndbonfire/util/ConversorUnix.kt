package com.example.dndbonfire.util

object ConversorUnix {
    fun milisAFechaDMY(milis: Long?): String {
        if (milis != null) {
            return java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault())
                .format(java.util.Date(milis))
        } else {
            return "__-__-____"
        }
    }

    fun milisAFechaYMD(milis: Long): String {
        return java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            .format(java.util.Date(milis))
    }

    fun fechaYMDaMilis(fecha: String?): Long? {
        if (fecha == null) {
            return null
        } else {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            sdf.isLenient = false
            return sdf.parse(fecha)?.time
                ?: throw IllegalArgumentException("Fecha inválida: $fecha")
        }
    }

    fun fechaYMDaFechaDMY(fecha: String): String {
        val input = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        input.isLenient = false

        val date = input.parse(fecha)
            ?: throw IllegalArgumentException("Fecha inválida: $fecha")

        val output = java.text.SimpleDateFormat(
            "d 'de' MMMM 'de' yyyy",
            java.util.Locale("es", "ES")
        )

        return output.format(date)
    }

    fun milisAHoraHMS(milis: Long?): String {
        if (milis != null) {
            return java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                .format(java.util.Date(milis))
        } else {
            return "__:__"
        }
    }

    fun milisAFechaYMDHMS(milis: Long?): String {
        if (milis != null) {
            val formatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
            return formatter.format(java.util.Date(milis))
        } else {
            return "____-__-__T__:__:__"
        }
    }

    fun fechaYMDHMSConTAMilis(fecha: String): Long {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        sdf.isLenient = false
        return sdf.parse(fecha)?.time
            ?: throw IllegalArgumentException("Fecha inválida: $fecha")
    }

    fun fechaYMDHMSAMilis(fecha: String): Long {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        sdf.isLenient = false
        return sdf.parse(fecha)?.time
            ?: throw IllegalArgumentException("Fecha inválida: $fecha")
    }

    fun actualizarFechaYMDHMS(milis: Long?, hora: Int, minutos: Int): String {
        if (milis != null) {
            val fecha = java.util.Date(milis)
            val cal = java.util.Calendar.getInstance()
            cal.time = fecha
            cal.set(java.util.Calendar.HOUR_OF_DAY, hora)
            cal.set(java.util.Calendar.MINUTE, minutos)
            cal.set(java.util.Calendar.SECOND, 0)

            return java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                .format(cal.time)
        } else {
            return "____-__-__ __:__:__"
        }
    }

    fun fechaYMDHMSaFechaDMYHMS(fecha: String): String {
        val input = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())
        input.isLenient = false

        val date = input.parse(fecha)
            ?: throw IllegalArgumentException("Fecha inválida: $fecha")

        val output = java.text.SimpleDateFormat(
            "d 'de' MMMM 'de' yyyy 'a las' HH':'mm 'horas'",
            java.util.Locale("es", "ES")
        )

        return output.format(date)
    }

    fun fechaYMDHMSFormateadaAMilis(fecha: String): Long {
        val sdf = java.text.SimpleDateFormat("d 'de' MMMM 'de' yyyy 'a las' HH':'mm 'horas'", java.util.Locale.getDefault())
        sdf.isLenient = false
        return sdf.parse(fecha)?.time
            ?: throw IllegalArgumentException("Fecha inválida: $fecha")
    }
}