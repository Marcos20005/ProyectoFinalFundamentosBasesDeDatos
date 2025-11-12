import java.awt.Color;
import java.sql.CallableStatement;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class MantenimientoPelicula extends JPanel {
    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;

    JButton botonInsertar, botonActualizar, botonEliminar, botonConsultar;
    JScrollPane scroll;
    PanelPelicula panel;

    public MantenimientoPelicula() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root", "cRojas34");

        JLabel label = new JLabel("Mantenimiento de tabla pelicula");
        label.setBounds(200, 20, 280, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"Codigo", "Titulo", "ID Genero","Duracion","ID clasificacion"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);

        CallableStatement stmt = con.prepareCall("{CALL listar_pelicula_mantenimiento()}");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            String codigo = rs.getString("codigo");
            String titulo = rs.getString("titulo");
            String iDgenero = rs.getString("nombre_genero");
            String duracion = rs.getString("duracion");
            String iDclasificacion = rs.getString("tipo_clasificacion");
            String fila[] = {codigo, titulo, iDgenero,duracion,iDclasificacion};
            modelo.addRow(fila);
        }

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 700, 200);
        this.add(scroll);

        // Botones alineados horizontalmente
       botonInsertar = crearBoton("Insertar", 40, 360, 100, 40, "Insertar nuevo registro",
                "Iconos/insertar-cuadrado.png");
        botonInsertar.setBackground(new Color(46, 204, 113));
        botonInsertar.setForeground(Color.WHITE);
        this.add(botonInsertar);

        botonActualizar = crearBoton("Actualizar", 200, 360, 100, 40, "Actualizar registro existente",
                "Iconos/boton-editar.png");
        botonActualizar.setBackground(new Color(255, 179, 71));
        botonActualizar.setForeground(Color.WHITE);
        this.add(botonActualizar);

        botonEliminar = crearBoton("Eliminar", 360, 360, 100, 40, "Eliminar registro existente",
                "Iconos/eliminar.png");
        botonEliminar.setBackground(new Color(240, 128, 128));
        botonEliminar.setForeground(Color.WHITE);
        this.add(botonEliminar);

        botonConsultar = crearBoton("Consultar", 520, 360, 100, 40, "Consultar registro existente",
                "Iconos/buscar.png");
        botonConsultar.setBackground(new Color(135, 206, 250));
        botonConsultar.setForeground(Color.WHITE);
        this.add(botonConsultar);


        // Campos de texto para actualizar, eliminar y consultar
        JTextField campoActualizar = crearCampoTexto(200, 410, 100, 30, "Ingrese ID a actualizar");
        this.add(campoActualizar);

        JTextField campoEliminar = crearCampoTexto(360, 410, 100, 30, "Ingrese ID a eliminar");
        this.add(campoEliminar);

        JTextField campoConsultar = crearCampoTexto(520, 410, 100, 30, "Ingrese ID a consultar");
        this.add(campoConsultar);

        // Acción Insertar -> muestra panel de inserción
        botonInsertar.addActionListener(e -> {
            try {
                this.remove(scroll);
                this.remove(botonInsertar);
                this.remove(botonActualizar);
                this.remove(botonEliminar);
                this.remove(botonConsultar);
                // this.remove(campoActualizar);
                // this.remove(campoEliminar);
                // this.remove(campoConsultar);

               panel = new PanelPelicula(MantenimientoPelicula.this,0, "");
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 500);
                this.add(panel);
                this.setComponentZOrder(panel, 0);
                this.revalidate();
                this.repaint();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Acción Actualizar
        botonActualizar.addActionListener(e -> {
            modelo.setRowCount(0);
            boolean encontrado = false;

            if (tabla.isEditing()) {
                tabla.getCellEditor().stopCellEditing();
            }

            try {
               CallableStatement buscar = con.prepareCall("{CALL buscar_pelicula(?)}");
                buscar.setString(1, campoActualizar.getText());
                ResultSet resultado = buscar.executeQuery();

                if (resultado.next()) {
                    this.remove(scroll);
                    this.remove(botonInsertar);
                    this.remove(botonActualizar);
                    this.remove(botonEliminar);
                    this.remove(botonConsultar);

                // this.remove(campoActualizar);
                // this.remove(campoEliminar);
                // this.remove(campoConsultar);

               panel = new PanelPelicula(MantenimientoPelicula.this, 1, campoActualizar.getText());
                    panel.setLayout(null);
                    panel.setBounds(10, 70, 700, 500);
                    this.add(panel);
                    this.setComponentZOrder(panel, 1);
                    this.revalidate();
                    this.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }
                campoActualizar.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error SQL: " + ex.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });


        // Acción Eliminar
        botonEliminar.addActionListener(e -> {
            try {
                CallableStatement buscar = con.prepareCall("{CALL buscar_pelicula(?)}");
                    buscar.setString(1, campoEliminar.getText());
                    ResultSet resultado = buscar.executeQuery();

                    if (resultado.next()) {
                        int eleccion = JOptionPane.showConfirmDialog(null, "¿Desea confirmar la eliminación del registro?",
                                "Confirmación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                                if (eleccion == 0) {
                                    CallableStatement eliminar = con.prepareCall("{CALL eliminar_pelicula(?)}");
                                    // lo que hace es eliminar un registro de la base de datos
                                    eliminar.setString(1, campoEliminar.getText());
                                    eliminar.executeUpdate();
                                    JOptionPane.showMessageDialog(null, "Registro eliminado");
                                    recargarTabla();
                                } 
                    }else {
                        JOptionPane.showMessageDialog(null, "Registro no encontrado");
                    }
                    campoEliminar.setText("");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        // Acción Consultar
        botonConsultar.addActionListener(e -> {
           try{ 
            modelo.setRowCount(0);
           CallableStatement stmtBuscar;

           if(!campoConsultar.getText().isEmpty()){
               stmtBuscar = con.prepareCall("{CALL buscar_pelicula(?)}");
               stmtBuscar.setString(1, campoConsultar.getText());
           } else {
               stmtBuscar = con.prepareCall("{CALL listar_pelicula_mantenimiento()}");
           }

           ResultSet rsBuscar = stmtBuscar.executeQuery();
           while(rsBuscar.next()){
            String fila[] = {
                rsBuscar.getString("codigo"),
                rsBuscar.getString("titulo"),
                rsBuscar.getString("nombre_genero"),
                rsBuscar.getString("duracion"),
                rsBuscar.getString("tipo_clasificacion")
            };
            modelo.addRow(fila);
           }
           campoConsultar.setText("");
              } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    // Método para recargar tabla
   public void recargarTabla() {
        try {
            JTable tabla = (JTable) ((JScrollPane) scroll).getViewport().getView();
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
            modelo.setRowCount(0);
            CallableStatement stmt = con.prepareCall("{CALL listar_pelicula_mantenimiento()}");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fila[] = {
                    rs.getString("codigo"),
                    rs.getString("titulo"),
                    rs.getString("nombre_genero"),
                    rs.getString("duracion"),
                    rs.getString("tipo_clasificacion")
                };
            modelo.addRow(fila);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

