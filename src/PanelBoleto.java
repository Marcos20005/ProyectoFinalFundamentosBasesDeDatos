import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
    Statement stmt = null;
    Connection con = null;

    public PanelBoleto(MantenimientoBoleto controlOriginal, int funcion, String codigoActualizar) 
            throws ClassNotFoundException, SQLException {
        
        MantenimientoBoleto control = controlOriginal;

        // Estableciendo conexión a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true", 
            "root", "cRojas34"
        );
        stmt = con.createStatement();

        // Título del panel
        JLabel lblTitulo;
        if (funcion == 0) {
            lblTitulo = crearEtiqueta("Datos de nuevo registro de Boleto", 250, 20, 300, 30);
        } else {
            lblTitulo = crearEtiqueta("Actualizar registro de Boleto", 250, 20, 300, 30);
        }
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

        JLabel lblFechaEmision = crearEtiqueta("Fecha de Emisión:", 150, 190, 140, 30);
        JTextField txtFechaEmision = crearCampoTexto(300, 190, 200, 30, "Ingrese la fecha de emisión (YYYY-MM-DD)");
        this.add(lblFechaEmision);
        this.add(txtFechaEmision);

        // cargar datos si es actualización
        if (funcion == 1 && codigoActualizar != null) {
            try (java.sql.CallableStatement cs = con.prepareCall("{CALL consultar_boleto(?)}")) {
                cs.setString(1, codigoActualizar);
                ResultSet rs = cs.executeQuery();

                if (rs.next()) {
                    txtCodigo.setText(rs.getString("codigo"));
                    txtAsiento.setText(rs.getString("asiento"));
                    txtPrecioFinal.setText(rs.getString("precio_final"));
                    txtFechaEmision.setText(rs.getString("fecha_emision"));
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron datos del boleto a actualizar.",
                            "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar datos del boleto: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        JButton botonGuardar = crearBoton("Guardar", 280, 240, 100, 40, 
         "Guardar cambios", "Iconos/guardar-el-archivo.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int eleccion = JOptionPane.showConfirmDialog(null, 
                        "¿Desea confirmar la acción?", 
                        "Confirmar acción", 
                        JOptionPane.YES_NO_OPTION, 
                        JOptionPane.QUESTION_MESSAGE);

                    if (eleccion == JOptionPane.YES_OPTION) {
                        if (funcion == 0) {
                            try (java.sql.CallableStatement cs = con.prepareCall("{CALL insertar_boleto(?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
                            cs.setString(1, txtCodigo.getText());
                            cs.setString(2, txtAsiento.getText());
                        cs.setDouble(3, Double.parseDouble(txtPrecioFinal.getText()));
                            cs.setString(4, txtFechaEmision.getText());
                            cs.setString(5, null); 
                          cs.setString(6, null); 
                        cs.setString(7, null); 
                            cs.setString(8, null); 
                            cs.setString(9, null);
                               cs.executeUpdate();
                                JOptionPane.showMessageDialog(null, "Boleto insertado correctamente.");
                            }
                        } else {
                            try (java.sql.CallableStatement cs = con.prepareCall("{CALL actualizar_boleto(?, ?, ?, ?)}")) {
                                cs.setString(1, txtCodigo.getText());
                                cs.setString(2, txtAsiento.getText());
                                cs.setDouble(3, Double.parseDouble(txtPrecioFinal.getText()));
                                cs.setString(4, txtFechaEmision.getText());
                                
                                int filas = cs.executeUpdate();
                                if (filas > 0) {
                                    JOptionPane.showMessageDialog(null, "Boleto actualizado correctamente.");
                                } else {
                                    JOptionPane.showMessageDialog(null,  "No se actualizó ningún boleto (verifique el código).",
                                     "Aviso", JOptionPane.WARNING_MESSAGE);
                    }
                    }
                    }
                }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, 
                        "Error al guardar boleto: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }

                // Limpiar campos y regresar al menú principal
                txtCodigo.setText("");
                txtAsiento.setText("");
                txtPrecioFinal.setText("");
                txtFechaEmision.setText("");

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

        JButton botonCancelar = crearBoton("Cancelar", 400, 240, 100, 40, 
                "Regresar al menú principal", "Iconos/cancelar.png");
        botonCancelar.setBackground(new Color(240, 128, 128));
        botonCancelar.setForeground(Color.WHITE);
        this.add(botonCancelar);

        botonCancelar.addActionListener(e -> {
            txtCodigo.setText("");
            txtAsiento.setText("");
            txtPrecioFinal.setText("");
            txtFechaEmision.setText("");
            control.add(control.botonActualizar);
            control.add(control.botonInsertar);
            control.add(control.botonEliminar);
            control.add(control.botonConsultar);
            control.add(control.scroll);
            control.remove(control.panel);
            control.recargarTabla();
            control.revalidate();
            control.repaint();
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
