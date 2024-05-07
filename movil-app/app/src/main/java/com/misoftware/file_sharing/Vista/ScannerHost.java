package com.misoftware.file_sharing.Vista;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.misoftware.file_sharing.Controlador.Host;
import com.misoftware.file_sharing.Modelo.HostMessage;
import com.misoftware.file_sharing.Modelo.MyFile;
import com.misoftware.file_sharing.R;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ScannerHost extends AppCompatActivity {
    private volatile boolean flagStop;
    private String ipAddress;
    private ArrayList<Host> hosts;
    private Thread hiloBusqueda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scanner_host);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                flagStop = true;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        this.ipAddress = getIntent().getStringExtra("ip");

        flagStop = false;
        hosts = new ArrayList<>();

        Button btnBuscar = ((Button)findViewById(R.id.btnBuscar));
        btnBuscar.setOnClickListener(view -> {
            flagStop = false;
            ((LinearLayout)findViewById(R.id.lstHosts)).removeAllViews();
            btnBuscar.setText("Buscando...");
            btnBuscar.setEnabled(false);
            hiloBusqueda = new Thread(() -> buscarHosts());
            hiloBusqueda.start();
        });

        btnBuscar.setEnabled(false);
        btnBuscar.setText("Buscando...");
        hiloBusqueda = new Thread(() -> buscarHosts());
        hiloBusqueda.start();
    }

    @Override
    protected void onUserLeaveHint() {
        flagStop = true;
        super.onUserLeaveHint();
        finish();
    }

    private void buscarHosts() {
        String[] _ip = this.ipAddress.split("\\.");
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        for(int i=2; i<255 && !flagStop; i++) {
            String ip = _ip[0] + "." + _ip[1] + "." + _ip[2] + "." + i;

            Host h = new Host();
            if(h.conectar(ip, 7777, 100)) {
                h.setSoTimeout(7000);
                h.enviar(HostMessage.WAIT, 0, 1);
                h.cerrar();

                View conSel = inflater.inflate(R.layout.control_selection, null, false);
                ImageView img = (ImageView)conSel.findViewById(R.id.lblImagen);
                TextView txt = (TextView)conSel.findViewById(R.id.lblName);

                img.setImageResource(R.drawable.device);

                String hostName = null;

                try {
                    InetAddress inetAddress = InetAddress.getByName(ip);
                    hostName = inetAddress.getHostName();
                } catch (Exception e) { }

                txt.setText((hostName != null) ? hostName : ip);

                conSel.setOnClickListener(view -> {
                    flagStop = true;
                    Intent intent = new Intent(this, SendingProcess.class);
                    intent.putExtra("ip", ip);
                    intent.putExtra("lstFiles", getIntent().getSerializableExtra("lstFiles"));
                    intent.putExtra("lstUris", getIntent().getParcelableArrayListExtra("lstUris"));
                    startActivity(intent);
                });

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup parent = findViewById(R.id.lstHosts);
                        parent.addView(conSel);
                    }
                });
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((Button)findViewById(R.id.btnBuscar)).setText("Buscar");
                ((Button)findViewById(R.id.btnBuscar)).setEnabled(true);
            }
        });
    }
}