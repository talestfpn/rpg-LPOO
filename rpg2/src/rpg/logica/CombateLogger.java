package rpg.logica;

//Basicamente declara métodos para serem usados nas atualizações das "barras" e mensagens na interface de combate
public interface CombateLogger {
    void log(String message);
    void atualizarBarrasDeVida();
    void atualizarBarrasDeMana();
    void atualizarBarraDeExperiencia();
}