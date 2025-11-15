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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelEmpleado extends JPanel {
    CallableStatement stmt = null;
    Connection con = null;
    


    public PanelEmpleado(MantenimientoEmpleado controlOriginal, int funcion, String id, VistaPrincipal miVista) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", 
                "root", "cRojas34");
     
        

        if(funcion==0){
JLabel lBlcodigo = crearEtiqueta("Datos de nuevo empleado", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);
        }else{
            JLabel lBlcodigo = crearEtiqueta("Actualizar registro", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);
        }
        

        JLabel lblID = crearEtiqueta("ID empleado:", 150, 70, 140, 30);
        JTextField txtID = crearCampoTexto(300, 70, 200, 30, "Ingrese ID de empleado");
        this.add(lblID);
        this.add(txtID);

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

        JLabel lblIDPuesto = crearEtiqueta("Puesto:", 150, 320, 140, 30);
        javax.swing.JComboBox<String> comboPuesto = new javax.swing.JComboBox<>();
        comboPuesto.setBounds(300, 320, 200, 30);
        this.add(lblIDPuesto);
        this.add(comboPuesto);

        // Cargar puestos en el combo box
        java.util.Map<String, String> mapaPuestos = new java.util.HashMap<>();
        CallableStatement stmtPuestos = con.prepareCall("{CALL listar_puesto()}");
        ResultSet rsPuestos = stmtPuestos.executeQuery();
        while (rsPuestos.next()) {
            String idPuesto = rsPuestos.getString("id_puesto");
            String nombrePuesto = rsPuestos.getString("nombre_puesto");
            comboPuesto.addItem(nombrePuesto);
            mapaPuestos.put(nombrePuesto, idPuesto);
        }
        if (funcion == 1) {
            stmt = (CallableStatement) con.prepareCall("{CALL listar_empleado_mantenimiento()}");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                if (rs.getString("id").equals(id)) {
                    txtID.setText(rs.getString("id"));
                    txtPrimerNombre.setText(rs.getString("primer_nombre"));
                    txtSegundoNombre.setText(rs.getString("segundo_nombre"));
                    txtPrimerApellido.setText(rs.getString("primer_apellido"));
                    txtSegundoApellido.setText(rs.getString("segundo_apellido"));

                    String idPuestoEmpleado = rs.getString("id_puesto");
                    for (String nombrePuesto : mapaPuestos.keySet()) {
                        if (mapaPuestos.get(nombrePuesto).equals(idPuestoEmpleado)) {
                            comboPuesto.setSelectedItem(nombrePuesto);
                            break;
                        }
                    }
                }
            }
        }

        JButton botonCancelar = crearBoton("Cancelar", 400, 360, 100, 40, "Regresar atras", "Iconos/cancelar.png");
        botonCancelar.setBackground(new Color(240, 128, 128));
        botonCancelar.setForeground(Color.WHITE);
        this.add(botonCancelar);
        botonCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtID.setText("");
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

        JButton botonGuardar = crearBoton("Guardar", 290, 360, 100, 40, "Guardar nuevo empleado", "Iconos/guardar-el-archivo.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int eleccion;
                try {
                    if (funcion==0) {
                     eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);   
                             stmt = con.prepareCall("{CALL insertar_empleado(?,?,?,?,?,?)}");
                    } else{
                    eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar cambios?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);   
                             stmt = con.prepareCall("{CALL actualizar_empleado(?,?,?,?,?,?,?)}");
                    }
            
                    if (eleccion == 0) {
                      stmt.setString(1, txtID.getText());
                      stmt.setString(2, txtPrimerNombre.getText());
                      stmt.setString(3, txtSegundoNombre.getText());
                      stmt.setString(4, txtPrimerApellido.getText());
                      stmt.setString(5, txtSegundoApellido.getText());
                      String puestoSeleccionado = (String) comboPuesto.getSelectedItem();
                      String idPuestoReal = mapaPuestos.get(puestoSeleccionado);
                      stmt.setString(6, idPuestoReal);
                      if(funcion==1){
                 stmt.setString(7, id);
                      }
                    }
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    
                    JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }

                txtID.setText("");
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
                 miVista.actualizarPaneles();
            }
           
        });
    }

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
