package com.example.ludia;

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
        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(v -> finish());

        cargarEstadisticasTresEnRaya();
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
                        Log.d(TAG, "Documento usuario existe");

                        Object obj = documentSnapshot.get("tresEnRaya");

                        if (obj instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> mapTresEnRaya = (Map<String, Object>) obj;

                            Long victoriasX = getLongSafe(mapTresEnRaya, "victoriasX");
                            Long victoriasO = getLongSafe(mapTresEnRaya, "victoriasO");
                            Long empates = getLongSafe(mapTresEnRaya, "empates");

                            tresRayaVictoriasX.setText(String.valueOf(victoriasX));
                            tresRayaVictoriasO.setText(String.valueOf(victoriasO));
                            tresRayaEmpates.setText(String.valueOf(empates));

                            Log.d(TAG, "VictoriasX: " + victoriasX + ", VictoriasO: " + victoriasO + ", Empates: " + empates);

                        } else {
                            Log.d(TAG, "'tresEnRaya' no existe o no es un Map. Valor: " + obj);
                            setTresEnRayaStatsToZero();
                        }

                    } else {
                        Log.d(TAG, "Documento usuario NO existe");
                        setTresEnRayaStatsToZero();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al obtener documento usuario", e);
                    Toast.makeText(this, "Error al cargar estadísticas", Toast.LENGTH_SHORT).show();
                    setTresEnRayaStatsToZero();
                });
    }

    private void setTresEnRayaStatsToZero() {
        tresRayaVictoriasX.setText("0");
        tresRayaVictoriasO.setText("0");
        tresRayaEmpates.setText("0");
    }

    private Long getLongSafe(Map<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Long) return (Long) val;
        if (val instanceof Integer) return ((Integer) val).longValue();
        if (val instanceof Double) return ((Double) val).longValue();
        return 0L;
    }
}
