
package formularios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import prg.conecbd;


public class cargos extends javax.swing.JDialog {

    conecbd con;
    ResultSet rs, rs1, rs2, rs3;
    int ban;
    
    DefaultTableModel modtabla = new DefaultTableModel();
    javax.swing.table.DefaultTableModel cursor;
    
    public cargos(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.configrilla();
        this.cargargrilla();
       
    }
    private String convertirAMayusculas(String texto) {
    return texto.toUpperCase();
}
    private void configrilla(){
    modtabla.addColumn("codigo");
    modtabla.addColumn("descripcion");
    tabla_1.getColumnModel().getColumn(0).setPreferredWidth(10);
    tabla_1.getColumnModel().getColumn(1).setPreferredWidth(50);
    }
   private void cargargrilla() {
    try {
        String[] datos = new String[2];
        rs = con.listar_datos("select * from cargos order by car_cod asc");
        
       
        if (rs != null) {
            while (rs.next()) {
                datos[0] = rs.getString("car_cod");
                datos[1] = rs.getString("car_descri");
                modtabla.addRow(datos);
            }
        } else {
            
            JOptionPane.showMessageDialog(null, "Error al obtener datos de la base de datos");
        }
    } catch (SQLException ex) {
        Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
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
        this.txt_cod.setText("");
        this.txt_descri.setText("");
    }
    
   private void filtrar() {
    try {
        modtabla.setRowCount(0);
        String[] datos = new String[2];
        
        String consulta = "SELECT * FROM cargos WHERE CAST(car_cod AS TEXT) LIKE '%" + txt_buscar.getText() + "%' OR car_descri LIKE '%" + txt_buscar.getText() + "%'";
        rs = con.listar_datos(consulta);
        
        while (rs.next()) {
            datos[0] = rs.getString("car_cod");
            datos[1] = rs.getString("car_descri");
            modtabla.addRow(datos);
        }
    } catch (SQLException ex) {
        Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
    }
}



    private void gencod(){
        try {
            rs = con.listar_datos("SELECT COALESCE(MAX(car_cod), 0) + 1 AS codigo FROM cargos;");
            rs.next();
            txt_cod.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void grabar(){
        if(ban==1){//nuevo
            con.insertar_datos("cargos", "car_cod, car_descri", txt_cod.getText() + ",'" + txt_descri.getText().toUpperCase() + "'", 0);
        } 
        if (ban==2){
            con.actualizar_datos("cargos", "car_descri='" + txt_descri.getText().toUpperCase() + "'", "car_cod", txt_cod.getText());
        }
        
        
        
    }
    private void limpiargrilla(){
        while (modtabla.getRowCount()>0){
        modtabla.removeRow(0);
        }
        
    }
    private void visualizarDatos (){
        int fila = tabla_1.getSelectedRow();
        txt_cod.setText(tabla_1.getValueAt(fila, 0).toString());
        txt_descri.setText(tabla_1.getValueAt(fila, 1).toString());
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1_codigo = new javax.swing.JLabel();
        txt_cod = new javax.swing.JTextField();
        jLabel1_descripcion = new javax.swing.JLabel();
        txt_descri = new javax.swing.JTextField();
        btn_nuevo = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_borrar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_grabar = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        jLabel1_buscar = new javax.swing.JLabel();
        txt_buscar = new javax.swing.JTextField();
        btn_filtrar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_1 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1_codigo.setText("Código");

        txt_cod.setEditable(false);
        txt_cod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_codActionPerformed(evt);
            }
        });

        jLabel1_descripcion.setText("Descripción");

        txt_descri.setEnabled(false);
        txt_descri.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_descriActionPerformed(evt);
            }
        });

        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        btn_editar.setText("Editar");
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });

        btn_borrar.setText("Borrar");
        btn_borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_borrarActionPerformed(evt);
            }
        });

        btn_cancelar.setText("Cancelar");
        btn_cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_cancelarActionPerformed(evt);
            }
        });

        btn_grabar.setText("Grabar");
        btn_grabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_grabarActionPerformed(evt);
            }
        });
        btn_grabar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btn_grabarKeyPressed(evt);
            }
        });

        btn_salir.setText("Salir");
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        jLabel1_buscar.setText("Buscar");

        btn_filtrar.setText("Filtrar");
        btn_filtrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filtrarActionPerformed(evt);
            }
        });

        tabla_1.setModel(modtabla);
        tabla_1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabla_1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_cod))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txt_buscar, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                    .addComponent(txt_descri))))
                        .addGap(77, 77, 77)
                        .addComponent(btn_filtrar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_nuevo)
                        .addGap(46, 46, 46)
                        .addComponent(btn_editar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_borrar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_cancelar)
                        .addGap(18, 18, 18)
                        .addComponent(btn_grabar)
                        .addGap(27, 27, 27)
                        .addComponent(btn_salir)))
                .addContainerGap(71, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_cod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1_descripcion)
                    .addComponent(txt_descri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1_buscar)
                    .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_filtrar))
                .addGap(29, 29, 29)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_editar)
                    .addComponent(btn_borrar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salir)
                    .addComponent(btn_grabar))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
       ban = 1;
       this.habilitar(0);
        this.gencod();
        this.txt_descri.setEnabled(true);
        this.txt_descri.requestFocus();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_borrarActionPerformed
ban = 3;        // TODO add your handling code here:
    }//GEN-LAST:event_btn_borrarActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        this.limpiar();
        this.habilitar(1);
        this.limpiargrilla();
        this.cargargrilla();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
 ban=2;
 this.habilitar(0);
 this.txt_descri.setEnabled(true);
 this.txt_descri.requestFocus();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void txt_codActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_codActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_codActionPerformed

    private void btn_filtrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_filtrarActionPerformed
        this.filtrar();
    }//GEN-LAST:event_btn_filtrarActionPerformed

    private void txt_descriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_descriActionPerformed
if (txt_descri.getText().equals("")){
    JOptionPane.showMessageDialog(null, "El campo no puede estar vacio");
} else {
    this.btn_grabar.setEnabled(true);
    this.btn_grabar.grabFocus();
}
    }//GEN-LAST:event_txt_descriActionPerformed

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btn_salirActionPerformed

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
       grabar();
       this.limpiar();
       this.habilitar(1);
       this.limpiargrilla();
       this.cargargrilla();
       this.txt_descri.setEnabled(false);
       this.btn_nuevo.grabFocus();
    }//GEN-LAST:event_btn_grabarActionPerformed

    private void tabla_1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_1MouseClicked
this.visualizarDatos();
if(ban==3){
    int mensaje = JOptionPane.showConfirmDialog(this, " Deseas borrar ----->" + txt_descri.getText(),"Confirmar",JOptionPane.YES_NO_OPTION);
    if(mensaje == JOptionPane.YES_OPTION) {
        con.BorrarDatos("Cargos", "car_cod", txt_cod.getText());
        this.limpiargrilla();
        this.cargargrilla();
        this.limpiar();
        ban=0;
    }
}
    }//GEN-LAST:event_tabla_1MouseClicked

    private void btn_grabarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_grabarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_grabarKeyPressed

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
            java.util.logging.Logger.getLogger(cargos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(cargos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(cargos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(cargos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                cargos dialog = new cargos(new javax.swing.JFrame(), true);
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
    private javax.swing.JLabel jLabel1_buscar;
    private javax.swing.JLabel jLabel1_codigo;
    private javax.swing.JLabel jLabel1_descripcion;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabla_1;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_cod;
    private javax.swing.JTextField txt_descri;
    // End of variables declaration//GEN-END:variables
}
