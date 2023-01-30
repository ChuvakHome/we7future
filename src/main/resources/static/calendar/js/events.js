
let colorsForEvents = {
	'spbso': '#0033B7', 
	'shso': '#00B3FF',
	'comissarka': 'red', 
	'meeting': '#FEEF30', 
	'school': '#17A64A', 
	'preparation': '#B0B0B0',
	'intersquad': '#FF9C7D', 
	'agitation': '#8A00CB',
	'volunteering': '#FE9DE9'
};

oldDate = '';
oldTime = '';
currentEventJSON = null;

function countSubstring(s, t) {
	return s.length - s.replaceAll(t.toString(), '').length;
}

function showEventInfoPopup() {
	showPopup(document.querySelector('#event-info-block-box'));
}

function hideEventInfoPopup() {
	document.querySelector('#event-info-block-box').style.display = 'none';
	popupVisible = false;
	currentEventJSON = null;
}

function showEventAdminInfoPopup() {
	showPopup(document.querySelector('#event-admin-info-block-box'));
}

function hideEventAdminInfoPopup() {
	document.querySelector('#event-admin-info-block-box').style.display = 'none';
	popupVisible = false;
	currentEventJSON = null;
	document.getElementById('participants-list').style.display = 'none';
}

function changeColor(color) {
	Array.from(document.querySelectorAll('.color-background-change')).forEach(element => element.style.backgroundColor = color);
	Array.from(document.querySelectorAll('.color-border-change')).forEach(element => element.style.borderColor = color);
	Array.from(document.querySelectorAll('.color-change')).forEach(element => element.style.color = color);
}

function showEventBaseInfo(eventJSON) {
	const eventId = eventJSON.id;
	const eventName = eventJSON.name;
	const eventDate = new Date(eventJSON.dateTime);
	const eventDescription = eventJSON.description;
	const eventType = eventJSON.eventType;
			
	const dateStr = eventDate.toLocaleDateString();
	const timeStr = `${complete(eventDate.getHours(), 2)}:${complete(eventDate.getMinutes(), 2)}`;
	
	document.querySelector('#info-event-name').innerText = eventName;
	document.querySelector('#info-event-date').innerText = dateStr;
	document.querySelector('#info-event-time').innerText = timeStr;
	document.querySelector('#info-event-description').innerText = eventDescription;
			
	changeColor(colorsForEvents[eventType.toString().toLowerCase()]);
			
	const eventButton = document.querySelector('#info-event-button');
	const userVkId = getUserVkId();
			
	if (userVkId !== null) {
		fetch(`api/user/events/is_present?event_id=${eventId}&vk_id=${userVkId}`)
			.then(response => {
				if (response.ok) {
					response.json()
						.then(resultJSON => {
							const resultString = resultJSON.toString();
									
							if (resultString === 'true') {
								eventButton.setAttribute('event-button-type', 'unsubscribe');
								eventButton.innerText = 'Отписаться';
							}
							else if (resultString === 'false') {
								eventButton.setAttribute('event-button-type', 'subscribe');
								eventButton.innerText = 'Записаться';
							}	
						});
					}
					else
						eventButton.style.display = 'none';
			});
	}
	else
		eventButton.style.display = 'none';
			
	showEventInfoPopup();
}

function showEventAdminInfo(eventJSON) {
	const eventName = eventJSON.name;
	const eventDate = new Date(eventJSON.dateTime);
	const eventDescription = eventJSON.description;
	const eventType = eventJSON.eventType;
			
	const dateStr = eventDate.toLocaleDateString();
	const timeStr = `${complete(eventDate.getHours(), 2)}:${complete(eventDate.getMinutes(), 2)}`;
	
	document.querySelector('#admin-info-event-name').innerText = eventName;
	document.querySelector('#admin-info-event-date').innerText = dateStr;
	document.querySelector('#admin-info-event-time').innerText = timeStr;
	document.querySelector('#admin-info-event-description').innerText = eventDescription;
	
	fetch(`/api/user/events/participants?event_id=${eventJSON.id}`)
		.then(result => result.json())
		.then(participantsList => {
			currentEventJSON["participants"] = participantsList;
			
			const participantsCount = participantsList.length;
			
			document.getElementById("participants-count").innerText = participantsCount.toString();
			let participantsListElement = document.querySelector('#participants ul');
			
			participantsListElement.innerHTML = '';
			
			Array.from(participantsList).forEach(participant => {
				const fullName = `${participant.surname} ${participant.name} ${participant.middleName}`; 
				
				participantsListElement.insertAdjacentHTML('beforeend', `<li>${fullName}</li>`);
			});
		});
	
	changeColor(colorsForEvents[eventType.toString().toLowerCase()]);
	
	showEventAdminInfoPopup();
}

function showParticipantList() {
	const displayProp = document.getElementById("participants-list").style.display;
	
	/*if (displayProp === 'none')
		displayProp = ''
	else
		displayProp = 'none';*/
		
	document.getElementById("participants-list").style.display = displayProp === 'none' ? '' : 'none';
}

function showEventInfo(e) {
	const eventId = e.target.getAttribute('event-id');
	
	fetch(`/api/events/get?event_id=${eventId}`)
		.then(e => e.json())
		.then(eventJSON => {
			currentEventJSON = eventJSON;
			const user = getUser();
		
			if (user !== null)
				fetch(`/api/user/events/is_organizer?user_id=${user.id}&event_id=${eventId}`)
				.then(response => {
					if (response.ok) {
						response.json()
							.then(resultJSON => {
								const resultText = resultJSON.toString();
								
								if (resultText === 'true')
									showEventAdminInfo(eventJSON);
								else
									showEventBaseInfo(eventJSON);
							})
					}
					else
						showEventBaseInfo(eventJSON);
				})
		});
}

function addEventPopupUpdater() {
	const eventNameField = document.getElementById('event-name');
	const eventDateField = document.getElementById('event-date');
	const eventTimeField = document.getElementById('event-time');
	const eventDescriptionField = document.getElementById('event-description');
	
	let enabledFlag = false;
	
	if (eventNameField.value.length > 0 && eventDateField.value.length > 0 && 
		eventTimeField.value.length > 0 && eventDescriptionField.value.length > 0)
		enabledFlag = true;
		
	document.getElementById('finish-button').setAttribute('enabled', enabledFlag.toString());
}

Array.from(document.querySelectorAll('.event-input-field')).forEach(e => e.addEventListener('input', addEventPopupUpdater))

/*function validateDate(dateString) {
	if (dateString.length < 3) {
		if (Number(dateString) !== NaN) {
			const day = Number(dateString);
		
			return (day > 0 || dateString.length === 0 && day === 0) && day <= 31;
		}
		else
			return false;
	}
	else if (dateString.length < 6) {
		const parts = dateString.split('.');
		
		if (parts.length == 2 && Number(parts[0]) !== NaN && Number(parts[1]) !== NaN) {
			const day = Number(parts[0]);
			const month = Number(parts[1]);
			
			return (month > 0 || parts[1].legnth === 1 && month === 0) && month <= 12 && 
				   (day > 0 || parts[0].legnth == 1 && day == 0) && day <= 31;
		}
		else
			return false;
	}
	else {
		const parts = dateString.split('.');
		
		if (parts.length == 3 && Number(parts[0]) !== NaN && Number(parts[1]) !== NaN && Number(parts[2]) !== NaN) {
			const day = Number(parts[0]);
			const month = Number(parts[1]);
			const year = Number(parts[2]);
			
			return year >= 0 && year <= 9999 && 
				   (month > 0 || parts[1].legnth === 1 && month === 0) && month <= 12 && 
				   (day > 0 || parts[0].legnth == 1 && day == 0) && day <= 31;
		}
		else
			return false;
	}
}*/

function validateDateInput(dateString) {
	if (dateString.length < 3) {
		if (Number(dateString) !== NaN) {
			const day = Number(dateString);
		
			return (day > 0 || dateString.length === 1 && day === 0) && day <= 31;
		}
		else
			return false;
	}
	else if (dateString.length < 6) {
		const parts = dateString.split('.');
		
		if (parts.length == 2 && Number(parts[0]) !== NaN && Number(parts[1]) !== NaN) {
			const day = Number(parts[0]);
			const month = Number(parts[1]);
			
			return day >= 0 && day <= 31 && month >= 0 && month <= 12;
		}
		else
			return false;
	}
	else {
		const parts = dateString.split('.');
		
		if (parts.length == 3 && Number(parts[0]) !== NaN && Number(parts[1]) !== NaN && Number(parts[2]) !== NaN) {
			const day = Number(parts[0]);
			const month = Number(parts[1]);
			const year = Number(parts[2]);
			
			return year >= 0 && year <= 3000 && 
				   month >= 0 && month <= 12 && 
				   day >= 0 && day <= 31;
		}
		else
			return false;
	}
}

function checkDateInput(e) {
	const dateString = e.target.value;
	
	if (dateString.length == 0 || 
		dateString.length < oldDate.length && countSubstring(dateString, '.') == countSubstring(oldDate, '.') || 
		validateDateInput(dateString)) {
		oldDate = dateString;
		
		if (countSubstring(dateString, '.') < 2 && (dateString.length == 2 || dateString.length == 5)) {
			e.target.value = dateString + '.';
			oldDate = e.target.value;
		}
	}
	else
		e.target.value = oldDate;
}

document.getElementById('event-date').addEventListener('input', checkDateInput);

function validateTimeInput(timeString) {
	if (timeString.length < 3) {
		if (Number(timeString) !== NaN) {
			const hours = Number(timeString);
			
			return hours >= 0 && hours <= 23;
		}
	}
	else {
		const parts = timeString.split(':');
		
		if (parts.length == 2 && Number(parts[0]) !== NaN && Number(parts[1]) !== NaN) {
			const hours = Number(parts[0]);
			const minutes = Number(parts[1]);
			
			return hours >= 0 && hours <= 23 && minutes >= 0 && minutes <= 59;
		}
		else
			return false;
	}
}

function checkTimeInput(e) {
	const timeString = e.target.value;
	
	if (timeString.length == 0 || 
		timeString.length < oldTime.length && countSubstring(timeString, ':') == countSubstring(oldTime, ':') || 
		validateTimeInput(timeString)) {
		oldTime = timeString;
		
		if (countSubstring(timeString, ':') < 1 && timeString.length == 2) {
			e.target.value = timeString + ':';
			oldTime = e.target.value;
		}
	}
	else
		e.target.value = oldTime;
}

document.getElementById('event-time').addEventListener('input', checkTimeInput);

function getISODate(dateString) {
	const dateParts = dateString.split('.');
	const day = Number(dateParts[0]);
	const month = Number(dateParts[1]);
	const year = Number(dateParts[2]);
	const dateFormatString = `${year}-${complete(month, 2)}-${complete(day, 2)}`;
	
	return dateFormatString;
}

function toISODateTimeString(dateString, timeString) {
	const dateFormatString = getISODate(dateString);
	
	const timeParts = timeString.split(':');
	
	const hours = Number(timeParts[0]);
	const minutes = Number(timeParts[1]);
	const millis = Number(timeParts.length == 3 ? timeParts[2] : 0);
	const timeFormatString = `${complete(hours, 2)}:${complete(minutes, 2)}:${complete(millis, 2)}`;
	
	return `${dateFormatString}T${timeFormatString}`;
}

function submitEvent(e) {
	if (e.target.getAttribute('enabled') === 'true') {
		const name = document.getElementById("event-name").value;
		const eventType = document.getElementById("event-type").value;
		const date = document.getElementById("event-date").value;
		const time = document.getElementById("event-time").value;
		const description = document.getElementById("event-description").value;
		
		const user = getUser();
		
		if (user === null) {
			alert("Авторизируйтесь, прежде чем создать мероприятие.");
			
			return;
		}
		
		const eventJSONObject = {
			'name': name,
			'eventType': eventType,
			'dateTime': toISODateTimeString(date, time),
			'description': description,
			'creatorId': user.id	
		};
		
		if (currentEventJSON !== null) {
			eventJSONObject['eventId'] = currentEventJSON.id;
			
			fetch("/api/user/events", {
				'method': 'PUT',
				'headers': {
					'Content-Type': 'text/plain;charset=utf-8'
				},
				body: JSON.stringify(eventJSONObject)
			}).then(response => {
					if (response.ok) {
						response.json().then(_ => {
							updateCalendar(new Date(getISODate(date)));
							currentEventJSON = null;
							hideCreateEventPopup();
						});
					}
					else
						alert('При изменении мероприятия произошла ошибка. Возможно вы не организатор данного мероприятия или введенные данные содеражат ошибки.');
				});
		}
		else {
			fetch("/api/event", {
				'method': 'POST',
				'headers': {
					'Content-Type': 'text/plain;charset=utf-8'
				},
				body: JSON.stringify(eventJSONObject)
			}).then(response => {
					if (response.ok) {
						response.json().then(_ => {
							updateCalendar(new Date(getISODate(date)));
							hideCreateEventPopup();
						});
					}
					else
						alert('При добавлении мероприятия произошла ошибка. Проверьте введенные данные.');
				});
		}
	}
}

function onEditEventButtonClicked() {
	if (currentEventJSON !== null) {
		const name = currentEventJSON.name;
		const eventType = currentEventJSON.eventType;
		const dateTime = new Date(currentEventJSON.dateTime);
		const description = currentEventJSON.description;
		
		const dateStr = dateTime.toLocaleDateString();
		const timeStr = `${complete(dateTime.getHours(), 2)}:${complete(dateTime.getMinutes(), 2)}`;
		
		document.getElementById("create-event-header-text").innerText = 'Редактировать мероприятие';
		document.getElementById("event-name").value = name;
		document.getElementById("event-type").value = eventType;
		document.getElementById("event-date").value = dateStr;
		document.getElementById("event-time").value = timeStr;
		document.getElementById("event-description").value = description;
		
		document.querySelector('#event-admin-info-block-box').style.display = 'none';
		
		showCreateEventPopup();
	}
}

function onRemoveEventButtonClicked() {
	if (currentEventJSON !== null) {
		const user = getUser();
		
		if (user !== null) {
			const eventId = currentEventJSON.id;
			
			eventJSONObject = {
				'userId': user.id,
				'eventId': currentEventJSON.id
			};
			
			fetch('/api/event', {
				'method': 'DELETE',
				'headers': {
					'Content-Type': 'text/plain;charset=utf-8'
				},
				body: JSON.stringify(eventJSONObject)
			}).then(response => {
				if (response.ok) {
					response.json()
						.then(resultJSON => {
							const resultText = resultJSON.toString();
							
							if (resultText === 'true') {
								hideEventAdminInfoPopup();
								document.querySelector(`li[event-id="${eventId}"]`).remove();
							}
							else
								alert('При удалении мероприятия произошла ошибка. Возможно такого мероприятия не существует или вы не являетесь его организатором.');
						})
				}
				else
					alert('При удалении мероприятия произошла ошибка.');
			})
		}	
	}
}

function draggable(el) {
	if (isMobile()) {
		el.addEventListener('touchstart', e => {
			const offsetX = e.changedTouches[0].clientX - parseInt(window.getComputedStyle(el).left);
			const offsetY = e.changedTouches[0].clientY - parseInt(window.getComputedStyle(el).top);
			
		    el.style.cursor = 'move';
		    
		    function touchMoveHandler(e) {
				if (document.activeElement.value === undefined) {
					el.style.top = `${(e.changedTouches[0].clientY - offsetY)}px`;
				    el.style.left = `${(e.changedTouches[0].clientX - offsetX)}px`;
				}
				else
					e.preventDefault();
			}
		    
		    function reset() {
				el.style.cursor = 'default';
		    	window.removeEventListener('touchmove', touchMoveHandler);
				window.removeEventListener('touchend', reset);
				window.blockMenuHeaderScroll = false;
		    }
		
			window.addEventListener('touchmove', touchMoveHandler);
		    window.addEventListener('touchend', reset);
		    window.blockMenuHeaderScroll = true;
		});
	}
	else {
		el.addEventListener('mousedown', e => {
			const offsetX = e.clientX - parseInt(window.getComputedStyle(el).left);
			const offsetY = e.clientY - parseInt(window.getComputedStyle(el).top);
			
		    el.style.cursor = 'move';
		    
		    function mouseMoveHandler(e) {
				if (document.activeElement.value === undefined) {
					el.style.top = `${(e.clientY - offsetY)}px`;
			    	el.style.left = `${(e.clientX - offsetX)}px`;
			    }
			    else
			    	e.preventDefault();
			}
		
		    function reset() {
				el.style.cursor = 'default';
		    	window.removeEventListener('mousemove', mouseMoveHandler);
				window.removeEventListener('mouseup', reset);
		    }
		
		    window.addEventListener('mousemove', mouseMoveHandler);
		    window.addEventListener('mouseup', reset);
		});
	}
}

function onInfoEventButtonClicked(e) {
	const eventButton = e.target;
	const eventButtonType = eventButton.getAttribute('event-button-type');
	
	let httpMethod = null;
	
	if (eventButtonType === 'subscribe')
		httpMethod = 'POST';
	else if (eventButtonType === 'unsubscribe')
		httpMethod = 'DELETE';
		
	const user = getUser();
	const eventId = currentEventJSON.id;
	
	if (user !== null && httpMethod !== null) {
		const eventJSONObject = {
			'userId': user.id,
			'eventId': eventId
		};
		
		fetch('/api/user/events', {
				'method': httpMethod,
				'headers': {
					'Content-Type': 'text/plain;charset=utf-8'
				},
				body: JSON.stringify(eventJSONObject)
			}).then(response => {
				if (response.ok) {
					response.json()
						.then(resultJSON => {
							const resultText = resultJSON.toString();
							
							if (resultText === 'true') {
								visitFlag = undefined;
								
								if (httpMethod === 'POST')
									visitFlag = true;
								else if (httpMethod === 'DELETE')
									visitFlag = false;
								
								document.querySelector(`li[event-id="${eventId}"]`).setAttribute('will-visit', visitFlag.toString());
								hideEventInfoPopup();
							}
							else if (resultText === 'false')
								alert('Не получилось записаться/отписаться.');
						})
				}
				else {
					alert('Не получилось записаться/отписаться.');
				}
			})
	}
}

Array.from(document.querySelectorAll('.popup')).forEach(popupElement => draggable(popupElement));

window.blockMenuHeaderScroll = false;

if (isMobile()) {
	window.addEventListener('touchmove', _ => {
	    if (blockMenuHeaderScroll)
	        document.documentElement.style.overflow = 'hidden';
	    else
	    	document.documentElement.style.overflow = 'auto';
	});
}
