package licenta_imobiliare.main;

import licenta_imobiliare.dao.ParcareDAO;
import licenta_imobiliare.gui.PanouAutentificare;
import licenta_imobiliare.model.Client;

import javax.swing.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception e) {
            e.printStackTrace();
        }


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PanouAutentificare panouAutentificare = new PanouAutentificare();
                panouAutentificare.setVisible(true);
            }
        });

    }
}
