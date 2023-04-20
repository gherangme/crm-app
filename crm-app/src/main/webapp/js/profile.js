$(document).ready(function () {

    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/home`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('.nav-name').html(data[0].data["fullName"])
            $('#nav-email').html(data[0].data["email"])
            $('#user-avatar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
            $('#user-avatar-detail').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }

        if (data[1].data[0] != 0) {
            $('#undo-task').html(Math.floor(data[1].data[1] * 100 / data[1].data[0]) + '%')
            $('#doing-task').html(Math.floor(data[1].data[2] * 100 / data[1].data[0]) + '%')
            $('#done-task').html(Math.floor(data[1].data[3] * 100 / data[1].data[0]) + '%')

            $('#undo-rate').attr('style', 'width: ' + (data[1].data[1] * 100 / data[1].data[0]) + '%')
            $('#doing-rate').attr('style', 'width: ' + (data[1].data[2] * 100 / data[1].data[0]) + '%')
            $('#done-rate').attr('style', 'width: ' + (data[1].data[3] * 100 / data[1].data[0]) + '%')
        }
    })

    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/profile`,
    }).done(function (data) {
        console.log(data[0].data[0])
        for (const i in data[0].data) {
            const getId = data[0].data[i]["id"],
                getNameTask = data[0].data[i]["name"],
                getNameProject = data[0].data[i]["projectModel"]["name"],
                getStartDate = data[0].data[i]["startDate"],
                getEndDate = data[0].data[i]["endDate"],
                getStatus = data[0].data[i]["statusModel"]["name"]
            let stt = Number(Number(i) + 1)
            const html = `<tr>
                                                <td>${stt}</td>
                                                <td>${getNameTask}</td>
                                                <td>${getNameProject}</td>
                                                <td>${getStartDate}</td>
                                                <td>${getEndDate}</td>
                                                <td>${getStatus}</td>
                                                <td>
                                                    <a href="http://localhost:8080/profile-edit"
                                                       class="btn btn-sm btn-primary btn-update"
                                                    id=${getId}>Cập nhật</a>
                                                </td>
                                            </tr>`
            $('#task-list').append(html)
        }
    })

    $('#task-list').on('click', '.btn-update', function () {
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/profile`,
            data: {
                'function': 'goToTaskUpdate',
                'taskID': $(this).attr('id')
            }
        }).done(function (data) {
            // if(data.data != null){
            //     window.location.href = data.data;
            // }
        })

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