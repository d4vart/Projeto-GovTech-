import java.util.Collection;
import java.util.Scanner;

public class ObservaAcaoApp {
    private static ServicoSolicitacoes servico = new ServicoSolicitacoes();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== OBSERVAAÇÃO: GOVTECH ===");
            System.out.println("1. Registrar Solicitação\n2. Consultar Protocolo\n3. Painel Gestor (Listar)\n4. Sair");
            int opcao = Integer.parseInt(sc.nextLine());

            if (opcao == 1) registrar();
            else if (opcao == 2) consultar();
            else if (opcao == 3) painelGestor();
            else break;
        }
    }

    private static void registrar() {
        System.out.println("\n--- NOVO REGISTRO ---");
        System.out.print("Deseja fazer uma denúncia anônima? (s/n): ");
        boolean anon = sc.nextLine().equalsIgnoreCase("s");

        String nomeCidadao = "Anônimo";
        String documento = "N/A";


        if (!anon) {
            System.out.println(">> Você escolheu se identificar.");
            System.out.print("Digite seu nome completo: ");
            nomeCidadao = sc.nextLine();
            System.out.print("Digite seu CPF ou Telefone: ");
            documento = sc.nextLine();

        } else {
            System.out.println(">> ATENÇÃO: Denúncia Anônima. Seus dados não serão salvos.");
            System.out.println("Certifique-se de fornecer o máximo de detalhes na descrição.");
        }

        System.out.print("Categoria (Iluminação, Buraco, Zeladoria, etc): ");
        String cat = sc.nextLine();
        System.out.print("Descrição detalhada da demanda: ");
        String desc = sc.nextLine();
        System.out.print("Bairro/Localização: ");
        String bairro = sc.nextLine();

        System.out.println("Prioridade sugerida:");
        System.out.println("1-Baixa | 2-Média | 3-Alta | 4-Urgente");
        int p = Integer.parseInt(sc.nextLine());


        String protocolo = servico.criar(cat, desc, bairro, p, anon);

        System.out.println("\n-------------------------------------------");
        System.out.println("REGISTRO CONCLUÍDO COM SUCESSO!");
        System.out.println("Protocolo: " + protocolo);
        if (!anon) System.out.println("Cidadão: " + nomeCidadao);
        System.out.println("-------------------------------------------");
    }

    private static void consultar() {
        System.out.print("Informe o protocolo: ");
        String prot = sc.nextLine();
        Solicitacao s = servico.buscar(prot);
        if (s != null) s.exibirDetalhes();
        else System.out.println("Protocolo não encontrado.");
    }

    private static void painelGestor() {
        System.out.println("\n--- PAINEL DO GESTOR ---");
        Collection<Solicitacao> todas = servico.listarTodas();

        if (todas.isEmpty()) {
            System.out.println("Nenhuma demanda no sistema.");
            return;
        }

        todas.forEach(s ->
                System.out.println("Prot: " + s.getProtocolo() + " | Status: " + s.getStatusAtual() + " | Bairro: " + s.getBairro()));

        System.out.print("\nDigite o protocolo da demanda que deseja atualizar (ou '0' para voltar): ");
        String prot = sc.nextLine();

        if (prot.equals("0")) return;

        Solicitacao solicitacao = servico.buscar(prot);
        if (solicitacao == null) {
            System.out.println("Protocolo inválido!");
            return;
        }

        System.out.println("Escolha o novo Status:");
        System.out.println("1-TRIAGEM | 2-EM EXECUÇÃO | 3-RESOLVIDO | 4-ENCERRADO");
        int opStatus = Integer.parseInt(sc.nextLine());

        Status novoStatus;
        switch (opStatus) {
            case 1 -> novoStatus = Status.TRIAGEM;
            case 2 -> novoStatus = Status.EM_EXECUCAO;
            case 3 -> novoStatus = Status.RESOLVIDO;
            case 4 -> novoStatus = Status.ENCERRADO;
            default -> {
                System.out.println("Opção inválida.");
                return;
            }
        }

        System.out.print("Digite o comentário/justificativa (OBRIGATÓRIO): ");
        String comentario = sc.nextLine();

        try {
            solicitacao.atualizarStatus(novoStatus, "Gestor_Logado_01", comentario);
            System.out.println("Status atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
}
