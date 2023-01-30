package ru.itmo.squadapp.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import ru.itmo.squadapp.model.vk.VKPost;

public class VKUtils {
	public static String replaceLinks(String text) {
		Pattern p = Pattern.compile("\\[([\\w]+)\\|([^\\[\\]]+)\\]", Pattern.MULTILINE);		
		
		return p.matcher(text).replaceAll("<a class=\"post-link\" href=\"https://vk.com/$1\">$2</a>");
	}
	
	public static String getVKId(String vkText) {
		if (vkText.startsWith("https://")) {
			if (vkText.endsWith("/"))
				vkText = vkText.substring(0, vkText.length() - 1);
			
			int slashIndex = vkText.lastIndexOf('/');
			
			return vkText.substring(slashIndex + 1);
		}
		else
			return vkText;
	}
	
	public static URI getVKURI(String vkId) {
		String uri = vkId.startsWith("https://") ? vkId : "https://vk.com/" + vkId;
		
		try {
			return new URI(uri);
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public static URI getVKURIForPost(VKPost post) {
		return getVKURI(String.format("wall%d_%d", post.getOwnerId(), post.getId()));
	}
}
