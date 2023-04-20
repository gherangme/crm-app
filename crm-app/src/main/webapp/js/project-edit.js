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
        url: `http://localhost:8080/api/project-edit`,
    }).done(function (data) {
        id = data.data["id"]
        $('#name-project').val(data.data["name"])
        $('#start-date').val(data.data["startDate"])
        $('#end-date').val(data.data["endDate"])
    })

    $('#btn-edit-project').click(function (e) {
        e.preventDefault()
        const name = $('#name-project').val()
        const startDate = $('#start-date').val()
        const endDate = $('#end-date').val()

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/update-project-edit`,
            data: {
                'id': id,
                'name': name,
                'startDate': startDate,
                'endDate': endDate
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