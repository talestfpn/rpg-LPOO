package rpg.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rpg.main.Main;
import rpg.personagens.*;

public class SelecaoPersonagemGUI {
    private JFrame frame;
    private JComboBox<String> personagemComboBox;
    private JButton selecionarButton;
    private Main main;
    private JLabel personagemIconLabel;
    private JLabel descriptionLabel;

    //Inicializa a instancia de SelecaoPersonagemGUI, define o campo main com a instância de Main fornecida e por fim chama o método 
    //initialize para configurar a GUI.
    public SelecaoPersonagemGUI(Main main) {
        this.main = main;
        initialize();
    }

    //Basicamente cria uma nova instancia do JFrame e define, que o programa deve ser encerrado quando a janela for fechada, o tamanho
    //da janela, o layout, as cores, a fonte do texto, tamanho da fonte e cor da fonte. Também define um JComboBox com as opções de
    //personagens para serem escolhidos, fonte do texto, cor e outras coisas estéticas
    private void initialize() {
        frame = new JFrame("Seleção de Personagem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        Color darkGray = new Color(45, 45, 45);
        Color lightGray = new Color(169, 169, 169);
        Color darkRed = new Color(139, 0, 0);

        frame.getContentPane().setBackground(darkGray);

        JLabel titleLabel = new JLabel("Selecione seu Personagem", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setForeground(lightGray);
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBackground(darkGray);
        frame.add(panel, BorderLayout.CENTER);

        personagemComboBox = new JComboBox<>(new String[]{"Guerreiro", "Mago", "Arqueiro"});
        personagemComboBox.setFont(new Font("Serif", Font.BOLD, 20));
        personagemComboBox.setBackground(lightGray);
        personagemComboBox.setForeground(darkGray);
        panel.add(personagemComboBox);

        descriptionLabel = new JLabel("Descrição do Personagem", SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        descriptionLabel.setForeground(lightGray);
        panel.add(descriptionLabel);

        personagemIconLabel = new JLabel();
        personagemIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(personagemIconLabel);

        personagemComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPersonagem = (String) personagemComboBox.getSelectedItem();
                switch (selectedPersonagem) {
                    case "Guerreiro":
                        descriptionLabel.setText("<html><div style='text-align: center;'>O Guerreiro é forte e resistente,<br>perfeito para combate corpo a corpo.</div></html>");
                        break;
                    case "Mago":                    
                        descriptionLabel.setText("<html><div style='text-align: center;'>O Mago usa poderosas magias,<br>capaz de causar grandes danos à distância.</div></html>");
                        break;
                    case "Arqueiro":                      
                        descriptionLabel.setText("<html><div style='text-align: center;'>O Arqueiro é ágil e letal,<br>especialista em ataques à distância.</div></html>");
                        break;
                }
            }
        });

        //Cria o botão selecionar e define sua fonte, estilo, tamanho e outras coisas estéticas. Adiciona também um ActionListener
        //obtendo o personagem escolhido no comboBox, instanciando e iniciando o jogo com o personagem escolhido
        selecionarButton = new JButton("Selecionar");
        selecionarButton.setFont(new Font("Serif", Font.BOLD, 20));
        selecionarButton.setBackground(darkRed);
        selecionarButton.setForeground(lightGray);
        panel.add(selecionarButton);

        selecionarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPersonagem = (String) personagemComboBox.getSelectedItem();
                Personagem personagem = criarPersonagem(selectedPersonagem);
                main.iniciarJogo(personagem);
                frame.dispose();
            }
        });

        frame.setVisible(true);
    }

    //Cria uma instancia do Personagem com base no Enum Classe (tipo fornecido). Se o tipo for "Guerreiro", 
    //retorna uma nova instância de Guerreiro com os atributos especificados e assim sucessivamente.
    private Personagem criarPersonagem(String tipo) {
        if (tipo.equals("Guerreiro")) {
            return new Guerreiro("Guerreiro", 200, 50, 20, 15);
        } else if (tipo.equals("Mago")) {
            return new Mago("Mago", 150, 100, 25, 10);
        } else if (tipo.equals("Arqueiro")) {
            return new Arqueiro("Arqueiro", 175, 75, 22, 12);
        } else {
            return null;
        }
    }
}