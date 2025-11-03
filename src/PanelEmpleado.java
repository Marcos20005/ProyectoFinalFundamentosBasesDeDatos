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

public class PanelEmpleado extends JPanel {
    Statement stmt = null;
    Connection con = null;

    public PanelEmpleado(MantenimientoEmpleado controlOriginal) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", 
                "root", "cRojas34");
        stmt = con.createStatement();

        JLabel lBlcodigo = crearEtiqueta("Datos de nuevo empleado", 200, 20, 300, 30);
        lBlcodigo.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(lBlcodigo);

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

        JLabel lblIDPuesto = crearEtiqueta("ID Puesto:", 150, 320, 140, 30);
        JTextField txtIDPuesto = crearCampoTexto(300, 320, 200, 30, "Ingrese ID puesto");
        this.add(lblIDPuesto);
        this.add(txtIDPuesto);

        JButton botonGuardar = crearBoton("Guardar", 300, 370, 100, 40, "Guardar nuevo empleado", "src/imagenes/guardar.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (eleccion == 0) {
                        stmt.executeUpdate("INSERT INTO empleado (id, primer_nombre, segundo_nombre, primer_apellido, segundo_apellido, id_puesto) " +
                                "VALUES ('" + txtID.getText() + "', '" + txtPrimerNombre.getText() + "', '" + txtSegundoNombre.getText() + 
                                "', '" + txtPrimerApellido.getText() + "', '" + txtSegundoApellido.getText() + "', '" + txtIDPuesto.getText() + "');");
                    }
                } catch (SQLException ex) {
                    
                    JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar","Advertencia",JOptionPane.WARNING_MESSAGE);
                }

                txtID.setText("");
                txtPrimerNombre.setText("");
                txtSegundoNombre.setText("");
                txtPrimerApellido.setText("");
                txtSegundoApellido.setText("");
                txtIDPuesto.setText("");

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

