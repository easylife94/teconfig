package cn.teracloud.plugin.teconfig;

import java.io.File;
import java.util.List;

public class Config {
	private String name;
	private String type;
	private File template;
	private List<Scope> scopes;
	private File target;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public List<Scope> getScopes() {
		return scopes;
	}
	public void setScopes(List<Scope> scopes) {
		this.scopes = scopes;
	}
	
	public File getTemplate() {
		return template;
	}
	public void setTemplate(File template) {
		this.template = template;
	}
	public File getTarget() {
		return target;
	}
	public void setTarget(File target) {
		this.target = target;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
