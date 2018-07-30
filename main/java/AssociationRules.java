import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public class AssociationRules {

    public static void main(String... strings) throws IOException {
        List<Item> itens = new ArrayList<Item>();
        Set<Integer> materiais = new TreeSet<>();
        Set<Integer> pedidos = new TreeSet<>();
        
        loadListsFromFile("dataset_permanente.csv", itens, materiais, pedidos);
        Integer totalDePedidos = pedidos.size();
        pedidos.clear();
        
        materiais.forEach( id_material ->{
            Integer quantidade = contarOcorrenciasDoMaterial(id_material, itens);

            Map<Integer, AssociacaoMaterial> mapAssociacao = buscarAssociacaoDeMateriais(id_material, itens);
            exibirAssociacoes(quantidade, mapAssociacao, totalDePedidos);
        });
    }

    public static void loadListsFromFile(String nomeArquivo, List<Item> itens, Set<Integer> materiais, Set<Integer> pedidos) throws IOException, FileNotFoundException {
        try (FileReader fr = new FileReader(nomeArquivo)) {
            BufferedReader br = new BufferedReader(fr);
            
            String linha = null;
            Integer numeroLinha = 0;
            while ((linha = br.readLine()) != null) {
                ++numeroLinha;
                if(isCabecalho(numeroLinha)){
                    continue;
                }
                String[] linhaSplit = linha.split(";");
                try{
                    Item item = new Item(linhaSplit);
                    itens.add(item);
                    materiais.add(item.getId_material());
                    pedidos.add(item.getId_pedido());
                }catch(NumberFormatException e){
                    System.out.println(" LINHA COM ERRO "+ numeroLinha);
                }
            }
            System.out.println("Pedidos Analisados: " + pedidos.size());
            System.out.println("Itens de pedidos: " + itens.size());
            System.out.println("Materiais distintos: " + materiais.size());
        }
    }

    public static void exibirAssociacoes(Integer quantidade, Map<Integer, AssociacaoMaterial> mapAssociacao, Integer totalDePedidos) {
        Set<Integer> keySet = mapAssociacao.keySet();
        
        StringBuilder print = new StringBuilder();
        for(Integer key: keySet){
            AssociacaoMaterial associacao = mapAssociacao.get(key);
            
            BigDecimal suporte = calcularSuporteDeAssociacao(associacao.getOcorrencias(), totalDePedidos);
            BigDecimal confianca = calcularConfianca(associacao.getOcorrencias(), quantidade);
            
            if( suporteMenorQueMinimo(suporte) ){
                continue;
            }

            BigDecimal porcentagemConfianca = confianca.multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_EVEN);
            
            if(porcentagemConfianca.doubleValue() >= 60.00){
                if(!(print.length() > 0)){
                    print.append("O Material \""+ associacao.getItemBase().getDescricao() + "\" que foi pedido " + quantidade + " vezes. " );
                    print.append("Foi pedido junto com:" );
                }
                
                print.append("\n \"" +associacao.getItem().getDescricao() + "\" que apareceu em "+associacao.getOcorrencias()+ " das vezes"+
                             " com associação de "+ porcentagemConfianca + "% e suporte de "+ suporte + " e confiança de "+ confianca);    
            }
        }
        
        if(print.length() > 0){
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println(print);
        }
    }

    public static boolean suporteMenorQueMinimo(BigDecimal suporte) {
        int compareTo = suporte.setScale(2, BigDecimal.ROUND_HALF_EVEN).compareTo(BigDecimal.valueOf(0.00).setScale(2));
        return compareTo == 0 || compareTo == -1;
    }

    private static BigDecimal calcularConfianca(Integer ocorrencias, Integer quantidade) {
        return BigDecimal.valueOf(ocorrencias.doubleValue() / quantidade.doubleValue() );
    }

    private static BigDecimal calcularSuporteDeAssociacao(Integer ocorrenciasAssociacao, Integer quantidadePedidos) {
        return BigDecimal.valueOf(ocorrenciasAssociacao.doubleValue() / quantidadePedidos.doubleValue());
    }

    private static Map<Integer, AssociacaoMaterial> buscarAssociacaoDeMateriais(Integer id_material_base, List<Item> itens) {
        Map<Integer, AssociacaoMaterial> associacaoMaterial = new HashMap<>();
        
        Predicate<Item> predicate = itemBase -> itemBase.getId_material().equals(id_material_base);
//        p.and(itemBase.getId_pedido().equals(itemProcurado.getId_pedido())  && !id_material_base.equals(itemProcurado.getId_material()));
        
        itens.stream()
            .filter(predicate)
            .forEach(itemBase -> {
            
                itens.stream()
                    .filter(itemProcurado -> itemBase.getId_pedido().equals(itemProcurado.getId_pedido())  && !id_material_base.equals(itemProcurado.getId_material()) )
                    .forEach(itemProcurado -> {
                        
                        associacaoMaterial.computeIfAbsent(itemProcurado.getId_material(), (k) ->  new AssociacaoMaterial(itemProcurado, itemBase));
                        associacaoMaterial.computeIfPresent(itemProcurado.getId_material(), (k, v) -> { v.incrementarOcorrencias(); return v; });
                });
        });
        
        return associacaoMaterial;
    }

    private static Integer contarOcorrenciasDoMaterial(int id_material, List<Item> itens) {
      return (int) itens.stream().filter(item -> item.getId_material().equals(id_material)).count();
    }

    public static boolean isCabecalho(Integer numeroLinha) {
        return numeroLinha == 1;
    }
}