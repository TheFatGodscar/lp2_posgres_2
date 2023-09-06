package formularios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import prg.conecbd;


public class tipo_de_cliente extends javax.swing.JDialog {

    conecbd con;
    ResultSet rs;
    int ban;
    // definir modelo de tabla
    DefaultTableModel modtabla = new DefaultTableModel();
    
    public tipo_de_cliente(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.configrilla();
        this.cargargrilla();
       
    }
    private void configrilla(){
    modtabla.addColumn("cod_tipo");
    modtabla.addColumn("tip_descri");
    tabla_1.getColumnModel().getColumn(0).setPreferredWidth(10);
    tabla_1.getColumnModel().getColumn(1).setPreferredWidth(50);
    }
   private void cargargrilla() {
    try {
        String[] datos = new String[2];
        rs = con.listar_datos("select * from tiipo_cliente order by cod_tipo asc");
        
        // Verificar si el objeto ResultSet no es nulo antes de acceder a los datos
        if (rs != null) {
            while (rs.next()) {
                datos[0] = rs.getString("cod_tipo");
                datos[1] = rs.getString("tip_descri");
                modtabla.addRow(datos);
            }
        } else {
            // Manejar el caso en que el ResultSet sea nulo (puede ser un error de conexión)
            JOptionPane.showMessageDialog(null, "Error al obtener datos de la base de datos");
        }
    } catch (SQLException ex) {
        Logger.getLogger(tipo_de_cliente.class.getName()).log(Level.SEVERE, null, ex);
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
        
        String filtro = txt_buscar.getText();
        String consulta = "SELECT * FROM tiipo_cliente WHERE "
                        + "CAST(cod_tipo AS TEXT) LIKE '%" + filtro + "%' OR "
                        + "tip_descri LIKE '%" + filtro + "%'";
        
        rs = con.listar_datos(consulta);
        
        while (rs.next()) {
            datos[0] = rs.getString("cod_tipo");
            datos[1] = rs.getString("tip_descri");
            modtabla.addRow(datos);
        }
    } catch (SQLException ex) {
        Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
    }
}

        private void gencod(){
        try {
            rs = con.listar_datos("SELECT COALESCE(MAX(cod_tipo), 0) + 1 AS codigo FROM tiipo_cliente;");
            rs.next();
            txt_cod.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(tipo_de_cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void grabar(){
        if(ban==1){//nuevo
            con.insertar_datos("tiipo_cliente", "cod_tipo, tip_descri", txt_cod.getText() + ",'" + txt_descri.getText().toUpperCase() + "'", 0);
        } 
        if (ban==2){
            con.actualizar_datos("tiipo_cliente", "tip_descri='" + txt_descri.getText().toUpperCase() + "'", "cod_tipo", txt_cod.getText());
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
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
        jLabel3 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jLabel2.setText("jLabel2");

        jButton1.setText("jButton1");

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

        jLabel3.setFont(new java.awt.Font("Ubuntu", 0, 36)); // NOI18N
        jLabel3.setText("tipo de cliente");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_nuevo)
                .addGap(52, 52, 52)
                .addComponent(btn_editar)
                .addGap(56, 56, 56)
                .addComponent(btn_borrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(btn_cancelar)
                .addGap(35, 35, 35)
                .addComponent(btn_grabar)
                .addGap(66, 66, 66)
                .addComponent(btn_salir)
                .addGap(91, 91, 91))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1_descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_descri, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_cod, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(76, 76, 76)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_filtrar)
                            .addComponent(jLabel1_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addComponent(jLabel3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_cod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1_codigo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1_buscar)
                    .addComponent(txt_buscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_filtrar)
                    .addComponent(jLabel1_descripcion)
                    .addComponent(txt_descri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        con.BorrarDatos("tiipo_cliente", "cod_tipo", txt_cod.getText());
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
            java.util.logging.Logger.getLogger(tipo_de_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(tipo_de_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(tipo_de_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(tipo_de_cliente.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(() -> {
            tipo_de_cliente dialog = new tipo_de_cliente(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
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
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel1_buscar;
    private javax.swing.JLabel jLabel1_codigo;
    private javax.swing.JLabel jLabel1_descripcion;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabla_1;
    private javax.swing.JTextField txt_buscar;
    private javax.swing.JTextField txt_cod;
    private javax.swing.JTextField txt_descri;
    // End of variables declaration//GEN-END:variables
}
