
package com.bootx.entity;

import ch.qos.logback.core.net.server.Client;
import com.bootx.common.BaseAttributeConverter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "bootx_log")
public class Log extends BaseEntity<Long> {

	private static final long serialVersionUID = -4494144902110236826L;

	public static final String LOG_CONTENT_ATTRIBUTE_NAME = Log.class.getName() + ".CONTENT";

  @Column(nullable = false, updatable = false)
	private String operation;

  @Column(nullable = false, updatable = false)
  private String action;

  @Column(updatable = false)
	private String operator;

  @Column(length = 3000, updatable = false)
	private String content;

  @Column(length = 1000, updatable = false)
  @Convert(converter = ClientInfoConverter.class)
  private Map<String,String> clientInfo;

  @Lob
  @Column(updatable = false)
	private String parameter;

  @Column(nullable = false, updatable = false)
	private String ip;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

  public Map<String, String> getClientInfo() {
    return clientInfo;
  }

  public void setClientInfo(Map<String, String> clientInfo) {
    this.clientInfo = clientInfo;
  }

  @Converter
  public static class ClientInfoConverter extends BaseAttributeConverter<Map<String,String>> {
  }
}
