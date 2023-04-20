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

    $('.btn-delete-project').click(function (e) {
        e.preventDefault()
        const id = $(this).attr('id')
        const This = $(this)
        $.ajax({
            method: 'GET',
            url: `http://localhost:8080/api/projects/delete?id=${id}`,
            //            data:
        }).done(function (data) {
            if (data.data) {
                alert(data.message)
                This.closest('tr').remove()
            } else {
                alert(data.message)
            }
        })
    })

    $('.project-detail').click(function (e) {
        e.preventDefault()
        const id = $(this).attr('id')
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project-detail`,
            data: {
                'id': id
            }
        }).done(function (data) {
            window.location.href = "/project-detail"
        })
    })

    $('.btn-edit').click(function (e) {
        e.preventDefault()
        const id = $(this).attr('id')
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/project-edit`,
            data: {
                'id': id
            }
        }).done(function (data) {
            window.location.href = "/project-edit"
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