package com.misoftware.file_sharing.Vista;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.misoftware.file_sharing.Controlador.Host;
import com.misoftware.file_sharing.Controlador.MyApplication;
import com.misoftware.file_sharing.Modelo.HostMessage;
import com.misoftware.file_sharing.R;

public class WaitingAccept extends AppCompatActivity {
    private volatile boolean flagStop;
    private Host host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_waiting_accept);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        flagStop = false;
        String ip = getIntent().getStringExtra("ip");
        host = new Host(ip, 7777);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                builder.setMessage("¿Estás seguro de que deseas salir?. Se cancelará la espera de una conexión.")
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

        ((Button)findViewById(R.id.btnCancelarWA)).setOnClickListener(view -> {
            flagStop = true;
            host.cerrarServer();
        });

        Thread hilo = new Thread(this::AcceptProcess);
        hilo.start();
    }

    private void AcceptProcess() {
        final byte[] flag = new byte[] { 0 };

        while(!flagStop) {
            if(this.host.aceptar()) {
                byte[] opc = new byte[1];
                this.host.recibir(opc);
                if(opc[0] == HostMessage.PROCEED[0]) {
                    this.host.setSoTimeout(7000);
                    ((MyApplication)getApplication()).setHost(this.host);
                    flag[0] = 1;
                    break;
                }else {
                    this.host.cerrar();
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(flag[0] == 1) {
                    Intent i = new Intent(getApplicationContext(), ReceivingProcess.class);
                    i.putExtra("folder", (Uri)getIntent().getParcelableExtra("folder"));
                    startActivity(i);
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onUserLeaveHint() {
        flagStop = true;
        super.onUserLeaveHint();
        finish();
    }
}