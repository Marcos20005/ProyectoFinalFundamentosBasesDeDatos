
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.themes.FlatMacLightLaf;


public class VistaRegistro extends JFrame{

    public VistaRegistro() {
         //Ajustes de FlatLaf
    UIManager.setLookAndFeel(new FlatMacLightLaf());
      UIManager.put("Button.arc", 80);
        UIManager.put("Component.arc", 20);
        UIManager.put("TextComponent.arc", 20);
        UIManager.put("Button.foreground", Color.WHITE);    
        UIManager.put("Button.background", new Color(30, 144, 255));
  this.setTitle("Registro de nueva cuenta");
    this.setSize(400, 500);
    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
    this.setLocationRelativeTo(null);
    this.setVisible(true);
    }


}
