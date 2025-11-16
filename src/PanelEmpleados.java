
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class PanelEmpleados extends JPanel {

    private JTextField campoID, campoNombre1, campoNombre2, campoApellido1, campoApellido2, campoTelefono, campoDescripcion;
    private JComboBox<String> comboPuesto;
    PanelBoletos panelControl;
    

    public PanelEmpleados(JTabbedPane pestanias, Connection con, VistaPrincipal miVista) throws SQLException {
        this.setLayout(null);
        this.setBackground(new Color(245, 245, 245));
        CallableStatement stmt = con.prepareCall("{call listar_puesto()}");
        panelControl=new PanelBoletos(new JTabbedPane(), con);
      
        //Tarjeta de registro 
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(null);
        tarjeta.setBounds(100, 70, 600, 500);
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(javax.swing.BorderFactory.createLineBorder(new Color(130, 90, 160), 2, true));
        this.add(tarjeta);

        // Título
        JLabel titulo = new JLabel("Registro de Empleado", JLabel.CENTER);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titulo.setForeground(new Color(90, 60, 120));
        titulo.setBounds(0, 20, 600, 40);
        tarjeta.add(titulo);

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

        JLabel eti7 = VistaPrincipal.crearEtiqueta("Teléfono:", 100, 330, 150, 25);
        tarjeta.add(eti7);
        campoTelefono = VistaPrincipal.crearCampoTexto("", 280, 330, 200, 25, "Número telefónico del empleado");
        tarjeta.add(campoTelefono);

        JLabel eti8 = VistaPrincipal.crearEtiqueta("Descripción:", 100, 370, 150, 25);
        tarjeta.add(eti8);
        campoDescripcion = VistaPrincipal.crearCampoTexto("", 280, 370, 200, 25, "Ej: personal, oficina, etc.");
        tarjeta.add(campoDescripcion);

        cargarPuestos(con);

        JButton btnAtras = VistaPrincipal.crearBoton("<--", 120, 420, 60, 40, "Volver", "Iconos/paso-atras.png");
        tarjeta.add(btnAtras);
        btnAtras.addActionListener(e -> pestanias.setSelectedIndex(0));

        JButton btnGuardar = VistaPrincipal.crearBoton("Registrar empleado", 240, 420, 160, 40, "Guardar empleado", "Iconos/guardar-el-archivo.png");
        tarjeta.add(btnGuardar);
        btnGuardar.addActionListener(e -> {
            try {
                registrarEmpleado(con);
                miVista.actualizarPaneles();
                panelControl.cargarEmpleados(con);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        JButton btnSiguiente = VistaPrincipal.crearBoton("-->", 460, 420, 60, 40, "Siguiente", "Iconos/un-paso-adelante.png");
        tarjeta.add(btnSiguiente);
        btnSiguiente.addActionListener(e -> pestanias.setSelectedIndex(2));
    }

    private void cargarPuestos(Connection con) {
        try {
            java.sql.CallableStatement cstmt = con.prepareCall("{call listar_puesto()}");
            ResultSet rs = cstmt.executeQuery();

            // Limpiar el comboBox antes de cargar los datos
            comboPuesto.removeAllItems();
            while (rs.next()) {
                // Agregar cada puesto al comboBox
                comboPuesto.addItem(rs.getString("nombre_puesto"));
            }
            rs.close();
            cstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los puestos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarEmpleado(Connection con) throws SQLException {
        if (campoID.getText().trim().isEmpty() || campoNombre1.getText().trim().isEmpty() || campoApellido1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Se usa para llamar al procedimiento almacenado de insertar empleado para agregar la informacion en la base de datos 
        try (CallableStatement cstmt = con.prepareCall("{CALL insertar_empleado(?, ?, ?, ?, ?, ?)}")) {

            // Asignar los valores
            cstmt.setString(1, campoID.getText().trim());
            cstmt.setString(2, campoNombre1.getText().trim());
            cstmt.setString(3, campoNombre2.getText().trim());
            cstmt.setString(4, campoApellido1.getText().trim());
            cstmt.setString(5, campoApellido2.getText().trim());
            cstmt.setString(6, comboPuesto.getSelectedItem().toString().trim());

            cstmt.execute();

            // El SELECT en tu SP es solo para depuración. En un entorno real,
            // un procedimiento "insertar" no debería devolver un ResultSet.
            // Pero si lo hace, debes procesarlo y cerrarlo.
            try (ResultSet rs = cstmt.getResultSet()) {
                if (rs != null) {
                    while (rs.next()) {
                        System.out.println("🔹 Parámetros recibidos por el SP:");
                        System.out.println("ID: " + rs.getString("ID"));
                        // ... el resto de tus System.out.println ...
                    }
                }
            } // rs.close() se llama automáticamente aquí

        } // cstmt.close() se llama automáticamente aquí
        catch (SQLException e) {
            // Si el error ocurrió en el primer SP, muéstralo
            JOptionPane.showMessageDialog(this, "Error al registrar el empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // Detener la ejecución si el empleado no se pudo registrar
        }

        // Ahora que el primer statement está cerrado, podemos ejecutar el segundo de forma segura.
        try {
            if (!campoTelefono.getText().isEmpty()) {
                try (CallableStatement csTelefono = con.prepareCall("{CALL insertar_telefono_empleado(?, ?, ?)}")) {
                    csTelefono.setString(1, campoTelefono.getText());
                    csTelefono.setString(2, campoDescripcion.getText());
                    csTelefono.setString(3, campoID.getText().trim());
                    csTelefono.execute();
                } // csTelefono.close() se llama automáticamente aquí
            }

            JOptionPane.showMessageDialog(this, "Empleado registrado correctamente.");

            // Limpiar los campos después de registrar
            campoID.setText("");
            campoNombre1.setText("");
            campoNombre2.setText("");
            campoApellido1.setText("");
            campoApellido2.setText("");
            comboPuesto.setSelectedIndex(0);
            campoTelefono.setText("");
            campoDescripcion.setText("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al registrar el teléfono del empleado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
