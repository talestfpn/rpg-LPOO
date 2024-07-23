package rpg.inimigos;

import rpg.acao.Atacante;
import rpg.personagens.Personagem;
import rpg.logica.CombateLogger;
import javax.swing.JOptionPane;

public abstract class Inimigo implements Atacante {
    protected String nome;
    protected int vida;
    protected int vidaMaxima;
    protected int ataque;
    protected int defesa;
    protected CombateLogger gui;

    //Inicializa a instancia de Inimigo
    public Inimigo(String nome, int vida, int ataque, int defesa) {
        this.nome = nome;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.ataque = ataque;
        this.defesa = defesa;
    }
    
    //Calcula o dano subtraindo a defesa do inimigo do dano total, caso esse dano seja positivo subtrai o dano da vida do inimigo
    //caso o dano recebido seja negativo (defesa >= dano o ataque é defendido)
    public void receberDano(int dano) {
    	int danoRecebido = dano - defesa;
    	if (danoRecebido > 0) {
    		vida -= danoRecebido;
    		if (vida < 0) {
    			vida = 0;
    		}
    		if (gui != null) {
    			gui.log(nome + " recebe " + danoRecebido + " de dano.");
    		}
    		if (vida <= 0) {
    			morrer();
    		}
    	} else {
    		if (gui != null) {
    			gui.log(nome + " defendeu o ataque.");
    		}
    	}
    }

    //Calcula o danoBase com base no método calcularDano, depois disso calcula a diferença de nível do personagem com base em 1 já que
    //o primeiro nível é 1. Ajusta o dano base com base na diferença de nível do personagem, , caso for menor que 10, essa expressão 
    //será positiva, aumentando assim o dano do inimigo e que por final aplica o dano no personagem
    @Override
    public void atacar(Personagem alvo) {
        int danoBase = calcularDano(ataque);
        int nivelDiferenca = alvo.getNivel() - 1;
        int dano = danoBase + (int)(danoBase * (0.1 * (10 - nivelDiferenca)));

        if (gui != null) {
            gui.log(nome + " ataca " + alvo.getNome() + " causando " + dano + " de dano. (sem as chances de defesa)");
        }
        alvo.receberDano(dano);
    }
    
    // Implementação vazia, já que inimigos não podem se atacar
    @Override
    public void atacar(Inimigo alvo) {
    }


    //Caso o inimigo morra atualiza a log
    public void morrer() {
        if (gui != null) {
            gui.log(nome + " foi derrotado.");
        }
    }

    //Restaura a vida para seu valor máximo
    public void restaurarVida() {
        this.vida = this.vidaMaxima;
    }

    //Retorna o nome do inimigo
    public String getNome() {
        return nome;
    }

    //Retorna a vida atual do inimigo
    public int getVida() {
        return vida;
    }

    //Retorna a vida máxima do inimigo
    public int getVidaMaxima() {
        return vidaMaxima;
    }

    //Define os logs de combate
    public void setGui(CombateLogger gui) {
        this.gui = gui;
    }

    //Calcula dano com base no ataque base e um valor aleatório, retornando o ataque base + um valor aleatorio de 1 ate 9
    private int calcularDano(int ataqueBase) {
        return ataqueBase + (int)(Math.random() * 10);
    }

    // Método para obter o diálogo específico do inimigo. 
    //Deve ser implementado pelas subclasses.
    public abstract String getDialogo();
}