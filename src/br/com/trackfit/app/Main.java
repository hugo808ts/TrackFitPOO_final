package br.com.trackfit.app;

import br.com.trackfit.service.AcademiaService;
import br.com.trackfit.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                    // Mantem o visual padrao se o sistema nao permitir trocar o tema.
                }
                MainFrame frame = new MainFrame(AcademiaService.criarPadrao());
                frame.setVisible(true);
            }
        });
    }
}
