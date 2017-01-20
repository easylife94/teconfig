package cn.teracloud.plugin.teconfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import cn.teracloud.plugin.teconfig.exception.ValidateException;
import cn.teracloud.plugin.teconfig.exception.ValidateFailureException;


@Mojo(name = "build" , defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class ConfigBuilder  extends AbstractMojo {
	
	@Parameter()
	private List<Config> configs;
	@Parameter(defaultValue = "test", property = "teconfig.scope")
	private String scope;
	@Parameter(defaultValue = "true", property = "teconfig.validate")
	private boolean validate;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		 getLog().info("Teracloud auto config plugin");
		 getLog().info("Config building scope is '"+scope+"'" );
		 try{
			 if(configs != null){
				 getLog().info("config list:");
				 for(Config conf : configs){
					 getLog().info("config [ "+conf.getName()+" - "+conf.getType()+" - "+conf.getTemplate().getName()+" - "+conf.getTarget().getName()+"]");
					 List<Scope> scopes = conf.getScopes();
					 if(scopes != null){
						 //寻找匹配的scope
						 //是否匹配标志
						 boolean matching = false;
						 for(Scope scp : scopes){
							 if(scope.equals(scp.getName())){
								 getLog().info("scope [ "+scp.getName()+" - "+scp.getFile().getName()+"]");
								 
								 //是否验证模板
								 if(validate){
									 validate(conf.getType(),conf.getTemplate(),scp.getFile());
									 matching = true;
								 }
								 autoConfig( scp.getFile(), conf.getTarget());
								 break;
							 }
						 }
						 //开启验证且不匹配则抛异常
						 if(validate && !matching){
							 throw new ValidateFailureException("'"+conf.getName()+"' not found macthing scope '"+scope+"'");
						 }
						 
						 
					 }
				 }
			 }	
		 } catch (Exception e) {
			 getLog().error(e.getMessage());
			 rollbackAll();
			 throw new MojoFailureException(e.getMessage());
		 }finally{
			 
		 }
	}
	
	/**
	 * 验证配置文件中的配置项是否
	 * @param template
	 * @param file
	 * @throws ValidateException 
	 */
	private void validate(String type,File template,File file) throws ValidateException{
		switch(type){
		case "properties":
			InputStream tmpIn = null;
			InputStream fileIn = null;
			try {
				tmpIn = new FileInputStream(template);
				fileIn = new FileInputStream(file);
				
				Properties propTmp = new Properties();
				propTmp.load(tmpIn);
				Properties propFile = new Properties();
				propFile.load(fileIn);
				
				Set<Object> keysTmp = propTmp.keySet();
				for(Object key : keysTmp){
					//如果不包含模板中的key则抛出异常
					if(!propFile.containsKey(key)){
						throw new ValidateFailureException("config '"+file.getName()+"' not found key '"+key.toString()+"'");
					};
				}
			} catch (Exception e) {
				throw new ValidateException(e.getMessage());
			}finally{
				if(tmpIn != null){
					try {
						tmpIn.close();
					} catch (IOException e) {}
				}
				if(fileIn != null){
					try {
						fileIn.close();
					} catch (IOException e) {}
				}
			}
			break;
		default:
			String error = "config type '"+type+"' is illegal";
			getLog().error(error);
			throw new ValidateException(error);
		}
		
	}

	/**
	 * 回滚所有文件配置
	 */
	private void rollbackAll(){
		getLog().info("Auto configuration failed.Begin rollback... ");
	}
	
	/**
	 * 自动配置目标配置文件
	 * @param source
	 * @param target
	 */
	private void autoConfig(File source,File target){
		//如果目标文件是文件夹则抛出异常
		if(target.isDirectory()){
			
		}
		
		//如果文件不存则创建
		if(!target.exists()){
			try {
				target.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedReader  br = null;
		FileWriter fw = null;
		try {
			br = new BufferedReader(new FileReader(source));
			fw = new FileWriter(target);
			
			fw.write("");//清空内容
			String line = null;
			while( (line = br.readLine()) != null){
				fw.append(line);
				fw.append('\n');
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {}
			}
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {}
			}
		}
	}
	
}
