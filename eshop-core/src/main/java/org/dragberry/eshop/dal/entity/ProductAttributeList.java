package org.dragberry.eshop.dal.entity;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "PRODUCT_ATTRIBUTE_LIST")
public class ProductAttributeList  extends ProductAttribute<List<String>> {

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "PRODUCT_ATTRIBUTE_LIST_VALUES", joinColumns = @JoinColumn(name = "PRODUCT_ATTRIBUTE_KEY"))
	private List<String> value;
    
    @Override
    public List<String> getValue() {
        return value;
    }

    @Override
    public void setValue(List<String> value) {
        this.value = value;
    }
    
    public static ProductAttributeList of(ProductArticle productArticle, String group, String name, List<String> value, Integer order) {
    	var entity = new ProductAttributeList();
    	entity.setProductArticle(productArticle);
    	entity.setGroup(group);
    	entity.setName(name);
    	entity.setValue(value);
    	entity.setOrder(order);
    	return entity;
    }
    
    @Override
    public String getStringValue() {
    	return value.stream().collect(Collectors.joining(", "));
    }
}
