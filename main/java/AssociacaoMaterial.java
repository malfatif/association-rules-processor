
public class AssociacaoMaterial {

    private Item item;
    private Item itemBase;
    private Integer ocorrencias;

    public AssociacaoMaterial(Item item, Item itemBase) {
        this.item = item;
        this.itemBase = itemBase;
        ocorrencias = 1;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public Integer getOcorrencias() {
        return ocorrencias;
    }

    public void incrementarOcorrencias() {
        ocorrencias++;
    }
    
    public Item getItemBase() {
        return itemBase;
    }

    public void setItemBase(Item itemBase) {
        this.itemBase = itemBase;
    }

    public void setOcorrencias(Integer ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    
}
