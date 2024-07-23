package rpg.logica;

import java.util.ArrayList;
import java.util.List;
import rpg.personagens.Personagem;
import rpg.inimigos.Inimigo;

public class Combate {
    private List<Personagem> personagens;
    private List<Inimigo> inimigos;
    private Personagem personagemJogador;
    private boolean batalhaEmAndamento;
    private CombateLogger logger;
    private Missao missao; // Adicionar o campo missao

    //Inicializa uma nova instancia de Combate junto com o campo logger com valor fornecido. Também instancia uma nova lista de
    //personagens e inimigos, define que a batalha não está em andamento e inicializa a missao
    public Combate(CombateLogger logger) {
        this.logger = logger;
        personagens = new ArrayList<>();
        inimigos = new ArrayList<>();
        batalhaEmAndamento = false;
        missao = new Missao(); // Inicializar a missão
    }

    //Adiciona Personagens
    public void adicionarPersonagem(Personagem personagem) {
        personagens.add(personagem);
    }

    //Adiciona Inimigos
    public void adicionarInimigo(Inimigo inimigo) {
        inimigos.add(inimigo);
    }

    //Recebe personagem como parametro e define personagemJogador com um valor fornecido
    public void setPersonagemJogador(Personagem personagem) {
        this.personagemJogador = personagem;
    }

    //Getters que retornam listas de personagems, inimigos, o personagem controlado pelo usuário e se a batalha ainda está em andamento
    public List<Personagem> getPersonagens() {
        return personagens;
    }

    public List<Inimigo> getInimigos() {
        return inimigos;
    }

    public Personagem getPersonagemJogador() {
        return personagemJogador;
    }

    public boolean isBatalhaEmAndamento() {
        return batalhaEmAndamento;
    }

    //Declara o método que recebe o alvo na lista de inimigos, utilizo um if para que o código seja rodado apenas se a batalha estiver
    //em andamento, encontra o inimigo alvo pela lista fornecida e o personagem ataca o alvo. Verifica também se o inimigo foi derrotado
    //e caso seja remove o inimigo alvo da lista, registra no log e atualiza a missão. O else basicamente é o ataque do inimigo, caso ele
    //não seja derrotado. Também verifica se o personagem foi derrotado, caso seja, registra no log e coloca false batalhaEmAndamento
    public void atacar(int indiceAlvo) {
        if (batalhaEmAndamento) {
            Inimigo alvo = inimigos.get(indiceAlvo);
            personagemJogador.atacar(alvo);
            if (alvo.getVida() <= 0) {
                inimigos.remove(alvo);
                log(alvo.getNome() + " foi derrotado!");
                missao.registrarDerrotaInimigo(alvo, personagemJogador); // Atualizar a missão
            } else {
                alvo.atacar(personagemJogador);
                if (personagemJogador.getVida() <= 0) {
                    log(personagemJogador.getNome() + " foi derrotado!");
                    batalhaEmAndamento = false;
                }
            }
            if (inimigos.isEmpty()) {
                batalhaEmAndamento = false;
            }
        }
    }

    //Define batalhaEmAndamento como true e registra na log que a batalha começou
    public void iniciarBatalha() {
        batalhaEmAndamento = true;
        log("A batalha começou!");
    }

    //Registra alguma mensagem fornecida
    private void log(String message) {
        logger.log(message);
    }
}