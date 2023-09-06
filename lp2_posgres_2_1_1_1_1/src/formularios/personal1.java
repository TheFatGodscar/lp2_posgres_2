
package formularios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import prg.conecbd;

public class personal1 extends javax.swing.JDialog {
    conecbd con;
    ResultSet rs, rs1, rs2, rs3;   
    int ban;
    // Definir modelo de tabla
    DefaultTableModel modtabla = new DefaultTableModel();
    javax.swing.table.DefaultTableModel cursor;

    public personal1(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.configrilla();
        this.cargargrilla();
        this.cargar_combo();
    }
    private void configrilla(){
        
        modtabla.addColumn("CODIGO");
        modtabla.addColumn("CEDULA");
        modtabla.addColumn("NOMBRE");
        modtabla.addColumn("APELLIDO");
        modtabla.addColumn("CARGO");
        grilla.getColumnModel().getColumn(0).setPreferredWidth(10);
        grilla.getColumnModel().getColumn(1).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(2).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(3).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(4).setPreferredWidth(50);
    }
    private void habilitar(int estado){
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
    private void gencod(){
        
        try {
            rs = con.listar_datos("SELECT coalesce(MAX(per_cod),0)+1 AS codigo FROM personal");
            rs.next();
            txtcod.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(personal1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void limpiar(){
        this.txtcod.setText("");
        this.txtcedula.setText("");
        this.txtnombre.setText("");
        this.txtapellido.setText("");
    }
    private void cargargrilla(){       
        try {
            String[] datos = new String[5];
            rs = con.listar_datos("select a.per_cod, a.per_ci, a.per_nombre, a.per_apellido,\n" +
                    "concat(b.car_cod,'-',b.car_descri)as car_descri \n" +
                    "from personal a \n" +
                    "join cargos b on b.car_cod = a.car_cod \n" +
                    "order by a.per_cod asc");
            while (rs.next()){
                datos[0] = rs.getString("per_cod");
                datos[1] = rs.getString("per_ci");
                datos[2] = rs.getString("per_nombre");
                datos[3] = rs.getString("per_apellido");
                datos[4] = rs.getString("car_descri");
                modtabla.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(personal1.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    private void cargar_combo(){       
        try {
            rs = con.listar_datos("select concat(car_cod,'-',car_descri) as tipcargo from cargos");
            this.cbo_cargo.removeAllItems();
            while (rs.next()){
                this.cbo_cargo.addItem(rs.getString("tipcargo"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     private void grabar(){
        if (ban == 1){
        con.insertar_datos("personal", "per_cod, per_ci, per_nombre, per_apellido, car_cod", 
                txtcod.getText() + ",'" 
                + txtcedula.getText() + "','"
                + txtnombre.getText() + "','"
                + txtapellido.getText() + "',"
                + "(split_part('" + cbo_cargo.getSelectedItem() + "','-',1))::integer",
                    0);
    }
        if (ban == 2){
            con.actualizar_datos("personal", "per_ci='" + txtcedula.getText() 
                    + "',per_nombre='" + txtnombre.getText()
                    + "',per_apellido='" + txtapellido.getText()
                    + "',car_cod=" + "(split_part('" + cbo_cargo.getSelectedItem() + "','-',1))::integer",
                    "per_cod=" + txtcod.getText());
        }
    } 
    private void visualizar(){
        int fila = grilla.getSelectedRow();
        txtcod.setText(modtabla.getValueAt(fila, 0).toString());
        txtcedula.setText(modtabla.getValueAt(fila, 1).toString());
        txtnombre.setText(modtabla.getValueAt(fila, 2).toString());
        txtapellido.setText(modtabla.getValueAt(fila, 3).toString());
        cbo_cargo.setSelectedItem(modtabla.getValueAt(fila, 4).toString());
    }
    
    private void limpiargrilla(){
        while (modtabla.getRowCount() > 0){
            modtabla.removeRow(0);
        }
    }
    private void filtrar(){       
        try {
            modtabla.setRowCount(0);
            String[] datos = new String[7];
            rs = con.listar_datos("select per_cod,per_ci,per_nombre,per_apellido,car_descri"
                    + " from personal inner join cargos using(car_cod)"
                    + "where per_ci like '%" + txtbuscar.getText() + "%'"
                    + "or per_nombre like '%" + txtbuscar.getText() + "%'"
                    + "or per_apellido like '%" + txtbuscar.getText() + "%'"
                    + "or car_descri like '%" + txtbuscar.getText() + "%' order by per_cod asc");
            while (rs.next()) {
                datos[0] = rs.getString("per_cod");
                datos[1] = rs.getString("per_ci");
                datos[2] = rs.getString("per_nombre");
                datos[3] = rs.getString("per_apellido");
                datos[4] = rs.getString("car_descri");
                modtabla.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(personal1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtcod = new javax.swing.JTextField();
        txtcedula = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtapellido = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btn_filtrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        grilla = new javax.swing.JTable();
        cbo_cargo = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        btn_nuevo = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_grabar = new javax.swing.JButton();
        btn_borrar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del personal"));

        jLabel2.setText("Codigo");

        jLabel3.setText("Cedula");

        jLabel4.setText("Nombre");

        txtcedula.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcedulaKeyPressed(evt);
            }
        });

        txtnombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnombreKeyPressed(evt);
            }
        });

        jLabel5.setText("Apellido");

        txtapellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtapellidoKeyPressed(evt);
            }
        });

        jLabel6.setText("Cargo");

        jLabel7.setText("Buscar");

        btn_filtrar.setText("Filtrar");
        btn_filtrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_filtrarActionPerformed(evt);
            }
        });

        grilla.setModel(modtabla);
        grilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grillaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(grilla);

        cbo_cargo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));
        cbo_cargo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbo_cargoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtnombre, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtcod, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                .addComponent(txtcedula)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                                .addComponent(jLabel7)
                                .addGap(41, 41, 41)
                                .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(33, 33, 33)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtapellido, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                    .addComponent(cbo_cargo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(79, 79, 79)
                        .addComponent(btn_filtrar)
                        .addGap(126, 126, 126))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtapellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtcedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(cbo_cargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_filtrar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Operaciones"));

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

        btn_salir.setText("Salir");
        btn_salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_salirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_nuevo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_editar)
                .addGap(46, 46, 46)
                .addComponent(btn_grabar)
                .addGap(45, 45, 45)
                .addComponent(btn_borrar)
                .addGap(50, 50, 50)
                .addComponent(btn_cancelar)
                .addGap(45, 45, 45)
                .addComponent(btn_salir)
                .addGap(23, 23, 23))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_editar)
                    .addComponent(btn_grabar)
                    .addComponent(btn_borrar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salir))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Personal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        ban = 1;
        this.habilitar(0);
        this.txtcedula.setEnabled(true);
        this.txtcedula.requestFocus();
        this.gencod();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        ban = 2;
        this.habilitar(0);
        this.txtcedula.setEnabled(true);
        this.txtcedula.requestFocus();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
        this.grabar();
        this.limpiargrilla();
        this.cargargrilla();
        this.limpiar();
        this.habilitar(1);
    }//GEN-LAST:event_btn_grabarActionPerformed

    private void btn_borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_borrarActionPerformed
        ban = 3;
    }//GEN-LAST:event_btn_borrarActionPerformed

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        this.limpiar();
        this.habilitar(1);
        this.limpiargrilla();
        this.cargargrilla();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        dispose();
    }//GEN-LAST:event_btn_salirActionPerformed

    private void grillaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grillaMouseClicked
         this.visualizar();
        
        if (ban == 3){
            int mensaje = JOptionPane.showConfirmDialog(this, "Deseas borrar -->"
                    + txtcod.getText(), "Confirmar", JOptionPane.YES_NO_OPTION);
        if (mensaje == JOptionPane.YES_NO_OPTION){
        con.BorrarDatos("personal", "per_cod", txtcod.getText());
        this.limpiargrilla();
        this.cargargrilla();
        this.limpiar();
        ban = 0;
        }
        }
    }//GEN-LAST:event_grillaMouseClicked

    private void txtcedulaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcedulaKeyPressed
        if (evt.getKeyCode() == 10){
            if (txtcedula.getText().equals("")){
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            }else {
                this.txtnombre.setEnabled(true);
                this.txtnombre.grabFocus();
            }
        }
    }//GEN-LAST:event_txtcedulaKeyPressed

    private void txtnombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreKeyPressed
        if (evt.getKeyCode() == 10){
            if (txtnombre.getText().equals("")){
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            }else {
                this.txtapellido.setEnabled(true);
                this.txtapellido.grabFocus();
            }
    }//GEN-LAST:event_txtnombreKeyPressed
    }
    private void txtapellidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidoKeyPressed
        if (evt.getKeyCode() == 10){
            if (txtapellido.getText().equals("")){
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            }else {
                this.cbo_cargo.setEnabled(true);
                this.cbo_cargo.grabFocus();
            }
    }//GEN-LAST:event_txtapellidoKeyPressed
    }
    private void cbo_cargoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_cargoKeyPressed
        if (evt.getKeyCode() == 10){
        this.btn_grabar.setEnabled(true);
        this.btn_grabar.requestFocus();
            }
    }//GEN-LAST:event_cbo_cargoKeyPressed

    private void btn_grabarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_grabarKeyPressed
        if (evt.getKeyCode() == 10){
            this.btn_grabar.doClick();
        }
    }//GEN-LAST:event_btn_grabarKeyPressed

    private void btn_filtrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_filtrarActionPerformed
        this.filtrar();
    }//GEN-LAST:event_btn_filtrarActionPerformed
    
    
    
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
            java.util.logging.Logger.getLogger(personal1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(personal1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(personal1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(personal1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                personal1 dialog = new personal1(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> cbo_cargo;
    private javax.swing.JTable grilla;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcedula;
    private javax.swing.JTextField txtcod;
    private javax.swing.JTextField txtnombre;
    // End of variables declaration//GEN-END:variables
}
