package rpg.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import rpg.itens.*;
import rpg.logica.*;
import rpg.personagens.*;
import rpg.inimigos.*;

public class CombateGUI implements CombateLogger {
    private JFrame frame;
    private JComboBox<String> inimigoComboBox;
    private JComboBox<String> itemComboBox;
    private JTextArea logArea;
    private JTextArea inventarioArea;
    private JProgressBar vidaPersonagemBar;
    private JProgressBar manaPersonagemBar;
    private JProgressBar vidaInimigoBar;
    private JProgressBar experienciaPersonagemBar;
    private JLabel nivelPersonagemLabel;
    private Combate combate;
    private MapaInterface mapaGUI;

    //Inicializa a interface de combate instanciando combate e passando this como CombateLogger. Também define qual o personagem o 
    //jogador escolheu, a GUI no personagem, inicializa os itens predefinidos para o personagem e chama o método para configurar a GUI
    public CombateGUI(Personagem personagem) {
        this.combate = new Combate(this);
        this.combate.setPersonagemJogador(personagem);
        personagem.setGui(this);
        inicializarItensPredefinidos(personagem);
        initialize();
    }

    //Define a interface do MapaInterface
    public void setMapaGUI(MapaInterface mapaGUI) {
        this.mapaGUI = mapaGUI;
    }

    //Configura o JFrame criando um novo título, definindo a operação padrão ao fechar a janela como encerrar o programa, define o
    //tamanho e o layout
    private void initialize() {
        frame = new JFrame("Combate RPG");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);
        frame.setLayout(new BorderLayout());

        //Basicamente configura a parte superior
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        frame.add(topPanel, BorderLayout.NORTH);

        //Basicamente configura as informações do personagem criando um JLabel com o nível do personagem e um JPanel com as informações 
        //adicionando o JLabel nivelPersonagem ao JPanel personagemInfo
        JPanel personagemInfoPanel = new JPanel(new GridLayout(1, 3));
        topPanel.add(personagemInfoPanel);

        nivelPersonagemLabel = new JLabel("Nível: " + combate.getPersonagemJogador().getNivel(), SwingConstants.CENTER);
        nivelPersonagemLabel.setFont(new Font("Serif", Font.BOLD, 20));
        nivelPersonagemLabel.setForeground(new Color(220, 220, 220));
        personagemInfoPanel.add(nivelPersonagemLabel);

        //Crias as barras de status
        JPanel barrasPanel = new JPanel(new GridLayout(3, 1));
        personagemInfoPanel.add(barrasPanel);

        //Cria as barras de vida, definindo cor e fonte. Além de instanciar um JProgressBar que criará uma nova barra de progresso para
        //as barras de vida adicionando ao JPanel barrasPanel no final
        JLabel vidaPersonagemLabel = new JLabel("Vida do Personagem:", SwingConstants.CENTER);
        vidaPersonagemLabel.setForeground(new Color(220, 220, 220));
        barrasPanel.add(vidaPersonagemLabel);

        vidaPersonagemBar = new JProgressBar();
        vidaPersonagemBar.setStringPainted(true);
        barrasPanel.add(vidaPersonagemBar);

        //Faz a mesma coisa só que com as barras de mana adicionando ao JPanel barrasPanel no final
        JLabel manaPersonagemLabel = new JLabel("Mana do Personagem:", SwingConstants.CENTER);
        manaPersonagemLabel.setForeground(new Color(220, 220, 220));
        barrasPanel.add(manaPersonagemLabel);

        manaPersonagemBar = new JProgressBar();
        manaPersonagemBar.setStringPainted(true);
        barrasPanel.add(manaPersonagemBar);

        //Mesma coisa só que com as barras de EXP adicionando ao JPanel barrasPanel no final
        JLabel experienciaPersonagemLabel = new JLabel("Experiência:", SwingConstants.CENTER);
        experienciaPersonagemLabel.setForeground(new Color(220, 220, 220));
        barrasPanel.add(experienciaPersonagemLabel);

        experienciaPersonagemBar = new JProgressBar();
        experienciaPersonagemBar.setStringPainted(true);
        barrasPanel.add(experienciaPersonagemBar);

        //Cria o painel de combate definindo o layout
        JPanel combatePanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(combatePanel);

        //Cria uma seleção de alvo instanciando uma nova JLabel, definindo a cor e centralizando o texto. Instancia também um JComboBox
        //vazio para selecionar os inimigos
        JLabel escolhaAlvoLabel = new JLabel("Escolha seu alvo:", SwingConstants.CENTER);
        escolhaAlvoLabel.setForeground(new Color(220, 220, 220));
        combatePanel.add(escolhaAlvoLabel);

        inimigoComboBox = new JComboBox<>();
        combatePanel.add(inimigoComboBox);

        //Cria o botão atacar
        JButton atacarButton = new JButton("Atacar");
        combatePanel.add(atacarButton);

        //Cria a seleção de itens similar ao de inimigos, intanciando uma nova JLabel, definindo a cor e centralizando o texto. Intancia
        //também um JComboBox vazio para selecionar os itens 
        JLabel escolhaItemLabel = new JLabel("Escolha um item:", SwingConstants.CENTER);
        escolhaItemLabel.setForeground(new Color(220, 220, 220));
        combatePanel.add(escolhaItemLabel);

        itemComboBox = new JComboBox<>();
        combatePanel.add(itemComboBox);

        //Cria o botão usar item
        JButton usarItemButton = new JButton("Usar Item");
        combatePanel.add(usarItemButton);

        //Cria o botão usar habilidade especial
        JButton habilidadeEspecialButton = new JButton("Habilidade Especial");
        combatePanel.add(habilidadeEspecialButton);

        //Cria uma área de texto para o log que funcionará como um "dublador". São definidas a fonte, cor de fundo, cor do texto, título
        //e centralização
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Serif", Font.PLAIN, 16));
        logArea.setBackground(new Color(30, 30, 30));
        logArea.setForeground(new Color(220, 220, 220));
        JScrollPane logScrollPane = new JScrollPane(logArea);
        logScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 50, 50)), "Log de Combate"));
        frame.add(logScrollPane, BorderLayout.CENTER);

        //Cria outra área de status, só que agora para os inimigos
        JPanel statusPanel = new JPanel(new GridLayout(1, 2));
        frame.add(statusPanel, BorderLayout.SOUTH);

        //Barra de vida do inimigo similar ao do personagem
        JLabel vidaInimigoLabel = new JLabel("Vida do Inimigo:", SwingConstants.CENTER);
        vidaInimigoLabel.setForeground(new Color(220, 220, 220));
        statusPanel.add(vidaInimigoLabel);

        vidaInimigoBar = new JProgressBar();
        vidaInimigoBar.setStringPainted(true);
        statusPanel.add(vidaInimigoBar);

        //Cria painel do inventário similar a área de log de combate. só que menor
        JPanel inventarioPanel = new JPanel(new BorderLayout());
        inventarioArea = new JTextArea();
        inventarioArea.setEditable(false);
        inventarioArea.setFont(new Font("Serif", Font.PLAIN, 16));
        inventarioArea.setBackground(new Color(30, 30, 30));
        inventarioArea.setForeground(new Color(220, 220, 220));
        JScrollPane inventarioScrollPane = new JScrollPane(inventarioArea);
        inventarioScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(150, 50, 50)), "Inventário"));
        inventarioPanel.add(inventarioScrollPane, BorderLayout.CENTER);

        //Cria o botão de adicionar item
        JButton adicionarItemButton = new JButton("Adicionar Item");
        inventarioPanel.add(adicionarItemButton, BorderLayout.SOUTH);

        frame.add(inventarioPanel, BorderLayout.EAST);

        //Cria um botão para voltar ao mapa adicionando um actionlistener ao botão para definir a ação ao ser clicado 
        JButton voltarButton = new JButton("Voltar ao Mapa");
        voltarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                esconder();
                mapaGUI.mostrar();
            }
        });

        frame.add(voltarButton, BorderLayout.WEST);

        //Cria o ActionListener ao botão para definir a ação ao ser clicado.
        //Ao ser clicado chama o método atacar da classe combate passando o índice do inimigo selecionado. 
        //Isso inicia o ataque ao inimigo selecionado. Tambem atualiza a lista de inimigos, baras de vida, barras de mana e barras de EXP
        //Verifica se a batalha terminou, para determinar se todos os inimigos foram derrotados
        atacarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = inimigoComboBox.getSelectedIndex();
                if (selectedIndex >= 0) {
                    combate.atacar(selectedIndex);
                    atualizarInimigos();
                    atualizarBarrasDeVida();
                    atualizarBarrasDeMana();
                    atualizarBarraDeExperiencia();
                    verificarFimDaBatalha();
                } else {
                    log("Selecione um alvo para atacar.");
                }
            }
        });

      //Cria o ActionListener ao botão para definir a ação ao ser clicado.
      //Caso clicado exibe uma caixa de diálogo para o usuário inserir o nome do item e armazena o valor inserido na variável nomeItem.
      //exibe uma caixa de diálogo para o usuário inserir a descrição do item e armazena o valor inserido na variável descricaoItem.
      //exibe uma caixa de diálogo para o usuário inserir o valor do item. (valor é convertido para int e armazenado na variavel 'valor'
      //exibe uma caixa de diálogo para o usuário escolher entre dano e cura, logo depois se cria o novo objeto Item usando os valores
      //adiciona o item a caixa de inventario do personagem, atualiza o inventário, os itens e gera uma log indicando que o item foi
      //adicionado com sucesso
        adicionarItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomeItem = JOptionPane.showInputDialog(frame, "Nome do Item:");
                String descricaoItem = JOptionPane.showInputDialog(frame, "Descrição do Item:");
                int valor = Integer.parseInt(JOptionPane.showInputDialog(frame, "Valor:"));
                String[] opcoes = {"Cura", "Dano"};
                int tipoEscolhido = JOptionPane.showOptionDialog(frame, "Tipo de Item:", "Escolha o tipo", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcoes, opcoes[0]);
                TipoItem tipo = tipoEscolhido == 0 ? TipoItem.CURA : TipoItem.DANO;
                Item item = new Item(nomeItem, descricaoItem, valor, tipo);
                combate.getPersonagemJogador().getInventario().adicionarItem(item);
                atualizarInventario();
                atualizarItens();
                log("Item adicionado: " + item.getNome());
            }
        });

      //Cria o ActionListener ao botão para definir a ação ao ser clicado.
      //Ao ser clicado obtém o item selecionado no ComboBox a partir do inventário do personagem, além de retornar o item com base 
      //na seleção atual. Logo após chama o método usarItem e aplica o efeito do item específico. Atualiza também barra de vida, itens,
      //o inventário e o log de combate
        usarItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = itemComboBox.getSelectedIndex();
                if (selectedIndex >= 0) {
                    Item item = combate.getPersonagemJogador().getInventario().getItem(itemComboBox.getItemAt(selectedIndex));
                    combate.getPersonagemJogador().usarItem(item);
                    atualizarInventario();
                    atualizarItens();
                    atualizarBarrasDeVida();
                    atualizarBarrasDeMana();
                    log("Item usado: " + item.getNome());
                } else { 
                    log("Selecione um item para usar.");
                }
            }
        });

      //Cria o ActionListener ao botão para definir a ação ao ser clicado.
      //Ao ser clicado verifica se a lista de inimigos não está vazia, se estiver retorna false e uma mensagem de log é exibida
      //obtém o primeiro inimigo da lista e chama o método de habilidade especial, passando o primeiro inimigo como argumento e
      //aplicando a habilidade. Atualiza a lista de inimigos, barras de vida e barras de mana  
        habilidadeEspecialButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!combate.getInimigos().isEmpty()) {
                    Inimigo inimigo = combate.getInimigos().get(0);
                    combate.getPersonagemJogador().usarHabilidadeEspecial(inimigo);
                    atualizarInimigos();
                    atualizarBarrasDeVida();
                    atualizarBarrasDeMana();
                    verificarFimDaBatalha();
                } else {
                    log("Não há inimigos para usar a habilidade especial.");
                }
            }
        });

        frame.setVisible(false);
    }

    //Adiciona os itens predefinidos ao inventário do personagem. Utilizando um Enum TipoItem diferenciando se o item é para cura ou
    //dano
    private void inicializarItensPredefinidos(Personagem personagem) {
        personagem.getInventario().adicionarItem(new Item("🍖", "Cura 80 de vida", 80, TipoItem.CURA));
        personagem.getInventario().adicionarItem(new Item("🥛", "Cura 40 de vida", 40, TipoItem.CURA));
        personagem.getInventario().adicionarItem(new Item("🗡", "Dobra o dano", 2, TipoItem.DANO));
    }

    //Getter que retorna a instancia atual de combate
    public Combate getCombate() {
        return combate;
    }

    //Métodos da interface MapaInterface
    public void mostrar() {
        frame.setVisible(true);
        atualizarInimigos();
        atualizarInventario();
        atualizarItens();
        atualizarBarrasDeVida();
        atualizarBarrasDeMana();
        atualizarBarraDeExperiencia();
    }

    public void esconder() {
        frame.setVisible(false);
    }

    //Adiciona uma mensagem a área de log 
    @Override
    public void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    //Remove todos os itens do inimigoComboBox e adiciona os nomes dos inimigos atuais.
    private void atualizarInimigos() {
        inimigoComboBox.removeAllItems();
        for (Inimigo i : combate.getInimigos()) {
            inimigoComboBox.addItem(i.getNome());
        }
    }

    //Define o texto do inventarioArea com a representação em string do inventário do personagem.
    private void atualizarInventario() {
        inventarioArea.setText(combate.getPersonagemJogador().getInventario().toString());
    }

    // Remove todos os itens do itemComboBox e adiciona os nomes dos itens atuais do inventário do personagem.
    private void atualizarItens() {
        itemComboBox.removeAllItems();
        for (Item item : combate.getPersonagemJogador().getInventario().getItens().values()) {
            itemComboBox.addItem(item.getNome());
        }
    }

    //Atualiza a vidaPersonagemBar com os valores atuais de vida do personagem 
    //e a vidaInimigoBar com os valores atuais de vida do inimigo (se houver).
    @Override
    public void atualizarBarrasDeVida() {
        Personagem jogador = combate.getPersonagemJogador();
        vidaPersonagemBar.setMaximum(jogador.getVidaMaxima());
        vidaPersonagemBar.setValue(Math.max(jogador.getVida(), 0));
        vidaPersonagemBar.setString(jogador.getVida() + "/" + jogador.getVidaMaxima());

        if (!combate.getInimigos().isEmpty()) {
            Inimigo inimigo = combate.getInimigos().get(0);
            vidaInimigoBar.setMaximum(inimigo.getVidaMaxima());
            vidaInimigoBar.setValue(Math.max(inimigo.getVida(), 0));
            vidaInimigoBar.setString(inimigo.getVida() + "/" + inimigo.getVidaMaxima());
        } else {
            vidaInimigoBar.setValue(0);
            vidaInimigoBar.setString("");
        }
    }

    //Atualiza a manaPersonagemBar com os valores atuais de mana do personagem.
    @Override
    public void atualizarBarrasDeMana() {
        Personagem jogador = combate.getPersonagemJogador();
        manaPersonagemBar.setMaximum(jogador.getManaMaxima());
        manaPersonagemBar.setValue(jogador.getMana());
        manaPersonagemBar.setString(jogador.getMana() + "/" + jogador.getManaMaxima());
    }

    //Atualiza a experienciaPersonagemBar com os valores atuais de 
    //experiência do personagem e define o texto do nivelPersonagemLabel com o nível atual do personagem.
    @Override
    public void atualizarBarraDeExperiencia() {
        Personagem jogador = combate.getPersonagemJogador();
        experienciaPersonagemBar.setMaximum(jogador.getExperienciaParaProximoNivel());
        experienciaPersonagemBar.setValue(jogador.getExperiencia());
        experienciaPersonagemBar.setString(jogador.getExperiencia() + "/" + jogador.getExperienciaParaProximoNivel());

        nivelPersonagemLabel.setText("Nível: " + jogador.getNivel());
    }

    //Verifica o fim da batalha, se não estiver em andamento atualiza o log. Se os inimigos forem derrotados
    //concede EXP ao jogador e atualiza a barra de EXP, caso não, chama o método mostrarTelaDerrota
    
    private void verificarFimDaBatalha() {
        if (!combate.isBatalhaEmAndamento()) {
            log("Batalha finalizada!");
            if (combate.getInimigos().isEmpty()) {
                log("Os personagens venceram!");
                combate.getPersonagemJogador().ganharExperiencia(50);
                atualizarBarraDeExperiencia();
            } else if (combate.getPersonagemJogador().getVida() <= 0) {
                log("Os inimigos venceram!");
                mostrarTelaDerrota();
            } else {
                log("Batalha interrompida.");
            }
        }
    }

    //Exibe a interface de derrota istanciando um novo JFrame e definindo a fonte e cor
    private void mostrarTelaDerrota() {
        JFrame derrotaFrame = new JFrame("Derrota");
        derrotaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        derrotaFrame.setSize(400, 200);
        derrotaFrame.setLayout(new BorderLayout());

        //Mostra a mensagem de derrota criando um JLabel. Centraliza o texto e define a cor e fonte do texto
        JLabel derrotaLabel = new JLabel("Você foi derrotado!", SwingConstants.CENTER);
        derrotaLabel.setFont(new Font("Serif", Font.BOLD, 24));
        derrotaLabel.setForeground(new Color(220, 0, 0));
        derrotaFrame.add(derrotaLabel, BorderLayout.CENTER);

        //Cria a área para os botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        //Cria o botão de jogar novamente adicionando um ActionListener ao botão para definir a ação ao ser clicado.
        //ao ser clicado ele fecha a interface de derrota, restaura a vida do personagem e volta a interface do mapa
        JButton jogarNovamenteButton = new JButton("Jogar Novamente");
        jogarNovamenteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                derrotaFrame.dispose();
                combate.getPersonagemJogador().restaurarVida();
                mapaGUI.mostrar();
            }
        });
        buttonPanel.add(jogarNovamenteButton);

        //Cria o botão de sair adicionando um ActionListener ao botão para definir a ação ao ser clicado. Ao ser clicado
        //fecha o programa
        JButton sairButton = new JButton("Sair");
        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        buttonPanel.add(sairButton);

        derrotaFrame.add(buttonPanel, BorderLayout.SOUTH);

        derrotaFrame.setVisible(true);
        frame.dispose();
    }
}