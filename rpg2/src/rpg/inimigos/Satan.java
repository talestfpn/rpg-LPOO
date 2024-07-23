package rpg.inimigos;

public class Satan extends Inimigo {
    public Satan(String nome, int vida, int ataque, int defesa) {
        super(nome, vida, ataque, defesa);
    }

  //Retorna o diálogo específico
    @Override
    public String getDialogo() {
        return "Satan: Eu sou o seu pior pesadelo! Enfrente-me se tiver coragem!";
    }
}