package rpg.personagens;

import rpg.acao.Atacante;
import rpg.itens.Inventario;
import rpg.itens.Item;
import rpg.itens.TipoItem;
import rpg.inimigos.Inimigo;
import rpg.logica.CombateLogger;
import rpg.excecoes.VidaCheiaException;

public abstract class Personagem implements Atacante {
    private String nome;
    private Classe classe;
    private int vida;
    private int vidaMaxima;
    private int mana;
    private int manaMaxima;
    private int ataque;
    private int defesa;
    private Inventario inventario;
    private CombateLogger gui;
    private int nivel;
    private int experiencia;
    private int experienciaParaProximoNivel;

    //Inicializa uma nova instancia de Personagem
    public Personagem(String nome, Classe classe, int vida, int mana, int ataque, int defesa) {
        this.nome = nome;
        this.classe = classe;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.mana = mana;
        this.manaMaxima = mana;
        this.ataque = ataque;
        this.defesa = defesa;
        this.inventario = new Inventario();
        this.nivel = 1;
        this.experiencia = 0;
        this.experienciaParaProximoNivel = 100;
    }

    //Define a interface CombateLogger que será utilizada para atualizar eventos do combate
    public void setGui(CombateLogger gui) {
        this.gui = gui;
    }

    //Retorna a interface de combate associada ao personagem
    public CombateLogger getGui() {
        return gui;
    }

    //Método para usar habilidade especial
    public abstract void usarHabilidadeEspecial(Inimigo alvo);
    

    // Basicamente verifica se a vida do inimigo é menor ou igual a zero, caso seja registra o erro no log de combate
    @Override
    public void atacar(Inimigo alvo) {
        if (alvo.getVida() <= 0) {
            if (gui != null) {
                gui.log("Erro ao aplicar dano: Inimigo já está morto.");
            }
            return;
        }

        // Calcula o dano base baseado no método calcularDano e sem seguida aplica o dano
        int dano = calcularDano(ataque);
        if (gui != null) {
            gui.log(nome + " ataca " + alvo.getNome() + " causando " + dano + " de dano. (sem as chances de defesa)");
        }
        alvo.receberDano(dano);
    }

 // Implementação vazia, já que personagens não podem se atacar
    @Override
    public void atacar(Personagem alvo) {
    }
        
    //Verifica a defesa calculando se a chance de defesa gerada é menor que a chance base, caso seja defende o ataque
    //também calcula o dano recebido, subtraindo a defesa do dano, caso o dano recebido seja maior que zero aplica dano a vida do 
    //personagem, reduz a vida e registra o dano na log de combate
    public void receberDano(int dano) {
    	int chanceDeDefesa = (int) (Math.random() * 100);
    	int chanceBase = 10 + (nivel * 2);
    	
    	if (chanceDeDefesa < chanceBase) {
    		if (gui != null) {
    			gui.log(nome + " defendeu o ataque.");
    		}
    	} else {
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
    				if (gui != null) {
    					gui.log(nome + " foi derrotado.");
    				}
    			}
    		}                 
    	}
    }   
    
    //Verifica se o tipo do item é cura, caso seja verifica se a vida está cheia, e caso esteja aplica a excecao
    //Também aplica o item de cura aumentando a vida do personagem pelo valor do item, garante que a vida nao exceda a vida maxima
    //remove o item do inventário quando usado e registra na log de combate de acordo com o que faz o item
    //o else verifica se o item é de dano, caso seja aumenta o ataque, remove o item do inventário e registra na log de combate
    public void usarItem(Item item) {
        try {
            if (item.getTipo() == TipoItem.CURA) {
                if (vida == vidaMaxima) {
                    throw new VidaCheiaException("Vida já está cheia, não é possível usar item de cura.");
                }
                vida += item.getValor();
                if (vida > vidaMaxima) {
                    vida = vidaMaxima;
                }
                inventario.removerItem(item);
                if (gui != null) {
                    gui.log(nome + " usou " + item.getNome() + " e recuperou " + item.getValor() + " de vida.");
                }
            } else if (item.getTipo() == TipoItem.DANO) {
                ataque *= item.getValor();
                inventario.removerItem(item);
                if (gui != null) {
                    gui.log(nome + " usou " + item.getNome() + " e seu ataque foi aumentado.");
                }
            }
        } catch (VidaCheiaException e) {
            if (gui != null) {
                gui.log(e.getMessage());
            }
        }
    }

    //Permite ao personagem ganhar EXP adicionando experiencia ganha a EXP atual, verifica se a EXP é suficiente para subir de nivel
    //caso seja chama o método
    public void ganharExperiencia(int experienciaGanha) {
        experiencia += experienciaGanha;
        if (experiencia >= experienciaParaProximoNivel) {
            subirNivel();
        }
    }

    //Sobe vários níveis e basicamente chama o método subirNivel para cada nível subido
    public void subirNivel(int niveis) { // Adicionando o método que aceita um argumento int
        for (int i = 0; i < niveis; i++) {
            subirNivel();
        }
    }

    //Sobe apenas 1 nível por vez, reduzindo a EXP pela quantidade necessária para o prox nível, também ajusta a quantidade de EXP
    //para o prox nível. Cada vez que sobe de nível aumenta os atributos base, registra na log de combate e atualiza as barras de status
    private void subirNivel() {
        experiencia = experiencia - experienciaParaProximoNivel;
        nivel++;
        experienciaParaProximoNivel += experienciaParaProximoNivel / 2;

        vidaMaxima += vidaMaxima * 0.20;
        vida = vidaMaxima;
        ataque += ataque * 0.10;
        defesa += defesa * 0.15;

        manaMaxima += manaMaxima * 0.20;
        mana = manaMaxima;

        if (gui != null) {
            gui.log(nome + " subiu para o nível " + nivel + "!");
            gui.atualizarBarrasDeVida();
            gui.atualizarBarrasDeMana();
            gui.atualizarBarraDeExperiencia();
        }
    }

    //Restaura a vida do personagem para o máximo
    public void restaurarVida() {
        this.vida = this.vidaMaxima;
    }

    //Getters que retornam basicamente o nome do personagem, vida atual, vida máxima, nivel, EXP dentre outros
    public String getNome() {
        return nome;
    }

    public int getVida() {
        return vida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getNivel() {
        return nivel;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getExperienciaParaProximoNivel() {
        return experienciaParaProximoNivel;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getManaMaxima() {
        return manaMaxima;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public Classe getClasse() {
        return classe;
    }

    //Calcula dano com base no ataque base e um valor aleatório, retornando o ataque base + um valor aleatorio de 1 ate 9
    private int calcularDano(int ataqueBase) {
        return ataqueBase + (int) (Math.random() * 10);
    }
}