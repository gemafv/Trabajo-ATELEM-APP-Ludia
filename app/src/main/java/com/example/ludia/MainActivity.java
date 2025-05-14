package com.example.ludia;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Declarar los botones
    Button btnTresEnRaya, btnAhorcado, btnTirarDado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Cargar el layout

        // Asociar los botones con sus IDs del XML
        btnTresEnRaya = findViewById(R.id.btnTresEnRaya);
        btnAhorcado = findViewById(R.id.btnAhorcado);
        btnTirarDado = findViewById(R.id.btnTirarDado);

        // Agregar acciones a cada botón (por ahora solo un mensaje)
        btnTresEnRaya.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TresEnRayaActivity.class);
            startActivity(intent);
        });

        btnAhorcado.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Abrir Ahorcado (pendiente)", Toast.LENGTH_SHORT).show()
        );

        btnTirarDado.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Abrir Tirar Dado (pendiente)", Toast.LENGTH_SHORT).show()
        );
    }
}
