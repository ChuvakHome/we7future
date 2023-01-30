
let currentDate = new Date();
let fullLegend = true;

activePopup = null;
popupVisible = false;

/*const eventClasses = [
	'spbso', 
	'shso',
	'commisarka', 
	'meeting', 
	'school', 
	'preparation',
	'intersquad', 
	'agitation',
	'volunteering'
];*/

let months = [
	'Январь',
	'Февраль',
	'Март',
	'Апрель',
	'Май',
	'Июнь',
	'Июль',
	'Август',
	'Сентябрь',
	'Октябрь',
	'Ноябрь',
	'Декабрь'
];

lastDays = [
	31,
	28,
	31,
	30,
	31,
	30,
	31,
	31,
	30,
	31,
	30,
	31	
];

function complete(s, len) {
	let newStr = s.toString();
	
	while (newStr.length < len)
		newStr = '0' + newStr;
		
	return newStr;
}

function getStartDate(date) {
	const parseDateStr = `${date.getFullYear()}-${complete(date.getMonth() + 1, 2)}-01`;
	
	return new Date(parseDateStr);
}

function getEndDay(date) {
	const month = date.getMonth();
	const year = date.getFullYear();
	
	const lastDay = lastDays[month];
	
	return month == 1 && isLeapYear(year) ? lastDay + 1 : lastDay;
}

function getEndDate(date) {
	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	const day = getEndDay(date);
	
	return new Date(`${year}-${complete(month, 2)}-${complete(day, 2)}`);
}

function prevDate(date) {
	let month = date.getMonth() - 1;
	let year = date.getFullYear();
	
	if (month == -1) {
		month = 11;
		--year;
	}
	
	return new Date(`${year}-${complete(month + 1, 2)}-01`);
}

function nextDate(date) {
	let month = date.getMonth() + 1;
	let year = date.getFullYear();
	
	if (month == 12) {
		month = 0;
		++year;
	}
	
	return new Date(`${year}-${complete(month + 1, 2)}-01`);
}

function getNormalizedDay(day) {
	return (day + 6) % 7; 
}

function isLeapYear(year) {
	return year % 400 == 0 || year % 4 == 0 && year % 100 != 0;
}

function getMonthCalendarWeek(date) {
	if (date === undefined || date === null)
		date = new Date();
	
	let globalArray = [];
	let array = [];
	
	let startDate = getStartDate(date);
	
	let day = getNormalizedDay(startDate.getDay());
	let monthDay = 1;
	const lastDay = getEndDay(startDate);
	
	for (let i = 0; i < day; ++i)
		array.push('');
	
	while (monthDay <= lastDay) {
		array.push(monthDay);
		
		++day;
		++monthDay;
		
		if (day == 7) {
			globalArray.push(array);
			array = [];
			day = 0;
		}
	}
	
	if (day >= 1) {
		while (day < 7) {
			array.push('');
			++day;
		}
		
		globalArray.push(array);
	}
	
	return globalArray;
}

function stringifyDate(date) {
	const year = date.getFullYear();
	const month = date.getMonth() + 1;
	const day = date.getDate();
	
	return `${year}-${complete(month, 2)}-${complete(day, 2)}`;
}

/*
	<ul style="width: 0px; padding: 0px 0px 0px 15px; font-size: 1em; margin: 0px 0px 0px 0px;">
        <li>ШВМ 2.0</li>
        <li>Репетиция творческого конкурса</li>
        <li>Отчет комсостава</li>
    </ul>
 */

function doAlign() {
	/*let calendarCellElements = document.querySelectorAll('td.calendar-cell');
	
	Array.from(calendarCellElements).forEach(calendarCell => {
		const calendarDateDiv = calendarCell.querySelector('.calendar-cell-date-div');
		
		if (calendarDateDiv !== null) {
			const calendarCellRect = calendarCell.getBoundingClientRect();
			const calendarDateRect = calendarDateDiv.getBoundingClientRect();

			const calendarCellHeight = calendarCellRect.width * 0.5;			
			const goodHeight = calendarCellHeight - calendarDateRect.height;
			
			calendarDateDiv.style.marginBottom = `${goodHeight}px`;
			calendarCell.style.height = `${calendarCellHeight}px`;
		}
	});*/
	
	/*const calendarCellElements = document.querySelectorAll('tbody td.calendar-cell');
	
	Array.from(calendarCellElements).forEach(calendarCell => {
		const calendarCellRect = calendarCell.getBoundingClientRect();
		const calendarCellHeight = calendarCellRect.width * 0.5;			
		calendarCell.style.height = `${calendarCellHeight}px`;
	});*/
}

function fillCells() {
	const start = stringifyDate(currentDate);
	const end = stringifyDate(getEndDate(currentDate));
	const userVkId = getUserVkId();
	
	fetch(`/api/events/get_for_period?start=${start}&end=${end}`)
		.then(e => e.json())
		.then(events => {
			Array.from(events).forEach(event => {
				const eventId = event.id;
				const eventName = event.name;
				const eventType = event.eventType;
				const eventDate = new Date(event.dateTime);
				
				const eventsList = document.querySelector(`#h-${eventDate.getDate()} ul`);
				
				if (userVkId !== null) {
					fetch(`api/user/events/is_present?event_id=${eventId}&vk_id=${userVkId}`)
						.then(response => {
							if (response.ok) {
								response.json()
									.then(resultJSON => {
										const resultString = resultJSON.toString();
										let visitFlag = 'undefined';
												
										if (resultString === 'true')
											visitFlag = true;
										else if (resultString === 'false')
											visitFlag = false;
											
										eventsList.insertAdjacentHTML('beforeend', `<li onclick="showEventInfo(event)" event-id="${eventId}" will-visit="${visitFlag}" class="event event-${eventType.toString().toLowerCase()}">${eventName}</li>`);
									});
							}
						});
				}
				else				
					eventsList.insertAdjacentHTML('beforeend', `<li onclick="showEventInfo(event)" event-id="${eventId}" will-visit="undefined" class="event event-${eventType.toString().toLowerCase()}">${eventName}</li>`);
			});
		});
}

function showDate(date) {
	if (date !== null && date !== undefined)
		currentDate = date;
	
	document.getElementById('date-name').innerText = `${months[currentDate.getMonth()]} ${currentDate.getFullYear()}`;

	let calendarTableBody = document.querySelector('table#calendar-table tbody');

	calendarTableBody.innerHTML = '';

	getMonthCalendarWeek(currentDate).forEach(week => {
		let calendarRow = document.createElement('tr');
		calendarRow.classList.add('calendar-row');
		
		for (let i = 0; i < 7; ++i) {
			let weekStr = week[i];
			
			let htmlText = '';
			
			/*
				<table>
					<tr>
						<td id="h-${weekStr}" class="calendar-cell-h-td">
							<ul style="width: 0px; padding: 0px 0px 0px 20px; font-size: 1em; margin: 0px;"></ul>
						</td>
						<td class="calendar-date">
							<div class="calendar-cell-date-div" style="text-align: center;">${weekStr}</div>
						</td>
					</tr>
				</table>
			 */
			
			if (weekStr.toString().length > 0) {
				htmlText = `<td class="calendar-cell">
					<div id="h-${weekStr}" class="left-event-div">
						<ul style="width: 0px; padding: 0px 0px 0px 25px; font-size: 1em; margin: 0px;"></ul>
					</div>
					<div class="calendar-cell-date-parent">
						<div class="calendar-cell-date-div">${weekStr}</div>
					</div>
				</td>`;
			}
			else
				htmlText = `<td class="calendar-cell">
					<div></div>
					<div></div>
				</td>`;
			
			calendarRow.insertAdjacentHTML('beforeend', htmlText);
		}
		
		calendarTableBody.insertAdjacentElement('beforeend', calendarRow);
	});
}

function updateCalendar(date) {
	showDate(date);
	fillCells();
}

updateCalendar(new Date());

document.getElementById('prev-month').addEventListener('click', _ => {
	updateCalendar(prevDate(currentDate))
});

document.getElementById('next-month').addEventListener('click', _ => {
	updateCalendar(nextDate(currentDate))
});

window.addEventListener('resize', _ => doAlign());
window.addEventListener('keydown', e => {
	if (e.key == 'ArrowLeft' && !popupVisible)
		updateCalendar(prevDate(currentDate));
	else if (e.key == 'ArrowRight' && !popupVisible)
		updateCalendar(nextDate(currentDate));
	else if (e.key == 'Escape' && popupVisible)
		hidePopup();
})

function showOnly(eventType) {
	Array.from(document.querySelectorAll('ul.legend-list li')).forEach(li => {
		const liEventType = li.getAttribute('event-type');
		
		li.style.display = liEventType === eventType ? '' : "none";
	});
	
	const eventClass = `event-${eventType}`;
	
	Array.from(document.querySelectorAll('li.event')).forEach(li => {
		li.style.display = li.classList.contains(eventClass.toString()) ? '' : 'none';
	});
}

function showAll() {
	Array.from(document.querySelectorAll('ul.legend-list li')).forEach(li => li.style.display = '');
	
	Array.from(document.querySelectorAll('li.event')).forEach(li => li.style.display = '');
}

function redisplayLegend(e) {
	const eventType = e.target.getAttribute('event-type');
	
	fullLegend = !fullLegend;
	
	if (fullLegend)
		showAll();
	else
		showOnly(eventType);
}

function showPopup(popupElement) {
	if (popupElement !== null) {
		activePopup = popupElement;
		activePopup.style.display = '';
		popupVisible = true;
	}
}

function hidePopup() {
	if (activePopup !== null) {
		activePopup.style.display = 'none';
		activePopup = null;
	}
	
	popupVisible = false;
}

function showCreateEventPopup() {
	showPopup(document.querySelector('#create-event-block-box'));
}

function hideCreateEventPopup() {
	document.querySelector('#create-event-block-box').style.display = 'none';
	popupVisible = false;
	
	Array.from(document.querySelectorAll('.event-input-field')).forEach(field => field.value = '');
}

function manipulateScroll() {
	document.body.style.overflow = popupVisible ? 'hidden' : 'visible';
}

window.addEventListener('wheel', manipulateScroll);
