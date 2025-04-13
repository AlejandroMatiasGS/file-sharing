package com.example.file.sharing.views;

import com.example.file.sharing.controllers.Converter;
import com.google.gson.Gson;
import com.example.file.sharing.controllers.Global;
import com.example.file.sharing.models.MiFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class SendingProcess extends javax.swing.JFrame {

    private boolean stop;
    private Thread thread;
    private Socket s;

    public SendingProcess(Socket s) {
        this.s = s;
        stop = false;
        setLocationRelativeTo(null);
        initComponents();
        init();
    }

    private void init() {
        thread = new Thread(this::send);
        thread.start();
    }

    private void send() {
        Global app = Global.getInstance();
        Gson gson = new Gson();
        List<MiFile> files = app.getFiles();

        OutputStream out = null;
        try {
            out = s.getOutputStream();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error de E/S: \n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long totalSize = files.stream().mapToLong(MiFile::getSize).sum();

        Map<String, Object> jsonObj = new HashMap<>();
        jsonObj.put("totalSize", totalSize);
        jsonObj.put("files", app.getFiles());

        byte[] jsonBytes = gson.toJson(jsonObj).getBytes();
        byte[] jsonSizeBytes = Converter.intToBytes(jsonBytes.length);

        if (stop) {
            return;
        }
        
        try {
            out.write(jsonSizeBytes);
            out.write(jsonBytes);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Hubo un error en el proceso de envío", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(SendingProcess.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        boolean fileError = false;
        long bytesSent = 0;
        for (MiFile f : files) {
            txtFile.setText("File: " + f.getName());

            try (
                    FileInputStream fis = new FileInputStream(f.getPath()); FileChannel fc = fis.getChannel();) {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                while (fc.read(buffer) != -1 && !stop) {
                    buffer.flip();
                    out.write(buffer.array(), 0, buffer.limit());
                    bytesSent += buffer.limit();
                    progressBar.setValue((int) ((bytesSent * 100) / totalSize));
                    buffer.clear();
                }
            } catch (Exception ex) {
                fileError = true;
                break;
            }

            if (stop) {
                break;
            }
        }

        if (stop) {
            return;
        }

        if (fileError) {
            JOptionPane.showMessageDialog(rootPane, "Hubo un error en el proceso de envío", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            out.close();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(rootPane, "Error de E/S: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JOptionPane.showMessageDialog(rootPane, "Archivo(s) Enviado(s) con éxito", "Información", JOptionPane.INFORMATION_MESSAGE);
        app.popUntil(Home.class);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        progressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        txtFile = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        btn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("File Sharing");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Console", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Progreso:");

        txtFile.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        txtFile.setText("File: ");

        cancelBtn.setFont(new java.awt.Font("Lucida Console", 0, 24)); // NOI18N
        cancelBtn.setText("Cancelar");
        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelBtnMouseClicked(evt);
            }
        });

        btn.setText("jButton1");
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                    .addComponent(txtFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(btn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelBtnMouseClicked
        this.stop = true;
        Global.getInstance().popUntil(Home.class);
    }//GEN-LAST:event_cancelBtnMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.stop = true;
        Global.getInstance().popUntil(Home.class);
    }//GEN-LAST:event_formWindowClosing

    private void btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnMouseClicked
        try {
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(SendingProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel txtFile;
    // End of variables declaration//GEN-END:variables
}
