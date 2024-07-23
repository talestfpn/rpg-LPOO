package rpg.personagens;

import rpg.inimigos.Inimigo;
import rpg.excecoes.ManaInvalidaException;

public class Guerreiro extends Personagem {

	//mesma logica todos
    public Guerreiro(String nome, int vida, int mana, int ataque, int defesa) {
        super(nome, Classe.GUERREIRO, vida, mana, ataque, defesa);
    }

    @Override
    public void usarHabilidadeEspecial(Inimigo alvo) {
        try {
            if (alvo.getVida() <= 0) {
                if (getGui() != null) {
                    getGui().log("Erro: Inimigo já está morto.");
                }
                return;
            }

            if (getMana() < 5) {
                throw new ManaInvalidaException("Mana insuficiente para usar Golpe de Escudo.");
            }
            System.out.println(getNome() + " está usando Golpe de Escudo");
            setDefesa(getDefesa() * 2);
            setMana(getMana() - 5);
            if (getGui() != null) {
                getGui().log(getNome() + " usou Golpe de Escudo. Defesa duplicada. Custo: 5 de mana.");
                getGui().atualizarBarrasDeMana();
            }
        } catch (ManaInvalidaException e) {
            if (getGui() != null) {
                getGui().log(e.getMessage());
            }
        }
    }
}