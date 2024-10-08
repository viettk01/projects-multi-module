let paymentMethod;
let paymentUrl;

function stageFour() {
    $.ajax({
        url: '/booking/get/stage-four',
        type: 'GET',
        success: function (htmlResponse) {
            divStage.innerHTML = htmlResponse;
            updateTicketBill();
            nextBtn.classList.add("disabled");
        },
        error: function (xhr, status, error) {
            console.log(error);
        }
    });
}

function usePoints() {
    points = document.getElementById('points').value;
    if (points === '') {
        toastShow("Vui lòng nhập số điểm")
        return false;
    } else if (points < 20 || points > 100) {
        toastShowFail("Điểm số sử dụng phải đạt từ 20 đến 100");
    } else {
        $.ajax({
            url: '/api/v1/promotion/check/' + points,
            type: 'GET',
            success: function (htmlResponse) {
                if (htmlResponse === true) {
                    ticketTotalPriceShow();
                    updateTicketBill();
                } else {
                    toastShowFail("Bạn không có đủ điểm để sử dụng")
                }
            },
            error: function (xhr, status, error) {
                console.error(`Đã xảy ra lỗi: ${error}`);
            }
        });
    }
}

function getSelectedPayment() {
    const selectedInput = document.querySelector('input[name="payment"]:checked');
    if (selectedInput) {
        nextBtn.classList.remove("disabled");
        paymentMethod = selectedInput.value;
    } else {
        nextBtn.classList.add("disabled");
    }
}

function updateTicketBill() {
    const updateElement = (id, html) => document.getElementById(id).innerHTML = html;

    updateElement('bill-movie-name', `
        <p class="fs-16px fw-600 text-blue">${showtime.movieName}</p>
        <p class="fs-14px">${showtime.auditoriumType} 
        <span> - </span> 
        <span class="fs-14px px-2 py-1 fw-700 bg-orange text-white" style="border-radius: 5px">${showtime.ageRequirement}</span>
        </p>
    `);

    updateElement('bill-cinema-name', `
        <p class="fs-16px fw-600 text-blue">${showtime.cinemaName}</p>
        <p class="fw-600 fs-14px">${showtime.startTime}, ${showtime.screeningDate}</p>
    `);

    const formatSeatList = () => Array.from(currentSeatsChose)
        .map(seatId => seatsData.find(e => e.id === seatId))
        .filter(Boolean)
        .map(seat => seat.seatRow + seat.seatColumn)
        .join(', ');

    const formatComboList = () => Array.from(comboData)
        .map(([comboId, quantity]) => {
            if (quantity < 1) return '';
            const combo = combosName.get(comboId);
            return combo ? `<span class="fs-14px"><strong>${quantity} <span> x </span></strong> ${combo}</span>` : '';
        })
        .filter(Boolean)
        .join(' ');

    updateElement('bill-auditorium', `
        <p class="fs-16px">${showtime.auditoriumName}: <strong class="fs-14px ms-2">${formatSeatList()}</strong></p>
        ${formatComboList()}
    `);

    if (points > 0) {
        const oldPrice = document.createElement('span');
        oldPrice.classList.add('text-decoration-line-through', 'ms-1', 'fs-12px');
        oldPrice.innerHTML = `${formatPrice((totalTicketPrice + totalComboPrice))}đ`
        updateElement('bill-total-price', `${formatPrice((totalTicketPrice + totalComboPrice) - (points * 1000))}<span>đ</span>`);
        document.getElementById('bill-total-price').appendChild(oldPrice);
    } else {
        updateElement('bill-total-price', `${formatPrice(totalTicketPrice + totalComboPrice)}<span>đ</span>`);

    }

    billSubmit(
        billAccept = document.getElementById("submit-check"),
        billSubmitBtn = document.getElementById("btn-payment-submit")
    )
}

function billSubmit(
    billAccept,
    billSubmitBtn
) {
    billAccept.addEventListener('click', function () {
        billAccept.checked = true;
    })
    billSubmitBtn.addEventListener('click', function () {
        if (billAccept.checked) {
            if (paymentUrl == null || paymentUrl === "") {
                createBill()
            } else {
                window.location.href = paymentUrl;
            }
        } else {
            alert('Vui lòng xác nhận thông tin đặt vé');
        }
    })
}

async function createBill() {
    const comboRequest = Array.from(comboData, ([key, value]) => ({
        comboId: key,
        quantity: value
    }));

    const seatRequest = Array.from(currentSeatsChose);

    const paymentRequest = {
        showtimeId: showtime.id,
        comboRequest: comboRequest,
        seatRequest: seatRequest,
        points : points,
        paymentMethod: paymentMethod
    };

    try {
        let res = await axios.post(`/api/v1/bills/create`, paymentRequest);
        paymentUrl = res.data;
        console.log(paymentUrl)
        if (paymentUrl != null) {
            window.location.href = paymentUrl;
        }
    } catch (e) {
        console.error(e);
    }
}