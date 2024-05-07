package com.misoftware.file_sharing.Vista;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.misoftware.file_sharing.Controlador.Host;
import com.misoftware.file_sharing.Modelo.HostMessage;
import com.misoftware.file_sharing.Modelo.MyFile;
import com.misoftware.file_sharing.R;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SendingProcess extends AppCompatActivity {
    private ArrayList<MyFile> lstFiles;
    private ArrayList<Uri> lstUris;
    private String ip;
    private double proBar;
    private Host h;
    private volatile boolean flagStop;
    private boolean flagError;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sending_process);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                builder.setMessage("¿Estás seguro de que deseas salir. Se cancelara el envío?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                flagStop = true;
                                dialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        proBar = 0;
        flagStop = false;
        flagError = false;

        Intent _i = getIntent();

        lstFiles = (ArrayList<MyFile>)_i.getSerializableExtra("lstFiles");
        lstUris = _i.getParcelableArrayListExtra("lstUris");
        ip = _i.getStringExtra("ip");

        this.h = new Host();

        ((Button)findViewById(R.id.btnCancelarSP)).setOnClickListener(view -> {
            flagStop = true;
            this.h.cerrar();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        Thread hiloSend = new Thread(this::sendProcess);
        hiloSend.start();
    }

    private void sendProcess() {
        Gson gson = new Gson();
        byte[] fileCount = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(lstFiles.size()).array();

        if(this.h.conectar(ip, 7777, 1000)) {
            this.h.setSoTimeout(7000);
            if(this.h.enviar(HostMessage.PROCEED, 0, 1)) {
                if(this.h.enviar(fileCount, 0, fileCount.length)) {
                    for(int i=0; i<lstFiles.size() && !flagStop; i++) {
                        String json = gson.toJson(lstFiles.get(i));
                        byte[] _json;
                        byte[] _jsonL;

                        try {
                            _json = json.getBytes(StandardCharsets.UTF_8);
                            _jsonL = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(_json.length).array();
                        }catch (Exception e) {
                            flagError = true;
                            break;
                        }

                        if(this.h.enviar(_jsonL, 0, _jsonL.length) && this.h.enviar(_json, 0, _json.length)) {
                            byte[] opc = new byte[1];

                            if(this.h.recibir(opc) > 0) {
                                if(opc[0] == 1) {
                                    int bytesRead;
                                    byte[] buffer = new byte[65535];

                                    try {
                                        ContentResolver cr = this.getContentResolver();
                                        InputStream in = cr.openInputStream(lstUris.get(i));

                                        while((bytesRead = in.read(buffer, 0, buffer.length)) > 0 && !flagStop) {
                                            if(this.h.enviar(buffer, 0 ,bytesRead)) {
                                                proBar += ((double)bytesRead * (100 / lstFiles.size()) / lstFiles.get(i).getSize());

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ((ProgressBar)findViewById(R.id.progressBarSP)).setProgress((int)Math.round(proBar));
                                                    }
                                                });
                                            }else { flagError = true; break;}
                                        }
                                    }catch (Exception e) { flagError = true; }
                                }
                            }else { flagError = true; }
                        }else { flagError = true; }

                        if(flagError) break;
                    }
                }else { flagError = true; }
            }else { flagError = true; }
        }else { flagError = true; }

        if(flagError) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("message", "Hubo un error en el proceso de envío.");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if(!flagStop && !flagError) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("message", "Enviado con éxito.");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        flagStop = true;
    }

    @Override
    protected void onUserLeaveHint() {
        flagStop = true;
        super.onUserLeaveHint();
        finish();
    }
}