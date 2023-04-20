$(document).ready(function () {
    let taskId = 0
    $.ajax({
        method: 'GET',
        url: `http://localhost:8080/api/profile-task-update`,
    }).done(function (data) {
        if (data[0].data != null) {
            $('#nav-name').html(data[0].data["fullName"])
            $('#user-avatar').attr('src', 'plugins/images/users/' + data[0].data["avatar"])
        }
        if (data[1].data != null) {
            taskId = data[1].data["id"]
            console.log(data[1].data)
            $('#project-name').attr('value', data[1].data["projectModel"]["name"])
            $('#task-name').attr('value', data[1].data["name"])
            $('#start-date').attr('value', data[1].data["startDate"])
            $('#end-date').attr('value', data[1].data["endDate"])
            if (data[2].data != null) {
                for (const i in data[2].data) {
                    const html = `<option value="${data[2].data[i]["id"]}">${data[2].data[i]["name"]}</option>`
                    $('#select-task').append(html)
                }
                // console.log(data[1].data["id"])
                document.getElementById('select-task').value = data[1].data["statusModel"]["id"]
            }
        }
    })

    $('#btn-save-status').click(function (e) {
        e.preventDefault()
        console.log(taskId + " " + $('#select-task').val())
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/profile-task-update`,
            data: {
                'function': 'saveStatus',
                'taskId': taskId,
                'statusId': $('#select-task').val()
            }
        }).done(function (data) {
            console.log(data.data)
            if (data.data > 0) {
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