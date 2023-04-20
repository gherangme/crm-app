$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/home`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#nav-name').html(data[0].data["fullName"])
            $('#user-avatar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
    })

    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/user-detail`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#member-name').html(data[0].data["fullName"])
            $('#nav-email').html(data[0].data["email"])
            $('#user-avatar-bar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if (data[1].data != null && data[1].data[0] != 0) {
            $('#undo-task').html(Math.floor(data[1].data[1] * 100 / data[1].data[0]) + '%')
            $('#doing-task').html(Math.floor(data[1].data[2] * 100 / data[1].data[0]) + '%')
            $('#done-task').html(Math.floor(data[1].data[3] * 100 / data[1].data[0]) + '%')

            $('#undo-rate').attr('style', 'width: ' + (data[1].data[1] * 100 / data[1].data[0]) + '%')
            $('#doing-rate').attr('style', 'width: ' + (data[1].data[2] * 100 / data[1].data[0]) + '%')
            $('#done-rate').attr('style', 'width: ' + (data[1].data[3] * 100 / data[1].data[0]) + '%')
        }
        if (data[2].data != null) {
            for (const i in data[2].data) {
                console.log(data[2].data)
                const html = `<a href="#">
                                    <div class="mail-contnet">
                                        <h4 style="font-weight: 500">${data[2].data[i]["projectModel"]["name"]}</h4>
                                        <h5>${data[2].data[i]["name"]}</h5>
                                        <span class="time">Bắt đầu: ${data[2].data[i]["startDate"]}</span>
                                        <span class="time">Kết thúc: ${data[2].data[i]["endDate"]}</span>
                                    </div>
                                </a>`
                if (data[2].data[i]["statusModel"]["id"] == 1) {
                    $('#list-undone').append(html)
                } else if (data[2].data[i]["statusModel"]["id"] == 2) {
                    $('#list-doing').append(html)
                } else if (data[2].data[i]["statusModel"]["id"] == 3) {
                    $('#list-done').append(html)
                }
            }
        }
    })

    $('#logout').click(function () {
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/home-logout`,
            data: {
                'logout': true
            }
        }).done(function (data) {

        })
    })

})