package com.example.ludia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class TirarDadoActivity extends AppCompatActivity {

    private ImageView imageDado;
    private Button btnTirarDado;
    private Button btnVolverMenu;
    private Random random;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tirar_dado);

        imageDado = findViewById(R.id.imageDado);
        btnTirarDado = findViewById(R.id.btnTirarDado);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);

        random = new Random();

        btnTirarDado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarDadoConAnimacion();
            }
        });

        btnVolverMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TirarDadoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void tirarDadoConAnimacion() {
        // Crear una animación de rotación
        RotateAnimation rotate = new RotateAnimation(
                0, 360 * 2, // Dos vueltas completas
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivote en el centro X
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivote en el centro Y
        );
        rotate.setDuration(500); // medio segundo
        rotate.setFillAfter(false); // No mantiene el estado final

        // Al terminar la animación, cambia la imagen
        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                btnTirarDado.setEnabled(false); // Desactiva el botón durante la animación
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                int resultado = random.nextInt(6) + 1;
                mostrarImagenDado(resultado);
                btnTirarDado.setEnabled(true); // Reactiva el botón
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        imageDado.startAnimation(rotate);
    }

    private void mostrarImagenDado(int numero) {
        switch (numero) {
            case 1:
                imageDado.setImageResource(R.drawable.dado_1);
                break;
            case 2:
                imageDado.setImageResource(R.drawable.dado_2);
                break;
            case 3:
                imageDado.setImageResource(R.drawable.dado_3);
                break;
            case 4:
                imageDado.setImageResource(R.drawable.dado_4);
                break;
            case 5:
                imageDado.setImageResource(R.drawable.dado_5);
                break;
            case 6:
                imageDado.setImageResource(R.drawable.dado_6);
                break;
        }
    }
}
