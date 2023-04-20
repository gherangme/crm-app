$(document).ready(function () {

    let id = 0;

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
        url: `http://localhost:8080/api/role-edit`,
    }).done(function (data) {
        console.log(data.data)
        id = data.data["id"]
        $('#name').val(data.data["roleName"])
        $('#desc').val(data.data["description"])
    })

    $('#btn-update').click(function (e) {
        e.preventDefault()
        const name = $('#name').val()
        const description = $('#desc').val()
        console.log(id + name + description)

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/update-role-edit`,
            data: {
                'id': id,
                'name': name,
                'description': description
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