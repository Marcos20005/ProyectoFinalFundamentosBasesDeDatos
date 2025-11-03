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

public class PanelFunciones extends JPanel {
    Statement stmt = null;
    Connection con = null;

    public PanelFunciones(MantenimientoFunciones controlOriginal) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root", "cRojas34");
        stmt = con.createStatement();

        JLabel lBlcodigo = crearEtiqueta("Datos de nueva función", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);

        JLabel lblCodigo = crearEtiqueta("Código de función:", 150, 70, 140, 30);
        JTextField txtCodigo = crearCampoTexto(300, 70, 200, 30, "Ingrese código de función");
        this.add(lblCodigo);
        this.add(txtCodigo);

        JLabel lblFecha = crearEtiqueta("Fecha (YYYY-MM-DD):", 150, 120, 150, 30);
        JTextField txtFecha = crearCampoTexto(300, 120, 200, 30, "Ingrese fecha de la función");
        this.add(lblFecha);
        this.add(txtFecha);

        JLabel lblHora = crearEtiqueta("Hora (HH:MM:SS):", 150, 170, 140, 30);
        JTextField txtHora = crearCampoTexto(300, 170, 200, 30, "Ingrese hora de la función");
        this.add(lblHora);
        this.add(txtHora);

        JLabel lblPrecio = crearEtiqueta("Precio base:", 150, 220, 140, 30);
        JTextField txtPrecio = crearCampoTexto(300, 220, 200, 30, "Ingrese precio base");
        this.add(lblPrecio);
        this.add(txtPrecio);

        JButton botonGuardar = crearBoton("Guardar", 300, 270, 100, 40, "Guardar nueva función", "Iconos/guardar.png");
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
                        stmt.executeUpdate("INSERT INTO funciones (codigo, fecha, hora, precio_base) VALUES ('"
                                + txtCodigo.getText() + "', '"
                                + txtFecha.getText() + "', '"
                                + txtHora.getText() + "', '"
                                + txtPrecio.getText() + "');");
                    }
                } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);    
                }

                txtCodigo.setText("");
                txtFecha.setText("");
                txtHora.setText("");
                txtPrecio.setText("");

                // Regresar al panel de mantenimiento
                controlOriginal.add(controlOriginal.botonActualizar);
                controlOriginal.add(controlOriginal.botonInsertar);
                controlOriginal.add(controlOriginal.botonEliminar);
                controlOriginal.add(controlOriginal.botonConcultar);
                controlOriginal.add(controlOriginal.scroll);
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
