import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelCartelera extends JPanel {

    private DefaultTableModel modeloTabla;
    private JTable tablaPeliculas;
    private PanelBoletos panelBoleto;

    public PanelCartelera(JTabbedPane pestanias, Connection con, PanelBoletos panelBoleto) {
        this.panelBoleto = panelBoleto;
        this.setLayout(null);
        this.setBackground(new Color(245, 245, 245));

        JLabel titulo = VistaPrincipal.crearEtiqueta("<html><center>Cartelera de Películas</center></html>", 0, 20, 800, 50);
        titulo.setHorizontalAlignment(JLabel.CENTER);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titulo.setForeground(new Color(64, 64, 64));
        this.add(titulo);

        String[] columnas = {"Código", "Título", "Género", "Duración (min)", "Clasificación"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaPeliculas = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tablaPeliculas);
        scroll.setBounds(50, 80, 700, 300);
        this.add(scroll);

        JButton btnSeleccionar = VistaPrincipal.crearBoton("Seleccionar Película", 100, 400, 200, 40, "Seleccionar película para boleto", "Iconos/seleccionar.png");
        this.add(btnSeleccionar);

        JButton btnActualizar = VistaPrincipal.crearBoton("Actualizar Cartelera", 350, 400, 200, 40, "Actualizar cartelera", "Iconos/actualizar.png");
        this.add(btnActualizar);

        // Acción para actualizar
        btnActualizar.addActionListener(e -> cargarPeliculas(con));

        // Acción para seleccionar una película
        btnSeleccionar.addActionListener(e -> {
            int fila = tablaPeliculas.getSelectedRow();
            if (fila >= 0) {
                String codigo = (String) modeloTabla.getValueAt(fila, 0);
                String tituloPelicula = (String) modeloTabla.getValueAt(fila, 1);

                panelBoleto.setPeliculaSeleccionada(codigo, tituloPelicula);
                pestanias.setSelectedIndex(4);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione una película primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        cargarPeliculas(con);
    }

    private void cargarPeliculas(Connection con) {
        try {
            modeloTabla.setRowCount(0); // Limpiar tabla
           java.sql.CallableStatement cstmt = con.prepareCall("{CALL listar_peliculas_cartelera()}");
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
               Object[] fila = {
                    rs.getString("codigo"),
                    rs.getString("titulo"),
                    rs.getString("genero"),
                    rs.getInt("duracion"),
                    rs.getString("clasificacion")
                };
                modeloTabla.addRow(fila);
            }
            rs.close();
            cstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar la cartelera: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
