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

    $('#btn-add-user').click(function (e) {
        e.preventDefault()
        const fullName = $('#full-name').val()
        const email = $('#user-email').val()
        const password = $('#user-password').val()
        const role_id = $('#user-role-id').val()
        const avatar = $('#user-image').val()

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/users/add`,
            data: {
                'fullname': fullName,
                'email': email,
                'password': password,
                'role_id': role_id,
                'avatar': avatar
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
            } else {
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