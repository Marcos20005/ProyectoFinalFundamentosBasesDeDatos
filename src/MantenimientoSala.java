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

public class MantenimientoSala extends JPanel {
    // Conexión a la base de datos
    Statement stmt = null;
    Connection con = null;
    ResultSet rs = null;

    public JTextField campoActualizar, campoEliminar, campoConsultar;
    JButton botonInsertar, botonActualizar, botonEliminar, botonConsultar;
    JScrollPane scroll;
    PanelSala panel;

    public MantenimientoSala(VistaPrincipal miVista) throws SQLException, ClassNotFoundException {
        // conexión a la base de datos
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cine?verifyServerCertificate=false&useSSL=true",
                "root", "cRojas34");

        JLabel label = new JLabel("Mantenimiento de tabla sala");
        label.setBounds(200, 20, 280, 30);
        this.add(label);

        JTable tabla = new JTable();
        Object columnas[] = {"Numero de sala", "Capacidad", "ID tipo"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tabla.setModel(modelo);

        // Cargar datos iniciales en la tabla desde la base de datos con el procedimiento almacenado
        CallableStatement stmtListar = con.prepareCall("{CALL listar_sala_mantenimiento()}");
        rs = stmtListar.executeQuery();
        while (rs.next()) {
            String fila[] = {rs.getString("numero_sala"), rs.getString("capacidad"), rs.getString("id_tipo")};
            modelo.addRow(fila);
        }

        scroll = new JScrollPane(tabla);
        scroll.setBounds(30, 70, 700, 200);
        this.add(scroll);

        // Botones para insertar, actualizar, eliminar y consultar
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
         campoActualizar = crearCampoTexto(200, 410, 100, 30, "Ingrese ID a actualizar");
        this.add(campoActualizar);
      

         campoEliminar = crearCampoTexto(360, 410, 100, 30, "Ingrese ID a eliminar");
        this.add(campoEliminar);

         campoConsultar = crearCampoTexto(520, 410, 100, 30, "Ingrese ID a consultar");
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

               panel = new PanelSala(MantenimientoSala.this,0,"", miVista);
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
            try {
                // Verificar si el registro existe antes de actualizar con el procedimiento almacenado
                CallableStatement buscar = con.prepareCall("{CALL buscar_sala(?)}");
                buscar.setString(1, campoActualizar.getText());
                ResultSet resultado = buscar.executeQuery();

                if (resultado.next()) {
                this.removeAll();
                panel = new PanelSala(MantenimientoSala.this,1, campoActualizar.getText(), miVista);
                panel.setLayout(null);
                panel.setBounds(10, 70, 700, 500);
                this.add(panel);
                this.revalidate();
                this.repaint();

                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }
                campoActualizar.setText("");
            } catch (SQLException ex) { 
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        // Acción Eliminar
        botonEliminar.addActionListener(e -> {
            try {
                // Verificar si el registro existe antes de eliminar con el procedimiento almacenado
                CallableStatement buscar = con.prepareCall("{CALL buscar_sala(?)}");
                buscar.setString(1, campoEliminar.getText());
                ResultSet resultado = buscar.executeQuery();
    
                if (resultado.next()) {
                    int eleccion = JOptionPane.showConfirmDialog(null,
                            "¿Desea eliminar este registro?", "Confirmar acción",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    
                    if (eleccion == JOptionPane.YES_OPTION) {
                        // Eliminar el registro usando el procedimiento almacenado
                        CallableStatement eliminar = con.prepareCall("{CALL eliminar_sala(?)}");
                        eliminar.setString(1, campoEliminar.getText());
                        eliminar.execute();
                        JOptionPane.showMessageDialog(null, "Registro eliminado exitosamente");
                        recargarTabla();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Registro no encontrado");
                }
                
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
         campoEliminar.setText("");
            miVista.actualizarPaneles();
        });

        // Acción Consultar
        botonConsultar.addActionListener(e -> {
            modelo.setRowCount(0);
            try {
                CallableStatement stmtBuscar;
                if (!campoConsultar.getText().isEmpty()) {
                    // Buscar por ID usando el procedimiento almacenado
                    stmtBuscar = con.prepareCall("{CALL buscar_sala(?)}");
                    stmtBuscar.setString(1, campoConsultar.getText());
                } else {
                    // Listar todos los registros si no se proporciona ID usando el procedimiento almacenado
                    stmtBuscar = con.prepareCall("{CALL listar_sala_mantenimiento()}");
                }

                ResultSet rsBuscar = stmtBuscar.executeQuery();
                while (rsBuscar.next()) {
                    String fila[] = {
                        rsBuscar.getString("numero_sala"),
                        rsBuscar.getString("capacidad"),
                        rsBuscar.getString("id_tipo")
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

            //Se utiliza el procedimiento almacenado para listar las salas
            CallableStatement stmt = con.prepareCall("{CALL listar_sala_mantenimiento()}");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String fila[] = {rs.getString("numero_sala"), rs.getString("capacidad"), rs.getString("id_tipo")};
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Métodos para crear componentes GUI
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

