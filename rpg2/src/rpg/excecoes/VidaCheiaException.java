package rpg.excecoes;

//Basicamente define um construtor público que aceita uma string mensagem como argumento. 
public class VidaCheiaException extends Exception {
    private static final long serialVersionUID = 1L;

    public VidaCheiaException(String mensagem) {
        super(mensagem);
    }
}
