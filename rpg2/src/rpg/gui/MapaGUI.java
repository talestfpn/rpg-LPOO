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

public class MapaGUI implements MapaInterface {
    private JFrame frame;
    private JPanel mapPanel;
    private JButton upButton;
    private JButton downButton;
    private JButton leftButton;
    private JButton rightButton;
    private JLabel playerLabel;
    private Mapa mapa;
    private Personagem personagem;

    //Inicializa a instancia de MapaGUI e define o campo personagem com o personagem fornecido.
    ///Chama o método initialize para configurar a GUI.
    public MapaGUI(Personagem personagem) {
        this.personagem = personagem;
        initialize();
    }

    //Similar a classe passada basicamente cria um JFrame com o título, define que o programa deve 
    //ser encerrado quando a janela for fechada, define também o tamanho da janela e layout
    private void initialize() {
        frame = new JFrame("Mapa RPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLayout(new BorderLayout());

        //Instancia JPanel e define o layout como null para posicionamento absoluto dos componentes, além de definir a cor do fundo
        //do painel e centraliza-lo
        mapPanel = new JPanel();
        mapPanel.setLayout(null);
        mapPanel.setBackground(new Color(34, 139, 34)); // Cor verde para representar grama
        frame.add(mapPanel, BorderLayout.CENTER);

        //Basicamente instancia Font e define sua fonte, estilo e tamanho. Também cria novas instancias de JButton com textos 
        //direcionais e também define a fonte dos botões
        Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 24);

        upButton = new JButton("↑");
        downButton = new JButton("↓");
        leftButton = new JButton("←");
        rightButton = new JButton("→");

        upButton.setFont(emojiFont);
        downButton.setFont(emojiFont);
        leftButton.setFont(emojiFont);
        rightButton.setFont(emojiFont);

        //Basicamente instancia JPanel mais uma vez definindo os layouts e adicionando os botões em suas respectivas posições
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(upButton, BorderLayout.NORTH);
        controlPanel.add(downButton, BorderLayout.SOUTH);
        controlPanel.add(leftButton, BorderLayout.WEST);
        controlPanel.add(rightButton, BorderLayout.EAST);

        frame.add(controlPanel, BorderLayout.SOUTH);

        //Instancia JLabel com o icone obtido pelo método getPersonagem, define a fonte, posição e tamanho adicionando ao mapa
        playerLabel = new JLabel(getPersonagemIcon());
        playerLabel.setFont(emojiFont);
        playerLabel.setBounds(50, 50, 50, 50); // Ajustar o tamanho do jogador
        mapPanel.add(playerLabel);

        // Adiciona inimigos no mapa criando uma nova lista e adiciona instancias de ogro, satan e dragao na lista dos inimigos
        //já com os atributos especificados
        List<Inimigo> inimigos = new ArrayList<>();
        inimigos.add(new Ogro("Ogro", 150, 25, 5));
        inimigos.add(new Satan("Satan", 200, 35, 10));
        inimigos.add(new Dragao("Dragão", 250, 40, 20));

        //Basicamente criamos o mapa criando uma nova instância de Mapa com o painel do mapa,
        //o label do jogador, a lista de inimigos, o personagem e a referência à própria GUI do mapa.
        mapa = new Mapa(mapPanel, playerLabel, inimigos, personagem, this);

        // Adiciona a área do chefe criando um JLabel com o emoji específico. Define a fonte, tamanho, posição e adicionando ao mapa
        JLabel pontoAcesso = new JLabel("🚪");
        pontoAcesso.setFont(emojiFont);
        pontoAcesso.setBounds(300, 300, 50, 50); // Ajustar a posição conforme necessário
        mapPanel.add(pontoAcesso);

        //Cada botão recebe um ActionListener que move o jogador e verifica o ponto de acesso
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(0, -10);//cima
                verificarPontoAcesso();
            }
        });

        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(0, 10);//baixo
                verificarPontoAcesso();
            }
        });

        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(-10, 0);//esq
                verificarPontoAcesso();
            }
        });

        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapa.movePlayer(10, 0);//dir
                verificarPontoAcesso();
            }
        });

        frame.setVisible(true);
    }

    //Verifica se o personagem passou pela área do chefe obtendo a posição atual do jogador, definindo a posição da área do chefe.
    //Verifica também se a distancia entre o jogador e a área é < 10, caso seja, mostra o diálogo, se a resposta for sim esconde o mapa
    //e mostra o mapa chefe
    private void verificarPontoAcesso() {
        Point playerPos = playerLabel.getLocation();
        Point pontoAcessoPos = new Point(300, 300); // Posição do ponto de acesso

        if (playerPos.distance(pontoAcessoPos) < 10) { // Ajuste a distância conforme necessário
            int resposta = mostrarDialogoPersonalizado("Deseja entrar na área do chefe?");
            if (resposta == JOptionPane.YES_OPTION) {
                esconder();
                MapaChefeGUI mapaChefeGUI = new MapaChefeGUI(personagem);
                mapaChefeGUI.mostrar();
            }
        }
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

    //Basicamente é a interface onde aparecem os diálogos, nesse métodos se define o layout, cor de fundo, qual mensagem vai ser
    //mostrada, fonte e cor (essa parte mais estética)
    private int mostrarDialogoPersonalizado(String mensagem) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(20, 20, 20)); // Cor de fundo escura para tema épico

        JLabel label = new JLabel(mensagem);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setForeground(new Color(220, 220, 220)); // Cor de texto clara
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);

        Border padding = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        panel.setBorder(padding);

        UIManager.put("OptionPane.background", new Color(20, 20, 20));
        UIManager.put("Panel.background", new Color(20, 20, 20));
        UIManager.put("OptionPane.messageForeground", new Color(220, 220, 220));

        return JOptionPane.showConfirmDialog(mapPanel, panel, "Mensagem", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
    }
}