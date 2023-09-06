/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package formularios;

import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import prg.conecbd;

/**
 *
 * @author games
 */
public class proveedor extends javax.swing.JDialog {
        DefaultTableModel modtabla = new DefaultTableModel();
         javax.swing.table.DefaultTableModel cursor;
         conecbd con;
         ResultSet rs, rs1, rs2, rs3;
         int ban;
    /**
     * Creates new form formularios
     */
    public proveedor(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.confi_grilla();
        this.cargar_grilla();
        this.limpiar();
    }
    
    private void confi_grilla(){
         modtabla.addColumn("codigo");
         modtabla.addColumn("denominacion");
         modtabla.addColumn("dirección");
         modtabla.addColumn("teléfono");
         modtabla.addColumn("ruc"); 
         grilla_prove.getColumnModel().getColumn(0).setPreferredWidth(10);
         grilla_prove.getColumnModel().getColumn(1).setPreferredWidth(10);
         grilla_prove.getColumnModel().getColumn(2).setPreferredWidth(10);
         grilla_prove.getColumnModel().getColumn(3).setPreferredWidth(10);
         grilla_prove.getColumnModel().getColumn(4).setPreferredWidth(10);
    }
        private void cargar_grilla(){
            try {
                String [] datos  = new String [5];
                rs = con.listar_datos ("select * from proveedor order by prov_cod asc"); 
                while(rs.next()){
                    datos [0] = rs.getString("prov_cod");
                    datos [1] = rs.getString("prov_denominacion");
                    datos [2] = rs.getString("prov_direccion");
                    datos [3] = rs.getString("prov_telefono");
                    datos [4] = rs.getString("prov_ruc");
                    modtabla.addRow(datos);
                } 
            } catch (SQLException ex) {
                Logger.getLogger(proveedor.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        
             private void habilitar (int estado){
    switch (estado){
        case 0:
            btn_borrar.setEnabled(false);
            btn_editar.setEnabled(false);
            btn_grabar.setEnabled(false);
            btn_cancelar.setEnabled(true);
            btn_salir.setEnabled(true);
            btn_nuevo.setEnabled(false);
            break;
        case 1:
            btn_borrar.setEnabled(true);
            btn_editar.setEnabled(true);
            btn_grabar.setEnabled(false);
            btn_cancelar.setEnabled(true);
            btn_salir.setEnabled(true);
            btn_nuevo.setEnabled(true);
            break;        
        }
    }
           private void limpiar(){
         txt_cod.setText("");
         txt_deno.setText("");
         txt_direc.setText("");
         txt_tel.setText("");
         txt_buscar.setText("");
         txt_ruc.setText("");
         
     }
           
                private void gencod(){
        try {
            rs = con.listar_datos("select coalesce (max(prov_cod), 0)+1 as codigo from proveedor");
            rs.next();
            txt_cod.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
                
                     private void grabar() {
    if (ban == 1) { // Nuevo
String values = txt_cod.getText() + ", '"
                + txt_deno.getText() + "', '"
                + txt_direc.getText() + "', '"
                + txt_tel.getText() + "', '"
                + txt_ruc.getText() + "'";
        
        con.insertar_datos("proveedor", "prov_cod, prov_denominacion, prov_telefono, prov_ruc, prov_direccion", values, 0);
    }
if (ban == 2) { // Editar
    String updates = "prov_denominacion = '" + txt_deno.getText() + "', " +
                     "prov_direccion = '" + txt_direc.getText() + "', " +
                     "prov_telefono = '" + txt_tel.getText() + "', " +
                     "prov_ruc = '" + txt_ruc.getText() + "'";
    
    con.actualizar_datos2("proveedor", updates, "prov_cod = " + txt_cod.getText());
}

}
                          private void filtrar(){
        try {
            modtabla.setRowCount(0);
            String[] datos = new String[5];
            rs = con.listar_datos("select * from proveedor where prov_denominacion like '%" + txt_buscar.getText() + "%'");
            while (rs.next()){
                datos [0] = rs.getString("prov_cod");
                datos [1] = rs.getString("prov_denominacion");
                datos [2] = rs.getString("prov_direccion");
                datos [3] = rs.getString("prov_telefono");
                datos [4] = rs.getString("prov_ruc");
                modtabla.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
                          
                               private void limpiar_grilla (){
        while(modtabla.getRowCount()>0){
            modtabla.removeRow(0);
        }
     }
          private void visualizarDatos(){
        int fila = grilla_prove.getSelectedRow();
        txt_cod.setText(grilla_prove.getValueAt(fila, 0).toString());
        txt_deno.setText(grilla_prove.getValueAt(fila, 1).toString());
        txt_direc.setText(grilla_prove.getValueAt(fila, 2).toString());
        txt_tel.setText(grilla_prove.getValueAt(fila, 3).toString());
        txt_ruc.setText(grilla_prove.getValueAt(fila, 4).toString());
    }
          private void validar (){
        try {
            rs = con.listar_datos("select * from proveedor where prov_denominacion = '" +this.txt_deno.getText()+ " ' ");
            boolean encontro = rs.next();
            if(encontro == true){
                JOptionPane.showMessageDialog(null, "El proveedor " + txt_deno.getText()+ "Ya se encuentra registrado");
                this.habilitar(1);
            } else {
                grabar();
            }
        } catch (SQLException ex) {
            Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }     
          

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        btn_nuevo = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btn_salir = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btn_editar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btn_cancelar = new javax.swing.JButton();
        txt_cod = new javax.swing.JTextField();
        btn_borrar = new javax.swing.JButton();
        txt_deno = new javax.swing.JTextField();
        btn_grabar = new javax.swing.JButton();
        txt_direc = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txt_tel = new javax.swing.JTextField();
        txt_buscar = new javax.swing.JTextField();
        txt_ruc = new javax.swing.JTextField();
        btn_filtrar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        grilla_prove = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setText("Denominación");

        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        jLabel4.setText("Dirección");

        btn_salir.setText("Salir");
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        jLabel5.setText("Teléfono");

        btn_editar.setText("Editar");
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });

        jLabel6.setText("RUC");

        btn_cancelar.setText("Cancelar");
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });

        btn_borrar.setText("Borrar");
        btn_borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_borrarActionPerformed(evt);
            }
        });

        txt_deno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_denoActionPerformed(evt);
            }
        });
        txt_deno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_denoKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_denoKeyTyped(evt);
            }
        });

        btn_grabar.setText("Grabar");
        btn_grabar.setEnabled(false);
        btn_grabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_grabarActionPerformed(evt);
            }
        });

        jLabel7.setText("Buscar");

        txt_tel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_telActionPerformed(evt);
            }
        });

        txt_ruc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_rucKeyPressed(evt);
            }
        });

        btn_filtrar.setText("Filtrar");
        btn_filtrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filtrarActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setText("PROVEEDOR");

        grilla_prove.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createCompoundBorder(), javax.swing.BorderFactory.createCompoundBorder()));
        grilla_prove.setModel(modtabla);
        grilla_prove.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grilla_proveMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(grilla_prove);

        jLabel2.setText("Código");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(276, 276, 276)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt_cod, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(127, 127, 127)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(txt_buscar, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_tel, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                                    .addComponent(txt_direc, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_deno, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(45, 45, 45)
                                .addComponent(btn_filtrar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 654, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(btn_editar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_borrar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btn_grabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btn_salir, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txt_cod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txt_ruc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_deno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_direc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txt_tel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_filtrar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_salir)
                    .addComponent(btn_editar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_borrar)
                    .addComponent(btn_grabar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        ban= 1;
        this.habilitar(0);
        this.txt_deno.setEnabled(true);
        this.txt_deno.requestFocus();
        this.gencod();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    
    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        dispose();
    }//GEN-LAST:event_btn_salirActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        ban = 2;
        this.habilitar(0);
        this.txt_deno.setEnabled(true);
        this.txt_deno.requestFocus();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        this.limpiar();
        this.habilitar(1);
        this.limpiar_grilla();
        this.cargar_grilla();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_borrarActionPerformed
        ban = 3;
    }//GEN-LAST:event_btn_borrarActionPerformed

    private void txt_denoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_denoActionPerformed
        if (txt_deno.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "El campo no puede estar vacio");
        }else{
            this.btn_grabar.setEnabled(true);
            this.btn_grabar.grabFocus();
        }
    }//GEN-LAST:event_txt_denoActionPerformed

    private void txt_denoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_denoKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txt_deno.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacío.. ");
            } else {
                this.txt_direc.setEnabled(true);
                this.txt_direc.grabFocus();
            }
        }
    }//GEN-LAST:event_txt_denoKeyPressed

    private void txt_denoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_denoKeyTyped
int k = evt.getKeyChar();
if (Character.isDigit(k)) { // Verificar si el carácter es un dígito numérico
    evt.setKeyChar((char) KeyEvent.VK_CLEAR);
    getToolkit().beep();
    evt.consume();
    JOptionPane.showMessageDialog(null, "No puede ingresar números, solo letras y otros caracteres.");
    txt_deno.requestFocus();
}

    }//GEN-LAST:event_txt_denoKeyTyped

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
        grabar();
        this.limpiar();
        this.habilitar(1);
        this.limpiar_grilla();
        this.cargar_grilla();
        txt_deno.setEnabled(false);
        this.btn_nuevo.grabFocus();
    }//GEN-LAST:event_btn_grabarActionPerformed

    private void txt_telActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_telActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_telActionPerformed

    private void btn_filtrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_filtrarActionPerformed
        this.filtrar();
    }//GEN-LAST:event_btn_filtrarActionPerformed

    private void grilla_proveMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grilla_proveMouseClicked
        this.visualizarDatos();
        if (ban==3){
            int mensaje = JOptionPane.showConfirmDialog(this, " Desea borrar --> " +
                txt_deno.getText(), "Confirmar", JOptionPane.YES_NO_OPTION);
            if (mensaje == JOptionPane.YES_OPTION){
                con.BorrarDatos("proveedor", "prov_cod", txt_cod.getText());
                this.limpiar_grilla();
                this.cargar_grilla();
                this.limpiar();
                ban=0;
            }
        }
    }//GEN-LAST:event_grilla_proveMouseClicked

    private void txt_rucKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_rucKeyPressed
        if (evt.getKeyCode() == 10){
        this.btn_grabar.setEnabled(true);
        this.btn_grabar.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_txt_rucKeyPressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(proveedor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                proveedor dialog = new proveedor(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_borrar;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_filtrar;
    private javax.swing.JButton btn_grabar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_salir;
    private javax.swing.JTable grilla_prove;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_cod;
    private javax.swing.JTextField txt_deno;
    private javax.swing.JTextField txt_direc;
    private javax.swing.JTextField txt_ruc;
    private javax.swing.JTextField txt_tel;
    // End of variables declaration//GEN-END:variables
}
