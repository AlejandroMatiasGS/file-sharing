package com.example.file.sharing.views;

import com.example.file.sharing.controllers.Converter;
import com.example.file.sharing.controllers.Global;
import com.example.file.sharing.models.FilesResponse;
import com.example.file.sharing.models.MiFile;
import com.example.file.sharing.models.MiNetworkInterface;
import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JOptionPane;

public class ReceivingProcess extends javax.swing.JFrame {

    private final MiNetworkInterface ni;
    private boolean stop;
    private Thread thread;

    public ReceivingProcess(MiNetworkInterface ni) {
        this.ni = ni;
        stop = false;
        initComponents();
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        txtIp.setText(ni.getIp());
        thread = new Thread(this::receive);
        thread.start();
    }

    private void receive() {
        Socket s = null;
        try (ServerSocket ss = new ServerSocket(7777)) {
            ss.setSoTimeout(1000);

            while (!stop) {
                try {
                    s = ss.accept();
                    //s.setSoTimeout(2000);
                    break;
                } catch (SocketTimeoutException ex) {  }
            }

            if (stop) {
                return;
            }

            InputStream is = s.getInputStream();

            byte[] jsonSizeBytes = new byte[4];
            byte[] jsonBytes = null;

            is.read(jsonSizeBytes);
            int jsonSize = Converter.bytesToInt(jsonSizeBytes);
            jsonBytes = new byte[jsonSize];
            is.read(jsonBytes);

            if (stop) {
                return;
            }

            String json = new String(jsonBytes, StandardCharsets.UTF_8);
            Gson gson = new Gson();

            FilesResponse fRes = gson.fromJson(json, FilesResponse.class);
            List<MiFile> files = fRes.getFiles();

            long totalBytesRead = 0;
            int filesRead = 0;
            for (MiFile f : files) {
                if (stop) {
                    break;
                }
                txtFile.setText("File: " + f.getName());
                File _file = new File(Paths.get(Global.getInstance().getDownloadPath(), f.getName()).toString());

                if (_file.isFile() && _file.exists()) {
                    boolean deleted = _file.delete();
                    if (!deleted) {
                        s.close();
                        ss.close();
                        throw new Exception("Hubo un error en el proceso. se cancela el envío");
                    }
                }

                if (stop) {
                    break;
                }

                byte[] buffer = new byte[1024];
                int bytesRead = 0;

                FileOutputStream fos = new FileOutputStream(_file);
                while (bytesRead < f.getSize() && !stop) {
                    int bytesToRead = (int) Math.min(buffer.length, f.getSize() - bytesRead);
                    int read = is.read(buffer, 0, bytesToRead);

                    if (read == -1) {
                        break;
                    }

                    fos.write(buffer, 0, read);
                    bytesRead += read;
                    totalBytesRead += read;
                    progressBar.setValue((int) ((totalBytesRead * 100) / fRes.getTotalSize()));
                }

                fos.flush();
                filesRead += 1;
            }

            if (filesRead > 0) {
                JOptionPane.showMessageDialog(rootPane, "Se recibieron archivos", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            s.close();
            Global.getInstance().popUntil(Home.class);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(rootPane, "Hubo un error en el proceso: \n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            try {
                s.close();
            } catch (Exception e) {
            }
        }
    }

    private long descartarBytes(InputStream is, long totalBytes) throws IOException {
        byte[] buffer = new byte[1024];
        long bytesRead = 0;

        while (bytesRead < totalBytes) {
            long left = totalBytes - bytesRead;
            int tamañoLeída = is.read(buffer, 0, (int) Math.min(buffer.length, left));

            if (tamañoLeída == -1) {
                break; // Fin del stream
            }
            bytesRead += tamañoLeída;
        }

        return bytesRead;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtIp = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        txtFile = new javax.swing.JLabel();
        cancelBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("File Sharing");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtIp.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        txtIp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtIp.setText("X.X.X.X");

        jLabel2.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("<html><center>Ingresa esta valor</center> <br> en la aplicación de envío</html>");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtIp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtFile.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        txtFile.setText("File: ");

        cancelBtn.setFont(new java.awt.Font("Lucida Console", 0, 24)); // NOI18N
        cancelBtn.setText("Cancelar");
        cancelBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelBtnMouseClicked(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Lucida Console", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Progreso:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                    .addComponent(txtFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelBtn))
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.stop = true;
        Global.getInstance().popUntil(Home.class);
    }//GEN-LAST:event_formWindowClosing

    private void cancelBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cancelBtnMouseClicked
        this.stop = true;
        Global.getInstance().popUntil(Home.class);
    }//GEN-LAST:event_cancelBtnMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel txtFile;
    private javax.swing.JLabel txtIp;
    // End of variables declaration//GEN-END:variables
}
