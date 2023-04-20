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
        url: `http://localhost:8080/api/user-edit`,
    }).done(function (data) {
        $('#full-name').val(data[0].data["fullName"])
        $('#user-email').val(data[0].data["email"])
        $('#user-password').val(data[0].data["password"])
        $('#user-image').val(data[0].data["avatar"])
        $('#user-role-id').val(data[0].data["roleModel"]["id"])
    })

    $('#btn-add-update').click(function (e) {
        e.preventDefault()
        const fullName = $('#full-name').val()
        const email = $('#user-email').val()
        const password = $('#user-password').val()
        const role_id = $('#user-role-id').val()
        const avatar = $('#user-image').val()

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/update-user-edit`,
            data: {
                'fullName': fullName,
                'email': email,
                'password': password,
                'roleId': role_id,
                'avatar': avatar
            }
        }).done(function (data) {
            if (data.data === 1) {
                alert(data.message)
            } else if (data.data === -1) {
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