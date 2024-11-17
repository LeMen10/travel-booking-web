

const getUsersBookingCounts = async (sortOrder) => {
	const userTable = document.getElementById('user-table');
	userTable.innerHTML = ''
    try {
        const response = await fetch(`/admin/bookings-count?sort=${sortOrder}`);
        const data = await response.json();
		data.forEach(userArray => {
		        const user = userArray[0];
		        const row = document.createElement('tr');
		        
		        row.innerHTML = `
		            <td>${user.user_id}</td>
		            <td>${user.fullName}</td>
		            <td>${user.phone}</td>
		            <td>${user.email}</td>
		            <td>${user.gender}</td>
		        `;
		        
		        userTable.appendChild(row);
		    });
    } catch (error) {
        console.error('Error fetching user booking counts:', error);
    }
};

document.getElementById('filter-customer-asc').onclick = () => getUsersBookingCounts('asc');;
document.getElementById('filter-customer-desc').onclick = () => getUsersBookingCounts('desc');
