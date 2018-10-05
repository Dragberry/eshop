package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.dragberry.eshop.dal.entity.converter.MenuPageStatusConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "MENU_PAGE")
@TableGenerator(
		name = "MENU_PAGE_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "MENU_PAGE_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class MenuPage extends AuditableEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "MENU_PAGE_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "MENU_PAGE_GEN")
	private Long entityKey;
	
	@OneToOne
	@JoinColumn(name = "PAGE_KEY", referencedColumnName = "PAGE_KEY")
    private Page page;
	
	@Column(name = "`ORDER`")
    private Integer order;
	
	@Column(name = "STATUS")
    @Convert(converter = MenuPageStatusConverter.class)
    private Status status;
	
	public static enum Status implements BaseEnum<Character> {

        ACTIVE('A'), INACTIVE('I');
        
        public final Character value;
        
        private Status(Character value) {
            this.value = value;
        }
        
        public static Status valueOf(Character value) {
            if (value == null) {
                throw BaseEnum.npeException(Status.class);
            }
            for (Status status : Status.values()) {
                if (value.equals(status.value)) {
                    return status;
                }
            }
            throw BaseEnum.unknownValueException(Status.class, value);
        }
        
        @Override
        public Character getValue() {
            return value;
        }
    }
	
}
