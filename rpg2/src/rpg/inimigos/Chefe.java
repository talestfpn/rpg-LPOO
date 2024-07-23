package rpg.inimigos;

public class Chefe extends Inimigo {
    public Chefe(String nome, int vida, int ataque, int defesa) {
        super(nome, vida, ataque, defesa);
    }

    //Retorna o diálogo específico
    @Override
    public String getDialogo() {
        return "Chefe: Eu sou o chefe supremo! Prepare-se para uma batalha épica!";
    }
}
