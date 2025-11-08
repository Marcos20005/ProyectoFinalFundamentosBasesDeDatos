import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
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

    public PanelCatalogoGenero(MantenimientoCatalogoGenero mantenimiento, int funcion) {
        this.mantenimiento = mantenimiento;
        setLayout(null);

         if (funcion==0) {
              JLabel labelTitulo = new JLabel("Registro de puesto");
        labelTitulo.setBounds(100, 20, 120, 30);
        this.add(labelTitulo);
        }else{
                  JLabel labelTitulo = new JLabel("Acualizar registro");
        labelTitulo.setBounds(100, 20, 120, 30);
        this.add(labelTitulo);
        }
        // Label y campo para ID
        JLabel lblId = new JLabel("ID Genero:");
        lblId.setBounds(80, 60, 100, 30);
        this.add(lblId);
        campoId = new JTextField();
        campoId.setBounds(190, 60, 200, 30);
        this.add(campoId);

        // Label y campo para Nombre
        JLabel lblNombre = new JLabel("Nombre Genero:");
        lblNombre.setBounds(80, 110, 120, 30);
        this.add(lblNombre);
        campoNombre = new JTextField();
        campoNombre.setBounds(190, 110, 200, 30);
        this.add(campoNombre);

                JButton botonCancelar = MantenimientoCatalogoGenero.crearBoton("Cancelar", 320, 160, 100, 40, "Regresar atras", "Iconos/cancelar.png");
botonCancelar.setBackground(new Color(240, 128, 128));
botonCancelar.setForeground(Color.WHITE);
this.add(botonCancelar);
botonCancelar.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e){
 mantenimiento.add(mantenimiento.botonActualizar);
                mantenimiento.add(mantenimiento.botonInsertar);
                mantenimiento.add(mantenimiento.botonEliminar);
                mantenimiento.add(mantenimiento.botonConsultar);
                mantenimiento.add(mantenimiento.scroll);
                mantenimiento.remove(PanelCatalogoGenero.this);
                mantenimiento.recargarTabla();
                mantenimiento.repaint();
                mantenimiento.revalidate();
                campoId.setText("");
                campoNombre.setText("");
    }
});

        // Botón Guardar
        botonGuardar = new JButton("Guardar");
        botonGuardar.setBounds(160, 160, 120, 40);
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setToolTipText("Guardar registro");
        ImageIcon icono = new ImageIcon("Iconos/guardar-el-archivo.png");
        botonGuardar.setIcon(icono);
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
                mantenimiento.remove(this);
                mantenimiento.recargarTabla();
                 mantenimiento.revalidate();
                 mantenimiento.repaint();
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
