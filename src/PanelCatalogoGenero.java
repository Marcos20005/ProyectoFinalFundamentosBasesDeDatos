import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

public class PanelCatalogoGenero extends JPanel {

    JTextField campoId, campoNombre;
    JButton botonGuardar;
    Connection con;
    Statement stmt;
    MantenimientoCatalogoGenero mantenimiento;

    public PanelCatalogoGenero(MantenimientoCatalogoGenero mantenimiento) {
        this.mantenimiento = mantenimiento;
        setLayout(null);

        // Label y campo para ID
        JLabel lblId = new JLabel("ID Genero:");
        lblId.setBounds(80, 50, 100, 30);
        this.add(lblId);
        campoId = new JTextField();
        campoId.setBounds(190, 50, 200, 30);
        this.add(campoId);

        // Label y campo para Nombre
        JLabel lblNombre = new JLabel("Nombre Genero:");
        lblNombre.setBounds(80, 100, 120, 30);
        this.add(lblNombre);
        campoNombre = new JTextField();
        campoNombre.setBounds(190, 100, 200, 30);
        this.add(campoNombre);

        // Botón Guardar
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(160, 160, 120, 40);
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        // Acción de Guardar
        botonGuardar.addActionListener(e -> {
            if (campoId.getText().isEmpty() || campoNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                return;
            }

            int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(eleccion==0){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                        "root", "cRojas34");
                stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO catalogo_genero (id_genero, nombre_genero) VALUES ('"
                        + campoId.getText() + "','" + campoNombre.getText() + "');");

                JOptionPane.showMessageDialog(null, "Registro insertado correctamente");
                 mantenimiento.add(mantenimiento.botonActualizar);
                mantenimiento.add(mantenimiento.botonInsertar);
                mantenimiento.add(mantenimiento.botonEliminar);
                mantenimiento.add(mantenimiento.botonConsultar);
                mantenimiento.add(mantenimiento.scroll);
                mantenimiento.add(mantenimiento.combo);
                mantenimiento.remove(this);
                mantenimiento.recargarTabla();

                campoId.setText("");
                campoNombre.setText("");

            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al insertar el registro");
            }
        }
        });
    }
}
