package com.example.ludia;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    // Declarar los botones
    Button btnTresEnRaya, btnAhorcado, btnTirarDado, btnCerrarSesion;
    ImageButton btnEstadisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Cargar el layout

        // Asociar los botones con sus IDs del XML
        btnTresEnRaya = findViewById(R.id.btnTresEnRaya);
        btnAhorcado = findViewById(R.id.btnAhorcado);
        btnTirarDado = findViewById(R.id.btnTirarDado);
        btnEstadisticas = findViewById(R.id.btnEstadisticas);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Agregar acciones a cada botón (por ahora solo un mensaje)

        btnEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EstadisticasActivity.class);
            startActivity(intent);
            finish();
        });

        btnTresEnRaya.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TresEnRayaActivity.class);
            startActivity(intent);
            finish();
        });

        btnAhorcado.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AhorcadoActivity.class);
            startActivity(intent);
            finish();
        });


        btnTirarDado.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TirarDadoActivity.class);
            startActivity(intent);
            finish();
        });
        // NUEVO: Botón cerrar sesión
        btnCerrarSesion.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut(); // Cierra sesión en Firebase
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia el backstack
            startActivity(intent);
            finish();
        });
    }
}
