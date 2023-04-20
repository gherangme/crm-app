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

    $('#btn-add-project').click(function (e) {
        e.preventDefault()
        const name = $('#name-project').val()
//                const startDate = new Date()
//                const endDate = new Date()
        const startDate = $('#start-date').val()
        const endDate = $('#end-date').val()

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/projects/add`,
            data: {
                //Tên trong '' giống tên id(hoặc class) trong '' ở dòng 134 135
                'name-project': name,
                'start-date': startDate,
                'end-date': endDate
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