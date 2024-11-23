const rangeMin = document.getElementById('range-min');
const rangeMax = document.getElementById('range-max');
const minPriceInput = document.getElementById('min-price');
const maxPriceInput = document.getElementById('max-price');
const sliderTrack = document.querySelector('.slider-track');
const minGap = 500000;

function formatCurrency(value) {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(value);
}

function updatePrices() {
    let minVal = parseInt(rangeMin.value);
    let maxVal = parseInt(rangeMax.value);

    if (maxVal - minVal < minGap) {
        if (this === rangeMin) {
            rangeMin.value = maxVal - minGap;
        } else {
            rangeMax.value = minVal + minGap;
        }
    }

    minPriceInput.value = formatCurrency(rangeMin.value);
    maxPriceInput.value = formatCurrency(rangeMax.value);
    updateTrack();
}

function updateTrack() {
    const percent1 = (rangeMin.value - rangeMin.min) / (rangeMin.max - rangeMin.min) * 100;
    const percent2 = (rangeMax.value - rangeMax.min) / (rangeMax.max - rangeMax.min) * 100;
    sliderTrack.style.left = percent1 + '%';
    sliderTrack.style.right = (100 - percent2) + '%';
}

rangeMin.addEventListener('input', updatePrices);
rangeMax.addEventListener('input', updatePrices);

updatePrices();

function showRangeBox(isVisible)
{
	if(isVisible)
	{
		document.querySelector(".price-range-container").classList.remove("in-active");
	}
	else document.querySelector(".price-range-container").classList.add("in-active");
}
