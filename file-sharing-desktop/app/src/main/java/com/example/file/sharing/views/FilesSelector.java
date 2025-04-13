    package com.example.file.sharing.views;

import com.example.file.sharing.controllers.Global;
import com.example.file.sharing.models.MiFile;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class FilesSelector extends javax.swing.JFrame {

    public FilesSelector() {
        initComponents();
        setResizable(false);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        panelFiles.setLayout(new BoxLayout(panelFiles, BoxLayout.Y_AXIS));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private List<MiFile> selectFiles() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.setDialogTitle("Selecciona archivos");

            int seleccion = chooser.showOpenDialog(null);

            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File[] _files = chooser.getSelectedFiles();
                List<MiFile> files = new ArrayList<>();

                for (File file : _files) {
                    files.add(new MiFile(file.getName(), file.getAbsolutePath(), file.length()));
                }

                return files;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        panelFiles = new javax.swing.JPanel();
        addBtn = new javax.swing.JButton();
        nextBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("File Sharing");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        panelFiles.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panelFilesLayout = new javax.swing.GroupLayout(panelFiles);
        panelFiles.setLayout(panelFilesLayout);
        panelFilesLayout.setHorizontalGroup(
            panelFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 486, Short.MAX_VALUE)
        );
        panelFilesLayout.setVerticalGroup(
            panelFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );

        scrollPane.setViewportView(panelFiles);

        addBtn.setFont(new java.awt.Font("Lucida Console", 0, 24)); // NOI18N
        addBtn.setText("Agregar");
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                addBtnMouseClicked(evt);
            }
        });

        nextBtn.setFont(new java.awt.Font("Lucida Console", 0, 14)); // NOI18N
        nextBtn.setText("Siguiente");
        nextBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                nextBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(nextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addBtnMouseClicked
        List<MiFile> files = selectFiles();
        if (files != null) {
            files.forEach((f) -> {
                FileComponent fc = new FileComponent(f, this::onDeleteFile);
                fc.setMaximumSize(new Dimension(430, 88));
                panelFiles.add(fc);
            });
            panelFiles.revalidate();
            panelFiles.repaint();
            Global app = Global.getInstance();
            app.addFiles(files);
        }
    }//GEN-LAST:event_addBtnMouseClicked

    private void nextBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_nextBtnMouseClicked
        Global app = Global.getInstance();

        if (app.getFiles().size() > 0) {
            
            boolean match = false;
            String host = null;
            while (!match) {
                 host = JOptionPane.showInputDialog(rootPane, "Ingrese la dirección IP del receptor", "Ingrese la dirección IP", JOptionPane.INFORMATION_MESSAGE);

                if (host == null) break;

                match = host.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");
                if (!match) JOptionPane.showMessageDialog(rootPane, "Ingresa un IP válida", "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    break;
                }
            }
            
            if(!match) return;

            try {
                Socket s = new Socket();
                s.connect(new InetSocketAddress(host, 7777), 5000);

                SendingProcess sp = new SendingProcess(s);
                app.push(sp);
                sp.setVisible(true);
                this.setVisible(false);
            } catch (SocketTimeoutException ex) {
                JOptionPane.showMessageDialog(rootPane, "No hay un dispositivo esperando por una conexión", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(rootPane, "Error de E/S: \n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else
            JOptionPane.showMessageDialog(rootPane, "Seleccione al menos un archivo", "Información", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_nextBtnMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Global.getInstance().popFrame();
    }//GEN-LAST:event_formWindowClosing

    private void onDeleteFile(MiFile file, FileComponent fc) {
        Global app = Global.getInstance();
        app.removeFile(file);
        panelFiles.remove(fc);
        panelFiles.revalidate();
        panelFiles.repaint();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton nextBtn;
    private javax.swing.JPanel panelFiles;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
