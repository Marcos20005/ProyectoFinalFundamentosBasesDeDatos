import java.awt.Color;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class PanelCliente extends JPanel {
    private JTextField campoCedula, campoNombre1, campoNombre2, campoApellido1, campoApellido2;
    private JTextField campoTelefono, campoDescripcionTelefono, campoCorreo, campoDescripcionCorreo;

    public PanelCliente(JTabbedPane pestanias, Connection con) {
        this.setLayout(null);
        this.setBackground(new Color(245, 245, 245));

        //Tarjeta de cliente
        JPanel tarjetaCliente = new JPanel();
        tarjetaCliente.setLayout(null);
        tarjetaCliente.setBounds(100, 60, 600, 520);
        tarjetaCliente.setBackground(Color.WHITE);
        tarjetaCliente.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(130, 90, 160), 2, true));
        this.add(tarjetaCliente);

        //Titulo de la tarjeta
        JLabel titulo = new JLabel("Datos del Cliente", JLabel.CENTER);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titulo.setForeground(new Color(90, 60, 120));
        titulo.setBounds(0, 20, 600, 40);
        tarjetaCliente.add(titulo);

        //Etiquetas y campos de texto dentro de la tarjeta
        JLabel etiCedula = VistaPrincipal.crearEtiqueta("Cédula:", 100, 80, 200, 25);
        tarjetaCliente.add(etiCedula);
        campoCedula = VistaPrincipal.crearCampoTexto("", 300, 80, 200, 25, "Cédula del cliente");
        tarjetaCliente.add(campoCedula);

        JLabel etiNombre1 = VistaPrincipal.crearEtiqueta("Primer nombre:", 100, 120, 200, 25);
        tarjetaCliente.add(etiNombre1);
        campoNombre1 = VistaPrincipal.crearCampoTexto("", 300, 120, 200, 25, "Primer nombre");
        tarjetaCliente.add(campoNombre1);

        JLabel etiNombre2 = VistaPrincipal.crearEtiqueta("Segundo nombre:", 100, 160, 200, 25);
        tarjetaCliente.add(etiNombre2);
        campoNombre2 = VistaPrincipal.crearCampoTexto("", 300, 160, 200, 25, "Segundo nombre");
        tarjetaCliente.add(campoNombre2);

        JLabel etiApellido1 = VistaPrincipal.crearEtiqueta("Primer apellido:", 100, 200, 200, 25);
        tarjetaCliente.add(etiApellido1);
        campoApellido1 = VistaPrincipal.crearCampoTexto("", 300, 200, 200, 25, "Primer apellido");
        tarjetaCliente.add(campoApellido1);

        JLabel etiApellido2 = VistaPrincipal.crearEtiqueta("Segundo apellido:", 100, 240, 200, 25);
        tarjetaCliente.add(etiApellido2);
        campoApellido2 = VistaPrincipal.crearCampoTexto("", 300, 240, 200, 25, "Segundo apellido");
        tarjetaCliente.add(campoApellido2);

        JLabel etiTelefono = VistaPrincipal.crearEtiqueta("Teléfono:", 100, 280, 200, 25);
        tarjetaCliente.add(etiTelefono);
        campoTelefono = VistaPrincipal.crearCampoTexto("", 300, 280, 200, 25, "Número de teléfono");
        tarjetaCliente.add(campoTelefono);

        JLabel etiDescripcionTelefono = VistaPrincipal.crearEtiqueta("Descripción teléfono:", 100, 320, 200, 25);
        tarjetaCliente.add(etiDescripcionTelefono);
        campoDescripcionTelefono = VistaPrincipal.crearCampoTexto("", 300, 320, 200, 25, "Ejemplo: Personal / Trabajo");
        tarjetaCliente.add(campoDescripcionTelefono);

        JLabel etiCorreo = VistaPrincipal.crearEtiqueta("Correo electrónico:", 100, 360, 200, 25);
        tarjetaCliente.add(etiCorreo);
        campoCorreo = VistaPrincipal.crearCampoTexto("", 300, 360, 200, 25, "Correo electrónico");
        tarjetaCliente.add(campoCorreo);

        JLabel etiDescripcionCorreo = VistaPrincipal.crearEtiqueta("Descripción correo:", 100, 400, 200, 25);
        tarjetaCliente.add(etiDescripcionCorreo);
        campoDescripcionCorreo = VistaPrincipal.crearCampoTexto("", 300, 400, 200, 25, "Ejemplo: Personal / Trabajo");
        tarjetaCliente.add(campoDescripcionCorreo);

        //Botones dentro de la tarjeta
        JButton btnAtras = VistaPrincipal.crearBoton("<--", 100, 460, 60, 40, "Volver", "Iconos/paso-atras.png");
        tarjetaCliente.add(btnAtras);
        btnAtras.setToolTipText("Volver al registro de empleado");
        btnAtras.addActionListener(e -> pestanias.setSelectedIndex(0));

        JButton btnGuardar = VistaPrincipal.crearBoton("Guardar", 220, 460, 160, 40, "Guardar cliente", "Iconos/guardar-el-archivo.png");
        tarjetaCliente.add(btnGuardar);
        btnGuardar.setToolTipText("Guardar los datos del cliente");
        btnGuardar.addActionListener(e -> {
            try {
                registrarCliente(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JButton btnSiguiente = VistaPrincipal.crearBoton("-->", 440, 460, 60, 40, "Siguiente", "Iconos/paso-siguiente.png");
        tarjetaCliente.add(btnSiguiente);
        btnSiguiente.setToolTipText("Ir al registro de vehículo");
        btnSiguiente.addActionListener(e -> pestanias.setSelectedIndex(2));
    }

    private void registrarCliente(Connection con) throws SQLException {
        if (campoCedula.getText().isEmpty() || campoNombre1.getText().isEmpty() || campoApellido1.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Statement stmt = con.createStatement();

        // Insertar cliente
        String sqlCliente = "INSERT INTO cliente (cedula, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido) VALUES ('"
                + campoCedula.getText() + "', '"
                + campoNombre1.getText() + "', '"
                + campoNombre2.getText() + "', '"
                + campoApellido1.getText() + "', '"
                + campoApellido2.getText() + "')";
        stmt.executeUpdate(sqlCliente);

        // Insertar teléfono (si hay)
        if (!campoTelefono.getText().isEmpty()) {
            String sqlTelefono = "INSERT INTO telefono_cliente (id, telefono, descripcion, cedula_cliente) VALUES (UUID(), '"
                    + campoTelefono.getText() + "', '"
                    + campoDescripcionTelefono.getText() + "', '"
                    + campoCedula.getText() + "')";
            stmt.executeUpdate(sqlTelefono);
        }

        // Insertar correo (si hay)
        if (!campoCorreo.getText().isEmpty()) {
            String sqlCorreo = "INSERT INTO correo_cliente (id, correo, descripcion, cedula_cliente2) VALUES (UUID(), '"
                    + campoCorreo.getText() + "', '"
                    + campoDescripcionCorreo.getText() + "', '"
                    + campoCedula.getText() + "')";
            stmt.executeUpdate(sqlCorreo);
        }

        stmt.close();

        JOptionPane.showMessageDialog(this, "Cliente registrado correctamente con teléfono y correo.");

        // Limpiar campos
        campoCedula.setText("");
        campoNombre1.setText("");
        campoNombre2.setText("");
        campoApellido1.setText("");
        campoApellido2.setText("");
        campoTelefono.setText("");
        campoDescripcionTelefono.setText("");
        campoCorreo.setText("");
        campoDescripcionCorreo.setText("");
    }
}
