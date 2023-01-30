
function doClick(e) {
	let vk = document.getElementById('vk-field').value;
	
	e.preventDefault();
	
	fetch(`/api/login?vk_id=${vk}`).then(e => {
		if (e.status == 200) {
			e.json().then(userJson => {
				localStorage.setItem('user', JSON.stringify(userJson));
				alert("Авторизация прошла успешно!");
				location.assign("/");
			});
		}
		else {
			alert("Вiйдi отсюда, розбiйник ты!");
		}
	});
}

function onRegisterButton() {
	location.assign('/register');
}

document.getElementById("submit-btn").addEventListener('click', doClick);