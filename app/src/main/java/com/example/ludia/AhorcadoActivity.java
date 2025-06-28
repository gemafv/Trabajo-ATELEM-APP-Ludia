package com.example.ludia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AhorcadoActivity extends AppCompatActivity {

    private String[] bancoPalabras = {
            "GATO", "CASA", "ARBOL", "PERRO", "ESCUELA", "LIBRO", "MESA", "SILLA", "PUERTA", "VENTANA",
            "CAMINO", "CALLE", "COCHE", "JARDIN", "FLOR", "PLANTA", "CIELLO", "SOL", "LUNA", "ESTRELLA",
            "MAR", "RIO", "MONTAÑA", "PIEDRA", "TIERRA", "FOCO", "LUZ", "SOMBRA", "FUEGO", "AGUA",
            "PAN", "LECHE", "QUESO", "MANZANA", "PLATANO", "NARANJA", "UVA", "PERA", "SANDIA", "MELON",
            "CAMISA", "PANTALON", "ZAPATO", "CHAQUETA", "BUFANDA", "RELOJ", "BOLSO", "GAFAS", "PELOTA", "JUEGO",
            "CABALLO", "VACA", "OVEJA", "CERDO", "PATO", "GALLINA", "RATON", "LEON", "TIGRE", "ELEFANTE",
            "BICICLETA", "AVION", "BARCO", "TREN", "AUTOBUS", "MOTO", "MAPA", "CAJA", "BOLIGRAFO",
            "LAPIZ", "PAPEL", "LIBRETA", "MOCHILA", "CARPETA", "SOFA", "CAMA", "ARMARIO", "ESPEJO",
            "CUADRO", "TELEFONO", "ORDENADOR", "TABLET", "TELEVISION", "RADIO", "PARED", "TECHO", "SUELO", "ESCALERA",
            "LADRILLO", "COCINA", "HORNO", "FREGADERO", "NEVERA", "VENTILADOR", "BOMBA", "CABLE", "CLAVO", "MARTILLO",
            "DESTORNILLADOR", "TORNILLO", "TUERCA", "LLAVE", "ESCOBA", "TRAPO", "JABON", "DETERGENTE", "LAVADORA", "SECADORA",
            "HILO", "AGUJA", "TIJERA", "PEGAMENTO", "PINTURA", "BROCHA", "RODILLO", "ESCALERA", "TABURETE", "ALMOHADA",
            "MANTAS", "COLCHON", "SABANA", "CORTINA", "PERSIANA", "LAMINA", "CUCHARA", "TENEDOR", "CUCHILLO", "VASO",
            "TAZA", "PLATO", "BANDEJA", "SARTEN", "CAZUELA", "OLLAS", "COLADOR", "ABRIDOR", "SACACORCHOS", "TERMOMETRO",
            "REGLA", "COMPAS", "ESCALIMETRO", "GRAPADORA", "PERFORADORA", "TIJERAS", "BANDERA", "GLOBO", "MAPAMUNDI", "CALENDARIO",
            "RELOJ", "CRONOMETRO", "TIZA", "PIZARRA", "MOCHILA", "PORTATIL", "ROUTER", "TECLADO", "RATON", "MONITOR",
            "ANTENA", "BATERIA", "CARGADOR", "ALTAVOZ", "AURICULARES", "MICROFONO", "CABLE", "CONEXION", "INTERNET", "RED",
            "TINTA", "IMPRESORA", "ESCANER", "FAX", "ARCHIVO", "DOCUMENTO", "CARPETA", "SOBRE", "SELLO", "BUZON",
            "PINTOR", "ACTOR", "CANTANTE", "MUSICO", "POETA", "ESCRITOR", "DIRECTOR", "FOTOGRAFO", "DIBUJANTE", "ARQUITECTO",
            "LADRILLO", "COCINA", "HORNO", "FREGADERO", "NEVERA", "VENTILADOR", "BOMBA", "CABLE", "CLAVO", "MARTILLO",
            "DESTORNILLADOR", "TORNILLO", "TUERCA", "LLAVE", "ESCOBA", "TRAPO", "JABON", "DETERGENTE", "LAVADORA", "SECADORA",
            "HILO", "AGUJA", "TIJERA", "PEGAMENTO", "PINTURA", "BROCHA", "RODILLO", "ESCALERA", "TABURETE", "ALMOHADA",
            "MANTAS", "COLCHON", "SABANA", "CORTINA", "PERSIANA", "LAMINA", "CUCHARA", "TENEDOR", "CUCHILLO", "VASO",
            "TAZA", "PLATO", "BANDEJA", "SARTEN", "CAZUELA", "OLLAS", "COLADOR", "ABRIDOR", "SACACORCHOS", "TERMOMETRO",
            "REGLA", "COMPAS", "ESCALIMETRO", "GRAPADORA", "PERFORADORA", "TIJERAS", "BANDERA", "GLOBO", "MAPAMUNDI", "CALENDARIO",
            "RELOJ", "CRONOMETRO", "TIZA", "PIZARRA", "MOCHILA", "PORTATIL", "ROUTER", "TECLADO", "RATON", "MONITOR",
            "ANTENA", "BATERIA", "CARGADOR", "ALTAVOZ", "AURICULARES", "MICROFONO", "CABLE", "CONEXION", "INTERNET", "RED",
            "TINTA", "IMPRESORA", "ESCANER", "FAX", "ARCHIVO", "DOCUMENTO", "CARPETA", "SOBRE", "SELLO", "BUZON",
            "PINTOR", "ACTOR", "CANTANTE", "MUSICO", "POETA", "ESCRITOR", "DIRECTOR", "FOTOGRAFO", "DIBUJANTE", "ARQUITECTO"
    };

    private String[] palabras = new String[5];
    private int nivel = 0;
    private String palabraActual;
    private StringBuilder palabraOculta;
    private int errores;
    private boolean haPerdido = false;
    private Set<Character> letrasUsadasIncorrectas;

    private TextView palabraTextView, nivelTextView, letrasIncorrectasTextView;
    private EditText letraEditText;
    private ImageView ahorcadoImage;
    private Button probarButton, volverMenuButton;

    private FirebaseFirestore db;
    private FirebaseUser user;

    private int[] imagenesAhorcado = {
            R.drawable.ahorcado0, R.drawable.ahorcado1, R.drawable.ahorcado2,
            R.drawable.ahorcado3, R.drawable.ahorcado4, R.drawable.ahorcado5,
            R.drawable.ahorcado6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ahorcado_activity);

        // Firebase
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // UI
        palabraTextView = findViewById(R.id.palabraTextView);
        nivelTextView = findViewById(R.id.nivelTextView);
        letrasIncorrectasTextView = findViewById(R.id.letrasIncorrectasTextView);
        letraEditText = findViewById(R.id.letraEditText);
        ahorcadoImage = findViewById(R.id.ahorcadoImage);
        probarButton = findViewById(R.id.probarButton);
        volverMenuButton = findViewById(R.id.btnVolverMenu);

        volverMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(AhorcadoActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        seleccionarPalabrasAleatorias();
        iniciarNivel();

        probarButton.setOnClickListener(v -> probarLetra());
    }

    private void seleccionarPalabrasAleatorias() {
        List<String> listaPalabras = new ArrayList<>();
        Collections.addAll(listaPalabras, bancoPalabras);
        Collections.shuffle(listaPalabras);
        for (int i = 0; i < 5; i++) {
            palabras[i] = listaPalabras.get(i);
        }
    }

    private void iniciarNivel() {
        if (nivel >= palabras.length) {
            Toast.makeText(this, "¡Has completado todos los niveles!", Toast.LENGTH_LONG).show();
            probarButton.setEnabled(false);

            if (!haPerdido) {
                Toast.makeText(this, "¡Victoria Magistral!", Toast.LENGTH_LONG).show();
                guardarResultadoEnFirestore("magistral");
            }

            return;
        }

        palabraActual = palabras[nivel];
        palabraOculta = new StringBuilder();
        letrasUsadasIncorrectas = new HashSet<>();

        for (int i = 0; i < palabraActual.length(); i++) {
            palabraOculta.append("_");
        }

        errores = 0;
        actualizarUI();
    }

    private void probarLetra() {
        String letra = letraEditText.getText().toString().toUpperCase();
        letraEditText.setText("");

        if (letra.length() != 1) {
            Toast.makeText(this, "Introduce solo una letra", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean acierto = false;
        for (int i = 0; i < palabraActual.length(); i++) {
            if (palabraActual.charAt(i) == letra.charAt(0)) {
                palabraOculta.setCharAt(i, letra.charAt(0));
                acierto = true;
            }
        }

        if (!acierto) {
            if (!letrasUsadasIncorrectas.contains(letra.charAt(0))) {
                letrasUsadasIncorrectas.add(letra.charAt(0));
                errores++;
            }

            if (errores >= imagenesAhorcado.length - 1) {
                ahorcadoImage.setImageResource(imagenesAhorcado[imagenesAhorcado.length - 1]);
                Toast.makeText(this, "¡Has perdido! La palabra era: " + palabraActual, Toast.LENGTH_LONG).show();
                haPerdido = true;
                guardarResultadoEnFirestore("derrota");
                nivel++;
                iniciarNivel();
                return;
            }
        }

        if (palabraOculta.toString().equals(palabraActual)) {
            Toast.makeText(this, "¡Bien hecho!", Toast.LENGTH_SHORT).show();
            guardarResultadoEnFirestore("victoria");
            nivel++;
            iniciarNivel();
        } else {
            actualizarUI();
        }
    }

    private void actualizarUI() {
        StringBuilder palabraFormateada = new StringBuilder();
        for (int i = 0; i < palabraOculta.length(); i++) {
            palabraFormateada.append(palabraOculta.charAt(i)).append(" ");
        }

        palabraTextView.setText(palabraFormateada.toString().trim());
        nivelTextView.setText("Nivel: " + (nivel + 1));
        ahorcadoImage.setImageResource(imagenesAhorcado[errores]);

        StringBuilder letrasIncorrectas = new StringBuilder("Letras incorrectas: ");
        for (char c : letrasUsadasIncorrectas) {
            letrasIncorrectas.append(c).append(" ");
        }
        letrasIncorrectasTextView.setText(letrasIncorrectas.toString().trim());
    }

    private void guardarResultadoEnFirestore(String resultado) {
        if (user == null) return;

        String uid = user.getUid();
        String campo;
        switch (resultado) {
            case "victoria":
                campo = "victorias";
                break;
            case "derrota":
                campo = "derrotas";
                break;
            case "magistral":
                campo = "victoriasMagistrales";
                break;
            default:
                campo = "";
                break;
        }


        if (campo.isEmpty()) return;

        db.collection("users")
                .document(uid)
                .update("ahorcado." + campo, FieldValue.increment(1))
                .addOnFailureListener(e -> {
                    Map<String, Object> stats = new HashMap<>();
                    Map<String, Object> juego = new HashMap<>();
                    juego.put("victorias", resultado.equals("victoria") ? 1 : 0);
                    juego.put("derrotas", resultado.equals("derrota") ? 1 : 0);
                    juego.put("victoriasMagistrales", resultado.equals("magistral") ? 1 : 0);
                    stats.put("ahorcado", juego);

                    db.collection("users").document(uid).set(stats);
                });
    }
}
