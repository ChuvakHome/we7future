
function onLoginButton() {
	location.assign('/login');
}

function doClick(e) {
	let surname = document.getElementById('surname-field').value;
	let name = document.getElementById('name-field').value;
	let middleName = document.getElementById('middle-name-field').value;
	let vkId = document.getElementById('vk-field').value;
	let phone = document.getElementById('phone-field').value;
	let email = document.getElementById('email-field').value;
	
	let userJSONObject = {
		'surname': surname,
		'name': name,
		'middleName': middleName,
		'role': 'CANDIDATE',
		'vkId': vkId,
		'phoneNumber': phone,
		'email': email
	};
	
	e.preventDefault();
	
	if (surname !== null && surname.length > 0 &&
		name !== null && name.length > 0 &&
		middleName !== null && middleName.length > 0 &&
		vkId !== null && vkId.length > 0 &&
		phone !== null && phone.length > 0 &&
		email !== null && email.length > 0) {
		fetch('/api/registration', {
			'method': 'POST',
			'headers': {
				'Content-Type': 'text/plain;charset=utf-8'
			},
			body: JSON.stringify(userJSONObject)
		}).then(e => e.json()).then(serverUserJSON => {
				if (serverUserJSON.id > 0) {
					alert("Регистрация прошла успешно");
					localStorage.setItem('user', JSON.stringify(serverUserJSON));
					location.assign('/');
				}
				else {
					alert("Ошибка регистрации");
				}
			});
	}
	else
		alert('Вы пропустили одно или несколько поле(й)');
}

document.getElementById("submit-btn").addEventListener('click', doClick);