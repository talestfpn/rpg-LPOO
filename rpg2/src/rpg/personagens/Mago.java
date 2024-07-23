package rpg.personagens;

import rpg.inimigos.Inimigo;
import rpg.excecoes.ManaInvalidaException;

public class Mago extends Personagem {

	//mesma logica todos
    public Mago(String nome, int vida, int mana, int ataque, int defesa) {
        super(nome, Classe.MAGO, vida, mana, ataque, defesa);
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

            if (getMana() < 15) {
                throw new ManaInvalidaException("Mana insuficiente para usar Bola de Fogo.");
            }
            System.out.println(getNome() + " está usando Bola de Fogo");
            int dano = getAtaque() * 3;
            alvo.receberDano(dano);
            setMana(getMana() - 15);
            if (getGui() != null) {
                getGui().log(getNome() + " usou Bola de Fogo em " + alvo.getNome() + " causando " + dano + " de dano. Custo: 15 de mana.");
                getGui().atualizarBarrasDeMana();
            }

            if (alvo.getVida() <= 0) {
                if (getGui() != null) {
                    getGui().log(alvo.getNome() + " foi derrotado.");
                }
                ganharExperiencia(50);
            } else {
                alvo.atacar(this);
            }
        } catch (ManaInvalidaException e) {
            if (getGui() != null) {
                getGui().log(e.getMessage());
            }
        }
    }
}