package com.misoftware.file_sharing.Vista;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.YuvImage;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.os.ParcelFileDescriptor;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.documentfile.provider.DocumentFile;

import com.google.gson.Gson;
import com.misoftware.file_sharing.Controlador.Host;
import com.misoftware.file_sharing.Controlador.MyApplication;
import com.misoftware.file_sharing.Modelo.HostMessage;
import com.misoftware.file_sharing.Modelo.MyFile;
import com.misoftware.file_sharing.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class ReceivingProcess extends AppCompatActivity {
    private volatile boolean flagStop;
    private Host host;
    private double proBar;
    private Uri folder;
    private final byte[] opc = new byte[] { -1 };
    private Thread hilo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_receiving_process);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        flagStop = false;
        folder = (Uri)getIntent().getParcelableExtra("folder");
        host = ((MyApplication)getApplication()).getHost();
        proBar = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                builder.setMessage("¿Estás seguro de que deseas salir?. Se cancelará el recibimiento.")
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

        ((Button)findViewById(R.id.btnCancelarRP)).setOnClickListener(view -> {
            flagStop = true;
            this.host.cerrar();
            this.host.cerrarServer();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        hilo = new Thread(this::receivingProcess);
        hilo.start();
    }


    private void receivingProcess() {
        Gson gson = new Gson();
        byte[] fileCount_ = new byte[4];
        int result;
        int filesReceived = 0;

        if((result = this.host.recibir(fileCount_)) > 0) {
            int fileCount = 0;

            try {
                ByteBuffer buffer = ByteBuffer.wrap(fileCount_);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                fileCount = buffer.getInt();
            }catch (Exception e) {
                e.printStackTrace();
                result = -2;
            }

            for(int i=0; i<fileCount && !flagStop; i++) {
                byte[] jsonL_ = new byte[4];

                if((result = this.host.recibir(jsonL_)) > 0) {
                    int jsonL;
                    byte[] json_;

                    try {
                        ByteBuffer buffer = ByteBuffer.wrap(jsonL_);
                        buffer.order(ByteOrder.LITTLE_ENDIAN);
                        jsonL = buffer.getInt();
                    }catch (Exception e) {
                        e.printStackTrace();
                        result = -2;
                        break;
                    }

                    json_ = new byte[jsonL];
                    if((result = this.host.recibir(json_)) > 0) {
                        String json;

                        try {
                            json = new String(json_, StandardCharsets.UTF_8);
                        }catch (Exception e) {
                            e.printStackTrace();
                            result = -2;
                            break;
                        }

                        MyFile file = gson.fromJson(json, MyFile.class);

                        if(file != null) {

                            DocumentFile dFile = DocumentFile.fromTreeUri(getApplicationContext(), folder);
                            DocumentFile file_;
                            DocumentFile fileE = null;

                            if (dFile != null && dFile.exists() && dFile.isDirectory()) {
                                DocumentFile[] archivos = dFile.listFiles();

                                boolean exists = false;
                                for (DocumentFile archivo : archivos) {
                                    if (archivo.getName().equals(file.getName())) {
                                        fileE = archivo;
                                        exists = true;
                                        break;
                                    }
                                }

                                if (exists) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ReceivingProcess.this);
                                            builder.setCancelable(false);

                                            builder.setMessage("¿Desea sobrescribir el archivo " + file.getName() + "?")
                                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            opc[0] = 0;
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            opc[0] = 1;
                                                            dialog.dismiss();
                                                        }
                                                    });

                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    });

                                    while(true) {
                                        if(opc[0] != -1) break;
                                    }

                                    if(opc[0] == 1) continue;
                                    else {
                                        file_ = fileE;
                                    }
                                }else {
                                    file_ = dFile.createFile("application/octet-stream", file.getName());
                                }

                                if(this.host.enviar(HostMessage.PROCEED, 0, 1)) {
                                    try {
                                        OutputStream os = getContentResolver().openOutputStream(file_.getUri(), "rwt");

                                        byte[] buffer = new byte[65535];
                                        int flagTamanoFile = 0;
                                        int bytesRead = 0;
                                        boolean flagBuffer = false;

                                        while (!(flagTamanoFile == file.getSize()) && (bytesRead = this.host.recibir(buffer)) > 0 && !flagStop) {
                                            os.write(buffer, 0, bytesRead);
                                            flagTamanoFile += bytesRead;

                                            proBar += ((double)bytesRead * (100 / fileCount) / file.getSize());

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ((ProgressBar)findViewById(R.id.progressBarRP)).setProgress((int)Math.round(proBar));
                                                }
                                            });

                                            if (flagBuffer) {
                                                buffer = new byte[65535];
                                                flagBuffer = false;
                                            }

                                            if (!(flagTamanoFile == file.getSize()) && (flagTamanoFile + 65535) > file.getSize()) {
                                                buffer = new byte[(int)(file.getSize() - flagTamanoFile)];
                                                flagBuffer = true;
                                            }
                                        }

                                        if (flagTamanoFile == file.getSize()) filesReceived++;

                                        os.close();

                                        if (bytesRead == -1) {
                                            result = -1;
                                            file_.delete();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        result = -2;
                                        break;
                                    }
                                }else { result = -1; break; }
                            }else { result = -2; break; }
                        }else { result = -2; break; }
                    } else {break;}
                }else {break;}
            }
        }

        if(result == -1)  {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("message", "Error de conexión.");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if(result == -2) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("message", "Error interno.");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        if(!flagStop && result >= 0 && filesReceived>0) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("message", "Recibido con éxito.");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onUserLeaveHint() {
        flagStop = true;
        opc[0] = -2;
        super.onUserLeaveHint();
        finish();
    }
}