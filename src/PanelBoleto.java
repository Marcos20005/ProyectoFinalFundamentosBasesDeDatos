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

public class PanelBoleto extends JPanel {
    //Objetos de conexion SQL
    Statement stmt = null;
    Connection con = null;

    public PanelBoleto(MantenimientoBoleto controlOriginal) throws ClassNotFoundException, SQLException {
        MantenimientoBoleto control = controlOriginal;
        //Estableciendo conexion a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", "root", "cRojas34");
        stmt = con.createStatement();

        JLabel lblTitulo = crearEtiqueta("Datos de nuevo registro de Boleto", 250, 20, 300, 30);
        this.add(lblTitulo);

        JLabel lblCodigo = crearEtiqueta("Código:", 150, 70, 140, 30);
        JTextField txtCodigo = crearCampoTexto(300, 70, 200, 30, "Ingrese el código del boleto");
        this.add(lblCodigo);
        this.add(txtCodigo);

        JLabel lblAsiento = crearEtiqueta("Número de Asiento:", 150, 110, 140, 30);
        JTextField txtAsiento = crearCampoTexto(300, 110, 200, 30, "Ingrese el número de asiento");
        this.add(lblAsiento);
        this.add(txtAsiento);

        JLabel lblPrecioFinal = crearEtiqueta("Precio Final:", 150, 150, 140, 30);
        JTextField txtPrecioFinal = crearCampoTexto(300, 150, 200, 30, "Ingrese el precio final");
        this.add(lblPrecioFinal);
        this.add(txtPrecioFinal);

        JLabel lblFechaEmicion = crearEtiqueta("Fecha de Emisión:", 150, 190, 140, 30);
        JTextField txtFechaEmicion = crearCampoTexto(300, 190, 200, 30, "Ingrese la fecha de emisión (YYYY-MM-DD)");
        this.add(lblFechaEmicion);
        this.add(txtFechaEmicion);

        JButton botonGuardar = crearBoton("Guardar", 300, 240, 100, 40, "Guardar nuevo boleto", "src/imagenes/guardar.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar nuevo registro?", "Confirmar acción", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        stmt.executeUpdate("INSERT INTO boleto (codigo, asiento, precio_final, fecha_emicion) VALUES ('" + txtCodigo.getText() + "', '" + txtAsiento.getText() + "', '" + txtPrecioFinal.getText() + "', '" + txtFechaEmicion.getText() + "');");
                    }
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
                txtCodigo.setText("");
                txtAsiento.setText("");
                txtPrecioFinal.setText("");
                txtFechaEmicion.setText("");

                control.add(control.botonActualizar);
                control.add(control.botonInsertar);
                control.add(control.botonEliminar);
                control.add(control.botonConsultar);
                control.add(control.scroll);
                control.remove(control.panel);
                control.recargarTabla();
                control.revalidate();
                control.repaint();
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

    //Metodo declarado para crear campos de texto
    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campoTexto = new JTextField();
        campoTexto.setBounds(x, y, ancho, alto);
        campoTexto.setToolTipText(toolTip);
        return campoTexto;
    }

    //Metodo declarado para crear etiquetas
    static public JLabel crearEtiqueta(String texto, int x, int y, int ancho, int alto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setBounds(x, y, ancho, alto);
        return etiqueta;
    }
}