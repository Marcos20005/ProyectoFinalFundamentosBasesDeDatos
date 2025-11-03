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
import javax.swing.SwingConstants;

public class PanelCatalogoClasificacion extends JPanel {

    JTextField campoId, campoTipo;
    JButton botonGuardar;
    Connection con;
    Statement stmt;
    MantenimientoCatalogoClasificacion mantenimiento;

    public PanelCatalogoClasificacion(MantenimientoCatalogoClasificacion mantenimiento) {
        this.mantenimiento = mantenimiento;
        setLayout(null);

        // Label y campo para ID
        JLabel lblId = new JLabel("ID Clasificacion:");
        lblId.setBounds(90, 50, 120, 30);
        this.add(lblId);
        campoId = crearCampoTexto(190, 50, 200, 30, "Ingrese ID");
        this.add(campoId);

        // Label y campo para Tipo
        JLabel lblTipo = new JLabel("Tipo Clasificacion:");
        lblTipo.setBounds(80, 100, 120, 30);
        this.add(lblTipo);
        campoTipo = crearCampoTexto(190, 100, 200, 30, "Ingrese tipo de clasificacion");
        this.add(campoTipo);

        // Botón Guardar
        botonGuardar = crearBoton("Guardar", 180, 160, 120, 40, "Guardar registro", "Iconos/guardar-el-archivo.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        // Acción Guardar
        botonGuardar.addActionListener(e -> {
            if (campoId.getText().isEmpty() || campoTipo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe llenar todos los campos");
                return;
            }
            try {
                int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if(eleccion==0){
                                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                        "root", "cRojas34");
                stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO catalogo_clasificacion (id_clasificacion, tipo_clasificacion) VALUES ('"
                        + campoId.getText() + "','" + campoTipo.getText() + "');");

                JOptionPane.showMessageDialog(null, "Registro insertado correctamente");
                mantenimiento.recargarTabla();
                  mantenimiento.add(mantenimiento.botonActualizar);
                mantenimiento.add(mantenimiento.botonInsertar);
                mantenimiento.add(mantenimiento.botonEliminar);
                mantenimiento.add(mantenimiento.botonConsultar);
                mantenimiento.add(mantenimiento.scroll);
                mantenimiento.add(mantenimiento.combo);
                mantenimiento.remove(this);
                campoId.setText("");
                campoTipo.setText("");

            } 
        }   catch (SQLException ex) {
            } catch (ClassNotFoundException e1) {
         
              JOptionPane.showMessageDialog(null, "Error al insertar el registro");
        }
        });
    }

    // Método para crear botones (tal como lo proporcionaste)
    static public JButton crearBoton(String texto, int x, int y, int ancho, int alto, String toolTip, String ruta) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, ancho, alto);
        boton.setToolTipText(toolTip);
        boton.setIcon(new javax.swing.ImageIcon(ruta));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        if (texto.equals("")) {
            boton.setHorizontalAlignment(SwingConstants.CENTER);
        }
        return boton;
    }

    // Método para crear campos de texto (tal como lo proporcionaste)
    static public JTextField crearCampoTexto(int x, int y, int ancho, int alto, String toolTip) {
        JTextField campo = new JTextField();
        campo.setBounds(x, y, ancho, alto);
        campo.setToolTipText(toolTip);
        return campo;
    }
}
