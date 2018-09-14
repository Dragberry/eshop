package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@TableGenerator(
        name = "PRODUCT_ATTRIBUTE_GEN", 
        table = "GENERATOR",
        pkColumnName = "GEN_NAME", 
        pkColumnValue = "PRODUCT_ATTRIBUTE_GEN",
        valueColumnName = "GEN_VALUE",
        initialValue = 1000,
        allocationSize = 1)
@Getter
@Setter
public abstract class ProductAttribute<T> {
    
    @Id
    @Column(name = "PRODUCT_ATTRIBUTE_KEY")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "PRODUCT_ATTRIBUTE_GEN")
    private Long entityKey;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ARTICLE_KEY", referencedColumnName = "PRODUCT_ARTICLE_KEY")
    private ProductArticle productArticle;

    @Column(name = "NAME")
    private String name;
    
    protected abstract T getValue();
    
    protected abstract void setValue(T value);
    
}
