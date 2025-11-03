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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class PanelCorreoCliente extends JPanel {
    Statement stmt = null;
    Connection con = null;

    public PanelCorreoCliente(MantenimientoCorreoCliente controlOriginal) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root",
                "cRojas34");
        stmt = con.createStatement();

        JLabel lTitulo = crearEtiqueta("Datos de nuevo correo de cliente", 200, 20, 300, 30);
        lTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lTitulo);

        JLabel lblID = crearEtiqueta("ID:", 150, 70, 140, 30);
        JTextField txtID = crearCampoTexto(300, 70, 200, 30, "Ingrese ID");
        this.add(lblID);
        this.add(txtID);

        JLabel lblCorreo = crearEtiqueta("Correo:", 150, 120, 140, 30);
        JTextField txtCorreo = crearCampoTexto(300, 120, 200, 30, "Ingrese correo");
        this.add(lblCorreo);
        this.add(txtCorreo);

        JLabel lblDescripcion = crearEtiqueta("Descripcion:", 150, 170, 140, 30);
        JTextField txtDescripcion = crearCampoTexto(300, 170, 200, 30, "Ingrese descripcion");
        this.add(lblDescripcion);
        this.add(txtDescripcion);

        JButton botonGuardar = crearBoton("Guardar", 300, 230, 100, 40, "Guardar nuevo correo", "src/imagenes/guardar.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        // Acción para guardar registro
        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar nuevo registro?", "Confirmar acción", JOptionPane.YES_NO_OPTION);
                    if (eleccion == 0) {
                        stmt.executeUpdate("INSERT INTO correo_cliente (id, correo, descripcion) VALUES ('" 
                                + txtID.getText() + "', '" 
                                + txtCorreo.getText() + "', '" 
                                + txtDescripcion.getText() + "');");
                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }

                // Limpiar campos
                txtID.setText("");
                txtCorreo.setText("");
                txtDescripcion.setText("");

                // Volver al panel principal
                controlOriginal.add(controlOriginal.botonActualizar);
                controlOriginal.add(controlOriginal.botonInsertar);
                controlOriginal.add(controlOriginal.botonEliminar);
                controlOriginal.add(controlOriginal.botonConsultar);
                controlOriginal.add(controlOriginal.scroll);
                controlOriginal.add(controlOriginal.combo);
                controlOriginal.remove(controlOriginal.panel);
                controlOriginal.recargarTabla();
                controlOriginal.revalidate();
                controlOriginal.repaint();
            }
        });
    }

    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto, String toolTip, String ruta) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setToolTipText(toolTip);
        ImageIcon icono = new ImageIcon(ruta);
        boton.setIcon(icono);
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        if (texto.equals("")) {
            boton.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return boton;
    }

    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, ancho, alto);
        campo.setToolTipText(toolTip);
        return campo;
    }

    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }
}
