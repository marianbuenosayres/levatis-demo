package com.plugtree.levatis.rest;

import java.net.URI;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement()
public class Asset {
    private String title;
    private String binaryContentAttachmentFileName;
    private String description;
    private String author;    
    private String format;
    private String uuid;
    private URI binaryLink, sourceLink, refLink;
    
    @XmlElement
    public URI getBinaryLink() {
        return binaryLink;
    }

    public void setBinaryLink(URI binaryLink) {
        this.binaryLink = binaryLink;
    }

    @XmlElement
    public URI getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(URI sourceLink) {
        this.sourceLink = sourceLink;
    }
    
    @XmlElement
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlElement
    public String getBinaryContentAttachmentFileName() {
        return binaryContentAttachmentFileName;
    }

    public void setBinaryContentAttachmentFileName(String binaryContentAttachmentFileName) {
        this.binaryContentAttachmentFileName = binaryContentAttachmentFileName;
    }

    @XmlElement()
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlElement
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
    
    @XmlElement
    public URI getRefLink() {
        return refLink;
    }

    public void setRefLink(URI refLink) {
        this.refLink = refLink;
    }

    @XmlElement
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@XmlElement
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}