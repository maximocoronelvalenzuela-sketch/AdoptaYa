package com.example.adoptaya.data.servicios

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

// Extiende de CoroutineWorker para que Android ejecute la tarea en un hilo secundario sin trabar la UI
class NotificacionWorker(
    private val contexto: Context,
    parametros: WorkerParameters
) : CoroutineWorker(contexto, parametros) {

    // Funcion obligatoria que WorkManager ejecuta automaticamente cuando llega el turno de esta tarea
    override suspend fun doWork(): Result {
        mostrarNotificacion()
        return Result.success() // Le avisa a Android que la tarea terminó bien
    }

    private fun mostrarNotificacion() {
        // Lee el diccionario (workDataOf) que se le envia desde la pantalla de origen
        val titulo = inputData.getString("titulo") ?: "¡Novedades en AdoptaYa!"
        val descripcion = inputData.getString("descripcion") ?: "Entrá para ver qué pasó."
        val notificacionId = inputData.getString("notificacionId") ?: "" // Recibimos el ID

        val canalId = "canal_adoptaya_defecto"
        // Herramienta nativa para controlar las alertas de la barra de estado
        val notificationManager = contexto.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Se crea un "Canal" para que el usuario pueda administrar los permisos de este tipo de notificaciones
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(canalId, "Notificaciones Generales", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(canal)
        }

        // Intent para abrir el detalle de la notificacion
        val intent = android.content.Intent(
            android.content.Intent.ACTION_VIEW,
            android.net.Uri.parse("adoptaya://notificaciones/$notificacionId") // Deep Link hacia la notificacion exacta
        ).apply {
            // Esto asegura que la app se abra en una nueva tarea si estaba cerrada
            flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            setClassName(contexto, "com.example.adoptaya.MainActivity")
        }

        val pendingIntent = android.app.PendingIntent.getActivity(
            contexto,
            notificacionId.hashCode(), // Un ID único para la accion para que no se pisen las notificaciones
            intent,
            android.app.PendingIntent.FLAG_IMMUTABLE or android.app.PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Diseño visual de la notificacion
        val builder = NotificationCompat.Builder(contexto, canalId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(descripcion)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent) // Vinculamos el toque con el Deep Link

        // Finalmente, le ordena al sistema operativo que muestre la notificacion en pantalla
        notificationManager.notify(notificacionId.hashCode(), builder.build())
    }
}