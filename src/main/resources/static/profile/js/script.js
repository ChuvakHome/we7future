
function onLoginButton() {
	location.assign("/login");
}

function onLogoutButton() {
	localStorage.removeItem('user');
	location.assign("/");
}

function onRemoveUserButton() {
	const user = getUser();
	
	if (user !== null) {
		fetch(`/api/user`, {
				'method': 'DELETE',
				'headers': {
					'Content-Type': 'text/plain;charset=utf-8'
				},
				body: JSON.stringify({'id': user.id})
			}).then(response => {
			if (response.ok) {
				response.json()
					.then(result => {
						const resultText = result.toString();
						
						if (resultText === 'true') {
							alert('Ваша учетная запись успешно удалено.');
							localStorage.removeItem('user');
							location.assign('/');
						}
						else
							alert('При удалении учетной записи произошла ошибка.');
					});
			}
			else
				alert('При удалении учетной записи произошла ошибка.');
		})
	}
}