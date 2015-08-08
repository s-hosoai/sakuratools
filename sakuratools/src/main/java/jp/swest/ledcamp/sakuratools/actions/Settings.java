package jp.swest.ledcamp.sakuratools.actions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlTransient;

public class Settings {
	private static Settings instance;
	public static Settings getInstance(){
		if(instance==null){
			if(Files.exists(sakuratoolsSettingPath.resolve("setting.xml"))){
				instance = JAXB.unmarshal(sakuratoolsSettingPath.resolve("setting.xml").toFile(), Settings.class);
			}else{
				instance = new Settings();
				instance.sakuraDrive = "D://";
				instance.e2makePath = "c:/Renesas/e2_studio/Utilities/make.exe";
			}
		}
		return instance;
	}
	
	@XmlTransient
	private static String userFolder = System.getProperty("user.home");

	@XmlTransient
	private static Path sakuratoolsSettingPath = Paths.get(userFolder).resolve(".astah/plugins/sakuratools/");
	
	private String sakuraDrive;
	private String e2makePath;
	public String getSakuraDrive() {
		return sakuraDrive;
	}
	public void setSakuraDrive(String sakuraDrive) {
		this.sakuraDrive = sakuraDrive;
	}
	public String getE2makePath() {
		return e2makePath;
	}
	public void setE2makePath(String e2makePath) {
		this.e2makePath = e2makePath;
	}	
	private void save(){
		if(Files.notExists(sakuratoolsSettingPath)){
			try {
				Files.createDirectories(sakuratoolsSettingPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JAXB.marshal(this, sakuratoolsSettingPath.resolve("setting.xml").toFile());				
	}
	public static void main(String[] args) {
		Settings.getInstance().save();
	}
}
