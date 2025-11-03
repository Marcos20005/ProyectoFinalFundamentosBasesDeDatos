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

public class PanelCatalogoTipo extends JPanel {

    // Objetos SQL
    Statement stmt = null;
    Connection con = null;

    public PanelCatalogoTipo(MantenimientoCatalogoTipo controlOriginal) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                    "root", "cRojas34");
            stmt = con.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JLabel lBlcodigo = crearEtiqueta("Datos de nuevo tipo de catálogo", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);

        JLabel lblID = crearEtiqueta("ID de tipo:", 150, 70, 140, 30);
        JTextField txtID = crearCampoTexto(300, 70, 200, 30, "Ingrese ID de tipo");
        this.add(lblID);
        this.add(txtID);

        JLabel lblNombre = crearEtiqueta("Nombre de tipo:", 150, 120, 140, 30);
        JTextField txtNombre = crearCampoTexto(300, 120, 200, 30, "Ingrese nombre de tipo");
        this.add(lblNombre);
        this.add(txtNombre);

        JButton botonGuardar = crearBoton("Guardar", 300, 170, 100, 40,
                "Guardar nuevo tipo", "src/imagenes/guardar.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        stmt.executeUpdate("INSERT INTO catalogo_tipo (id_tipo, nombre_tipo) VALUES ('"
                                + txtID.getText() + "', '" + txtNombre.getText() + "');");
                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar",
                            "Advertencia", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                txtID.setText("");
                txtNombre.setText("");

                // Regresar al panel de mantenimiento
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
