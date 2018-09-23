package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.dragberry.eshop.dal.entity.converter.CommentStatusConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "COMMENT")
@TableGenerator(
		name = "COMMENT_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "COMMENT_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class Comment extends AuditableEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "COMMENT_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "COMMENT_GEN")
	private Long entityKey;

	@Column(name = "USER_NAME")
    private String userName;
	
	@Column(name = "USER_IP")
    private String userIP;
	
	@Column(name = "TEXT")
    private String text;
	
	@Column(name = "STATUS")
	@Convert(converter = CommentStatusConverter.class)
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
