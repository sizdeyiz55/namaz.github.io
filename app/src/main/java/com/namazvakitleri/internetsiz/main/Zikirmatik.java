package com.namazvakitleri.internetsiz.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.namazvakitleri.internetsiz.R;

public class Zikirmatik extends AppCompatActivity {

    InterstitialAd mInterstitialAd = null;


    Button zikirButton,resetButton,buttonHedef;
    TextView zikirText,hedefler;
    Vibrator vibrator;
    ImageView imageSound,imageVibration,imageColor,zikirView,trueView,falseView,imageHedef;
    EditText hedefText;
    SharedPreferences sharedPreferenceszikir;
    int numbZikir;
    int textNumber = 0;
    int storedZikir;
    int currentSoundImage;
    int currentVibImage;
    int currentColorImage;
    int currentChangeColor;
    int[] soundImages = {R.drawable.soundon,R.drawable.soundoff};
    int[] vibImages = {R.drawable.vibon,R.drawable.viboff};
    int[] zikirColorImages = {R.drawable.zikirbordo,R.drawable.zikirpembe,R.drawable.zikirsari,R.drawable.zikirturuncu,R.drawable.zikiryesil,R.drawable.zikir};
    int[] changeColor = {R.drawable.coloricon,R.drawable.changecolor};
    int sesSayac = 1;
    int vibSayac = 1;

    private void createNewAd(){
        InterstitialAd.load(this, getString(R.string.interstitial_ad_unit_id), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;

            }

            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd =  interstitialAd;
            }

        });

        if(mInterstitialAd != null){
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                    mInterstitialAd = null;
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zikirmatik);

        createNewAd();

        //mInterstitialAd.setAdUnitId("ca-app-pub-3582614483058227/4810581548");



//        mInterstitialAd.loadAd(adRequest);
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//            }
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                // Proceed to the next level.
//                mInterstitialAd.loadAd(adRequest);
//
//            }
//        });




        sharedPreferenceszikir = this.getSharedPreferences("com.example.zikirmatik", Context.MODE_PRIVATE);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        imageHedef = findViewById(R.id.imageHedef);
        buttonHedef = findViewById(R.id.buttonHedef);
        hedefler = findViewById(R.id.hedefler);
        trueView = findViewById(R.id.trueView);
        falseView = findViewById(R.id.falseView);
        hedefText = findViewById(R.id.hedefText);
        zikirButton = findViewById(R.id.buttonZikir);
        resetButton = findViewById(R.id.buttonReset);
        zikirText = findViewById(R.id.zikirText);
        imageSound = findViewById(R.id.imageSound);
        imageVibration = findViewById(R.id.imageVibration);
        imageColor = findViewById(R.id.imageColor);
        zikirView = findViewById(R.id.imageView);

        storedZikir = sharedPreferenceszikir.getInt("zikir", numbZikir);
        zikirText.setText(String.valueOf(storedZikir));

    }

    public void zikir(View view) {
        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.bounce);
        final MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.ses);
        storedZikir += 1;

        if(storedZikir%50 == 0){
            if (mInterstitialAd != null)
                mInterstitialAd.show(this);
            Toast.makeText(com.namazvakitleri.internetsiz.main.Zikirmatik.this, "50", Toast.LENGTH_SHORT).show();
            recreate();


        }

        if (vibSayac == 1){
            vibrator.vibrate(100);
        }
       if (sesSayac == 1){
            mediaPlayer.start();
        }
       if (textNumber != 0 ){
           zikirText.setText(String.valueOf(storedZikir));
           numbZikir = storedZikir;
           sharedPreferenceszikir.edit().putInt("zikir", numbZikir).apply();
           if (textNumber == storedZikir){
               vibrator.vibrate(1500);
               AlertDialog.Builder alert = new AlertDialog.Builder(this);
               alert.setTitle("Hedefe Ulaştınız");
               alert.setMessage("Hedefe Ulaştınız zikirlerinizi sıfırlamak ister misiniz?");
               alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       zikirText.setText(String.valueOf(0));
                       storedZikir = 0;
                       numbZikir = 0;
                       sharedPreferenceszikir.edit().putInt("zikir", numbZikir).apply();
                       hedefler.setText("");
                       textNumber = 0;
                   }
               });
               alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Toast.makeText(com.namazvakitleri.internetsiz.main.Zikirmatik.this, "Zikirlerinize kaldığınız yerden devam edebilirsiniz. Hedefiniz sıfırlandı.", Toast.LENGTH_SHORT).show();
                       textNumber = 0;
                       hedefler.setText("");
                   }
               });
               alert.create().show();
           }
       } else {
           zikirText.setText(String.valueOf(storedZikir));
           numbZikir = storedZikir;
           sharedPreferenceszikir.edit().putInt("zikir", numbZikir).apply();
       }
        zikirButton.startAnimation(animation);
    }

    public void reset(View view) {
        final Animation animation = AnimationUtils.loadAnimation(this,R.anim.bounce);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        resetButton.startAnimation(animation);
        alert.setTitle("Sıfırla");
        alert.setMessage("Zikirlerinizini sıfırlamak istediğinize emin misiniz?");
        alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                zikirText.setText(String.valueOf(0));
                storedZikir = 0;
                numbZikir = 0;
                sharedPreferenceszikir.edit().putInt("zikir", numbZikir).apply();
                hedefler.setText("");
            }
        });
        alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(com.namazvakitleri.internetsiz.main.Zikirmatik.this,"Zikirleriniz sıfırlanmadı!!", Toast.LENGTH_LONG).show();
            }
        });
        alert.create().show();
    }

    public void buttonVibration(View view) {
        currentVibImage++;
        currentVibImage = currentVibImage % vibImages.length;
        imageVibration.setImageResource(vibImages[currentVibImage]);
        if (vibImages[currentVibImage] == R.drawable.vibon){
            vibSayac = 1;
        } else if (vibImages[currentVibImage] == R.drawable.viboff){
            vibSayac = 0;
        }

    }

    public void buttonSound(View view) {
        currentSoundImage++;
        currentSoundImage = currentSoundImage % soundImages.length;
        imageSound.setImageResource(soundImages[currentSoundImage]);
        if (soundImages[currentSoundImage] == R.drawable.soundon){
            sesSayac = 1;
        } else if (soundImages[currentSoundImage] == R.drawable.soundoff){
            sesSayac = 0;
        }
    }

    public void buttonColor(View view) {
        currentChangeColor++;
        currentColorImage++;
        currentChangeColor = currentChangeColor % changeColor.length;
        currentColorImage = currentColorImage % zikirColorImages.length;
        zikirView.setImageResource(zikirColorImages[currentColorImage]);
        imageColor.setImageResource(changeColor[currentChangeColor]);
    }

    public void buttonHedef(View view) {
        hedefText.setVisibility(View.VISIBLE);
        trueView.setVisibility(View.VISIBLE);
        falseView.setVisibility(View.VISIBLE);
        buttonHedef.setVisibility(View.INVISIBLE);
        imageHedef.setVisibility(View.INVISIBLE);
    }

    public void trueOnClick(View view) {
        if (hedefText.length() == 0 || hedefText.equals("") || hedefText == null){
            Toast.makeText(com.namazvakitleri.internetsiz.main.Zikirmatik.this,"Bu alan boş olamaz. Çıkmak için çarpıya basınız.", Toast.LENGTH_LONG).show();
        } else if (hedefText.length() > 4){
            Toast.makeText(com.namazvakitleri.internetsiz.main.Zikirmatik.this,"Lütfen maksimum 4 haneli sayı giriniz.", Toast.LENGTH_LONG).show();
        } else {

            if (Integer.parseInt(String.valueOf(hedefText.getText())) <= storedZikir) {
                Toast.makeText(com.namazvakitleri.internetsiz.main.Zikirmatik.this,"Girdiğiniz sayı mevcut zikirlerinizden fazla olduğu için, mevcut zikirleriniz sıfırlandı. ", Toast.LENGTH_LONG).show();
                zikirText.setText(String.valueOf(0));
                storedZikir = 0;
                numbZikir = 0;
                sharedPreferenceszikir.edit().putInt("zikir", numbZikir).apply();
                hedefler.setText("Hedef: " + hedefText.getText());
                textNumber = Integer.parseInt(String.valueOf(hedefText.getText()));
            } else if(storedZikir == 0) {
                hedefler.setText("Hedef: " + hedefText.getText());
                textNumber = Integer.parseInt(String.valueOf(hedefText.getText()));
            } else {

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Sıfırla");
                alert.setMessage("Mevcut zikirlerinizin silinmesini ister misiniz?");
                alert.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        zikirText.setText(String.valueOf(0));
                        storedZikir = 0;
                        numbZikir = 0;
                        sharedPreferenceszikir.edit().putInt("zikir", numbZikir).apply();
                        hedefler.setText("Hedef: " + hedefText.getText());
                        textNumber = Integer.parseInt(String.valueOf(hedefText.getText()));
                    }
                });
                alert.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hedefler.setText("Hedef: " + hedefText.getText());
                        textNumber = Integer.parseInt(String.valueOf(hedefText.getText()));
                    }
                });
                alert.create().show();
            }
            hedefText.setVisibility(View.INVISIBLE);
            trueView.setVisibility(View.INVISIBLE);
            falseView.setVisibility(View.INVISIBLE);
            buttonHedef.setVisibility(View.VISIBLE);
            imageHedef.setVisibility(View.VISIBLE);
        }
    }
    public void falseOnClick(View view) {
        hedefText.setVisibility(View.INVISIBLE);
        trueView.setVisibility(View.INVISIBLE);
        falseView.setVisibility(View.INVISIBLE);
        buttonHedef.setVisibility(View.VISIBLE);
        imageHedef.setVisibility(View.VISIBLE);
    }
}
