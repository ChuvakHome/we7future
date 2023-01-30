
function doClick(e) {
	let name = document.getElementById("event-name").value;
	let eventType = document.getElementById("event-type").value;
	let date = document.getElementById("event-date").value;
	let description = document.getElementById("event-description").value;
	
	let eventJSONObject = {
		'name': name,
		'eventType': eventType,
		'dateTime': date,
		'description': description	
	};
	
	e.preventDefault();
	
	if (name !== null && name.length > 0 &&
		eventType !== null && eventType.length > 0 &&
		date !== null && date.length > 0 &&
		description !== null && description.length > 0) {
		fetch("/api/event/add", {
		'method': 'POST',
		'headers': {
			'Content-Type': 'text/plain;charset=utf-8'
		},
		body: JSON.stringify(eventJSONObject)
		})
			.then(e => {
				if (e.status == 200) {
					e.json().then(eventJSON => {
						let eventList = document.getElementById("event-list");
						eventList.insertAdjacentHTML("beforeend", `<tr>
							<td>${eventJSON.name}</td>
							<td>${eventJSON.dateTime}</td>
							<td>${eventJSON.eventType}</td>
							<td>${eventJSON.description}</td>
						</tr>`);
					});
				}	
				else
					alert("Ошибка добавления мероприятия!");
			});	
	}
	else
		alert('Вы пропустили одно или несколько поле(й)');
}

document.getElementById("submit-btn").addEventListener('click', doClick);