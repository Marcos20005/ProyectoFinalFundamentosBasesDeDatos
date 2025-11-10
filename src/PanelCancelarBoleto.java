import java.awt.Color;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class PanelCancelarBoleto extends JPanel {   

    private DefaultTableModel modeloTabla;
    private JTable tablaBoletos;
    private Connection con;

    public PanelCancelarBoleto(JTabbedPane pestanias, Connection con) {
        this.con = con;
        this.setLayout(null);
        this.setBackground(new Color(245, 245, 245));

        JLabel titulo = VistaPrincipal.crearEtiqueta("<html><center>Cancelar Boleto</center></html>", 0, 20, 800, 40);
        titulo.setHorizontalAlignment(JLabel.CENTER);
        titulo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
        titulo.setForeground(new Color(50, 50, 50));
        this.add(titulo);

        String[] columnas = {"Código", "Película", "Cliente", "Sala", "Función", "Asiento", "Precio"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaBoletos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaBoletos);
        scrollPane.setBounds(50, 80, 700, 350);
        this.add(scrollPane);

        JButton botonActualizar = VistaPrincipal.crearBoton("Actualizar", 150, 460, 150, 40, "Actualizar lista de boletos", "Iconos/actualizar.png");
        this.add(botonActualizar);

        JButton botonCancelar = VistaPrincipal.crearBoton("Cancelar Boleto", 325, 460, 200, 40, "Cancelar boleto seleccionado", "Iconos/eliminar.png");
        this.add(botonCancelar);

        JButton botonSiguiente = VistaPrincipal.crearBoton("", 560, 460, 150, 40, "Siguiente tabla", "Iconos/un-paso-adelante.png");
        this.add(botonSiguiente);
     

        botonActualizar.addActionListener(e -> cargarBoletos());
        botonCancelar.addActionListener(e -> eliminarBoleto());
        botonSiguiente.addActionListener(e -> pestanias.setSelectedIndex(6));

        // Cargar los datos inicialmente
        cargarBoletos();
    }

    private void cargarBoletos() {
         modeloTabla.setRowCount(0);

         try(java.sql.CallableStatement cstmt = con.prepareCall("{CALL cine.listar_boleto()}")) {
            ResultSet rs = cstmt.executeQuery();
            while (rs.next()) {
                modeloTabla.addRow(new Object[]{
                    rs.getString("codigo"),
                    rs.getString("pelicula"),
                    rs.getString("cliente"),
                    rs.getString("numero_sala"),
                    rs.getString("funcion"),
                    rs.getString("asiento"),
                    rs.getDouble("precio_final")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar los boletos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarBoleto() {
    int fila = tablaBoletos.getSelectedRow();
    if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un boleto para cancelar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

         String codigoBoleto = modeloTabla.getValueAt(fila, 0).toString();
         String pelicula = modeloTabla.getValueAt(fila, 1).toString();
        String cliente = modeloTabla.getValueAt(fila, 2).toString();

     int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de cancelar este boleto?\n\n" +
                "Código: " + codigoBoleto + "\n" +
                "Película: " + pelicula + "\n" +
                "Cliente: " + cliente,
                "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);

    if (confirmar == JOptionPane.YES_OPTION) {
            try(java.sql.CallableStatement cstmt = con.prepareCall("{CALL eliminar_boleto(?)}")) {
                cstmt.setString(1, codigoBoleto);
                cstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Boleto cancelado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarBoletos();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al cancelar el boleto: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}