
public class Item {

    public Item(String[] linhaSplit) {
        id_material = Integer.valueOf(linhaSplit[0]);
        descricao = linhaSplit[1];
        id_pedido = Integer.valueOf(linhaSplit[2]);
//        quantidade_pedida = Integer.valueOf(linhaSplit[3].replace(",", ""));
    }
    
    private Integer id_material;
    private String descricao;
    private Integer id_pedido;
    private Integer quantidade_pedida;
    
    public Integer getId_material() {
        return id_material;
    }
    public String getDescricao() {
        return descricao;
    }
    public Integer getId_pedido() {
        return id_pedido;
    }
    public Integer getQuantidade_pedida() {
        return quantidade_pedida;
    }
    
    @Override
    public String toString() {
        return "Item [id_material=" + id_material + ", descricao=" + descricao + ", id_pedido=" + id_pedido + ", quantidade_pedida=" + quantidade_pedida + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_material == null) ? 0 : id_material.hashCode());
        result = prime * result + ((id_pedido == null) ? 0 : id_pedido.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item other = (Item) obj;
        if (id_material == null) {
            if (other.id_material != null)
                return false;
        } else if (!id_material.equals(other.id_material))
            return false;
        if (id_pedido == null) {
            if (other.id_pedido != null)
                return false;
        } else if (!id_pedido.equals(other.id_pedido))
            return false;
        return true;
    }
    
    
}
