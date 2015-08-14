package jp.swest.ledcamp.sakuratools.actions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlTransient;

public class SettingManager extends HashMap<String, GenerateSetting> {
    private static SettingManager instance;
    @XmlTransient private String userFolder = System.getProperty("user.home");
// Accessor
    private GenerateSetting currentSetting;
	@XmlTransient private String settingFilePath;
    @XmlTransient private String m2tPluginFolderPath = userFolder + "/.astah/plugins/m2t/";
    @XmlTransient private String currentAstahFileName;
    public GenerateSetting getCurrentSetting() {
		return currentSetting;
	}
	public void setCurrentSetting(GenerateSetting currentSetting) {
		this.currentSetting = currentSetting;
	}
	public String getSettingFilePath() {
		return settingFilePath;
	}
	public void setSettingFilePath(String settingFilePath) {
		this.settingFilePath = settingFilePath;
	}
	public String getM2tPluginFolderPath() {
		return m2tPluginFolderPath;
	}
	public void setM2tPluginFolderPath(String m2tPluginFolderPath) {
		this.m2tPluginFolderPath = m2tPluginFolderPath;
	}
	public String getCurrentAstahFileName() {
		return currentAstahFileName;
	}
	public void setCurrentAstahFileName(String currentAstahFileName) {
		this.currentAstahFileName = currentAstahFileName;
	}
    
    // Singleton
    private SettingManager(){
    	super();
    	settingFilePath = m2tPluginFolderPath + "m2tsetting.xml";
    }
    static SettingManager getInstance() {
        if (instance == null) {
            instance = new SettingManager();
        }
        instance.load();
        return instance;
    }
    
    void save() {
        JAXB.marshal(instance, new File(settingFilePath));
    }

    void load() {
        // load property file
        userFolder = System.getProperty("user.home");
        m2tPluginFolderPath = userFolder + "/.astah/plugins/m2t/";
        settingFilePath = m2tPluginFolderPath + "m2tsetting.xml";
        File asgenPluginFolder = new File(m2tPluginFolderPath);
        if (!(asgenPluginFolder.exists())) {  // install setting files.
            try {
                asgenPluginFolder.getParentFile().mkdirs();
                File zipFile = File.createTempFile("astahm2t", "templeatezip");
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("m2t.zip"));
//                    println(getClass().classLoader.getResource("m2t.zip")
                    bos = new BufferedOutputStream(new FileOutputStream(zipFile));
                    writeFile(bis, bos);
                } finally {
                    bis.close();
                    bos.close();
                }
                unzip(new ZipFile(zipFile), asgenPluginFolder.getParent());
            } catch (IOException ioe) {
                System.err.println("cannot create template folders.");
            }
        }
        try {
            File settingFile = new File(settingFilePath);
            if(settingFile.exists()){
                SettingManager settings = JAXB.unmarshal(new File(settingFilePath), SettingManager.class);
                instance.clear();
                instance.putAll(settings);
                instance.currentSetting = settings.currentSetting;
            }else{
                createDefaultSetting();
                save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // load fail. 
        }
    }
    
    private void createDefaultSetting(){
        GenerateSetting sampleGenerateSetting = new GenerateSetting();
        sampleGenerateSetting.targetPath = new File(userFolder).getAbsolutePath();
        sampleGenerateSetting.templatePath = new File(m2tPluginFolderPath+"templates/grsakura/").getAbsolutePath();
        sampleGenerateSetting.templateEngine = TemplateEngine.Groovy;
        sampleGenerateSetting.templateID = "grsakura";
        sampleGenerateSetting.mapping.add(TemplateMap.newDefaultTemplateMap("cpp.template", "cpp"));
        sampleGenerateSetting.mapping.add(TemplateMap.newDefaultTemplateMap("header.template", "h"));
        sampleGenerateSetting.mapping.add(TemplateMap.newGlobalTemplateMap("sketch.template", "Sketch.cpp"));
        instance.put("grsakura", sampleGenerateSetting);
        instance.currentSetting = sampleGenerateSetting;
        save();
    }

    Map<String, GenerateSetting> getMap() {
        return (Map<String, GenerateSetting>)instance;
    }

    void setMap(Map<String, GenerateSetting> settings) {
        putAll(settings);
    }

    private void unzip(ZipFile zipFile, String path) throws IOException{
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File extractFile = new File(path, entry.getName());
            if (entry.isDirectory()) {
                extractFile.mkdirs();
            } else {
                BufferedInputStream bis = null;
                BufferedOutputStream bos = null;
                try {
                    bis = new BufferedInputStream(zipFile.getInputStream(entry));
                    bos = new BufferedOutputStream(new FileOutputStream(extractFile));
                    writeFile(bis, bos);
                } finally {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                }
            }
        }
    }

    private void writeFile(BufferedInputStream bis, BufferedOutputStream bos) throws IOException{
        int available = 0;
        while ((available = bis.available()) > 0) {
            byte[] bs = new byte[available];
            bis.read(bs);
            bos.write(bs);
        }
    }
}

    
