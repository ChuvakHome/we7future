
function getUser() {
	let localStorageUser = localStorage.getItem('user');
	
	if (localStorageUser !== undefined && localStorageUser !== null) {
		let userJSON = JSON.parse(localStorageUser);
		
		if (userJSON !== undefined && userJSON !== null)
			return userJSON;
	}
	
	return null;
}

function getUserVkId() {
	let user = getUser();
	
	if (user !== null && user['vkId'] !== null)
		return user['vkId'];
	else
		return null;
}

function isMobile() {
    const toMatch = [
        /Android/i,
        /webOS/i,
        /iPhone/i,
        /iPad/i,
        /iPod/i,
        /BlackBerry/i,
        /Windows Phone/i
    ];
    
    return toMatch.some(toMatchItem => {
        return navigator.userAgent.match(toMatchItem);
    });
}