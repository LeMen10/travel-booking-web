const chartDefaultValue = [0, 0, 0, 0, 0, 0, 0];
const salary = 8000000;
document.addEventListener("DOMContentLoaded", async function() {
	const desiredRevenue = 100000000;
	const customerData = await getStatisticsList("customer");
	const revenueData = await getStatisticsList("revenue");
	const newAccountData = await getStatisticsList("new-account");
	const bookingData = await getStatisticsList("booking");
	const originalPriceData = await getStatisticsList("original-price-tour");
	const rateTourData = await getStatisticsList("rate-tour");
	const countEmployee = await getNumEmployee();
	const totalSalary = countEmployee * salary;
	const listPrfitData = calculateRateProfitPerMonth(revenueData, originalPriceData, totalSalary);

	const revenueRateList = getPercentageMonths(revenueData != null ? revenueData : chartDefaultValue, desiredRevenue);

	const sumCustomer = sumValue(customerData != null ? customerData : chartDefaultValue);
	const sumRevenue = sumValue(revenueData != null ? revenueData : chartDefaultValue);
	const sumNewUser = sumValue(newAccountData != null ? newAccountData : dchartDefaultValue);
	const sumBooking = sumValue(bookingData != null ? bookingData : chartDefaultValue);
	const averageRate = averageValue(rateTourData != null ? rateTourData : chartDefaultValue).toFixed(2);
	const sumProfit = sumProfitPerMonth(revenueData, originalPriceData, totalSalary);

	setValueToMiniChart("total-new-user-value", sumNewUser);
	setValueToMiniChart("total-cutomer-value", sumCustomer);
	setValueToMiniChart("total-revenue-value", sumRevenue);
	setValueToMiniChart("total-booking-value", sumBooking);
	setValueToMiniChart("total-rate-tour-value", averageRate);
	setValueToMiniChart("total-profit-value", sumProfit);

	createChart('new-user-chart', '#487fff', newAccountData != null ? newAccountData : dchartDefaultValue);
	createChart('active-user-chart', '#45b369', customerData != null ? customerData : chartDefaultValue);
	createChart('total-sales-chart', '#f4941e', bookingData != null ? bookingData : chartDefaultValue);
	createChart('conversion-user-chart', '#8252e9', revenueRateList);
	createChart('leads-chart', '#de3ace', rateTourData != null ? rateTourData : chartDefaultValue);
	createChart('total-profit-chart', '#00b8f2', listPrfitData != null ? listPrfitData : chartDefaultValue);
});

function setValueToMiniChart(elementId, value) {
	const element = document.getElementById(elementId);
	element.innerText = value;
}

function sumValue(listData) {
	var sum = 0;
	for (index in listData) {
		sum += listData[index];
	}
	return sum;
}

function averageValue(listData) {
	var sum = 0;
	for (index in listData) {
		sum += listData[index];
	}
	return sum / listData.length;
}

async function getStatisticsList(nameStattistics) {
	const response = await fetch(`http://localhost:8080/api-get-statistics-${nameStattistics}-7-months`, {
		method: 'GET',
		headers: {
			'Content-Type': 'application/json'
		},
	});

	if (response.ok) {
		const data = await response.json();
		if (data != null) {
			const listData = data.map(item => item[0]);

			return listData.reverse();
		}
	}

	return null;
}

async function getNumEmployee() {
	const response = await fetch(`http://localhost:8080/api-get-count-employee`, {
		method: 'GET',
		headers: {
			'Content-Type': 'application/json'
		},
	});

	if (response.ok) {
		const data = await response.json();
		if (data != null) {
			return data;
		}
	}

	return 0;
}

function sumProfitPerMonth(listRevenue, listOriginalPrice, totalEmployeeSalary) {
	let totalProfit = 0;
	for (let i = 0; i < listRevenue.length; i++) {
		totalProfit += (listRevenue[i] - totalEmployeeSalary - listOriginalPrice[i]);
	}
	return totalProfit;
}

function calculateRateProfitPerMonth(listRevenue, listOriginalPrice, totalEmployeeSalary) {
	let listProfit = [];
	let profit = 0;
	console.log(listOriginalPrice, listRevenue, totalEmployeeSalary)
	for (let i = 0; i < listRevenue.length; i++) {
		profit = listRevenue[i] - totalEmployeeSalary - listOriginalPrice[i];
		console.log(profit);
		let rate = listRevenue[i] != 0 ? (profit / listRevenue[i]).toFixed(2) : 0;
		listProfit.push(rate);
	}
	return listProfit;
}

function getPercentageMonths(lisData, desiredRevenue) {
	var newList = [];
	for (index in lisData) {
		const rate = (lisData[index] - desiredRevenue) / desiredRevenue * 100;
		newList.push(rate);
	}
	return newList;
}
function createChart(chartId, chartColor, listData) {

	let currentYear = new Date().getFullYear();

	var options = {
		series: [
			{
				name: 'series1',
				data: listData,
			},
		],
		chart: {
			type: 'area',
			width: 200,
			height: 42,
			sparkline: {
				enabled: true // Remove whitespace
			},

			toolbar: {
				show: false
			},
			padding: {
				left: 0,
				right: 0,
				top: 0,
				bottom: 0
			}
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			curve: 'smooth',
			width: 2,
			colors: [chartColor],
			lineCap: 'round'
		},
		grid: {
			show: true,
			borderColor: 'transparent',
			strokeDashArray: 0,
			position: 'back',
			xaxis: {
				lines: {
					show: false
				}
			},
			yaxis: {
				lines: {
					show: false
				}
			},
			row: {
				colors: undefined,
				opacity: 0.5
			},
			column: {
				colors: undefined,
				opacity: 0.5
			},
			padding: {
				top: -3,
				right: 0,
				bottom: 0,
				left: 0
			},
		},
		fill: {
			type: 'gradient',
			colors: [chartColor], // Set the starting color (top color) here
			gradient: {
				shade: 'light', // Gradient shading type
				type: 'vertical',  // Gradient direction (vertical)
				shadeIntensity: 0.5, // Intensity of the gradient shading
				gradientToColors: [`${chartColor}00`], // Bottom gradient color (with transparency)
				inverseColors: false, // Do not invert colors
				opacityFrom: .75, // Starting opacity
				opacityTo: 0.3,  // Ending opacity
				stops: [0, 100],
			},
		},
		// Customize the circle marker color on hover
		markers: {
			colors: [chartColor],
			strokeWidth: 2,
			size: 0,
			hover: {
				size: 8
			}
		},
		xaxis: {
			labels: {
				show: false
			},
			categories: [`Jan ${currentYear}`, `Feb ${currentYear}`, `Mar ${currentYear}`, `Apr ${currentYear}`, `May ${currentYear}`, `Jun ${currentYear}`, `Jul ${currentYear}`, `Aug ${currentYear}`, `Sep ${currentYear}`, `Oct ${currentYear}`, `Nov ${currentYear}`, `Dec ${currentYear}`],
			tooltip: {
				enabled: false,
			},
		},
		yaxis: {
			labels: {
				show: false
			}
		},
		tooltip: {
			x: {
				format: 'dd/MM/yy HH:mm'
			},
		},
	};

	var chart = new ApexCharts(document.querySelector(`#${chartId}`), options);
	chart.render();
}

// ================================ Revenue Growth Area Chart Start ================================ 
function createChartTwo(chartId, chartColor) {

	var options = {
		series: [
			{
				name: 'This Day',
				data: [4, 18, 13, 40],
			},
		],
		chart: {
			type: 'area',
			width: '100%',
			height: 162,
			sparkline: {
				enabled: false // Remove whitespace
			},
			toolbar: {
				show: false
			},
			padding: {
				left: 0,
				right: 0,
				top: 0,
				bottom: 0
			}
		},
		dataLabels: {
			enabled: false
		},
		stroke: {
			curve: 'smooth',
			width: 2,
			colors: [chartColor],
			lineCap: 'round'
		},
		grid: {
			show: true,
			borderColor: 'red',
			strokeDashArray: 0,
			position: 'back',
			xaxis: {
				lines: {
					show: false
				}
			},
			yaxis: {
				lines: {
					show: false
				}
			},
			row: {
				colors: undefined,
				opacity: 0.5
			},
			column: {
				colors: undefined,
				opacity: 0.5
			},
			padding: {
				top: -30,
				right: 0,
				bottom: -10,
				left: 0
			},
		},
		fill: {
			type: 'gradient',
			colors: [chartColor], // Set the starting color (top color) here
			gradient: {
				shade: 'light', // Gradient shading type
				type: 'vertical',  // Gradient direction (vertical)
				shadeIntensity: 0.5, // Intensity of the gradient shading
				gradientToColors: [`${chartColor}00`], // Bottom gradient color (with transparency)
				inverseColors: false, // Do not invert colors
				opacityFrom: .6, // Starting opacity
				opacityTo: 0.3,  // Ending opacity
				stops: [0, 100],
			},
		},
		// Customize the circle marker color on hover
		markers: {
			colors: [chartColor],
			strokeWidth: 3,
			size: 0,
			hover: {
				size: 10
			}
		},
		xaxis: {
			labels: {
				show: false
			},
			categories: [`Jan`, `Feb`, `Mar`, `Apr`, `May`, `Jun`, `Jul`, `Aug`, `Sep`, `Oct`, `Nov`, `Dec`],
			tooltip: {
				enabled: false,
			},
			tooltip: {
				enabled: false
			},
			labels: {
				formatter: function(value) {
					return value;
				},
				style: {
					fontSize: "14px"
				}
			},
		},
		yaxis: {
			labels: {
				show: false
			},
		},
		tooltip: {
			x: {
				format: 'dd/MM/yy HH:mm'
			},
		},
	};

	var chart = new ApexCharts(document.querySelector(`#${chartId}`), options);
	chart.render();
}
createChartTwo('revenue-chart', '#487fff');
// ================================ Revenue Growth Area Chart End ================================ 

// ================================ Earning Statistics bar chart Start ================================ 
var options = {
	series: [{
		name: "Sales",
		data: [{
			x: 'Jan',
			y: 85000,
		}, {
			x: 'Feb',
			y: 70000,
		}, {
			x: 'Mar',
			y: 40000,
		}, {
			x: 'Apr',
			y: 50000,
		}, {
			x: 'May',
			y: 60000,
		}, {
			x: 'Jun',
			y: 50000,
		}, {
			x: 'Jul',
			y: 40000,
		}, {
			x: 'Aug',
			y: 50000,
		}, {
			x: 'Sep',
			y: 40000,
		}, {
			x: 'Oct',
			y: 60000,
		}, {
			x: 'Nov',
			y: 30000,
		}, {
			x: 'Dec',
			y: 50000,
		}]
	}],
	chart: {
		type: 'bar',
		height: 310,
		toolbar: {
			show: false
		}
	},
	plotOptions: {
		bar: {
			borderRadius: 4,
			horizontal: false,
			columnWidth: '23%',
			endingShape: 'rounded',
		}
	},
	dataLabels: {
		enabled: false
	},
	fill: {
		type: 'gradient',
		colors: ['#487FFF'], // Set the starting color (top color) here
		gradient: {
			shade: 'light', // Gradient shading type
			type: 'vertical',  // Gradient direction (vertical)
			shadeIntensity: 0.5, // Intensity of the gradient shading
			gradientToColors: ['#487FFF'], // Bottom gradient color (with transparency)
			inverseColors: false, // Do not invert colors
			opacityFrom: 1, // Starting opacity
			opacityTo: 1,  // Ending opacity
			stops: [0, 100],
		},
	},
	grid: {
		show: true,
		borderColor: '#D1D5DB',
		strokeDashArray: 4, // Use a number for dashed style
		position: 'back',
	},
	xaxis: {
		type: 'category',
		categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
	},
	yaxis: {
		labels: {
			formatter: function(value) {
				return (value / 1000).toFixed(0) + 'k';
			}
		}
	},
	tooltip: {
		y: {
			formatter: function(value) {
				return value / 1000 + 'k';
			}
		}
	}
};

var chart = new ApexCharts(document.querySelector("#barChart"), options);
chart.render();
// ================================ Earning Statistics bar chart End ================================ 

// ================================ Custom Overview Donut chart Start ================================ 
var options = {
	series: [500, 500, 500],
	colors: ['#45B369', '#FF9F29', '#487FFF'],
	labels: ['Active', 'New', 'Total'],
	legend: {
		show: false
	},
	chart: {
		type: 'donut',
		height: 300,
		sparkline: {
			enabled: true // Remove whitespace
		},
		margin: {
			top: -100,
			right: -100,
			bottom: -100,
			left: -100
		},
		padding: {
			top: -100,
			right: -100,
			bottom: -100,
			left: -100
		}
	},
	stroke: {
		width: 0,
	},
	dataLabels: {
		enabled: false
	},
	responsive: [{
		breakpoint: 480,
		options: {
			chart: {
				width: 200
			},
			legend: {
				position: 'bottom'
			}
		}
	}],
	plotOptions: {
		pie: {
			startAngle: -90,
			endAngle: 90,
			offsetY: 10,
			customScale: 0.8,
			donut: {
				size: '70%',
				labels: {
					show: true,
					total: {
						showAlways: true,
						show: true,
						label: 'Customer Report',
						// formatter: function (w) {
						//     return w.globals.seriesTotals.reduce((a, b) => {
						//         return a + b;
						//     }, 0);
						// }
					}
				},
			}
		}
	},
};

var chart = new ApexCharts(document.querySelector("#donutChart"), options);
chart.render();
// ================================ Custom Overview Donut chart End ================================ 

// ================================ Client Payment Status chart End ================================ 
var options = {
	series: [{
		name: 'Net Profit',
		data: [44, 100, 40, 56, 30, 58, 50]
	}, {
		name: 'Revenue',
		data: [90, 140, 80, 125, 70, 140, 110]
	}, {
		name: 'Free Cash',
		data: [60, 120, 60, 90, 50, 95, 90]
	}],
	colors: ['#45B369', '#144bd6', '#FF9F29'],
	labels: ['Active', 'New', 'Total'],

	legend: {
		show: false
	},
	chart: {
		type: 'bar',
		height: 350,
		toolbar: {
			show: false
		},
	},
	grid: {
		show: true,
		borderColor: '#D1D5DB',
		strokeDashArray: 4, // Use a number for dashed style
		position: 'back',
	},
	plotOptions: {
		bar: {
			borderRadius: 4,
			columnWidth: 8,
		},
	},
	dataLabels: {
		enabled: false
	},
	states: {
		hover: {
			filter: {
				type: 'none'
			}
		}
	},
	stroke: {
		show: true,
		width: 0,
		colors: ['transparent']
	},
	xaxis: {
		categories: ['Mon', 'Tues', 'Wed', 'Thurs', 'Fri', 'Sat', 'Sun'],
	},
	yaxis: {
		categories: ['0', '10,000', '20,000', '30,000', '50,000', '1,00,000', '1,00,000'],
	},
	fill: {
		opacity: 1,
		width: 18,
	},
};

var chart = new ApexCharts(document.querySelector("#paymentStatusChart"), options);
chart.render();
// ================================ Client Payment Status chart End ================================ 

// ================================ J Vector Map Start ================================ 
$('#world-map').vectorMap(
	{
		map: 'world_mill_en',
		backgroundColor: 'transparent',
		borderColor: '#fff',
		borderOpacity: 0.25,
		borderWidth: 0,
		color: '#000000',
		regionStyle: {
			initial: {
				fill: '#D1D5DB'
			}
		},
		markerStyle: {
			initial: {
				r: 5,
				'fill': '#fff',
				'fill-opacity': 1,
				'stroke': '#000',
				'stroke-width': 1,
				'stroke-opacity': 0.4
			},
		},
		markers: [{
			latLng: [35.8617, 104.1954],
			name: 'China : 250'
		},

		{
			latLng: [25.2744, 133.7751],
			name: 'AustrCalia : 250'
		},

		{
			latLng: [36.77, -119.41],
			name: 'USA : 82%'
		},

		{
			latLng: [55.37, -3.41],
			name: 'UK   : 250'
		},

		{
			latLng: [25.20, 55.27],
			name: 'UAE : 250'
		}],

		series: {
			regions: [{
				values: {
					"US": '#487FFF ',
					"SA": '#487FFF',
					"AU": '#487FFF',
					"CN": '#487FFF',
					"GB": '#487FFF',
				},
				attribute: 'fill'
			}]
		},
		hoverOpacity: null,
		normalizeFunction: 'linear',
		zoomOnScroll: false,
		scaleColors: ['#000000', '#000000'],
		selectedColor: '#000000',
		selectedRegions: [],
		enableZoom: false,
		hoverColor: '#fff',
	});
// ================================ J Vector Map End ================================ 

// ================================ J History payment ================================ 

async function filterTopTour() {
	const total = document.getElementById("input-quantity-custome").value;
	try {
		// Gọi API
		const response = await fetch(`/api-get-statistics-tour-top?top=4&total=${total}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
			},
		});

		if (!response.ok) {
			throw new Error(`Error ${response.status}: ${response.statusText}`);
		}

		// Chuyển đổi kết quả sang JSON
		const data = await response.json();
		if (data.length == 0) {
			document.getElementById("body-chart-top-tour").innerHTML = "<div class='empty-list-Data'>No data available</div>";
		}
		else {
			const bodyChart = document.getElementById("body-chart-top-tour");
			var bodyHTML = "";
			for (let i = 0; i < data.length; i++) {
				let item = data[i];
				bodyHTML += `<div class="d-flex align-items-center justify-content-between gap-3 mb-32">
								<div class="d-flex align-items-center">
									<div class="top-level-title-${item[0]}"> ${item[0]}</div>
									<div class="flex-grow-1">
									<h6 class="text-md mb-0">${item[2]}</h6>
									</div>
								</div>
								<span class="text-primary-light text-md fw-medium">${item[3]}</span>
							</div>`;
			}
			bodyChart.innerHTML = bodyHTML;
		}
		console.log(data);
	} catch (error) {
		console.error("Error fetching payment history:", error);
		throw error; // Ném lỗi ra nếu cần xử lý thêm
	}
}

async function handleFilterMomo(pageNum) {
	const apiUrl = '/api-get-history';

	const params = getFilterParams("momo", pageNum);

	// Gọi hàm lấy dữ liệu
	getHistoryPayment(apiUrl, params)
		.then(data => {
			innerTablePaypal("momo", data.content);
			innerPagePaypal("momo", data.totalPages, pageNum);
			console.log("Result:", data);
		})
		.catch(error => {
			const bodyTable = document.getElementById("body-table-momo");
			const pagination = document.getElementById("pagination-container-momo");
			bodyTable.innerHTML = "";
			pagination.innerHTML = "<div class='empty-list-Data'>No data available</div>";
			console.error("Error:", error);

		});

}

async function handleFilterPaypal(pageNum) {
	const apiUrl = '/api-get-history';

	const params = getFilterParams("paypal", pageNum);

	// Gọi hàm lấy dữ liệu
	getHistoryPayment(apiUrl, params)
		.then(data => {
			innerTablePaypal("paypal", data.content);
			innerPagePaypal("paypal", data.totalPages, pageNum);
			console.log("Result:", data);
		})
		.catch(error => {
			const bodyTable = document.getElementById("body-table-paypal");
			const pagination = document.getElementById("pagination-container-paypal");
			bodyTable.innerHTML = "";
			pagination.innerHTML = "<div class='empty-list-Data'>No data available</div>"
			console.error("Error:", error);
		});

}

function getFilterParams(nameSystem, pageNum) {
	const name = document.getElementById(`user-name-${nameSystem}`).value;
	const bookingId = document.getElementById(`booking-id-${nameSystem}`).value;
	const paymentStatus = document.getElementById(`payment-status-${nameSystem}`).value;
	const startDate = document.getElementById(`input-start-date-${nameSystem}`).value;
	const endDate = document.getElementById(`input-end-date-${nameSystem}`).value;
	const dateType = document.getElementById(`payment-date-${nameSystem}`).value;
	const amouttype = document.getElementById(`payment-amount-${nameSystem}`).value;
	const params = {
		paymentMethod: nameSystem == "momo" ? 3 : 4,
		paymentStatus: paymentStatus,
		dateType: dateType,  			  // ASC = 0, DESC = 1
		amountType: amouttype,              // ASC = 0, DESC = 1
		pageNum: pageNum
	};
	if (name != null && name.trim() != "") params.name = name;
	if (bookingId != "") params.bookingId = bookingId;
	if (startDate != "") params.startDate = startDate;
	if (endDate != "") params.endDate = endDate
	return params;
}

async function getHistoryPayment(apiUrl, params) {
	try {
		// Tạo query string từ params
		const queryString = new URLSearchParams(params).toString();
		console.log(queryString);
		// Gọi API
		const response = await fetch(`${apiUrl}?${queryString}`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
			},
		});

		// Kiểm tra phản hồi từ API
		if (!response.ok) {
			throw new Error(`Error ${response.status}: ${response.statusText}`);
		}

		// Chuyển đổi kết quả sang JSON
		const data = await response.json();
		return data;
	} catch (error) {
		console.error("Error fetching payment history:", error);
		throw error; // Ném lỗi ra nếu cần xử lý thêm
	}
}

function innerTablePaypal(nameSystem, listPayment) {
	const bodyTable = document.getElementById("body-table-" + nameSystem);
	var bodyHTML = "";
	for (let i = 0; i < listPayment.length; i++) {
		let item = listPayment[i];
		bodyHTML += `<tr>
						<td class="text-center align-middle" th:text="">${nameSystem == "momo" ? item.momoId : item.captureId}</td>
						<td class="text-center align-middle">${item.bookingId}</td>
						<td class="text-center align-middle">${item.fullName}</td>
						<td class="text-center align-middle">${item.paymentDate}</td>
						<td class="text-center align-middle">${nameSystem == "momo" ? item.amount : item.totalPriceDolar}</td>
						<td class="text-center align-middle">${item.paymentStatus}</td>
						<td class="text-center align-middle"><i class="fa-regular fa-eye"></i></td>
					</tr>`
	}
	bodyTable.innerHTML = bodyHTML;
}

function innerPagePaypal(nameSystem, totalPage, currentPage) {
	const nameSystemUpper = nameSystem == "momo" ? "Momo" : "Paypal";
	const pagination = document.getElementById("pagination-container-" + nameSystem);
	var bodyHTML = "";
	if (totalPage > 1) {
		bodyHTML += `<ul class="pagination" id="pagination">`;
		if (currentPage > 0) {
			bodyHTML += `<li><button onclick='handleFilter${nameSystemUpper}(${currentPage - 1})'>
								<i class="fas fa-chevron-left icon"></i></button></li>`;
		}
		for (let i = 0; i < totalPage; i++) {
			bodyHTML += `<li><a onclick='handleFilter${nameSystemUpper}(${i})' class="${currentPage == i ? 'current' : ''}">${i + 1}</a></li>`;
		}
		if (currentPage + 1 < totalPage) {
			bodyHTML += `<li><button onclick='handleFilter${nameSystemUpper}(${currentPage + 1})'>
							<i class="fas fa-chevron-right icon"></i></button></li>`;
		}
		bodyHTML += `</ul>`;
	}
	console.log(bodyHTML);
	pagination.innerHTML = bodyHTML;
}