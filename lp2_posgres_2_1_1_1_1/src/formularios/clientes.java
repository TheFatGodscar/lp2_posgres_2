/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

public class clientes extends javax.swing.JDialog {

    conecbd con;
    ResultSet rs, rs1, rs2, rs3;
    DefaultTableModel modtabla = new DefaultTableModel();
    javax.swing.table.DefaultTableModel cursor;
    int ban;

    /**
     * Creates new form clientes
     */
    public clientes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.configrilla();
        this.cargargrilla();
        this.cargar_combo();
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

    private void configrilla() {
        modtabla.addColumn("CODIGO");
        modtabla.addColumn("C.I");
        modtabla.addColumn("NOMBRE");
        modtabla.addColumn("APELLIDO");
        modtabla.addColumn("DIRECCION");
        modtabla.addColumn("TELEFONO");
        modtabla.addColumn("TIPO CLIENTE");
        grilla.getColumnModel().getColumn(0).setPreferredWidth(10);
        grilla.getColumnModel().getColumn(1).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(2).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(3).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(4).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(5).setPreferredWidth(50);
        grilla.getColumnModel().getColumn(6).setPreferredWidth(50);

    }

    private void gencod() {
       
        try {
            rs = con.listar_datos("SELECT coalesce(MAX(cod_clie),0)+1 AS codigo FROM clientes");
            rs.next();
            txtcod.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void limpiar() {
        this.txtcod.setText("");
        this.txtci.setText("");
        this.txtnombre.setText("");
        this.txtapellido.setText("");
        this.txtdireccion.setText("");
        this.txttel.setText("");
    }

    private void cargargrilla() {
        try {
            String[] datos = new String[7];
            rs = con.listar_datos("select a.cod_clie, a.cli_ci, a.cli_nombre, a.cli_apellido, a.cli_celular,\n" +
                        "a.cli_direc, concat(b.cod_tipo,'-',b.tip_descri)as tip_descri\n" +
                        "from clientes a\n" +
                        "join tiipo_cliente b on b.cod_tipo = a.cod_tipo \n" +
                        "order by a.cod_clie desc ");
            while (rs.next()) {
                datos[0] = rs.getString("cod_clie");
                datos[1] = rs.getString("cli_ci");
                datos[2] = rs.getString("cli_nombre");
                datos[3] = rs.getString("cli_apellido");
                datos[4] = rs.getString("cli_direc");
                datos[5] = rs.getString("cli_celular");
                datos[6] = rs.getString("tip_descri");
                modtabla.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     private void filtrar() {
    try {
        modtabla.setRowCount(0);
        String[] datos = new String[7];
        
        String filtro = txtbuscar.getText();
        String consulta = "select a.cod_clie, a.cli_ci, a.cli_nombre, a.cli_apellido, a.cli_celular," +
                          "a.cli_direc, concat(b.cod_tipo,'-',b.tip_descri)as tip_descri " +
                          "from clientes a " +
                          "join tiipo_cliente b on b.cod_tipo = a.cod_tipo " +
                          "where (a.cli_ci like '%" + filtro + "%'"
                               + " or a.cli_nombre like '%" + filtro + "%' "
                               + " or a.cli_apellido like '%" + filtro + "%' )";
        
        rs = con.listar_datos(consulta);
        
        while (rs.next()) {
            datos[0] = rs.getString("cod_clie");
            datos[1] = rs.getString("cli_ci");
            datos[2] = rs.getString("cli_nombre");
            datos[3] = rs.getString("cli_apellido");
            datos[4] = rs.getString("cli_direc");
            datos[5] = rs.getString("cli_celular");
            datos[6] = rs.getString("tip_descri");
            modtabla.addRow(datos);
        }
    } catch (SQLException ex) {
        Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    private void cargar_combo() {
        try {
            rs = con.listar_datos("SELECT CONCAT(cod_tipo,'-',tip_descri) AS tipcliente FROM tiipo_cliente");
            this.cbo_tipo.removeAllItems();
            while (rs.next()) {
                this.cbo_tipo.addItem(rs.getString("tipcliente"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(clientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void grabar() {
    String codCliente = txtcod.getText().toUpperCase();
    String ciCliente = txtci.getText().toUpperCase();
    String nom = txtnombre.getText().toUpperCase();
    
    
    if (con.existeRegistro("clientes", "cli_nombre = " + nom.toUpperCase())) {
        JOptionPane.showMessageDialog(null, "El nombre del cliente ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        return; 
    }
        if (con.existeRegistro("clientes", "cod_clie = " + codCliente.toUpperCase())) {
        JOptionPane.showMessageDialog(null, "El código de cliente ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        return; 
    }
    
    
    if (con.existeRegistro("clientes", "cli_ci = '" + ciCliente + "'")) {
        JOptionPane.showMessageDialog(null, "El número de cédula ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
        return; 
    }
    
    if (ban == 1) {
        con.insertar_datos("clientes", "cod_clie,cli_nombre,cli_apellido,cli_ci,cli_direc,cli_celular,cod_tipo",
                codCliente + ",'"
                + txtnombre.getText().toUpperCase() + "','"
                + txtapellido.getText().toUpperCase() + "','"
                + ciCliente + "','"
                + txtdireccion.getText().toUpperCase() + "','"
                + txttel.getText() + "',"
                + "(split_part('" + cbo_tipo.getSelectedItem() + "','-',1))::integer",
                0);
    }
    if (ban == 2) {
        con.actualizar_datos("clientes", "cli_nombre='" + txtnombre.getText().toUpperCase()
                + "',cli_apellido='" + txtapellido.getText().toUpperCase()
                + "',cli_ci='" + ciCliente
                + "',cli_direc='" + txtdireccion.getText().toUpperCase()
                + "',cli_celular='" + txttel.getText().toUpperCase()
                + "',cod_tipo=" + "(split_part('" + cbo_tipo.getSelectedItem() + "','-',1))::integer",
                "cod_clie=" + codCliente);
    }
}


    private void limpiargrilla() {
        while (modtabla.getRowCount() > 0) {
            modtabla.removeRow(0);
        }
    }

    private void visualizar() {
        int fila = grilla.getSelectedRow();
        txtcod.setText(modtabla.getValueAt(fila, 0).toString());
        txtci.setText(modtabla.getValueAt(fila, 1).toString());
        txtnombre.setText(modtabla.getValueAt(fila, 2).toString());
        txtapellido.setText(modtabla.getValueAt(fila, 3).toString());
        txtdireccion.setText(modtabla.getValueAt(fila, 4).toString());
        txttel.setText(modtabla.getValueAt(fila, 5).toString());
        cbo_tipo.setSelectedItem(modtabla.getValueAt(fila, 6).toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtcod = new javax.swing.JTextField();
        txtci = new javax.swing.JTextField();
        txtnombre = new javax.swing.JTextField();
        txtapellido = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtdireccion = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txttel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        cbo_tipo = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        grilla = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        btnfiltrar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btn_nuevo = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_grabar = new javax.swing.JButton();
        btn_borrar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Cliente"));

        jLabel1.setText("Codigo:");

        jLabel2.setText("C.I. Nro:");

        jLabel3.setText("Nombre:");

        jLabel4.setText("Apellido:");

        txtcod.setEnabled(false);

        txtci.setEnabled(false);
        txtci.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtciKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtciKeyPressed(evt);
            }
        });

        txtnombre.setEnabled(false);
        txtnombre.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtnombreKeyPressed(evt);
            }
        });

        txtapellido.setEnabled(false);
        txtapellido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtapellidoKeyPressed(evt);
            }
        });

        jLabel5.setText("Direccion");

        txtdireccion.setEnabled(false);
        txtdireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtdireccionKeyPressed(evt);
            }
        });

        jLabel6.setText("Telefono:");

        txttel.setEnabled(false);
        txttel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttelActionPerformed(evt);
            }
        });
        txttel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txttelKeyPressed(evt);
            }
        });

        jLabel7.setText("Tipo Cliente");

        cbo_tipo.setEnabled(false);
        cbo_tipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbo_tipoActionPerformed(evt);
            }
        });
        cbo_tipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbo_tipoKeyPressed(evt);
            }
        });

        grilla.setModel(modtabla);
        grilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grillaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(grilla);

        jLabel8.setText("Buscar");

        btnfiltrar.setText("Filtrar");
        btnfiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfiltrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtci, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(100, 100, 100)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtapellido))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(12, 12, 12)
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnfiltrar))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txttel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbo_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtnombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4)
                    .addComponent(txtci, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtapellido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtdireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(txttel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(cbo_tipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnfiltrar))
                .addGap(24, 24, 24)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
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
        btn_grabar.setEnabled(false);
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
                .addGap(78, 78, 78)
                .addComponent(btn_nuevo)
                .addGap(43, 43, 43)
                .addComponent(btn_editar)
                .addGap(41, 41, 41)
                .addComponent(btn_grabar)
                .addGap(41, 41, 41)
                .addComponent(btn_borrar)
                .addGap(40, 40, 40)
                .addComponent(btn_cancelar)
                .addGap(50, 50, 50)
                .addComponent(btn_salir)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_editar)
                    .addComponent(btn_grabar)
                    .addComponent(btn_borrar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salir))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_borrarActionPerformed
        ban = 3;
    }//GEN-LAST:event_btn_borrarActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        ban = 1;
        this.habilitar(0);
        this.txtci.setEnabled(true);
        this.txtci.requestFocus();
        this.gencod();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void txtciKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtciKeyTyped
        int k = evt.getKeyChar();
        if ((k >= 32 && k <= 45) || (k >= 58 && k <= 126)) {
            evt.setKeyChar( (char) KeyEvent.VK_CLEAR );
            getToolkit().beep();///el sonido del error
            evt.consume();////mantiene el pulsor al presionar espacio
            JOptionPane.showMessageDialog(null, "No puede ingresar letras");
            txtci.requestFocus();
        }
    }//GEN-LAST:event_txtciKeyTyped

    private void txtciKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtciKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtci.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            } else {
                this.txtnombre.setEnabled(true);
                this.txtnombre.grabFocus();
            }
        }
    }//GEN-LAST:event_txtciKeyPressed

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        dispose();
    }//GEN-LAST:event_btn_salirActionPerformed

    private void cbo_tipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbo_tipoActionPerformed

    }//GEN-LAST:event_cbo_tipoActionPerformed

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
        this.grabar();
        this.limpiargrilla();
        this.cargargrilla();
        this.limpiar();
        this.habilitar(1);
    }//GEN-LAST:event_btn_grabarActionPerformed

    private void txtnombreKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtnombreKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtnombre.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            } else {
                this.txtapellido.setEnabled(true);
                this.txtapellido.requestFocus();
                this.txtapellido.grabFocus();
//                System.out.println(txtci.getText().length());
            }
        }
    }//GEN-LAST:event_txtnombreKeyPressed

    private void txtapellidoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtapellidoKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtapellido.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            } else {
                this.txtdireccion.setEnabled(true);
                this.txtdireccion.grabFocus();
//                System.out.println(txtci.getText().length());
            }
        }
    }//GEN-LAST:event_txtapellidoKeyPressed

    private void txtdireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtdireccionKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtdireccion.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            } else {
                this.txttel.setEnabled(true);
                this.txttel.grabFocus();
//                System.out.println(txtci.getText().length());
            }
        }
    }//GEN-LAST:event_txtdireccionKeyPressed

    private void txttelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txttelKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txttel.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            } else {
                this.cbo_tipo.setEnabled(true);
                this.cbo_tipo.grabFocus();
//                System.out.println(txtci.getText().length());
            }
        }
    }//GEN-LAST:event_txttelKeyPressed

    private void cbo_tipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_tipoKeyPressed
        if (evt.getKeyCode() == 10) {
            btn_grabar.setEnabled(true);
            btn_grabar.grabFocus();
        }
    }//GEN-LAST:event_cbo_tipoKeyPressed

    private void btnfiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfiltrarActionPerformed
        this.filtrar();
    }//GEN-LAST:event_btnfiltrarActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
        ban = 2;
        this.habilitar(0);
        this.txtci.setEnabled(true);
        this.txtci.requestFocus();
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_grabarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_grabarKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER) {
            this.grabar();
            this.limpiargrilla();
            this.cargargrilla();
            this.limpiar();
            this.habilitar(1);
        }
    }//GEN-LAST:event_btn_grabarKeyPressed

    private void grillaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grillaMouseClicked
        this.visualizar();

        if (ban == 3) {
            int mensaje = JOptionPane.showConfirmDialog(this, "Deseas Borrar-->"
                    + txtci.getText(), "Confirmar", JOptionPane.YES_NO_OPTION);
            if (mensaje == JOptionPane.YES_OPTION) {
                con.BorrarDatos("clientes", "cod_clie", txtcod.getText());
                this.limpiargrilla();
                this.cargargrilla();
                this.limpiar();
                ban = 0;
            }
        }
    }//GEN-LAST:event_grillaMouseClicked

    private void txttelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttelActionPerformed

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
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(clientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                clientes dialog = new clientes(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btn_grabar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_salir;
    private javax.swing.JButton btnfiltrar;
    private javax.swing.JComboBox cbo_tipo;
    private javax.swing.JTable grilla;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField txtapellido;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtci;
    private javax.swing.JTextField txtcod;
    private javax.swing.JTextField txtdireccion;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txttel;
    // End of variables declaration//GEN-END:variables
}
