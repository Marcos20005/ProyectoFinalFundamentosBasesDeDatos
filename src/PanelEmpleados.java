import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class PanelEmpleados extends JPanel {

    private JTextField campoID, campoNombre1, campoNombre2, campoApellido1, campoApellido2;
    private JComboBox<String> comboPuesto;

    public PanelEmpleados(JTabbedPane pestanias, Connection con){
        this.setLayout(null);
        this.setBackground(new Color(245, 245, 245));

        //Tarjeta de registro de empleado
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(null);
        tarjeta.setBounds(100, 100, 600, 420);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(130, 90, 160), 2, true));
        this.add(tarjeta);

        //Titulo
       JLabel titulo = new JLabel("Registro de Empleado", JLabel.CENTER);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titulo.setForeground(new Color(90, 60, 120));
        titulo.setBounds(0, 20, 600, 40);
        tarjeta.add(titulo);

        //Etiquetas y campos de texto
         JLabel eti1 = VistaPrincipal.crearEtiqueta("ID empleado:", 100, 90, 150, 25);
        tarjeta.add(eti1);
        campoID = VistaPrincipal.crearCampoTexto("", 280, 90, 200, 25, "ID del empleado");
        tarjeta.add(campoID);

        JLabel eti2 = VistaPrincipal.crearEtiqueta("Primer nombre:", 100, 130, 150, 25);
        tarjeta.add(eti2);
        campoNombre1 = VistaPrincipal.crearCampoTexto("", 280, 130, 200, 25, "Primer nombre");
        tarjeta.add(campoNombre1);

        JLabel eti3 = VistaPrincipal.crearEtiqueta("Segundo nombre:", 100, 170, 150, 25);
        tarjeta.add(eti3);
        campoNombre2 = VistaPrincipal.crearCampoTexto("", 280, 170, 200, 25, "Segundo nombre");
        tarjeta.add(campoNombre2);

        JLabel eti4 = VistaPrincipal.crearEtiqueta("Primer apellido:", 100, 210, 150, 25);
        tarjeta.add(eti4);
        campoApellido1 = VistaPrincipal.crearCampoTexto("", 280, 210, 200, 25, "Primer apellido");
        tarjeta.add(campoApellido1);

        JLabel eti5 = VistaPrincipal.crearEtiqueta("Segundo apellido:", 100, 250, 150, 25);
        tarjeta.add(eti5);
        campoApellido2 = VistaPrincipal.crearCampoTexto("", 280, 250, 200, 25, "Segundo apellido");
        tarjeta.add(campoApellido2);

        JLabel eti6 = VistaPrincipal.crearEtiqueta("Puesto:", 100, 290, 150, 25);
        tarjeta.add(eti6);
        comboPuesto = new JComboBox<>();
        comboPuesto.setBounds(280, 290, 200, 25);
        tarjeta.add(comboPuesto);
        cargarPuestos(con);

        //Botones
         JButton btnAtras = VistaPrincipal.crearBoton("<--", 100, 350, 60, 40, "Volver", "Iconos/paso-atras.png");
        tarjeta.add(btnAtras);
        btnAtras.setToolTipText("Volver al menu principal");
        btnAtras.addActionListener(e -> pestanias.setSelectedIndex(0));

        JButton btnGuardar = VistaPrincipal.crearBoton("Registrar empleado", 220, 350, 160, 40, "Guardar empleado", "Iconos/guardar-el-archivo.png");
        tarjeta.add(btnGuardar);
        btnGuardar.setToolTipText("Guardar el nuevo empleado en la base de datos");
        btnGuardar.addActionListener(e -> {
            try {
                registrarEmpleado(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JButton btnSiguiente = VistaPrincipal.crearBoton("-->", 440, 350, 60, 40, "Siguiente", "Iconos/un-paso-adelante.png");
        tarjeta.add(btnSiguiente);
        btnSiguiente.setToolTipText("Ir al registro de cliente");
        btnSiguiente.addActionListener(e -> pestanias.setSelectedIndex(2));
    }

    private void cargarPuestos(Connection con) {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nombre_puesto FROM catalogo_puesto");
        
            while (rs.next()) {
                comboPuesto.addItem(rs.getString("nombre_puesto"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void registrarEmpleado(Connection con) throws SQLException {
        if (campoID.getText().isEmpty() || campoNombre1.getText().isEmpty() || campoApellido1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO empleado (id, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, id_puesto) VALUES ('"
                + campoID.getText() + "', '"
                + campoNombre1.getText() + "', '"
                + campoNombre2.getText() + "', '"
                + campoApellido1.getText() + "', '"
                + campoApellido2.getText() + "', '"
                + comboPuesto.getSelectedItem().toString() + "')";

        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();

        JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.");

        campoID.setText("");
        campoNombre1.setText("");
        campoNombre2.setText("");
        campoApellido1.setText("");
        campoApellido2.setText("");
        comboPuesto.setSelectedIndex(0);
    }

}