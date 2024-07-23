package rpg.acao;

import rpg.inimigos.Inimigo;
import rpg.personagens.Personagem;

//Interface com método para ataque de inimigo ao personagem e personagem
//ao inimigo
public interface Atacante {
    void atacar(Personagem alvo);
    void atacar(Inimigo alvo);
}
