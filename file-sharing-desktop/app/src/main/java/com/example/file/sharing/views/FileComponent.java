package com.example.file.sharing.views;

import com.example.file.sharing.models.MiFile;
import java.util.function.BiConsumer;

public class FileComponent extends javax.swing.JPanel {
    private MiFile file;
    BiConsumer<MiFile, FileComponent> onDelete;

    public FileComponent(MiFile _file, BiConsumer<MiFile, FileComponent> onDelete) {
        file = _file;
        this.onDelete = onDelete;
        initComponents();
        txtName.setText(file.getName());
        txtSize.setText(String.valueOf(file.getSize()) + " Bytes");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JLabel();
        txtSize = new javax.swing.JLabel();
        removeBtn = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtName.setFont(new java.awt.Font("Lucida Console", 1, 18)); // NOI18N
        txtName.setText("FILENAME");

        txtSize.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        txtSize.setText("SIZE");

        removeBtn.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        removeBtn.setText("Quitar");
        removeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                removeBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSize, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void removeBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeBtnMouseClicked
        onDelete.accept(file, this);
    }//GEN-LAST:event_removeBtnMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton removeBtn;
    private javax.swing.JLabel txtName;
    private javax.swing.JLabel txtSize;
    // End of variables declaration//GEN-END:variables
}
