package com.example.file.sharing.views;

import com.example.file.sharing.controllers.Global;
import com.example.file.sharing.models.MiNetworkInterface;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 *
 * @author Aleja
 */
public class InterfaceSelector extends javax.swing.JFrame {
    private JFrame previous;
    private List<MiNetworkInterface> nets;
    /**
     * Creates new form InterfaceSelector
     */
    public InterfaceSelector(List<MiNetworkInterface> nets) {
        this.nets = nets;
        initComponents();
        setLocationRelativeTo(null);
        init();
    }
    
    private void init() {
        interfacesPanel.setLayout(new BoxLayout(interfacesPanel, BoxLayout.Y_AXIS));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        for(MiNetworkInterface ni : nets) {
            InterfaceComponent ic = new InterfaceComponent(this, ni);
            ic.setMaximumSize(new Dimension(314, 100));
            interfacesPanel.add(ic);
        }
        
        interfacesPanel.revalidate();
        interfacesPanel.repaint();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        interfacesPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("File Sharing");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout interfacesPanelLayout = new javax.swing.GroupLayout(interfacesPanel);
        interfacesPanel.setLayout(interfacesPanelLayout);
        interfacesPanelLayout.setHorizontalGroup(
            interfacesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 646, Short.MAX_VALUE)
        );
        interfacesPanelLayout.setVerticalGroup(
            interfacesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 383, Short.MAX_VALUE)
        );

        scrollPane.setViewportView(interfacesPanel);

        jLabel1.setFont(new java.awt.Font("Lucida Console", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Selecciona la interfaz a usar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                    .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        Global.getInstance().popFrame();
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel interfacesPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
}
