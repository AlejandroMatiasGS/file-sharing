package com.example.file.sharing.views;

import com.example.file.sharing.controllers.Global;
import com.example.file.sharing.models.MiNetworkInterface;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Home extends javax.swing.JFrame {

    public Home() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        receiveBtn = new javax.swing.JButton();
        sendBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("File Sharing");

        receiveBtn.setFont(new java.awt.Font("Lucida Console", 0, 36)); // NOI18N
        receiveBtn.setText("Recibir");
        receiveBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                receiveBtnMouseClicked(evt);
            }
        });

        sendBtn.setFont(new java.awt.Font("Lucida Console", 0, 36)); // NOI18N
        sendBtn.setText("Enviar");
        sendBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(receiveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(receiveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void receiveBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_receiveBtnMouseClicked
        String downloadPath = getDownloadPath();
        if(downloadPath == null) return; 
        Global.getInstance().setDownloadPath(downloadPath); 
        
        List<MiNetworkInterface> nets = getInterfaces();
        if (nets == null) { 
            JOptionPane.showMessageDialog(rootPane, "Hubo un error al obtener las interfaces de red", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        InterfaceSelector is = new InterfaceSelector(nets);
        Global.getInstance().push(is);
        is.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_receiveBtnMouseClicked

    private List<MiNetworkInterface> getInterfaces() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            List<MiNetworkInterface> nets = new ArrayList<MiNetworkInterface>();

            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                
                if(!ni.isUp()) continue;

                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = addresses.nextElement();
                    if (ip instanceof Inet4Address && !ip.isLoopbackAddress()) {
                        nets.add(new MiNetworkInterface(ni.getName(), ip.getHostAddress()));
                        break;
                    }
                }
            }

            return nets;
        } catch (Exception e) {
            return null;
        }
    }
    
    private void sendBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendBtnMouseClicked
        FilesSelector fs = new FilesSelector();
        Global.getInstance().push(fs);
        this.setVisible(false);
        fs.setVisible(true);
    }//GEN-LAST:event_sendBtnMouseClicked

    private String getDownloadPath() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int seleccion = chooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    public static void main(String args[]) {
        try {
            // Detectar el sistema operativo
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("win")) {
                // Si es Windows, usa el Look and Feel nativo de Windows
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } else {
                // En otros sistemas, usa Nimbus
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Home h = new Home();
                Global.getInstance().push(h);
                h.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton receiveBtn;
    private javax.swing.JButton sendBtn;
    // End of variables declaration//GEN-END:variables
}
