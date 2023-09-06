package formularios;

import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import prg.conecbd;

public class compras extends javax.swing.JDialog {

    //instanciar la conexion
    conecbd con;
    ResultSet rs, rs1, rs2, rs3;
    //definicion de cursores
    javax.swing.table.DefaultTableModel cursor;
    javax.swing.table.DefaultTableModel cursor_cabe;
    javax.swing.table.DefaultTableModel cursor_det;
    int ban;//para identificar las operaciones tanto insert, update y delete
    String condicion;//para contado o credito
    //para calculo del iva
    int total = 0;
    int totaliva5 = 0;
    int totaliva10 = 0;
    int totaliva = 0;
    int resultado = 0;
    //definicion de variable
    int r, r1 = 0;

    public compras(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        con = new conecbd();
        con.conectar();
        this.cargar_combo();
        this.fecha();
        this.ivaporc.setVisible(false);
        //System.out.println(txtfecha.getText().substring(6, 10));
    }

    private void gencod() {
        try {
            rs = con.listar_datos("SELECT coalesce(MAX(com_cod),0)+1 AS codigo FROM compra");
            rs.next();
            txtcod.setText(rs.getString("codigo"));
        } catch (SQLException ex) {
            Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void habilitar(int estado) {
        switch (estado) {
            case 0:
                btn_anular.setEnabled(false);
                btn_grabar.setEnabled(false);
                btn_cancelar.setEnabled(true);
                btn_salir.setEnabled(true);
                btn_nuevo.setEnabled(false);
                break;
            case 1:
                btn_anular.setEnabled(true);
                btn_grabar.setEnabled(false);
                btn_cancelar.setEnabled(true);
                btn_salir.setEnabled(true);
                btn_nuevo.setEnabled(true);
                break;
        }
    }

    private void cargar_combo() {
        try {
            rs1 = con.listar_datos("SELECT CONCAT(prov_cod,'-',prov_denominacion)AS proveedor FROM proveedor");
            this.cbo_prove.removeAllItems();
            while (rs1.next()) {
                this.cbo_prove.addItem(rs1.getString("proveedor"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fecha() {
        try {
            rs1 = con.listar_datos("SELECT to_char(now(), 'DD/MM/YYYY') AS fecha");
            rs1.next();
            txtfecha.setText(rs1.getString("fecha"));
        } catch (SQLException ex) {
            Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscar_producto() {
        try {
            rs2 = con.listar_datos("SELECT * "
                    + " FROM producto "
                    + " WHERE pro_cod = " + txtpro.getText() );
            boolean localizar = rs2.next();

            if (localizar == true) {
                this.txtdescri.setText(rs2.getString("pro_descri"));
                this.txtprecio.setText(rs2.getString("pro_precio"));
                this.ivaporc.setText(rs2.getString("pro_iva"));
            } else {
                JOptionPane.showMessageDialog(null,
                        "El producto que desea consultar no existe..!");
                txtpro.setText("");
                txtpro.requestFocus();

            }
        } catch (SQLException ex) {
            Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void cargar_tabla() {
        //validacion para que no se repita el item en la tabla
        int variable = 0;
        cursor = (DefaultTableModel) this.grilla.getModel();//definimos el cursor para nuestra tabla
        for (int c = 0; c < cursor.getRowCount(); c++) {
            //este if es en el caso que el producto ya exista en nuestra lista en la tabla
            if (cursor.getValueAt(c, 0).equals(txtpro.getText())) {
                //convierte los valores a enteros para poder realizar la operacion de suma cantidad ingresada por el usuario y la cantidad
                //que se encuentra ya en la tabla en la columna 3
                r = Integer.parseInt(txtcant.getText()) + Integer.parseInt(String.valueOf(cursor.getValueAt(c, 3)));
                //asignamos a nuestra columna 3 que seria cantidad la variable r a nuestro cursor tabla
                cursor.setValueAt(r, c, 3);
                
                //ahora se realiza la multiplicacion de la suma de la cantidad de la variable r * el precio del producto.
                r1 = r * Integer.parseInt(txtprecio.getText());
                
                //aqui asignamos los valores obtenido de la variable r1 del calculo precio * cantidad y vamos
                //asignando a las columnas que corresponde dependiendo del porcentaje de iva que maneja cada producto
                //0=exenta, 5=iva 5, 10=iva 10
                
                if (ivaporc.getText().equals("0")) {
                    cursor.setValueAt(r1, c, 4);

                }
                if (ivaporc.getText().equals("5")) {
                    cursor.setValueAt(r1, c, 5);
                }
                if (ivaporc.getText().equals("10")) {
                    cursor.setValueAt(r1, c, 6);
                }
                variable = 1;
            }
        }
        if (variable == 1) {
            calcular_subtotales();
            txtpro.setText("");
            txtpro.requestFocus();
            txtdescri.setText("");
            txtprecio.setText("");
            txtcant.setText("");
        } else {

            //cargar la tabla si todo esta bien
            int subtotal = 0;//definir una varible subtotal
            //esto realizara la operacion de calculo cantidad * precio unitario, pero primero debemos convertir nuestas variables
            //a valores enteros para realizar la operacion. Y utilizamos Integer.parseInt() que convertira a un valor manejable numerico
            subtotal = Integer.parseInt(txtcant.getText()) * Integer.parseInt(txtprecio.getText());
            //la variable total es la que se definio al inicio de nuestro formulario
            total = total + subtotal;
            //aqui asignamos el total a nuestra variable txttotal, para asignar y setear la variable debemos convertir nuestro valor total a un string
            //para lograr eso utilizamos el String.valueOf(), que se encarga de realizar el cambio numerico a un string
            txttotal.setText(String.valueOf(total));
            
            
            //en este sector como el anterior debemos preguntar el porcentaje de iva que maneja cada producto
            //y la variable ivaporc es la variable auxiliar que nos ayuda a identificar, cuando un usuario busca el producto 
            //recupera el producto, precio y el porcentaje de iva que posee
            //si el caso fuera exenta que es 0
            if (ivaporc.getText().equals("0")) {
                //entrara en este if, y debemos cargar nuestra tabla cuando el usuario presiona enter en el campo cantidad de nuestro formulario
                //de compras.  
                //Definimos un array de tipo String 
                //definimos nuestra variable con el nombre de campos
                //Instanciamos el array con new String[] y especificando con corchetes que es un array
                //Seguido de eso abrimos llave para poder especificar los datos que vamos almacenar en la variable campos de tipo array
                String campo[] = new String[]
                {
                    //estos datos son los del producto
                    txtpro.getText(), 
                    txtdescri.getText(),
                    txtprecio.getText(), 
                    txtcant.getText(), 
                    String.valueOf(subtotal),//y el calculo del subtotal que colocaremos en la columna exenta si fuese el caso
                    "0", //las columna de gravada 5 ira con valor 0
                    "0" // la columna de gravada 10 tambien ira con valor 0
                };
                //obteniendo esos datos y el array formado en la variable string campo
                //disponemos en agregar ese array a nuestro cursor, para que ser encargue de agregar las filas a la tabla
                cursor.addRow(campo);

            }
            //esta seccion es la misma explicacion del anterior esto dependera de cada producto y que tipo de iva maneja para poder ingresar en 
            //los if que corresponde tanto 0, 5 o 10 %
            if (ivaporc.getText().equals("5")) {
                String campo[] = new String[]
                {
                    txtpro.getText(),
                    txtdescri.getText(), 
                    txtprecio.getText(),
                    txtcant.getText(), 
                    "0", 
                    String.valueOf(subtotal), 
                    "0"
                };
                //agregamos el array campos a nuestro cursor para que pueda agregar las filas
                cursor.addRow(campo);
                //aqui realizamos el calculo del iva en este caso para iva 5%
                //la variable totaliva5 fue definida al comienzo de nuestro formulario
                //para poder realizar el calculo del iva 5 debemos dividir el subtotal entre 21
                totaliva5 = totaliva5 + (subtotal / 21);
                this.iva5.setText(String.valueOf(totaliva5));

            }
            if (ivaporc.getText().equals("10")) {
                String campo[] = new String[]
                {
                    txtpro.getText(),
                    txtdescri.getText(), 
                    txtprecio.getText(),
                    txtcant.getText(), 
                    "0", //exenta en 0
                    "0", //gravada 5 en 0
                    String.valueOf(subtotal)//y el valor del subtotal en la columna del gravada 10
                };
                //agregamos los datos al cursor para que nos cree la fila en la tabla
                cursor.addRow(campo);
                //se realiza el calculo del iva 10 para eso hay que dividirlo el subtotal entre 11
                totaliva10 = totaliva10 + (subtotal / 11);
                //obteniendo el calculo de iva 10 seteamos nuestra variable iva10 y asignamos el valor de la division
                this.iva10.setText(String.valueOf(totaliva10));//la variable iva10 es la de nuestro formulario que creamos, 
                //eso puede variar segun el nombre que le asignaron

            }

            //ahora utilizamos nuestra variable totaliva que tambien fue definida al inicio del formulario
            totaliva = totaliva5 + totaliva10;//sumamos el totaliva5 + el totaliva10 para obtener el totaliva
            //luego asignamos ese calculo a nuestra variable el formulario txtiva
            this.txtiva.setText(String.valueOf(totaliva));
            calcular_subtotales();//llama al metodo calcular_subtotales

            //limpiar los campos y poner el cursor en txtpro
            txtpro.setText("");
            txtpro.requestFocus();
            txtdescri.setText("");
            txtprecio.setText("");
            txtcant.setText("");
        }
    }

    private void calcular_subtotales() {
        int total1 = 0;
        int total2 = 0;
        int total3 = 0;
        int total4 = 0;

        for (int a = 0; a < grilla.getRowCount(); a++) {

            String exe = String.valueOf(cursor.getValueAt(a, 4));
            String gr5 = String.valueOf(cursor.getValueAt(a, 5));
            String gr10 = String.valueOf(cursor.getValueAt(a, 6));

            int mon1 = Integer.parseInt(exe);
            int mon2 = Integer.parseInt(gr5);
            int mon3 = Integer.parseInt(gr10);

            total1 = mon1 + total1;
            total2 = mon2 + total2;
            total3 = mon3 + total3;
            total4 = total1 + total2 + total3;
        }
        //asignar los valores de calculo a los campos correspondiente
        txtsubtotal.setText(String.valueOf(total1));
        txtsubtotal2.setText(String.valueOf(total2));
        txtsubtotal3.setText(String.valueOf(total3));
        txttotal.setText(String.valueOf(total4));
    }

    private void grabar() {
        if (ban == 1) {
            //realizar un subtring a la fecha para grabar en la base de datos con formato yyyy-mm-dd
            //utilizamos la operacion substring para poder convertir nuestra fecha a un objeto que podamos
            //grabar en la base de datos ya que solo se puede insertar con formato yyyy-mm-dd
            String fecha = txtfecha.getText().substring(6, 10) + "-" 
                    + txtfecha.getText().substring(3, 5) + "-" 
                    + txtfecha.getText().substring(0, 2);
            //llamamos a nuestra conexion y utilizamos la funcion insertar datos
            //donde insertaremos los datos primeramente de la cebecera de compra
            con.insertar_datos("compra", "com_cod,com_fecha,com_condicion,com_importe_total,nro_factura,estado,com_cuo,prov_cod,usu_cod",
                    txtcod.getText() + ",'"
                    + fecha + "','"
                    + condicion + "',"
                    + txttotal.getText() + ","
                    + txtfac.getText() + ", 'REALIZADO',"
                    + txtcuo.getText() + ","
                    + "(split_part('" + cbo_prove.getSelectedItem() + "','-',1))::integer,"
                    + acceso.codigo, 0);

            //esta seccion seria para poder grabar el detalle compra en la tabla detalle_compra
            //para poder guardar los detalle creamos el for para recorrer nuestra grilla
            //para poder capturar los valores de la grilla de las columnas, lo realizamos de la siguiente manera:
            //nuestra variable grilla con eso utilizamo getValueAt(a seria fila, y el valor columna)
            for (int a = 0; a < grilla.getRowCount(); a++) {
                con.insertar_datos("detalle_compra", "pro_cod,com_cod,cantidad,exenta,gra5,gra10,precio_unit",
                        grilla.getValueAt(a, 0) + ","
                        + txtcod.getText() + ","
                        + grilla.getValueAt(a, 3) + ","
                        + grilla.getValueAt(a, 4) + ","
                        + grilla.getValueAt(a, 5) + ","
                        + grilla.getValueAt(a, 6) + ","
                        + grilla.getValueAt(a, 2), 1);
            }
        }
    }

    private void remover_fila_grilla() {

        //utilizamos nuestra variable cursor y almacenamos nuestro modelo de grilla
        cursor = (DefaultTableModel) grilla.getModel();
        //definimos una variable de tipo entero filsel, la cual nos ayudara a capturar la fila que el usuario haya seleccionado
        int filsel = grilla.getSelectedRow();
        //si el usuario en el caso no haya seleccionado nada debere mostrar una alerta con el mensaje siguiente
        if (filsel == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar el registro que desea eliminar", "Atencion", 2);
        } else {//si el caso que haya seleccionado una fila se procesadera a borrar la fila seleccionada
            //para ello nuestar variable cursor tomara unas de las funciones habilitadas, en este caso removeRow(fila a ser borrada),
            //removeRow borrara la fila de la tabla.
            cursor.removeRow(filsel);
            txtpro.requestFocus();
        }
    }

    private void removerDato() {
        //definimos nuestra variables
        int iv5 = 0;
        int iv10 = 0;
        int tiva = 0;
        int totales = 0;

        //recorremos nuestra variable grilla con un for para capturar los valores exenta, grav5 y grav10
        for (int a = 0; a < grilla.getRowCount(); a++) {
            //capturamos en variables las columnas
            String exe = String.valueOf(cursor.getValueAt(a, 4));
            String gr5 = String.valueOf(cursor.getValueAt(a, 5));
            String gr10 = String.valueOf(cursor.getValueAt(a, 6));
            
            //parseamos los datos capturados y convertimos en valor entero
            int exenta = Integer.parseInt(exe);
            int grav5 = Integer.parseInt(gr5);
            int grav10 = Integer.parseInt(gr10);

            //realizamos los calculos para las columnas de los subtotales y calcular el iva
            iv5  = iv5 + (grav5 / 21);//calculo para iva 5 dividir entre 21
            iv10 = iv10 + (grav10 / 11);//calculo para iva 10 dividir entre 11
            tiva = iv5 + iv10;
            
            //caputurar los valores de nuestro campos del formulario de las variables txtsubtotal que son 3
            int subtotal1 = Integer.parseInt(txtsubtotal.getText());
            int subtotal2 = Integer.parseInt(txtsubtotal2.getText());
            int subtotal3 = Integer.parseInt(txtsubtotal3.getText());

            //sumamos para obtener el total general
            totales = subtotal1 + subtotal2 + subtotal3;
        }

        //aqui asignamos los calculos del iva obtenido de nuestros calculos realizados y seteamos las variables de los campos de nuestro formulario
        iva5.setText(String.valueOf(iv5));
        iva10.setText(String.valueOf(iv10));
        txtiva.setText(String.valueOf(tiva));
        txttotal.setText(String.valueOf(totales));

        totaliva5 = Integer.parseInt(iva5.getText());
        totaliva10 = Integer.parseInt(iva10.getText());
        total = Integer.parseInt(txttotal.getText());
    }

    private void cargargrilla(boolean buscar) {
        try { 
            cursor_cabe = (DefaultTableModel) grilla_compra.getModel();
            String filtrar = "";
            if(buscar==true && !txtbuscar.getText().equals("")){
            filtrar = " and a.nro_factura ='" + txtbuscar + "'";
          }
            String [] datos = new String[5];
                String sql = "SELECT a.com_cod, a.com_condicion,"
                        + "a.com_importe_total, a.nro_factura,"
                        + "b.prov_denominacion"
                        + "FROM compra a"
                        + "join proveedor b on b.prov_cod = a.prov_cod"
                        + "WHERE a.estado = 'REALIZADO'"
                        +filtrar;
      
            rs = con.listar_datos("SELECT a.com_cod,a.com_condicion,"
                    + " a.com_importe_total,a.nro_factura,"
                    + " b.prov_denominacion "
                    + " FROM compra a "
                    + " join proveedor b on b.prov_cod = a.prov_cod"
                    + " WHERE a.estado='REALIZADO' "
                    + " and a.nro_factura ='" + txtbuscar.getText() + "'");
            while (rs.next()) {
                datos[0] = rs.getString("com_cod");
                datos[1] = rs.getString("prov_denominacion");
                datos[2] = rs.getString("nro_factura");
                datos[3] = rs.getString("com_condicion");
                datos[4] = rs.getString("com_importe_total");
                cursor_cabe.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cargar_det() {
        try {
            cursor_det = (DefaultTableModel) grilla_det.getModel();
            String[] datos = new String[4];
            rs = con.listar_datos("SELECT b.pro_cod,b.com_cod,b.cantidad,"
                    + " b.precio_unit,c.pro_descri,a.nro_factura "
                    + " FROM compra a, detalle_compra b, producto c "
                    + " WHERE b.com_cod=a.com_cod AND b.pro_cod=c.pro_cod "
                    + " and a.estado='REALIZADO' "
                    + " AND a.nro_factura ='" + txtbuscar.getText() + "'");
            while (rs.next()) {
                datos[0] = rs.getString("pro_cod");
                datos[1] = rs.getString("pro_descri");
                datos[2] = rs.getString("cantidad");
                datos[3] = rs.getString("precio_unit");
                cursor_det.addRow(datos);
            }
        } catch (SQLException ex) {
            Logger.getLogger(compras.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpiargrilla() {
        cursor_cabe = (DefaultTableModel) grilla_compra.getModel();
        while (cursor_cabe.getRowCount() > 0) {
            cursor_cabe.removeRow(0);
        }
    }

    private void limpiargrilla2() {
        cursor = (DefaultTableModel) this.grilla.getModel();
        while (cursor.getRowCount() > 0) {
            cursor.removeRow(0);
        }
    }

    private void limpiargrilladetalle() {
        cursor_det = (DefaultTableModel) this.grilla_det.getModel();
        while (cursor_det.getRowCount() > 0) {
            cursor_det.removeRow(0);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtcod = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbo_prove = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        txtfecha = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtfac = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        contado = new javax.swing.JCheckBox();
        credito = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        txtcuo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtintervalo = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        grilla = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtpro = new javax.swing.JTextField();
        txtdescri = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtprecio = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtcant = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        btn_nuevo = new javax.swing.JButton();
        btn_anular = new javax.swing.JButton();
        btn_grabar = new javax.swing.JButton();
        btn_cancelar = new javax.swing.JButton();
        btn_salir = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        txtsubtotal = new javax.swing.JTextField();
        txtsubtotal2 = new javax.swing.JTextField();
        txtsubtotal3 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txttotal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        iva5 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        iva10 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtiva = new javax.swing.JTextField();
        ivaporc = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        grilla_det = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        grilla_compra = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jLabel1.setText("Codigo");

        txtcod.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtcod.setEnabled(false);

        jLabel2.setText("Proveedor");

        cbo_prove.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbo_prove.setEnabled(false);
        cbo_prove.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbo_proveKeyPressed(evt);
            }
        });

        jLabel3.setText("Fecha");

        txtfecha.setEnabled(false);

        jLabel4.setText("Nro Factura");

        txtfac.setEnabled(false);
        txtfac.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtfacKeyPressed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Condicion"));

        buttonGroup1.add(contado);
        contado.setText("Contado");
        contado.setEnabled(false);
        contado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contadoActionPerformed(evt);
            }
        });

        buttonGroup1.add(credito);
        credito.setText("Credito");
        credito.setEnabled(false);
        credito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditoActionPerformed(evt);
            }
        });

        jLabel6.setText("Cant. Cuota");

        txtcuo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtcuo.setText("1");
        txtcuo.setEnabled(false);
        txtcuo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcuoKeyPressed(evt);
            }
        });

        jLabel5.setText("Intervalo");

        txtintervalo.setEditable(false);
        txtintervalo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtintervalo.setText("30");
        txtintervalo.setEnabled(false);
        txtintervalo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtintervaloActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(contado)
                        .addGap(18, 18, 18)
                        .addComponent(credito))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtcuo, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtintervalo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contado)
                    .addComponent(credito))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtcuo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtintervalo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        grilla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod", "Productos", "Precio Unit", "Cantidad", "Exenta", "Gravada 5%", "Gravada 10%"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        grilla.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                grillaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(grilla);

        jLabel7.setText("Productos");

        txtpro.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtpro.setEnabled(false);
        txtpro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtproActionPerformed(evt);
            }
        });
        txtpro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtproKeyPressed(evt);
            }
        });

        txtdescri.setEnabled(false);

        jLabel8.setText("Precio");

        txtprecio.setEnabled(false);

        jLabel9.setText("Cantidad");

        txtcant.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtcant.setEnabled(false);
        txtcant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcantKeyTyped(evt);
            }
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcantKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtcantKeyReleased(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Operaciones"));

        btn_nuevo.setText("Registrar");
        btn_nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_nuevoActionPerformed(evt);
            }
        });

        btn_anular.setText("Anular");
        btn_anular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_anularActionPerformed(evt);
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

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(btn_nuevo)
                .addGap(62, 62, 62)
                .addComponent(btn_anular, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addComponent(btn_grabar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_cancelar)
                .addGap(70, 70, 70)
                .addComponent(btn_salir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_nuevo)
                    .addComponent(btn_anular)
                    .addComponent(btn_grabar)
                    .addComponent(btn_cancelar)
                    .addComponent(btn_salir))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jLabel11.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        jLabel11.setText("Subtotal:");

        txtsubtotal.setEditable(false);
        txtsubtotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsubtotal.setText("0");
        txtsubtotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtsubtotal.setFocusable(false);

        txtsubtotal2.setEditable(false);
        txtsubtotal2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsubtotal2.setText("0");
        txtsubtotal2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtsubtotal2.setFocusable(false);

        txtsubtotal3.setEditable(false);
        txtsubtotal3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtsubtotal3.setText("0");
        txtsubtotal3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtsubtotal3.setFocusable(false);

        jLabel16.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabel16.setText("Total:");

        txttotal.setEditable(false);
        txttotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txttotal.setText("0");
        txttotal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txttotal.setFocusable(false);

        jLabel13.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel13.setText("IVA 5%");

        iva5.setEditable(false);
        iva5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        iva5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        iva5.setText("0");
        iva5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        iva5.setFocusable(false);
        iva5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iva5ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel14.setText("IVA 10%");

        iva10.setEditable(false);
        iva10.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        iva10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        iva10.setText("0");
        iva10.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        iva10.setFocusable(false);

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel15.setText("TOTAL IVA:");

        txtiva.setEditable(false);
        txtiva.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        txtiva.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtiva.setText("0");
        txtiva.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        txtiva.setFocusable(false);
        txtiva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtivaActionPerformed(evt);
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
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(140, 140, 140)
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtfecha, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(cbo_prove, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(54, 54, 54)
                                                .addComponent(jLabel4))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel7)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(txtpro, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtdescri, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel8)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(txtfac, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtcant, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(ivaporc, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 70, Short.MAX_VALUE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtsubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(txtsubtotal2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(txtsubtotal3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(iva5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(iva10, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtiva, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtcod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(txtfecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbo_prove, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(txtfac, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtpro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtdescri, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel8)
                        .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9)
                        .addComponent(txtcant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ivaporc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtsubtotal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtsubtotal3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtsubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(iva5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(iva10, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtiva, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Registrar Compras", jPanel1);

        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel2MouseClicked(evt);
            }
        });

        jLabel10.setText("Buscar por Nro Factura:");

        txtbuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtbuscarKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtbuscarKeyTyped(evt);
            }
        });

        grilla_det.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        grilla_det.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod Producto", "Descripcion", "Cantidad", "Precio Unit"
            }
        ));
        jScrollPane2.setViewportView(grilla_det);

        grilla_compra.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        grilla_compra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod Compra", "Proveedor", "Nro Factura", "Condicion ", "Importe total"
            }
        ));
        grilla_compra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grilla_compraMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(grilla_compra);

        jLabel12.setText("Cabecera Compra");

        jLabel17.setText("Detalle Compra");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 791, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel12)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3)
                    .addContainerGap()))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(115, 115, 115))
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGap(91, 91, 91)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(291, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Anular Compras", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void iva5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iva5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_iva5ActionPerformed

    private void btn_nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_nuevoActionPerformed
        ban = 1;
        this.habilitar(0);
        this.cbo_prove.setEnabled(true);
        this.cbo_prove.requestFocus();
        this.gencod();
    }//GEN-LAST:event_btn_nuevoActionPerformed

    private void cbo_proveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbo_proveKeyPressed
        if (evt.getKeyCode() == 10) {
            txtfac.setEnabled(true);
            txtfac.requestFocus();
        }
    }//GEN-LAST:event_cbo_proveKeyPressed

    private void txtfacKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtfacKeyPressed
        if (evt.getKeyCode() == 10) {
            if (txtfac.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            } else {
                contado.setEnabled(true);
                contado.requestFocus();
                credito.setEnabled(true);
            }
        }
    }//GEN-LAST:event_txtfacKeyPressed

    private void creditoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditoActionPerformed
        condicion = "credito";
        txtcuo.setEnabled(true);
        txtcuo.requestFocus();
    }//GEN-LAST:event_creditoActionPerformed

    private void contadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contadoActionPerformed
        condicion = "contado";
        txtpro.setEnabled(true);
        txtpro.requestFocus();
    }//GEN-LAST:event_contadoActionPerformed

    private void txtcuoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcuoKeyPressed
        if (evt.getKeyCode() == 10) {
            txtpro.setEnabled(true);
            txtpro.requestFocus();
        }
    }//GEN-LAST:event_txtcuoKeyPressed

    private void txtproKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtproKeyPressed
        if (evt.getKeyCode() == 10) {//verificar que haya presionado enter
            if (txtpro.getText().trim().equals("")) {//validar que no este vacio y no poseea espacios en blanco
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..!");
                txtpro.requestFocus();
            } else {
                //si todo esta bien llamamos a nuestro metodo buscar_productos()
                this.buscar_producto();
                txtcant.setEnabled(true);
                txtcant.requestFocus();
            }
        }
    }//GEN-LAST:event_txtproKeyPressed

    private void txtcantKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcantKeyReleased
    }//GEN-LAST:event_txtcantKeyReleased

    private void txtcantKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcantKeyTyped
        int k = evt.getKeyChar();
        if ((k >= 32 && k <= 45) || (k >= 58 && k <= 126)) {
            evt.setKeyChar((char) KeyEvent.VK_CLEAR);
            getToolkit().beep();///el sonido del error
            evt.consume();////mantiene el pulsor al presionar espacio
            JOptionPane.showMessageDialog(null, "No puede ingresar letras");
            txtcant.requestFocus();
        }

    }//GEN-LAST:event_txtcantKeyTyped

    private void txtcantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcantKeyPressed
        if (evt.getKeyCode() == 10) {//validar que al presionar enter verifique que el campo no este vacio y sin espacio
            if (txtcant.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "El campo no puede estar vacio..");
            } else {
                //si cumple que el campo no esta vacio llamamos a nuestro metodo cargar_tabla()
                this.cargar_tabla();
                txtpro.requestFocus();
                btn_grabar.setEnabled(true);
            }
        }
    }//GEN-LAST:event_txtcantKeyPressed

    private void txtivaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtivaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtivaActionPerformed

    private void btn_grabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_grabarActionPerformed
        grabar();
        this.limpiargrilla();
        this.habilitar(0);
    }//GEN-LAST:event_btn_grabarActionPerformed

    private void grillaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_grillaKeyPressed
        //if (evt.getKeyCode() == 127) {
            remover_fila_grilla();
            this.calcular_subtotales();
            this.removerDato();
        //}
    }//GEN-LAST:event_grillaKeyPressed

    private void txtproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtproActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtproActionPerformed

    private void btn_anularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_anularActionPerformed
        ban = 2;
        jTabbedPane1.setSelectedIndex(1);

    }//GEN-LAST:event_btn_anularActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked

    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void jPanel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseClicked
        ban = 2;
    }//GEN-LAST:event_jPanel2MouseClicked

    private void btn_cancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_cancelarActionPerformed
        this.habilitar(1);
        this.limpiargrilla2();
    }//GEN-LAST:event_btn_cancelarActionPerformed

    private void txtbuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyTyped


    }//GEN-LAST:event_txtbuscarKeyTyped

    private void txtbuscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyPressed
        if (evt.getKeyCode() == 10) {
            limpiargrilla();
            this.cargargrilla(true);
            this.limpiargrilladetalle();
            this.cargar_det();
        }
    }//GEN-LAST:event_txtbuscarKeyPressed

    private void grilla_compraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grilla_compraMouseClicked
        if (ban == 2) {
            int fila = grilla_compra.getSelectedRow();
            int mensaje = JOptionPane.showConfirmDialog(this, "Deseas anular el nro de factura-->"
                    + txtbuscar.getText(), "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (mensaje == JOptionPane.YES_OPTION) {
                con.actualizar_datos("compra",
                        "estado='ANULADO'",
                        "com_cod=" + grilla_compra.getValueAt(fila, 0));
                this.limpiargrilla();
                this.limpiargrilladetalle();
//           ban = 0;
            }
        }
    }//GEN-LAST:event_grilla_compraMouseClicked

    private void btn_salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_salirActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btn_salirActionPerformed

    private void btn_grabarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btn_grabarKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == 10) {
            grabar();
            this.limpiargrilla();
            this.habilitar(0);
        }
    }//GEN-LAST:event_btn_grabarKeyPressed

    private void txtintervaloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtintervaloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtintervaloActionPerformed

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
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(compras.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                compras dialog = new compras(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btn_anular;
    private javax.swing.JButton btn_cancelar;
    private javax.swing.JButton btn_grabar;
    private javax.swing.JButton btn_nuevo;
    private javax.swing.JButton btn_salir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbo_prove;
    private javax.swing.JCheckBox contado;
    private javax.swing.JCheckBox credito;
    private javax.swing.JTable grilla;
    private javax.swing.JTable grilla_compra;
    private javax.swing.JTable grilla_det;
    private javax.swing.JTextField iva10;
    private javax.swing.JTextField iva5;
    private javax.swing.JTextField ivaporc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcant;
    private javax.swing.JTextField txtcod;
    private javax.swing.JTextField txtcuo;
    private javax.swing.JTextField txtdescri;
    private javax.swing.JTextField txtfac;
    private javax.swing.JTextField txtfecha;
    private javax.swing.JTextField txtintervalo;
    private javax.swing.JTextField txtiva;
    private javax.swing.JTextField txtprecio;
    private javax.swing.JTextField txtpro;
    private javax.swing.JTextField txtsubtotal;
    private javax.swing.JTextField txtsubtotal2;
    private javax.swing.JTextField txtsubtotal3;
    private javax.swing.JTextField txttotal;
    // End of variables declaration//GEN-END:variables
}
