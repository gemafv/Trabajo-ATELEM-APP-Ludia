package com.example.miapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView diceImage;
    private Button rollButton;
    private Random random;
    private int[] diceImages = {
            R.drawable.dice_1,
            R.drawable.dice_2,
            R.drawable.dice_3,
            R.drawable.dice_4,
            R.drawable.dice_5,
            R.drawable.dice_6
    };

    private Handler handler = new Handler();
    private boolean isRolling = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        diceImage = findViewById(R.id.diceImage);
        rollButton = findViewById(R.id.rollButton);
        random = new Random();

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRolling) {
                    rollDice();
                }
            }
        });
    }

    private void rollDice() {
        isRolling = true;
        rollButton.setEnabled(false);

        // Animación de rotación (opcional)
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(1000);
        diceImage.startAnimation(rotate);

        // Cambiar caras durante 1 segundo
        final int duration = 1000; // tiempo total de "ruleta"
        final int interval = 100;  // cada cuánto cambiar (ms)
        final long startTime = System.currentTimeMillis();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed < duration) {
                    int randomFace = random.nextInt(6);
                    diceImage.setImageResource(diceImages[randomFace]);
                    handler.postDelayed(this, interval);
                } else {
                    // Al terminar, mostrar el resultado final
                    int result = random.nextInt(6); // 0–5
                    diceImage.setImageResource(diceImages[result]);
                    isRolling = false;
                    rollButton.setEnabled(true);
                }
            }
        });
    }
}
