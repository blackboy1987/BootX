
package com.bootx.entity;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.hibernate.search.annotations.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * Entity - 排序基类
 *
 * @author blackboy
 * @version 1.0
 */
@MappedSuperclass
public abstract class OrderedEntity<ID extends Serializable> extends BaseEntity<ID> implements Comparable<OrderedEntity<ID>> {

	private static final long serialVersionUID = 5995013015967525827L;

	/**
	 * "排序"属性名称
	 */
	public static final String ORDER_PROPERTY_NAME = "order";

	/**
	 * 排序
	 */
	@Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
	@NumericField
	@Min(0)
	@Column(name = "orders")
	@JsonView({BaseView.class, IdView.class, ListView.class, EditView.class})
	private Integer order;

	/**
	 * 获取排序
	 *
	 * @return 排序
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 *
	 * @param order
	 *            排序
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * 实现compareTo方法
	 *
	 * @param orderEntity
	 *            排序对象
	 * @return 比较结果
	 */
	@Override
	public int compareTo(OrderedEntity<ID> orderEntity) {
		if (orderEntity == null) {
			return 1;
		}
		return new CompareToBuilder().append(getOrder(), orderEntity.getOrder()).append(getId(), orderEntity.getId()).toComparison();
	}

}
