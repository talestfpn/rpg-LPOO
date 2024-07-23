package rpg.main;

import javax.swing.SwingUtilities;
import rpg.gui.SelecaoPersonagemGUI;

public class Main {
	//Inicia a interface de seleção de personagem
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SelecaoPersonagemGUI(new Main());
        }); 
    }

    //Inicia o jogo com o personagem selecionado e a interface mapa passando o personagem selecionado
    public void iniciarJogo(rpg.personagens.Personagem personagem) {
        SwingUtilities.invokeLater(() -> {
            rpg.gui.MapaGUI mapaGUI = new rpg.gui.MapaGUI(personagem);
            mapaGUI.mostrar();
        });
    }
}