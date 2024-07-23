package rpg.itens;

import java.util.HashMap;
import java.util.Map;

//Basicamente crio um mapa que associo o nome dos itens a suas instancias
public class Inventario {
    private Map<String, Item> itens;

    //Inicializo a instancia de Inventario
    public Inventario() {
        this.itens = new HashMap<>();
    }

    //Adiciono o item ao mapa criado "itens" utilizando o nome do item criado como chave
    public void adicionarItem(Item item) {
        itens.put(item.getNome(), item);
    }

    //Faz a mesma coisa só que ao inverso
    public void removerItem(Item item) {
        itens.remove(item.getNome());
    }

    //Retorna o item do mapa "itens" associado ao nome criado
    public Item getItem(String nome) {
        return itens.get(nome);
    }

    //Retorna o mapa itens
    public Map<String, Item> getItens() {
        return itens;
    }

    //Basicamente gera uma string contendo a representação em texto de todos os itens armazenados no mapa itens organizado linha por
    //linha instanciando StringBuilder e iterando Item no mapa "itens"
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Item item : itens.values()) {
            sb.append(item.toString()).append("\n");
        }
        return sb.toString();
    }
}