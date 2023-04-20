$(document).ready(function () {
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/home`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#nav-name').html(data[0].data["fullName"])
            $('#user-avatar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if (data[1].data != null) {
            $('#undo-task').html(data[1].data[1])
            $('#doing-task').html(data[1].data[2])
            $('#done-task').html(data[1].data[3])
            $('#undo-rate').attr('style', 'width: ' + (data[1].data[1] * 100 / data[1].data[0]) + '%')
            $('#doing-rate').attr('style', 'width: ' + (data[1].data[2] * 100 / data[1].data[0]) + '%')
            $('#done-rate').attr('style', 'width: ' + (data[1].data[3] * 100 / data[1].data[0]) + '%')
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