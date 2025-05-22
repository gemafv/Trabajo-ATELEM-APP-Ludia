package com.example.ludia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TresEnRayaActivity extends AppCompatActivity {

    private Button[][] botones = new Button[3][3]; //creamos una variable privada de tipo Button
    private boolean turnoJugador1 = true;
    private int ronda = 0;
    private TextView textTurno;
    private Button btnReiniciar, btnVolverMenu;

    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tres_en_raya);

        //autentificación y extracción del uid del usuario para poder guardar datos:
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        textTurno = findViewById(R.id.textTurno);
        btnReiniciar = findViewById(R.id.btnReiniciar);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);

        // Inicialización de botones del tablero
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "btn" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                botones[i][j] = findViewById(resID);

                botones[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((Button) v).getText().toString().equals("")) {
                            return;
                        }

                        if (turnoJugador1) {
                            ((Button) v).setText("X");
                        } else {
                            ((Button) v).setText("O");
                        }

                        ronda++;

                        if (comprobarGanador()) {
                            if (turnoJugador1) {
                                mostrarMensaje("¡Jugador X gana!");
                                guardarResultadoEnFirestore("victoriaX");
                            } else {
                                mostrarMensaje("¡Jugador O gana!");
                                guardarResultadoEnFirestore("victoriaO");
                            }
                            resetearTablero();
                        } else if (ronda == 9) {
                            mostrarMensaje("¡Empate!");
                            guardarResultadoEnFirestore("empate");
                            resetearTablero();
                        } else {
                            turnoJugador1 = !turnoJugador1;
                            actualizarTextoTurno();
                        }
                    }
                });
            }
        }

        btnReiniciar.setOnClickListener(v -> resetearTablero());
        btnVolverMenu.setOnClickListener(v -> volverAlMenu());

        actualizarTextoTurno();
    }

    private void actualizarTextoTurno() {
        if (turnoJugador1) {
            textTurno.setText("Turno: X");
        } else {
            textTurno.setText("Turno: O");
        }
    }

    private boolean comprobarGanador() {
        String[][] tablero = new String[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tablero[i][j] = botones[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if (tablero[i][0].equals(tablero[i][1]) && tablero[i][0].equals(tablero[i][2]) && !tablero[i][0].equals("")) {
                return true;
            }
            if (tablero[0][i].equals(tablero[1][i]) && tablero[0][i].equals(tablero[2][i]) && !tablero[0][i].equals("")) {
                return true;
            }
        }

        if (tablero[0][0].equals(tablero[1][1]) && tablero[0][0].equals(tablero[2][2]) && !tablero[0][0].equals("")) {
            return true;
        }

        if (tablero[0][2].equals(tablero[1][1]) && tablero[0][2].equals(tablero[2][0]) && !tablero[0][2].equals("")) {
            return true;
        }

        return false;
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void resetearTablero() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setText("");
            }
        }
        ronda = 0;
        turnoJugador1 = true;
        actualizarTextoTurno();
    }

    private void volverAlMenu() {
        Intent intent = new Intent(TresEnRayaActivity.this, MainActivity.class); //el Intent funciona indicando la actividad destino deseada
        startActivity(intent); //activa el intent creado
        finish();  // finaliza la actividad actual
    }

    private void guardarResultadoEnFirestore(String resultado) {
        if (user == null) return; //esto no debería de ocurrir

        String uid = user.getUid();
        String campo = resultado.equals("victoriaX") ? "victoriasX" :
                resultado.equals("victoriaO") ? "victoriasO" :
                        "empates"; //si ha sido victoriaX, entonces campo=victoriasX, etc.

        db.collection("users")
                .document(uid)
                .update("tresEnRaya." + campo, FieldValue.increment(1))
                .addOnFailureListener(e -> {
                    // Si falla (por ejemplo, porque no existe el documento), creamos uno nuevo
                    Map<String, Object> stats = new HashMap<>();
                    Map<String, Object> juego = new HashMap<>();
                    juego.put("victoriasX", resultado.equals("victoriaX") ? 1 : 0);
                    juego.put("victoriasO", resultado.equals("victoriasO") ? 1 : 0);
                    juego.put("empates", resultado.equals("empate") ? 1 : 0);
                    stats.put("tresEnRaya", juego);

                    db.collection("users").document(uid).set(stats);
                });
    }

}
