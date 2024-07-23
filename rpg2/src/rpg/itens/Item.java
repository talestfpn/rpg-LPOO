package rpg.itens;

public class Item {
    private String nome;
    private String descricao;
    private int valor;
    private TipoItem tipo;

    //Inicializa uma nova instancia de Item
    public Item(String nome, String descricao, int valor, TipoItem tipo) {
        this.nome = nome;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
    }

    //Getters que declaram métodos que retornam o nome do item, descrição, valor e tipo
    
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getValor() {
        return valor;
    }

    public TipoItem getTipo() {
        return tipo;
    }

    //Sobrescreve o método que é declarado retornando uma string combinando o nome + descricao do item
    @Override
    public String toString() {
        return nome + ": " + descricao;
    }
}