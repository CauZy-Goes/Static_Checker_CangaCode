package github.cauzy.static_checker;

import github.cauzy.static_checker.partesDoCompilador.AnalisadorSintatico;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LauncherGUI extends JFrame {

    private File arquivoSelecionado;

    public LauncherGUI() {
        setTitle("CangaCode2025 - Analisador Léxico & Sintático");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal com padding
        JPanel painelPrincipal = new JPanel();
        painelPrincipal.setLayout(new BorderLayout());
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

        // Cabeçalho
        JLabel cabecalho = new JLabel("<html><div style='text-align:center;'>"
                + "<h2 style='color:blue;'>CangaCode2025 - Analisador Léxico & Sintático</h2>"
                + "<b style='text-align:center; font-weight:bold; font-size:13px; margin-bottom:0; padding-bottom:0;'>Código da Equipe:</b> EQ01<br>"
                + "<b style='text-align:center; font-weight:bold; font-size:13px; margin-bottom:0; padding-bottom:0;'>Componentes:</b><br>"
                + "CAUÃ GOES FARIAS - caua.farias@ucsal.edu.br - 71993209370<br>"
                + "GUILHERME ANDRADE DE LACERDA - guilhermeandrade.lacerda@ucsal.edu.br - 71988602565<br>"
                + "KAILAN DE SOUZA DIAS - kailan.dias@ucsal.edu.br - 75988059380"
                + "</div></html>");
        painelPrincipal.add(cabecalho, BorderLayout.NORTH);

// Área central (instruções + botões)
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

// Instruções alinhadas totalmente à esquerda
        // Painel que garante alinhamento à esquerda
        JPanel painelInstrucoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));  // Alinha totalmente à esquerda, sem margem lateral

        JLabel instrucoes = new JLabel("<html>"
                + "<div style='text-align:center; font-weight:bold; font-size:13px; margin-bottom:0; padding-bottom:0;'>Instruções:</div>"
                + "<div style='margin-top:4px;'>"
                + "- Clique em 'Escolher Arquivo' para selecionar um arquivo de código-fonte com extensão .251.<br>"
                + "- Após selecionar, clique em 'Executar Análise' para iniciar o processamento.<br>"
                + "- Os arquivos de saída (.LEX e .TAB) serão gerados na pasta /output do projeto."
                + "</div>"
                + "</html>");


        painelInstrucoes.add(instrucoes);
        painelCentro.add(painelInstrucoes);




        // Label que mostra o caminho
        JLabel caminhoArquivo = new JLabel(" ");
        caminhoArquivo.setAlignmentX(Component.LEFT_ALIGNMENT);
        painelCentro.add(Box.createVerticalStrut(10)); // Espaço vertical
        painelCentro.add(caminhoArquivo);

        // Painel dos botões lado a lado
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton botaoEscolher = new JButton("Escolher Arquivo .251");
        JButton botaoExecutar = new JButton("Executar Análise");

        painelBotoes.add(botaoEscolher);
        painelBotoes.add(botaoExecutar);

        painelCentro.add(Box.createVerticalStrut(10)); // Mais espaço vertical
        painelCentro.add(painelBotoes);

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        add(painelPrincipal);

        // Lógica dos botões
        botaoEscolher.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int resultado = fileChooser.showOpenDialog(this);

            if (resultado == JFileChooser.APPROVE_OPTION) {
                File arquivo = fileChooser.getSelectedFile();
                if (arquivo.getName().endsWith(".251")) {
                    arquivoSelecionado = arquivo;
                    caminhoArquivo.setText("Arquivo selecionado: " + arquivo.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(this, "Erro: O arquivo deve ter extensão .251", "Extensão Inválida", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        botaoExecutar.addActionListener(e -> {
            if (arquivoSelecionado != null) {
                try {
                    String caminho = arquivoSelecionado.getAbsolutePath();
                    AnalisadorSintatico.main(new String[]{caminho});

                    JOptionPane.showMessageDialog(this, "Análise concluída!\nArquivos .LEX e .TAB gerados em /output.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro durante a análise:\n" + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um arquivo .251 antes de executar.", "Arquivo não selecionado", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LauncherGUI().setVisible(true));
    }
}
