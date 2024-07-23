package rpg.inimigos;

public class Dragao extends Inimigo {
    public Dragao(String nome, int vida, int ataque, int defesa) {
        super(nome, vida, ataque, defesa);
    }

  //Retorna o diálogo específico
    @Override
    public String getDialogo() {
        return "Dragão: Achei que você era mais forte, ROARR!";
    }
}
