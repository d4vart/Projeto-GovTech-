import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServicoSolicitacoes {
    private Map<String, Solicitacao> bancoDeDados = new HashMap<>();

    public String criar(String cat, String desc, String bairro, int p, boolean anon) {
        Prioridade prioridade = Prioridade.values()[p-1];
        Solicitacao s = new Solicitacao(cat, desc, bairro, prioridade, anon);
        bancoDeDados.put(s.getProtocolo(), s);
        return s.getProtocolo();
    }

    public Solicitacao buscar(String protocolo) {
        return bancoDeDados.get(protocolo);
    }

    public Collection<Solicitacao> listarTodas() {
        return bancoDeDados.values();
    }
}
