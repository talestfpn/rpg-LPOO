package rpg.excecoes;

//Basicamente define um construtor público que aceita uma string mensagem como argumento. 
public class ManaInvalidaException extends Exception {
    private static final long serialVersionUID = 1L;//compatibilidade

    public ManaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
