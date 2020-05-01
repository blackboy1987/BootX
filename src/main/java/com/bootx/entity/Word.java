
package com.bootx.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 角色
 *
 * @author blackboy
 * @version 1.0
 */
@Entity
@Table(name = "bootx_word")
public class Word extends OrderedEntity<Long> {

	private static final long serialVersionUID = -6614052029623997372L;

	/**
	 * 名称
	 */
	@NotEmpty
	@Length(max = 200)
	@Column(nullable = false)
	@JsonView({TreeView.class,ListView.class,EditView.class})
	private String name;

	/**
	 * 是否内置
	 */
	@Column(nullable = false, updatable = false)
	@JsonView({ListView.class,EditView.class})
	private Boolean isEnabled;

  /**
   * 是否内置
   */
  @Column(nullable = false)
  @JsonView({ListView.class,EditView.class})
  private Boolean isDefault;

	/**
	 * 描述
	 */
	@Length(max = 200)
	@JsonView({ListView.class,EditView.class})
	private String description;

  @NotNull
	@ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false,updatable = false)
	private WordType wordType;

	/**
	 * 获取名称
	 *
	 * @return 名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 *
	 * @param name
	 *            名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取描述
	 *
	 * @return 描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 *
	 * @param description
	 *            描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

  public Boolean getIsDefault() {
    return isDefault;
  }

  public void setIsDefault(Boolean isDefault) {
    this.isDefault = isDefault;
  }

  public WordType getWordType() {
    return wordType;
  }

  public void setWordType(WordType wordType) {
    this.wordType = wordType;
  }

  @Transient
	@JsonView({EditView.class})
	public Long getWordTypeId() {
		if(wordType!=null){
			return wordType.getId();
		}
		return null;
	}

	@Transient
	@JsonView({ListView.class, EditView.class})
	public String getWordTypeName() {
		if(wordType!=null){
			return wordType.getName();
		}
		return null;
	}

}
