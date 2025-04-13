package com.example.file.sharing.views;

import com.example.file.sharing.controllers.Global;
import com.example.file.sharing.models.MiNetworkInterface;
import javax.swing.JFrame;

public class InterfaceComponent extends javax.swing.JPanel {
    private final MiNetworkInterface ni;
    private final JFrame container;
    
    public InterfaceComponent(JFrame c, MiNetworkInterface ni) {
        initComponents();
        this.ni = ni;
        container = c;
        txtName.setText(ni.getName());
        txtIp.setText(ni.getIp());
    }
    
    private void onClick() {
        ReceivingProcess rp = new ReceivingProcess(ni);
        rp.setVisible(true);
        Global.getInstance().push(rp);
        container.setVisible(false);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtName = new javax.swing.JLabel();
        txtIp = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        txtName.setFont(new java.awt.Font("Lucida Console", 1, 24)); // NOI18N
        txtName.setText("INTERFACENAME");
        txtName.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNameMouseClicked(evt);
            }
        });

        txtIp.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        txtIp.setText("IP.IP.IP.IP");
        txtIp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtIpMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtIp, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtNameMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNameMouseClicked
        onClick();
    }//GEN-LAST:event_txtNameMouseClicked

    private void txtIpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIpMouseClicked
        onClick();
    }//GEN-LAST:event_txtIpMouseClicked

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        onClick();
    }//GEN-LAST:event_formMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel txtIp;
    private javax.swing.JLabel txtName;
    // End of variables declaration//GEN-END:variables
}
