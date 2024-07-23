package rpg.logica;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;
import rpg.inimigos.*;
import rpg.personagens.*;
import rpg.gui.CombateGUI;
import rpg.gui.MapaInterface;
import rpg.gui.MapaChefeGUI; // Importar MapaChefeGUI

public class Mapa {
    private JPanel mapPanel;
    private JLabel playerLabel;
    private int playerX;
    private int playerY;
    private Map<Point, Inimigo> inimigos;
    private Set<Point> pontosAcesso;
    private Personagem personagem;
    private MapaInterface gui;
    private Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 24);

    //Basicamente instancia o Mapa, inicializando as coordenadas do jogador, os inimigos, a área do chefe e outros dois métodos
    //para manipulação
    public Mapa(JPanel mapPanel, JLabel playerLabel, List<Inimigo> inimigosList, Personagem personagem, MapaInterface gui) {
        this.mapPanel = mapPanel;
        this.playerLabel = playerLabel;
        this.personagem = personagem;
        this.gui = gui;
        this.playerX = 50;
        this.playerY = 50;
        this.inimigos = new HashMap<>();
        this.pontosAcesso = new TreeSet<>((p1, p2) -> p1.x != p2.x ? p1.x - p2.x : p1.y - p2.y);
        adicionarInimigos(inimigosList);
        inicializarPontosAcesso();
    }
    //Basicamente itero a lista de inimigos no laço for, crio um coordenada aleatória no mapa e com base nessa coordenada adiciono
    // os inimigos com seus ícones (emoji), tamanho e fonte 
    private void adicionarInimigos(List<Inimigo> inimigosList) {
        for (Inimigo inimigo : inimigosList) {
            Point ponto = new Point((int) (Math.random() * 300), (int) (Math.random() * 300));
            inimigos.put(ponto, inimigo);
            JLabel label = new JLabel(getInimigoIcon(inimigo));
            label.setFont(emojiFont);
            label.setBounds(ponto.x, ponto.y, 50, 50);
            mapPanel.add(label);
        }
    }

    //Basicamente inicializo uma coordenada fixa da área do chefe
    private void inicializarPontosAcesso() {
        pontosAcesso.add(new Point(300, 300)); 
    }

    //Retorna os ícones (emojis) dos inimigos
    private String getInimigoIcon(Inimigo inimigo) {
        if (inimigo instanceof Ogro) {
            return "👹";
        } else if (inimigo instanceof Satan) {
            return "👿";
        } else if (inimigo instanceof Dragao) {
            return "🐲";
        } else if (inimigo instanceof Chefe) {
            return "👑";
        } else {
            return "I";
        }
    }

    //Basicamente é o que atualiza a posição do personagem com dx e dy, atualiza a localização do ícone (emoji) do personagem
    //e verifica se encontrou com algum inimigo ou com a área do chefe
    public void movePlayer(int dx, int dy) {
        playerX += dx;
        playerY += dy;
        playerLabel.setLocation(playerX, playerY);
        verificarInimigo();
        verificarPontoAcesso();
    }

    //Representa a posição do personagem. itera atráves de um for os inimigos, verifica se a distância entre os personagens e inimigos
    //é menor que 10, caso seja, exibe o dialogo que caso a resposta seja sim abre a interface de combate
    private void verificarInimigo() {
        Point playerPos = new Point(playerX, playerY);
        for (Map.Entry<Point, Inimigo> entry : inimigos.entrySet()) {
            if (playerPos.distance(entry.getKey()) < 10) {
                Inimigo inimigo = entry.getValue();
                exibirDialogo(inimigo);
                int resposta = mostrarDialogoPersonalizado("Deseja entrar em combate com " + inimigo.getNome() + "?");
                if (resposta == JOptionPane.YES_OPTION) {
                    iniciarCombate(inimigo);
                }
                break;
            }
        }
    }
    
   //Basicamente a mesma lógica acima, só muda que chamo o método para esconder a interface do mapa visando a estética
    private void verificarPontoAcesso() {
        Point playerPos = new Point(playerX, playerY);
        for (Point pontoAcesso : pontosAcesso) {
            if (playerPos.distance(pontoAcesso) < 10) {
                int resposta = mostrarDialogoPersonalizado("Deseja entrar na área do chefe?");
                if (resposta == JOptionPane.YES_OPTION) {
                    gui.esconder();
                    MapaChefeGUI mapaChefeGUI = new MapaChefeGUI(personagem);
                    mapaChefeGUI.mostrar();
                }
                break;
            }
        }
    }

    //Exibe dialogo
    private void exibirDialogo(Inimigo inimigo) {
        mostrarDialogoPersonalizado(inimigo.getDialogo());
    }

    //Basicamente restaura a vida do inimigo, istancia CombateGUI, adiciona o inimigo no combate, inicia a batalha e esconde a interface
    //do mapa mostrando a de combate
    private void iniciarCombate(Inimigo inimigo) {
        inimigo.restaurarVida();
        CombateGUI combateGUI = new CombateGUI(personagem);
        combateGUI.setMapaGUI(gui);
        inimigo.setGui(combateGUI);
        combateGUI.getCombate().adicionarInimigo(inimigo);
        combateGUI.getCombate().iniciarBatalha();
        esconderMapa();
        combateGUI.mostrar();
    }

    //Torna a visibilidade do mapa como falsa
    public void esconderMapa() {
        mapPanel.setVisible(false);
    }

    //Torna a visibilidade do mapa e seus combonentes como verdadeira
    public void mostrarMapa() {
        mapPanel.setVisible(true);
        for (Component comp : mapPanel.getComponents()) {
            comp.setVisible(true);
        }
    }

    //Basicamente é a interface onde aparecem os diálogos, nesse métodos se define o layout, cor de fundo, qual mensagem vai ser
    //mostrada, fonte e cor (essa parte mais estética)
    private int mostrarDialogoPersonalizado(String mensagem) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(20, 20, 20));

        JLabel label = new JLabel(mensagem);
        label.setFont(new Font("Serif", Font.BOLD, 18));
        label.setForeground(new Color(220, 220, 220));
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