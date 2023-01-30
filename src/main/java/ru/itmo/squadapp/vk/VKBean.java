package ru.itmo.squadapp.vk;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.ServiceActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.responses.GetByIdLegacyResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import ru.itmo.squadapp.model.vk.VKGroup;
import ru.itmo.squadapp.model.vk.VKPost;
import ru.itmo.squadapp.util.VKUtils;

public class VKBean implements Serializable {
	private static final long serialVersionUID = -6790291855018416614L;
	
	private VkApiClient vkApiClient;
	private ServiceActor serviceActor;
	
	public VKBean() {
		TransportClient transportClient = HttpTransportClient.getInstance();
		vkApiClient = new VkApiClient(transportClient);
		serviceActor = new ServiceActor(ApplicationSettings.APP_ID, ApplicationSettings.APP_SECRET, ServiceSettings.ACCESS_TOKEN);
	}
	
	public URI getPhotoURI(String userVkId) {
		try {
			List<com.vk.api.sdk.objects.users.responses.GetResponse> list = vkApiClient.users().get(serviceActor).userIds(userVkId).fields(Fields.PHOTO_ID, Fields.PHOTO_200_ORIG).execute();
			
			if (list.size() > 0)
				return list.get(0).getPhoto200Orig();
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		try {
			return new URI("https://se.ifmo.ru/~s311683/db/course-project/missingno.png");
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	public List<VKPost> getPosts(int count, String groupVkId) {
		return getPosts(groupVkId, 0, count);
	}
	
	public List<VKPost> getPosts(String groupVkId) {
		return getPosts(groupVkId, 0, 6);
	}
	
	public List<VKPost> getPosts(String groupVkId, int offset, int count) {
		List<VKPost> posts = new ArrayList<VKPost>();
		
		try {
			GetResponse r = vkApiClient.wall().get(serviceActor).domain(groupVkId).offset(offset).count(count).execute();

			for (WallpostFull post: r.getItems()) {
				VKPost vkPost = new VKPost();
				String originalText = post.getText();
				
				Pattern p = Pattern.compile("(https://[\\S]+)", Pattern.MULTILINE);
				
				String formattedPostText = p.matcher(originalText).replaceAll("<a class=\"post-link\" href=\"$1\">$1</a>");
				formattedPostText = VKUtils.replaceLinks(formattedPostText).replace("\n", "<br>");
				
				vkPost.setId(post.getId());
				vkPost.setText(formattedPostText);
				vkPost.setDate(new Date(post.getDate() * 1000L));
				vkPost.setOwnerId(post.getOwnerId());
				
				posts.add(vkPost);
			}
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		return posts;
	}
	
	public VKGroup getGroupInfo(String groupVkId) {
		try {
			List<GetByIdLegacyResponse> list = vkApiClient.groups().getByIdLegacy(serviceActor).groupId(groupVkId).execute();
		
			GetByIdLegacyResponse response = list.get(0);
			
			VKGroup group = new VKGroup(groupVkId);
			group.setName(response.getName());
			group.setPhotoURI(response.getPhoto100());
			
			return group;
		} catch (ApiException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
