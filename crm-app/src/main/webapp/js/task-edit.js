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
        url: `http://localhost:8080/api/task-edit`
    }).done(function (data) {
        console.log(data.data)

        id = data.data[0]["id"]

        for (const i in data.data[1]) {
            const html = `<option value="${data.data[1][i]["id"]}">${data.data[1][i]["name"]}</option>`
            $('#edit-task-projectname').append(html)
        }
        document.getElementById('edit-task-projectname').value = data.data[0]["projectModel"]["id"]

        for (const i in data.data[2]) {
            const html = `<option value="${data.data[2][i]["id"]}">${data.data[2][i]["fullName"]} #${data.data[2][i]["id"]}</option>`
            $('#edit-task-userid').append(html)
        }
        document.getElementById('edit-task-userid').value = data.data[0]["userModel"]["id"]

        $('#edit-task-nametask').val(data.data[0]["name"])
        $('#edit-task-startdate').val(data.data[0]["startDate"])
        $('#edit-task-enddate').val(data.data[0]["endDate"])

    })

    $('#btn-edit-task').click(function (e) {
        e.preventDefault()
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/update-task-edit`,
            data: {
                'id': id,
                'idProject': $('#edit-task-projectname').val(),
                'idUser': $('#edit-task-userid').val(),
                'nameTask': $('#edit-task-nametask').val(),
                'startDate': $('#edit-task-startdate').val(),
                'endDate': $('#edit-task-enddate').val()
            }
        }).done(function (data) {
            if (data.data === 1) {
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