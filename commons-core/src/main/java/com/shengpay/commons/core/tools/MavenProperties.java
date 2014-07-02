package com.shengpay.commons.core.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by lindongcheng on 14-5-7.
 */
public class MavenProperties {
    private String groupId, artifactId;
    private Properties properties;

    public MavenProperties(String aGroupId, String aArtifactId) {
        this.groupId = aGroupId;
        this.artifactId = aArtifactId;
        this.properties = getProperties();
    }

    public String getVersion() {
        return properties.getProperty("version");
    }

    private Properties getProperties() {
        Properties p = new Properties();
        try {
            InputStream inputStream = getPropertiesInputStream();
            p.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("读取[" + groupId + ":" + artifactId + "]的maven版本信息时发生异常", e);
        }
        return p;
    }

    private InputStream getPropertiesInputStream() {
        String path = "/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties";
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream == null) {
            throw new RuntimeException("未从类路径找到配置文件[" + path + "]");
        }
        return inputStream;
    }

}
