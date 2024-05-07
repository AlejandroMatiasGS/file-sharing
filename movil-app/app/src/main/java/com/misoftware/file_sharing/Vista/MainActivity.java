package com.misoftware.file_sharing.Vista;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.misoftware.file_sharing.Controlador.FileUtils;
import com.misoftware.file_sharing.Modelo.MyFile;
import com.misoftware.file_sharing.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<String[]> launcher;
    ActivityResultLauncher<Uri> launcher2;
    private ArrayList<MyFile> lstFiles;
    private ArrayList<Uri> lstUris;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lstFiles = new ArrayList<>();
        lstUris = new ArrayList<>();
        launcher = registerForActivityResult(new ActivityResultContracts.OpenMultipleDocuments(), this::procesarArchivos);
        //launcher2 = registerForActivityResult(new ActivityResultContracts.OpenDocumentTree(), this::procesarCarpeta);

        ((Button)findViewById(R.id.btnEnviar)).setOnClickListener(view -> {
            launcher.launch(new String[]{"*/*"});
        });

        ((Button)findViewById(R.id.btnRecibir)).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(intent, 100);
            //launcher2.launch(Uri.EMPTY);
        });

        if(getIntent().hasExtra("message")) {
            String message = getIntent().getStringExtra("message");
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                Intent intent = new Intent(this, InterfaceSelection.class);
                intent.putExtra("opc", 1);
                intent.putExtra("folder", uri);
                startActivity(intent);
            }
        }
    }

    private void procesarCarpeta(Uri uri) {
        if(uri != null) {
            Intent intent = new Intent(this, InterfaceSelection.class);
            intent.putExtra("opc", 1);
            intent.putExtra("folder", uri);
            startActivity(intent);
        }
    }

    private void procesarArchivos(List<Uri> lst) {
        for(Uri u: lst) {
            String name = FileUtils.getFileNameFromUri(getApplicationContext(), u);
            String path = u.getPath();
            long size = FileUtils.getFileSizeFromUri(getApplicationContext(), u);
            lstFiles.add(new MyFile(name, path, size));
            lstUris.add(u);
        }

        if(!lstFiles.isEmpty()) {
            Intent intent = new Intent(this, InterfaceSelection.class);
            intent.putExtra("opc", 0);
            intent.putExtra("lstFiles", lstFiles);
            intent.putExtra("lstUris", lstUris);
            startActivity(intent);
        }
    }
}

