package com.misoftware.file_sharing.Vista;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.misoftware.file_sharing.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

public class InterfaceSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_interface_selection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initComponents();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        finish();
    }

    private void initComponents() {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface interfaz = interfaces.nextElement();
                Enumeration<InetAddress> direcciones = interfaz.getInetAddresses();
                while (direcciones.hasMoreElements()) {
                    InetAddress direccion = direcciones.nextElement();
                    if (!direccion.isLoopbackAddress() && direccion.getAddress().length == 4) {

                        View conSel = inflater.inflate(R.layout.control_selection, null, false);
                        ImageView img = (ImageView)conSel.findViewById(R.id.lblImagen);
                        TextView txt = (TextView)conSel.findViewById(R.id.lblName);

                        img.setImageResource(R.drawable.interface_);
                        txt.setText(interfaz.getName());

                        conSel.setOnClickListener(view -> {
                            String ip = direccion.getHostAddress();

                            int opc = getIntent().getIntExtra("opc", -1);

                            if(opc == 0) {
                                Intent i = new Intent(this, ScannerHost.class);
                                i.putExtra("lstFiles", getIntent().getSerializableExtra("lstFiles"));
                                i.putExtra("lstUris", getIntent().getParcelableArrayListExtra("lstUris"));
                                i.putExtra("ip", ip);
                                startActivity(i);
                            }else if(opc == 1) {
                                Intent i = new Intent(this, WaitingAccept.class);
                                i.putExtra("ip", ip);
                                i.putExtra("folder", (Uri)getIntent().getParcelableExtra("folder"));
                                startActivity(i);
                            }
                        });

                        ViewGroup parent = findViewById(R.id.lstInterfaces);
                        parent.addView(conSel);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}