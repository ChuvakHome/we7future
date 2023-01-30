
function goToProfile() {
	let localStorageUser = localStorage.getItem('user'); 
	let outcome = '/register';
	
	if (localStorageUser !== undefined && localStorageUser !== null) {
		let userJSON = JSON.parse(localStorageUser);
		
		if (userJSON !== undefined && userJSON !== null && userJSON['vkId'] !== null)
			outcome = `/profile/${userJSON.vkId}`;
	}
	
	location.assign(outcome);
}

function setProfileImage() {
	let profileImg = document.getElementById('profile-img');
	let localItem = localStorage.getItem('user');
	
	if (localItem !== null && localItem !== undefined && JSON.parse(localItem) !== null) {
		let userJSON = JSON.parse(localItem); 
		fetch(`/api/user/info?vk_id=${userJSON.vkId}`)
			.then(e => e.json())
			.then(json => {
				if (json.photoURI !== null) {
					profileImg.src = json.photoURI;
				}
				
				if (json.surname !== null && json.name !== null) {
					let userNameElement = document.getElementById("user-name");
					userNameElement.innerText = `${json.name} ${json.surname}`;
					userNameElement.style.display = '';
				}
			});
	}
	
	profileImg.style.display = '';
}

setProfileImage();
