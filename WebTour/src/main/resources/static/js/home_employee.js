document.addEventListener('DOMContentLoaded', function () {
            const calendarEl = document.getElementById('calendar');
            const currentMonthYearEl = document.getElementById('currentMonthYear');
            const prevMonthButton = document.getElementById('prevMonth');
            const nextMonthButton = document.getElementById('nextMonth');

            let today = new Date();
            let currentYear = today.getFullYear();
            let currentMonth = today.getMonth(); // 0-based (0 = January)

            function renderCalendar(year, month) {
                calendarEl.innerHTML = '';
                const firstDay = new Date(year, month, 1).getDay();
                const daysInMonth = new Date(year, month + 1, 0).getDate();
                ;
                

                currentMonthYearEl.textContent = `${new Date(year, month).toLocaleString('en-US', { month: 'long' })} ${year}`;

                const calendarDays = [];

                for (let i = 0; i < firstDay; i++) {
                    calendarDays.push('');
                }

                for (let i = 1; i <= daysInMonth; i++) {
                    calendarDays.push(i);
                }

                calendarDays.forEach(day => {
                    const dayEl = document.createElement('div');
                    dayEl.classList.add('day');
                    if (day) {
                        dayEl.innerHTML = `<div class="day-header">${day}</div>`;
                        if (
                            year === today.getFullYear() &&
                            month === today.getMonth() &&
                            day === today.getDate()
                        ) {
                            dayEl.classList.add('current-day');
                        }
                        // Add sample events
                        if (day % 2 === 0) {
                            const eventEl = document.createElement('div');
                            eventEl.classList.add('event');
                            eventEl.textContent = 'Working';
                            dayEl.appendChild(eventEl);
                        }
                    }
                    calendarEl.appendChild(dayEl);
                });
            }

            // Event listeners for navigation buttons
            prevMonthButton.addEventListener('click', function () {
                currentMonth--;
                if (currentMonth < 0) {
                    currentMonth = 11;
                    currentYear--;
                }
                renderCalendar(currentYear, currentMonth);
            });

            nextMonthButton.addEventListener('click', function () {
                currentMonth++;
                if (currentMonth > 11) {
                    currentMonth = 0;
                    currentYear++;
                }
                renderCalendar(currentYear, currentMonth);
            });

            // Initial render
            renderCalendar(currentYear, currentMonth);
        });