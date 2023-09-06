package formularios;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import prg.conecbd;

public class usuarios2 extends javax.swing.JDialog {

    conecbd con;
    ResultSet rs, rs1, rs2, rs3;
    DefaultTableModel modtabla = new DefaultTableModel();
    javax.swing.table.DefaultTableModel cursor;
    int ban;//caso insertar, modificar y borrar
    String estado; //variable para check box de manera global

    public usuarios2(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.cargar_combo();
        this.configrilla();
        this.cargargrilla();
    }

    private void gencod() {
        try {
            rs = con.listar_datos("SELECT coalesce(MAX(usu_cod),0)+1 AS codigo FROM usuarios");
            rs.next();
            txtcod.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(usuarios2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargar_combo() {
        try {
            rs1 = con.listar_datos("SELECT CONCAT(per_cod,'-',per_nombre,' ',per_apellido)"
                    + " AS funcionario "
                    + " FROM personal");
            this.cbo_personal.removeAllItems();
            while (rs1.next()) {
                this.cbo_personal.addItem(rs1.getString("funcionario"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(usuarios2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void configrilla() {
        modtabla.addColumn("CODIGO");
        modtabla.addColumn("PERSONAL");
        modtabla.addColumn("NICK");
        modtabla.addColumn("NIVEL");
        modtabla.addColumn("ESTADO");

        grilla.getColumnModel().getColumn(0).setPreferredWidth(10);
        grilla.getColumnModel().getColumn(1).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(2).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(3).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(4).setPreferredWidth(50);
    }

    private void cargargrilla() {
        try {
            String[] datos = new String[5];
            rs = con.listar_datos("SELECT a.usu_cod,a.usu_nick,a.usu_nivel,"
                    + " a.usu_estado,"
                    + " CONCAT(b.per_nombre,' ',b.per_apellido)AS perso "
                    + " FROM usuarios a, personal b "
                    + " WHERE a.per_cod=b.per_cod");
            while (rs.next()) {
                datos[0] = rs.getString("usu_cod");
                datos[1] = rs.getString("perso");
                datos[2] = rs.getString("usu_nick");
                datos[3] = rs.getString("usu_nivel");
                datos[4] = rs.getString("usu_estado");
                modtabla.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(usuarios2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void limpiargrilla() {
        while (modtabla.getRowCount() > 0) {
            modtabla.removeRow(0);
        }
    }
    private void limpiar() {
        this.txtcod.setText("");
        this.txtnick.setText("");
        this.txtclave.setText("");
        this.txtbuscar.setText("");
        this.buttonGroup1.clearSelection();
        estado = "";
    }
   private void filtrar() {
    try {
        modtabla.setRowCount(0);
        String[] datos = new String[5]; // Actualicé el tamaño del arreglo
        rs = con.listar_datos("SELECT a.usu_cod,a.usu_nick,a.usu_nivel,"
                + " a.usu_estado,"
                + " CONCAT(b.per_nombre,' ',b.per_apellido)AS personal "
                + " FROM usuarios a, personal b "
                + " WHERE a.per_cod=b.per_cod AND ("
                + " CAST(a.usu_cod AS TEXT) LIKE '%" + txtbuscar.getText() + "%' OR "
                + " CONCAT(b.per_nombre,' ',b.per_apellido) LIKE '%" + txtbuscar.getText() + "%' OR "
                + " a.usu_nick LIKE '%" + txtbuscar.getText() + "%' OR "
                + " CAST(a.usu_nivel AS TEXT) LIKE '%" + txtbuscar.getText() + "%' OR "
                + " CAST(a.usu_estado AS TEXT) LIKE '%" + txtbuscar.getText() + "%')");
        while (rs.next()) {
            datos[0] = rs.getString("usu_cod");
            datos[1] = rs.getString("personal");
            datos[2] = rs.getString("usu_nick");
            datos[3] = rs.getString("usu_nivel");
            datos[4] = rs.getString("usu_estado");
            modtabla.addRow(datos);
        }
    } catch (SQLException ex) {
        Logger.getLogger(usuarios2.class.getName()).log(Level.SEVERE, null, ex);
    }
}


    
    private void habilitar(int estado) {
        switch (estado) {
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

    private void grabar() {
    if (ban == 1) {
        String columnaDuplicados = "usu_nick"; // Cambiar esto al nombre de la columna que deseas verificar
        
        // Verificar si ya existe un registro con el mismo valor en la columna especificada
        String valorDuplicado = con.verificarDuplicado("usuarios", columnaDuplicados, txtnick.getText().toUpperCase());

        if (valorDuplicado != null) {
            JOptionPane.showMessageDialog(null, "Ya existe un registro con el valor: " + valorDuplicado, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            con.insertar_datos("usuarios", "usu_cod,per_cod,usu_nick,usu_pass,usu_nivel,usu_estado",
                    txtcod.getText() + ","
                    + "(split_part('" + cbo_personal.getSelectedItem() + "','-',1))::integer,'"
                    + txtnick.getText().toUpperCase() + "', md5('" + txtclave.getText() + "'),'"
                    + cbo_nivel.getSelectedItem()
                    + "','" + estado + "'",
                    0);
        }
    }
    if (ban == 2) {
        con.actualizar_datos("usuarios", "usu_estado='" + estado + "'", "usu_cod", txtcod.getText());
    }
}


    private void estado() {
        try {
            rs2 = con.listar_datos("SELECT usu_estado FROM usuarios "
                    + " WHERE usu_cod=" + txtcod.getText());
            rs2.next();

            if ("activo".equals(rs2.getString("usu_estado"))) {
                this.activo.setSelected(true);
            } else {
                if ("inactivo".equals(rs2.getString("usu_estado"))) {
                    this.inactivo.setSelected(true);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(usuarios2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void visualizar() {
        int fila = grilla.getSelectedRow();
        txtcod.setText(modtabla.getValueAt(fila, 0).toString());
        txtnick.setText(modtabla.getValueAt(fila, 2).toString());
        estado();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtcod = new javax.swing.JTextField();
        cbo_personal = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtnick = new javax.swing.JTextField();
        txtclave = new javax.swing.JPasswordField();
        jLabel5 = new javax.swing.JLabel();
        cbo_nivel = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        activo = new javax.swing.JCheckBox();
        inactivo = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        grilla = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btn_editar = new javax.swing.JButton();
        btn_nuevo = new javax.swing.JButton();
        btn_grabar = new javax.swing.JButton();
        btn_borrar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de Usuarios"));

        jLabel1.setText("Codigo:");

        jLabel2.setText("Personal:");

        txtcod.setEnabled(false);

        cbo_personal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {

            }
        });
        cbo_personal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbo_personalKeyPressed(evt);
            }
        });

        jLabel3.setText("Nick:");

        jLabel4.setText("Clave:");

        txtnick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnickActionPerformed(evt);
            }
        });
        txtnick.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnickKeyPressed(evt);
            }
        });

        txtclave.setText("123");
        txtclave.setEnabled(false);
        txtclave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtclaveActionPerformed(evt);
            }
        });

        jLabel5.setText("Nivel:");

        cbo_nivel.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3" }));
        cbo_nivel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_nivelActionPerformed(evt);
            }
        });
        cbo_nivel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbo_nivelKeyPressed(evt);
            }
        });

        jLabel6.setText("Estado:");

        buttonGroup1.add(activo);
        activo.setText("Activo");
        activo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activoActionPerformed(evt);
            }
        });

        buttonGroup1.add(inactivo);
        inactivo.setText("Inactivo");
        inactivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inactivoActionPerformed(evt);
            }
        });

        jLabel7.setText("Buscar");

        grilla.setModel(modtabla);
        grilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grillaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(grilla);

        jButton1.setText("Filtrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(154, 154, 154)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtnick, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbo_nivel, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbo_personal, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtclave, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(activo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(inactivo))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 519, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jButton1)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(71, 71, 71))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtnick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(cbo_nivel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbo_personal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtclave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(activo)
                    .addComponent(inactivo))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Operaciones"));

        btn_editar.setText("Editar");
        btn_editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editarActionPerformed(evt);
            }
        });

        btn_nuevo.setText("Nuevo");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        btn_grabar.setText("Grabar");
        btn_grabar.setEnabled(false);
        btn_grabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_grabarActionPerformed(evt);
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
                .addGap(39, 39, 39)
                .addComponent(btn_nuevo)
                .addGap(39, 39, 39)
                .addComponent(btn_editar)
                .addGap(58, 58, 58)
                .addComponent(btn_grabar)
                .addGap(31, 31, 31)
                .addComponent(btn_borrar)
                .addGap(45, 45, 45)
                .addComponent(btn_cancelar)
                .addGap(37, 37, 37)
                .addComponent(btn_salir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_editar)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_grabar)
                    .addComponent(btn_borrar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salir))
                .addGap(0, 11, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void grillaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grillaMouseClicked
        this.visualizar();

        if (ban == 3) {
            int mensaje = JOptionPane.showConfirmDialog(this, "Deseas Borrar-->"
                + txtcod.getText(), "Confirmar", JOptionPane.YES_NO_OPTION);
            if(mensaje == JOptionPane.YES_OPTION) {
                con.BorrarDatos("usuarios", "usu_cod", txtcod.getText());
                this.limpiargrilla();
                this.cargargrilla();
                this.limpiar();
                ban = 0;
            }
        }
    }//GEN-LAST:event_grillaMouseClicked

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        ban = 2;
        this.habilitar(0);
//        this.txtdescri.setEnabled(true);
//        this.txtdescri.requestFocus();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        ban = 1;//insercion
        this.habilitar(0);
        this.txtnick.setEnabled(true);
        this.txtnick.requestFocus();
        this.gencod();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
//        this.validar();
        this.grabar();
        this.limpiar();
        this.habilitar(1);
        this.limpiargrilla();
        this.cargargrilla();
        this.btn_nuevo.grabFocus();
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

    private void txtclaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtclaveActionPerformed
        // TODO add your handling code here:
        this.activo.setEnabled(true);
        this.inactivo.setEnabled(true);
    }//GEN-LAST:event_txtclaveActionPerformed

    private void activoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activoActionPerformed
        estado = "activo";
        btn_grabar.setEnabled(true);
        btn_grabar.grabFocus();
    }//GEN-LAST:event_activoActionPerformed

    private void inactivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inactivoActionPerformed
        estado = "inactivo";
        btn_grabar.setEnabled(true);
        btn_grabar.grabFocus();
    }//GEN-LAST:event_inactivoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        filtrar();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void cbo_personalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_personalKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            this.txtclave.setEnabled(true);
            this.txtclave.requestFocus();
        }
    }//GEN-LAST:event_cbo_personalKeyPressed

    private void txtnickKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnickKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            this.cbo_nivel.setEnabled(true);
            this.cbo_nivel.requestFocus();
        }
    }//GEN-LAST:event_txtnickKeyPressed

    private void cbo_nivelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_nivelKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == 10){
            this.cbo_personal.setEnabled(true);
            this.cbo_personal.requestFocus();
        }
    }//GEN-LAST:event_cbo_nivelKeyPressed

    private void cbo_nivelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_nivelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbo_nivelActionPerformed

    private void txtnickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnickActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnickActionPerformed

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
            java.util.logging.Logger.getLogger(usuarios2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(usuarios2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(usuarios2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(usuarios2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                usuarios2 dialog = new usuarios2(new javax.swing.JFrame(), true);
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
    private javax.swing.JCheckBox activo;
    private javax.swing.JButton btn_borrar;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_editar;
    private javax.swing.JButton btn_grabar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_salir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbo_nivel;
    private javax.swing.JComboBox cbo_personal;
    private javax.swing.JTable grilla;
    private javax.swing.JCheckBox inactivo;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JPasswordField txtclave;
    private javax.swing.JTextField txtcod;
    private javax.swing.JTextField txtnick;
    // End of variables declaration//GEN-END:variables
}
