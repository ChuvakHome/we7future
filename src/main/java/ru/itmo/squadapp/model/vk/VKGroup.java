package ru.itmo.squadapp.model.vk;

import java.net.URI;

import ru.itmo.squadapp.util.VKUtils;

public class VKGroup {
	private final String groupId;
	private final URI groupURI;
	private String name;
	private URI photoURI;
	
	public VKGroup(String groupId) {
		this.groupId = VKUtils.getVKId(groupId);
		this.groupURI = VKUtils.getVKURI(this.groupId);
	}
	
	public String getGroupId() {
		return this.groupId;
	}
	
	public URI getGroupURI() {
		return this.groupURI;
	}
	
	public void setName(String groupName) {
		this.name = groupName;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPhotoURI(URI photoURI) {
		this.photoURI = photoURI;
	}
	
	public URI getPhotoURI() {
		return this.photoURI;
	}
}
