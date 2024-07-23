package rpg.personagens;

import rpg.inimigos.Inimigo;
import rpg.excecoes.ManaInvalidaException;


public class Arqueiro extends Personagem {

	//Instancia arqueiro com os parametros fornecidos
    public Arqueiro(String nome, int vida, int mana, int ataque, int defesa) {
        super(nome, Classe.ARQUEIRO, vida, mana, ataque, defesa);
    }

    //Implementa a habilidade especial e verifica se o inimigo está morto, caso esteja retorna na log de combate o erro
    //Também verifica se a classe Arqueiro possui mana suficiente, caso não possua lança a excecao
    //Calcula também o dano da habilidade especial, aplica o dano, reduz a mana, registra no log que a habilidade foi utilizada e
    //atualiza a barra de mana
    @Override
    public void usarHabilidadeEspecial(Inimigo alvo) {
        try {
            if (alvo.getVida() <= 0) {
                if (getGui() != null) {
                    getGui().log("Erro: Inimigo já está morto.");
                }
                return;
            }

            if (getMana() < 10) {
                throw new ManaInvalidaException("Mana insuficiente para usar Tiro Certeiro.");
            }
            System.out.println(getNome() + " está usando Tiro Certeiro");
            int dano = getAtaque() * 2;
            alvo.receberDano(dano);
            setMana(getMana() - 10);
            if (getGui() != null) {
                getGui().log(getNome() + " usou Tiro Certeiro em " + alvo.getNome() + " causando " + dano + " de dano. Custo: 10 de mana.");
                getGui().atualizarBarrasDeMana();
            }

            //Verifica se a vida do inimigo é menor ou igual a zero após a habilidade especial.caso seja, concede experiência ao Arqueiro.
            if (alvo.getVida() <= 0) {
                if (getGui() != null) {
                    getGui().log(alvo.getNome() + " foi derrotado.");
                }
                ganharExperiencia(50);
            } else {
                alvo.atacar(this);
            }
            //captura a execao e registra na log de combate
        } catch (ManaInvalidaException e) {
            if (getGui() != null) {
                getGui().log(e.getMessage());
            }
        }
    }
}