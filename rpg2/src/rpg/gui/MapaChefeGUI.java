package rpg.gui;

import javax.swing.*;
import javax.swing.border.Border; // Importando a classe Border
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import rpg.inimigos.*;
import rpg.personagens.Personagem;
import rpg.logica.Mapa;

public class MapaChefeGUI implements MapaInterface {
    private JFrame frame;
    private JPanel mapPanel;
    private JButton upButton;
    private JButton downButton;
    private JButton leftButton;
    private JButton rightButton;
    private JLabel playerLabel;
    private Mapa mapa;
    private Personagem personagem;

    //Inicializa a interface do mapa do chefe, define o personagem e chama o método initialize para configurar a interface.
    public MapaChefeGUI(Personagem personagem) {
        this.personagem = personagem;
        initialize();
    }

    //Basicamente instancia um novo JFrame com o título, define que ao fechar a janela o programa encerra, o tamanho da janela e o layout
    private void initialize() {
        frame = new JFrame("Área do Chefe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        //Similar a classe anterior cria a interface do mapa, define o layout em null para posicionamento absoluto dos componentes,
        //define a cor e centraliza
        mapPanel = new JPanel();
        mapPanel.setLayout(null);
        mapPanel.setBackground(new Color(139, 0, 0)); // Cor vermelha para representar perigo
        frame.add(mapPanel, BorderLayout.CENTER);

        //Basicamente a cópia da classe anterior
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 24);

        upButton = new JButton("↑");
        downButton = new JButton("↓");
        leftButton = new JButton("←");
        rightButton = new JButton("→");

        upButton.setFont(emojiFont);
        downButton.setFont(emojiFont);
        leftButton.setFont(emojiFont);
        rightButton.setFont(emojiFont);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(upButton, BorderLayout.NORTH);
        controlPanel.add(downButton, BorderLayout.SOUTH);
        controlPanel.add(leftButton, BorderLayout.WEST);
        controlPanel.add(rightButton, BorderLayout.EAST);

        frame.add(controlPanel, BorderLayout.SOUTH);

        //Adiciona o ícone do personagem instanciando um JLabel, definindo a fonte, tamanho e posição adicionando o personagem ao mapa
        playerLabel = new JLabel(getPersonagemIcon());
        playerLabel.setFont(emojiFont);
        playerLabel.setBounds(50, 50, 50, 50); // Ajustar o tamanho do jogador
        mapPanel.add(playerLabel);

        // Adiciona inimigos no mapa criando uma nova lista e adicionando um 'chefe' a lista dos inimigos já com os atributos 
        //especificados
        List<Inimigo> inimigos = new ArrayList<>();
        inimigos.add(new Chefe("Chefe Supremo", 500, 100, 50));

        //Instancia mapa novamente
        mapa = new Mapa(mapPanel, playerLabel, inimigos, personagem, this);

        ////Cada botão recebe um ActionListener que move o jogador igual a classe anterior
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(0, -10);
            }
        });

        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(0, 10);
            }
        });

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(-10, 0);
            }
        });

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(10, 0);
            }
        });

        frame.setVisible(true);
    }

  //Obtem o icone (emoji do personagem), verificando a classe, caso seja Mago retorna seu emoji específico e assim sucessivamente
    private String getPersonagemIcon() {
        if (personagem instanceof rpg.personagens.Mago) {
            return "🧙"; // Emoji de mago
        } else if (personagem instanceof rpg.personagens.Arqueiro) {
            return "🏹"; // Emoji de arqueiro
        } else if (personagem instanceof rpg.personagens.Guerreiro) {
            return "⚔"; // Emoji de guerreiro
        } else {
            return "P"; // Placeholder
        }
    }

  //Métodos do MapaInterface
    @Override
    public void mostrar() {
        frame.setVisible(true);
        mapa.mostrarMapa();
    }

    @Override
    public void esconder() {
        frame.setVisible(false);
    }
}