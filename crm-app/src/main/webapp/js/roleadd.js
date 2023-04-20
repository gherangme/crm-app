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

    $('#btn-add').click(function (e) {
        e.preventDefault()
        const name = $('#name').val()
        const desc = $('#desc').val()

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/roles/add`,
            data: {
                'name': name,
                'desc': desc
            }
        }).done(function (data) {
            if (data.data === 1) {
                alert(data.message)
            } else if (data.data === -1) {
                alert(data.message)
            } else if (data.data === -2) {
                alert(data.message)
            } else if (data.data === -3) {
                alert(data.message)
            }
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