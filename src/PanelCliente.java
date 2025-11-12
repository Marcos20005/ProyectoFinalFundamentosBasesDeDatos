
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;



public class PanelCliente extends JPanel {
    // Objetos de conexion SQL
    CallableStatement stmt = null;
    Connection con = null;

    public PanelCliente(MantenimientoCliente controlOriginal, int funcion, String cedula) throws SQLException, ClassNotFoundException {
        setLayout(null);
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root",
                "cRojas34");
    
       if(funcion==0){
   JLabel lBlcodigo = crearEtiqueta("Datos de nuevo cliente", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);
       }else{
         JLabel lBlcodigo = crearEtiqueta("Actualizar registro", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);
       }
       

        JLabel lblCedula = crearEtiqueta("Cédula:", 150, 70, 140, 30);
        JTextField txtCedula = crearCampoTexto(300, 70, 200, 30, "Ingrese cédula");
        this.add(lblCedula);
        this.add(txtCedula);

        JLabel lblPrimerNombre = crearEtiqueta("Primer Nombre:", 150, 120, 140, 30);
        JTextField txtPrimerNombre = crearCampoTexto(300, 120, 200, 30, "Ingrese primer nombre");
        this.add(lblPrimerNombre);
        this.add(txtPrimerNombre);

        JLabel lblSegundoNombre = crearEtiqueta("Segundo Nombre:", 150, 170, 140, 30);
        JTextField txtSegundoNombre = crearCampoTexto(300, 170, 200, 30, "Ingrese segundo nombre");
        this.add(lblSegundoNombre);
        this.add(txtSegundoNombre);

        JLabel lblPrimerApellido = crearEtiqueta("Primer Apellido:", 150, 220, 140, 30);
        JTextField txtPrimerApellido = crearCampoTexto(300, 220, 200, 30, "Ingrese primer apellido");
        this.add(lblPrimerApellido);
        this.add(txtPrimerApellido);

        JLabel lblSegundoApellido = crearEtiqueta("Segundo Apellido:", 150, 270, 140, 30);
        JTextField txtSegundoApellido = crearCampoTexto(300, 270, 200, 30, "Ingrese segundo apellido");
        this.add(lblSegundoApellido);
        this.add(txtSegundoApellido);
        

        //Porcion de codigo para llenar los campos cuando se desee actualizar un registro
        stmt = (CallableStatement) con.prepareCall("{CALL listar_cliente_mantenimiento()}");
        ResultSet rs = stmt.executeQuery();

     if (funcion==1) {
            while (rs.next()) { 
            if (rs.getString("cedula").equals(cedula)) {
              txtCedula.setText(rs.getString("cedula"));  
              txtPrimerNombre.setText(rs.getString("primer_nombre"));
              txtSegundoNombre.setText(rs.getString("segundo_nombre"));
              txtPrimerApellido.setText(rs.getString("primer_apellido"));
              txtSegundoApellido.setText(rs.getString("segundo_apellido"));
            }
        }

     }

                  JButton botonCancelar = crearBoton("Cancelar", 400, 320, 100, 40, "Regresar atras", "Iconos/cancelar.png");
botonCancelar.setBackground(new Color(240, 128, 128));
botonCancelar.setForeground(Color.WHITE);
this.add(botonCancelar);
botonCancelar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e){
txtCedula.setText("");
                txtPrimerNombre.setText("");
                txtSegundoNombre.setText("");
                txtPrimerApellido.setText("");
                txtSegundoApellido.setText("");
                controlOriginal.add(controlOriginal.botonActualizar);
                controlOriginal.add(controlOriginal.botonInsertar);
                controlOriginal.add(controlOriginal.botonEliminar);
                controlOriginal.add(controlOriginal.botonConsultar);
                controlOriginal.add(controlOriginal.scroll);
                controlOriginal.remove(controlOriginal.panel);
                controlOriginal.recargarTabla();
                controlOriginal.revalidate();
                controlOriginal.repaint();
    }
});

        JButton botonGuardar = crearBoton("Guardar", 280, 320, 100, 40, "Guardar nuevo cliente", "Iconos/guardar-el-archivo.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int eleccion;
                try {
                    if(funcion==0){
                         eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar nuevo registro?", "Confirmar acción", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
   stmt = (CallableStatement) con.prepareCall("{CALL insertar_cliente(?, ?, ?, ?, ?)}");
                }  else{
                    eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar cambios?", "Confirmar acción", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    stmt = (CallableStatement) con.prepareCall("{CALL actualizar_cliente(?, ?, ?, ?, ?,?)}");
                }           
                    
                    if (eleccion == 0) {
                         stmt.setString(1, txtCedula.getText());      // cédula
         stmt.setString(2, txtPrimerNombre.getText());   // nombre1
         stmt.setString(3, txtSegundoNombre.getText());   // nombre1
         stmt.setString(4, txtPrimerApellido.getText());        // apellido1
         stmt.setString(5, txtSegundoApellido.getText());        // apellido2
         if (funcion==1) {
           
             stmt.setString(6, cedula);        // apellido2
         }
        stmt.executeUpdate();
       
        controlOriginal.recargarTabla();

JOptionPane.showMessageDialog(null, 
    (funcion == 0 ? "Cliente insertado correctamente." : "Cliente actualizado correctamente."),
    "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    }
                } catch (SQLException e1) {
                     JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar: "+e1.getMessage(),"Advertencia",JOptionPane.WARNING_MESSAGE);
               System.out.println(e1.getMessage());
                    }

                txtCedula.setText("");
txtPrimerNombre.setText("");
txtSegundoNombre.setText("");
txtPrimerApellido.setText("");
txtSegundoApellido.setText("");

        controlOriginal.add(controlOriginal.botonActualizar);
        controlOriginal.add(controlOriginal.botonInsertar);
        controlOriginal.add(controlOriginal.botonEliminar);
        controlOriginal.add(controlOriginal.botonConsultar);
        controlOriginal.add(controlOriginal.scroll);

        controlOriginal.remove(controlOriginal.panel);

        controlOriginal.recargarTabla();
        controlOriginal.scroll.revalidate();
        controlOriginal.scroll.repaint();
        controlOriginal.revalidate();
        controlOriginal.repaint();
            }
        });
    }

    // Métodos utilitarios
    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto, String toolTip, String ruta) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setToolTipText(toolTip);
        ImageIcon iconoOriginal = new ImageIcon(ruta);
        boton.setIcon(iconoOriginal);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        if (texto.equals("")) {
            boton.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return boton;
    }

    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campoTexto = new JTextField();
        campoTexto.setBounds(x, y, ancho, alto);
        campoTexto.setToolTipText(toolTip);
        return campoTexto;
    }

    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }
}
