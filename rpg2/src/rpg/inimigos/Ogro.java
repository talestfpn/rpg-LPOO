package rpg.inimigos;

public class Ogro extends Inimigo {
    public Ogro(String nome, int vida, int ataque, int defesa) {
        super(nome, vida, ataque, defesa);
    }

  //Retorna o diálogo específico
    @Override
    public String getDialogo() {
        return "Ogro: Eu sou grande e forte, mas meio bobo. Quer lutar?";
    }
}
