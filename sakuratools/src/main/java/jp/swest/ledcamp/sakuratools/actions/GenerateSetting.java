package jp.swest.ledcamp.sakuratools.actions;

import java.util.HashSet;

public class GenerateSetting {
	String templateID;
	TemplateEngine templateEngine;
	String targetPath;
	String templatePath;
	HashSet<TemplateMap> mapping;

	GenerateSetting() {
		mapping = new HashSet<TemplateMap>();
	}

	public String getTemplateID() {
		return templateID;
	}

	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}

	public TemplateEngine getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(TemplateEngine templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public HashSet<TemplateMap> getMapping() {
		return mapping;
	}

	public void setMapping(HashSet<TemplateMap> mapping) {
		this.mapping = mapping;
	}
}

class TemplateMap {
	String key;
	TemplateType templateType;
	String templateFile;
	String fileName;
	String fileExtension;
	String stereotype;
	String helper;

	TemplateMap() {
	}

	static TemplateMap newGlobalTemplateMap(String templateFile, String fileName) {
		TemplateMap globalTemplateMap = new TemplateMap();
		globalTemplateMap.templateType = TemplateType.Global;
		globalTemplateMap.templateFile = templateFile;
		globalTemplateMap.fileName = fileName;
		return globalTemplateMap;
	}

	static TemplateMap newDefaultTemplateMap(String templateFile,
			String fileExtension) {
		TemplateMap defaultTemplateMap = new TemplateMap();
		defaultTemplateMap.templateType = TemplateType.Default;
		defaultTemplateMap.templateFile = templateFile;
		defaultTemplateMap.fileExtension = fileExtension;
		return defaultTemplateMap;
	}

	static TemplateMap newStereotypeTemplateMap(String templateFile,
			String fileExtension, String stereotype) {
		TemplateMap stereotypeTemplateMap = new TemplateMap();
		stereotypeTemplateMap.templateType = TemplateType.Stereotype;
		stereotypeTemplateMap.stereotype = stereotype;
		stereotypeTemplateMap.templateFile = templateFile;
		stereotypeTemplateMap.fileExtension = fileExtension;
		return stereotypeTemplateMap;
	}

	static TemplateMap newHelperTemplateMap(String helperFile) {
		TemplateMap helperMap = new TemplateMap();
		helperMap.helper = helperFile;
		return helperMap;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public TemplateType getTemplateType() {
		return templateType;
	}

	public void setTemplateType(TemplateType templateType) {
		this.templateType = templateType;
	}

	public String getTemplateFile() {
		return templateFile;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getStereotype() {
		return stereotype;
	}

	public void setStereotype(String stereotype) {
		this.stereotype = stereotype;
	}

	public String getHelper() {
		return helper;
	}

	public void setHelper(String helper) {
		this.helper = helper;
	}
}

enum TemplateType {
	Stereotype, Default, Global, Helper
}

enum TemplateEngine {
	Groovy // , Velocity
}
