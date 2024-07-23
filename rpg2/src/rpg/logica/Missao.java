package rpg.logica;

import rpg.personagens.Personagem;
import rpg.inimigos.Inimigo;
import rpg.inimigos.Chefe;

public class Missao {
    private boolean chefeDerrotado;

    public Missao() {
        this.chefeDerrotado = false;
    }

    //Registra a derrota do inimigo e verifica se o inimigo é uma instância de Chefe
    public void registrarDerrotaInimigo(Inimigo inimigo, Personagem personagem) {
        if (inimigo instanceof Chefe) {
            chefeDerrotado = true;
            personagem.ganharExperiencia(150); // Adicionar experiência extra
            personagem.subirNivel(3); // Adicionar 3 níveis
            if (personagem.getGui() != null) {
                personagem.getGui().log("Missão completa: Chefe derrotado! Você ganhou 3 níveis!");
            }
            System.out.println("Missão completa: Chefe derrotado! Você ganhou 3 níveis!");
        }
    }
}