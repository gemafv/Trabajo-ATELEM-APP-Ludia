package com.example.ludia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class EstadisticasActivity extends AppCompatActivity {

    private static final String TAG = "EstadisticasActivity";

    private TextView tresRayaVictoriasX, tresRayaVictoriasO, tresRayaEmpates;
    private TextView ahorcadoAciertos, ahorcadoDerrotas, ahorcadoMagistrales;
    private ImageButton buttonBack;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadisticas);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        tresRayaVictoriasX = findViewById(R.id.tresRayaVictoriasX);
        tresRayaVictoriasO = findViewById(R.id.tresRayaVictoriasO);
        tresRayaEmpates = findViewById(R.id.tresRayaEmpates);
        ahorcadoAciertos = findViewById(R.id.ahorcadoAciertos);
        ahorcadoDerrotas = findViewById(R.id.ahorcadoDerrotas);
        ahorcadoMagistrales = findViewById(R.id.ahorcadoMagistrales);
        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(EstadisticasActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        cargarEstadisticasTresEnRaya();
        cargarEstadisticasAhorcado();
    }

    private void cargarEstadisticasTresEnRaya() {
        if (user == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Object obj = documentSnapshot.get("tresEnRaya");
                        if (obj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> data = (Map<String, Object>) obj;
                            tresRayaVictoriasX.setText(String.valueOf(getLongSafe(data, "victoriasX")));
                            tresRayaVictoriasO.setText(String.valueOf(getLongSafe(data, "victoriasO")));
                            tresRayaEmpates.setText(String.valueOf(getLongSafe(data, "empates")));
                        } else {
                            setTresEnRayaStatsToZero();
                        }
                    } else {
                        setTresEnRayaStatsToZero();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al obtener estadísticas tres en raya", e);
                    setTresEnRayaStatsToZero();
                });
    }

    private void setTresEnRayaStatsToZero() {
        tresRayaVictoriasX.setText("0");
        tresRayaVictoriasO.setText("0");
        tresRayaEmpates.setText("0");
    }

    private void cargarEstadisticasAhorcado() {
        if (user == null) return;

        String uid = user.getUid();

        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        Object obj = snapshot.get("ahorcado");
                        if (obj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> data = (Map<String, Object>) obj;
                            ahorcadoAciertos.setText(String.valueOf(getLongSafe(data, "victorias")));
                            ahorcadoDerrotas.setText(String.valueOf(getLongSafe(data, "derrotas")));
                            ahorcadoMagistrales.setText(String.valueOf(getLongSafe(data, "victoriasMagistrales")));
                        } else {
                            setAhorcadoStatsToZero();
                        }
                    } else {
                        setAhorcadoStatsToZero();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al cargar estadísticas de ahorcado", e);
                    setAhorcadoStatsToZero();
                });
    }

    private void setAhorcadoStatsToZero() {
        ahorcadoAciertos.setText("0");
        ahorcadoDerrotas.setText("0");
        ahorcadoMagistrales.setText("0");
    }

    private Long getLongSafe(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Long) return (Long) val;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Double) return ((Double) val).longValue();
        return 0L;
    }
}
