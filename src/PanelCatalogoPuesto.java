import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane;

public class PanelCatalogoPuesto extends JPanel {

    JTextField campoId, campoNombre;
    JButton botonGuardar, botonCancelar;
    MantenimientoCatalogoPuesto parent;

    public PanelCatalogoPuesto(MantenimientoCatalogoPuesto parent) {
        this.parent = parent;
        setLayout(null);

        // Etiquetas
        JLabel labelId = new JLabel("ID Puesto:");
        labelId.setBounds(100, 50, 120, 30);
        this.add(labelId);

        JLabel labelNombre = new JLabel("Nombre Puesto:");
        labelNombre.setBounds(100, 100, 120, 30);
        this.add(labelNombre);

        // Campos de texto
        campoId = crearCampoTexto(230, 50, 150, 30, "Ingrese ID del puesto");
        this.add(campoId);

        campoNombre = crearCampoTexto(230, 100, 150, 30, "Ingrese nombre del puesto");
        this.add(campoNombre);

        // Botones
        botonGuardar = crearBoton("Guardar", 100, 180, 120, 40, "Guardar nuevo puesto","Iconos/guardar-el-archivo.png");
        botonGuardar.setBackground(new Color(46, 204, 113));
        botonGuardar.setForeground(Color.WHITE);
        this.add(botonGuardar);

        botonCancelar = crearBoton("Cancelar", 260, 180, 120, 40, "Cancelar inserción","Iconos/paso-atras.png");
        botonCancelar.setBackground(new Color(240, 128, 128));
        botonCancelar.setForeground(Color.WHITE);
        this.add(botonCancelar);

        // Funcionalidad botones
        botonGuardar.addActionListener(e -> {
            if (campoId.getText().isEmpty() || campoNombre.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe completar todos los campos", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea confirmar nuevo registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
       if(eleccion==0){
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                        "root", "cRojas34");
                Statement stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO catalogo_puesto (id_puesto, nombre_puesto) VALUES ('"
                        + campoId.getText() + "', '" + campoNombre.getText() + "')");

                JOptionPane.showMessageDialog(null, "Registro insertado correctamente");
                 parent.add(parent.botonActualizar);
                parent.add(parent.botonInsertar);
                parent.add(parent.botonEliminar);
                parent.add(parent.botonConsultar);
                parent.add(parent.scroll);
                parent.add(parent.combo);
                parent.remove(this);
                parent.revalidate();
                parent.repaint();
                parent.recargarTabla();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Hubo un error por favor vuelva a intentar", "Advertencia", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        }
        });

        botonCancelar.addActionListener(e -> {
             parent.add(parent.botonActualizar);
                parent.add(parent.botonInsertar);
                parent.add(parent.botonEliminar);
                parent.add(parent.botonConsultar);
                parent.add(parent.scroll);
                parent.add(parent.combo);
            parent.remove(this);
            parent.revalidate();
            parent.repaint();
            parent.recargarTabla();
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
}
