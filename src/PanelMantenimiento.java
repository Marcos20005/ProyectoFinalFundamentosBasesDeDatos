import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class PanelMantenimiento extends JPanel{

    static JPanel panelSeleccionado = null;
    static JFrame pantalla;

    public PanelMantenimiento(JTabbedPane pestanias, Connection con, VistaPrincipal miVista) {
        this.setLayout(null);
        

        String listaMantenimiento[] = {
            "Mantenimiento Usuario",
            "Mantenimiento Boleto",
            //"Mantenimiento Catalogo Clasificacion",
           // "Mantenimiento Catalogo Genero",
            //"Mantenimiento Catalogo Puesto",
            //"Mantenimiento Catalogo Tipo",
            "Mantenimiento Cliente",
            //"Mantenimiento Correo Cliente",
            "Mantenimiento Empleado",
            "Mantenimiento Funciones",
            "Mantenimiento Pelicula",
            "Mantenimiento Sala",
            //"Mantenimiento Telefono Cliente",
            //"Mantenimiento Telefono Empleado"
        };

        JComboBox<String> comboMantenimientos = new JComboBox<>(listaMantenimiento);
        comboMantenimientos.setBounds(70, 30, 250, 30);
        this.add(comboMantenimientos);

        JButton botonSeleccionar = new JButton("Seleccionar");
        ImageIcon icon = new ImageIcon("Iconos/mouse-pointer-click.png");
        botonSeleccionar.setIcon(icon);
        botonSeleccionar.setToolTipText("Seleccion de tabla");
        botonSeleccionar.setBounds(370, 30, 120, 30);
        this.add(botonSeleccionar);

        botonSeleccionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String seleccion = (String) comboMantenimientos.getSelectedItem();

                try {
                    JPanel nuevoPanel = null;

                    switch (seleccion) {
                        case "Mantenimiento Usuario":
                            nuevoPanel = new MantenimientoUsuario();
                            break;
                        case "Mantenimiento Boleto":
                            nuevoPanel = new MantenimientoBoleto();
                            break;
                        // case "Mantenimiento Catalogo Clasificacion":
                        //     nuevoPanel = new MantenimientoCatalogoClasificacion();
                        //     break;
                        // case "Mantenimiento Catalogo Genero":
                        //     nuevoPanel = new MantenimientoCatalogoGenero();
                        //     break;
                        // case "Mantenimiento Catalogo Puesto":
                        //     nuevoPanel = new MantenimientoCatalogoPuesto();
                        //     break;
                        // case "Mantenimiento Catalogo Tipo":
                        //     nuevoPanel = new MantenimientoCatalogoTipo();
                        //     break;
                        case "Mantenimiento Cliente":
                            nuevoPanel = new MantenimientoCliente(miVista);
                            break;
                        // case "Mantenimiento Correo Cliente":
                        //     nuevoPanel = new MantenimientoCorreoCliente();
                        //     break;
                        case "Mantenimiento Empleado":
                            nuevoPanel = new MantenimientoEmpleado(miVista);
                            break;
                        case "Mantenimiento Funciones":
                            nuevoPanel = new MantenimientoFunciones(miVista);
                            break;
                        case "Mantenimiento Pelicula":
                            nuevoPanel = new MantenimientoPelicula();
                            break;
                        case "Mantenimiento Sala":
                            nuevoPanel = new MantenimientoSala(miVista);
                            break;
                        // case "Mantenimiento Telefono Cliente":
                        //     nuevoPanel = new MantenimientoTelefonoCliente();
                        //     break;
                        // case "Mantenimiento Telefono Empleado":
                        //     nuevoPanel = new MantenimientoTelefonoEmpleado();
                        //     break;
                    }


                    if (nuevoPanel != null) {
                        actualizarPanel(nuevoPanel,PanelMantenimiento.this);
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    static public void actualizarPanel(JPanel panel,JPanel principal) {
        if (panelSeleccionado != null) {
            principal.remove(panelSeleccionado);
        }
        panel.setBounds(20, 80, 780, 550);
        panel.setLayout(null);
        principal.add(panel);
        panelSeleccionado = panel;
        principal.revalidate();
        principal.repaint();
    }


}
