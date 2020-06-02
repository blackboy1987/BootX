
package com.bootx.common;

import com.bootx.entity.BaseEntity;
import com.bootx.util.SpringUtils;
import com.fasterxml.jackson.annotation.JsonView;

/**
 * 消息
 * 
 * @author blackboy
 * @version 1.0
 */
public class Message {

	/**
	 * 状态码。
	 * 0：成功
	 * -1： 错误
	 * -2：警告
	 */
	@JsonView({BaseEntity.BaseView.class, BaseEntity.IdView.class})
	private Integer code;

	/**
	 * 内容
	 */
	@JsonView({BaseEntity.BaseView.class, BaseEntity.IdView.class})
	private String content;

	@JsonView({BaseEntity.BaseView.class, BaseEntity.IdView.class})
	private Object data;

	/**
	 * 构造方法
	 */
	public Message() {
	}

	/**
	 * 构造方法
	 * 
	 * @param code
	 *            状态码
	 * @param content
	 *            内容
	 */
	public Message(Integer code, String content) {
		this.code = code;
		this.content = content;
	}

	/**
	 * 构造方法
	 * 
	 * @param code
	 *            状态码
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 */
	public Message(Integer code, String content,Object data, Object... args) {
		this.code = code;
		this.data = data;
		this.content = SpringUtils.getMessage(content, args);
	}

	/**
	 * 返回成功消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 成功消息
	 */
	public static Message success(String content, Object... args) {
		return new Message(0, content, args);
	}

	/**
	 * 返回警告消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 警告消息
	 */
	public static Message warn(String content, Object... args) {
		return new Message(-2, content, args);
	}

	/**
	 * 返回错误消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 错误消息
	 */
	public static Message error(String content, Object... args) {
		return new Message(-1, content, args);
	}


	/**
	 * 返回成功消息
	 *
	 * @param content
	 *            内容
	 * @return 成功消息
	 */
	public static Message success1(String content,Object data) {
		return new Message(0, content,data);
	}

	/**
	 * 返回警告消息
	 *
	 * @param content
	 *            内容
	 * @return 警告消息
	 */
	public static Message warn1(String content,Object data) {
		return new Message(-2, content,data);
	}

	/**
	 * 返回错误消息
	 *
	 * @param content
	 *            内容
	 * @return 错误消息
	 */
	public static Message error1(String content, Object data) {
		return new Message(-1, content,data);
	}


	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 重写toString方法
	 * 
	 * @return 字符串
	 */
	@Override
	public String toString() {
		return SpringUtils.getMessage(content);
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}