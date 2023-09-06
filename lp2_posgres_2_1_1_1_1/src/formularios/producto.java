
package formularios;

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
public class producto extends javax.swing.JFrame {
conecbd con;
ResultSet rs;
int ban;
    // definir modelo de tabla
DefaultTableModel tablapro = new DefaultTableModel();
    /**
     * Creates new form producto
     */
    public producto(java.awt.Frame parent, boolean modal) {
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.configrilla();
        this.cargargrilla();
    }

 


    
  private void configrilla(){
    tablapro.addColumn("pro_cod");
    tablapro.addColumn("pro_descri");
    tablapro.addColumn("pro_precio");
    tablapro.addColumn("pro_iva");
    tablapro.addColumn("pro_min");
    tablapro.addColumn("pro_max");
    tablapro.addColumn("cant_stock");
    tabla_pro.getColumnModel().getColumn(0).setPreferredWidth(10);
    tabla_pro.getColumnModel().getColumn(1).setPreferredWidth(50);
    tabla_pro.getColumnModel().getColumn(2).setPreferredWidth(10);
    tabla_pro.getColumnModel().getColumn(3).setPreferredWidth(10);
    tabla_pro.getColumnModel().getColumn(4).setPreferredWidth(10);
    tabla_pro.getColumnModel().getColumn(5).setPreferredWidth(10);
    tabla_pro.getColumnModel().getColumn(6).setPreferredWidth(10);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cod_pro = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        des_pro = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        precio_pro = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        buscar_pro = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        pro_stock = new javax.swing.JTextField();
        filtrar_pro = new javax.swing.JButton();
        btn_nuevo = new javax.swing.JButton();
        btn_editar = new javax.swing.JButton();
        btn_grabar = new javax.swing.JButton();
        btn_borrar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla_pro = new javax.swing.JTable();
        pro_iva = new javax.swing.JTextField();
        pro_min = new javax.swing.JTextField();
        pro_max = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 24)); // NOI18N
        jLabel1.setText("Producto");
        jLabel1.setToolTipText("");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setIconTextGap(5);
        jLabel1.setName(""); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(49, 40));

        jLabel2.setText("Código");

        jLabel3.setText("Descripción");

        des_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                des_proActionPerformed(evt);
            }
        });
        des_pro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                des_proKeyPressed(evt);
            }
        });

        jLabel4.setText("Precio");

        precio_pro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                precio_proKeyPressed(evt);
            }
        });

        jLabel5.setText("Buscar");

        jLabel6.setText("IVA");

        jLabel9.setText("Stock");

        pro_stock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pro_stockActionPerformed(evt);
            }
        });
        pro_stock.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pro_stockKeyPressed(evt);
            }
        });

        filtrar_pro.setText("Filtrar");
        filtrar_pro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filtrar_proActionPerformed(evt);
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

        btn_grabar.setText("Grabar");
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

        tabla_pro.setModel(tablapro);
        tabla_pro.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabla_pro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabla_proMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabla_pro);
        tabla_pro.getAccessibleContext().setAccessibleName("");
        tabla_pro.getAccessibleContext().setAccessibleDescription("");

        pro_iva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pro_ivaActionPerformed(evt);
            }
        });
        pro_iva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pro_ivaKeyPressed(evt);
            }
        });

        pro_min.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pro_minActionPerformed(evt);
            }
        });
        pro_min.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pro_minKeyPressed(evt);
            }
        });

        pro_max.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pro_maxKeyPressed(evt);
            }
        });

        jLabel7.setText("Mínimo");

        jLabel8.setText("Máximo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(btn_nuevo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btn_editar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btn_grabar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btn_borrar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btn_cancelar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btn_salir))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(211, 211, 211)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(33, 33, 33)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(filtrar_pro)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel5))
                                            .addGap(33, 33, 33)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(buscar_pro)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(jLabel9))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(precio_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jLabel8))))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel3)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(jLabel2)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(cod_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(157, 157, 157)
                                                    .addComponent(jLabel6))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addComponent(des_pro, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(85, 85, 85)
                                                    .addComponent(jLabel7)))))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pro_max, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pro_min, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pro_stock, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(pro_iva, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cod_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(des_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(precio_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(pro_iva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pro_min, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pro_max, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel9)
                        .addComponent(pro_stock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(buscar_pro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filtrar_pro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_editar)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_grabar)
                    .addComponent(btn_borrar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salir))
                .addGap(131, 131, 131))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 451, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
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
      private void grabar(){
        if (ban == 1){
        con.insertar_datos("producto", "pro_cod, pro_descri, pro_precio, pro_iva, pro_min, pro_max, cant_stock ",
                cod_pro.getText() + ",'"
                + des_pro.getText() + "','"
                + precio_pro.getText() + "','"
                +pro_iva.getText() + "','"
                + pro_min.getText() + "','" 
                + pro_max.getText() + "','"
                + pro_stock.getText() + "'", 0);   
        } 
        if (ban == 2){
            con.actualizar_datos2("producto", "pro_descri='" + des_pro.getText()
                    + "',pro_precio='" + precio_pro.getText()
                    + "',pro_iva='" + pro_iva.getText()
                    + "',pro_min='" + pro_min.getText()
                    + "',pro_max='" + pro_max.getText()
                    + "',cant_stock='" + pro_stock.getText()+"'"
                    ,"pro_cod=" + cod_pro.getText());  
        }
        
        
        
    }

     private void limpiargrilla(){
        while (tablapro.getRowCount()>0){
        tablapro.removeRow(0);
        }
     }
     
         private void gencod(){
        try {
            rs = con.listar_datos("SELECT COALESCE(MAX(pro_cod), 0) + 1 AS codigo FROM producto;");
            rs.next();
            cod_pro.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(producto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
       ban = 1;
       this.habilitar(0);
        this.gencod();
        this.des_pro.setEnabled(true);
        this.des_pro.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void btn_editarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editarActionPerformed
 ban=2;
 this.habilitar(0);
 this.des_pro.setEnabled(true);
 this.des_pro.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_btn_editarActionPerformed

    private void btn_borrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_borrarActionPerformed
ban = 3;         // TODO add your handling code here:
    }//GEN-LAST:event_btn_borrarActionPerformed
   private void limpiar(){
        this.cod_pro.setText("");
        this.des_pro.setText("");
        this.pro_stock.setText("");
        this.precio_pro.setText("");
        this.pro_min.setText("");
        this.pro_max.setText("");
        this.pro_iva.setText("");
   }
     private void cargargrilla() {
    try {
        String[] datos = new String[7];
        rs = con.listar_datos("select * from producto order by pro_cod asc");
        
        // Verificar si el objeto ResultSet no es nulo antes de acceder a los datos
        if (rs != null) {
            while (rs.next()) {
                datos[0] = rs.getString("pro_cod");
                datos[1] = rs.getString("pro_descri");
                datos[2] = rs.getString("pro_precio");
                datos[3] = rs.getString("pro_iva");
                datos[4] = rs.getString("pro_min");
                datos[5] = rs.getString("pro_max");
                datos[6] = rs.getString("cant_stock");
                
                tablapro.addRow(datos);
            }
        } else {
            // Manejar el caso en que el ResultSet sea nulo (puede ser un error de conexión)
            JOptionPane.showMessageDialog(null, "Error al obtener datos de la base de datos");
        }
    } catch (SQLException ex) {
        Logger.getLogger(producto.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        this.limpiar();
        this.habilitar(1);
        this.limpiargrilla();
        this.cargargrilla();        // TODO add your handling code here:
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
grabar();
       this.limpiar();
       this.habilitar(1);
       this.limpiargrilla();
       this.cargargrilla();
       this.des_pro.setEnabled(false);
       this.btn_nuevo.grabFocus();        
    }//GEN-LAST:event_btn_grabarActionPerformed
   private void filtrar() {
        try {
            tablapro.setRowCount(0);
            String[] datos = new String[2];
            rs = con.listar_datos("select * from producto where pro_descri like '%" + buscar_pro.getText() + "%'");
            while (rs.next()){
                datos [0] = rs.getString("pro_cod");
                datos [1] = rs.getString("pro_descri");
                tablapro.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(cargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       private void visualizarDatos (){
        int fila = tabla_pro.getSelectedRow();
        cod_pro.setText(tablapro.getValueAt(fila, 0).toString());
        des_pro.setText(tablapro.getValueAt(fila, 1).toString());
        precio_pro.setText(tablapro.getValueAt(fila, 2).toString());
        pro_iva.setText(tablapro.getValueAt(fila, 3).toString());
        pro_min.setText(tablapro.getValueAt(fila, 4).toString());
        pro_max.setText(tablapro.getValueAt(fila, 5).toString());
        pro_stock.setText(tablapro.getValueAt(fila, 6).toString()
        
        
        
        );    
    }
    private void des_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_des_proActionPerformed

    }//GEN-LAST:event_des_proActionPerformed

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_btn_salirActionPerformed

    private void filtrar_proActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filtrar_proActionPerformed
        this.filtrar();        // TODO add your handling code here:
    }//GEN-LAST:event_filtrar_proActionPerformed

    private void des_proKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_des_proKeyPressed
        if(evt.getKeyCode() == 10){
            this.precio_pro.setEnabled(true);
            this.precio_pro.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_des_proKeyPressed

    private void precio_proKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precio_proKeyPressed
            if(evt.getKeyCode() == 10){
            this.pro_iva.setEnabled(true);
            this.pro_iva.requestFocus();
            }// TODO add your handling code here:
    }//GEN-LAST:event_precio_proKeyPressed

    private void pro_ivaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pro_ivaKeyPressed
 if(evt.getKeyCode() == 10){
            this.pro_min.setEnabled(true);
            this.pro_min.requestFocus();
 }    // TODO add your handling code here:
    }//GEN-LAST:event_pro_ivaKeyPressed

    private void pro_minActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_minActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pro_minActionPerformed

    private void pro_minKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pro_minKeyPressed
if(evt.getKeyCode() == 10){
            this.pro_max.setEnabled(true);
            this.pro_max.requestFocus();
}  // TODO add your handling code here:
    }//GEN-LAST:event_pro_minKeyPressed

    private void pro_maxKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pro_maxKeyPressed
if(evt.getKeyCode() == 10){
            this.pro_stock.setEnabled(true);
            this.pro_stock.requestFocus();
}    // TODO add your handling code here:
    }//GEN-LAST:event_pro_maxKeyPressed

    private void pro_ivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_ivaActionPerformed

        

// TODO add your handling code here:
    }//GEN-LAST:event_pro_ivaActionPerformed

    private void pro_stockKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pro_stockKeyPressed
        if (evt.getKeyCode() == 10){
        this.btn_grabar.setEnabled(true);
        this.btn_grabar.requestFocus();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_pro_stockKeyPressed

    private void pro_stockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pro_stockActionPerformed

        btn_grabar.requestFocus();        // TODO add your handling code here:
    }//GEN-LAST:event_pro_stockActionPerformed

    private void tabla_proMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabla_proMouseClicked
        this.visualizarDatos();
        if(ban==3){
            int mensaje = JOptionPane.showConfirmDialog(this, " Deseas borrar ----->" + des_pro.getText(),"Confirmar",JOptionPane.YES_NO_OPTION);
            if(mensaje == JOptionPane.YES_OPTION) {
                con.BorrarDatos("producto", "pro_cod", cod_pro.getText());
                this.limpiargrilla();
                this.cargargrilla();
                this.limpiar();
                ban=0;
            }
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tabla_proMouseClicked

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
            java.util.logging.Logger.getLogger(producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(producto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new producto(null, true).setVisible(true);
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
    private javax.swing.JTextField buscar_pro;
    private javax.swing.JTextField cod_pro;
    private javax.swing.JTextField des_pro;
    private javax.swing.JButton filtrar_pro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField precio_pro;
    private javax.swing.JTextField pro_iva;
    private javax.swing.JTextField pro_max;
    private javax.swing.JTextField pro_min;
    private javax.swing.JTextField pro_stock;
    private javax.swing.JTable tabla_pro;
    // End of variables declaration//GEN-END:variables
}
