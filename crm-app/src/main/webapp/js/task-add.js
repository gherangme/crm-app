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

    $('#btn-add-task').click(function (e) {
        e.preventDefault()
        const projectName = $('#addtask-projectname').val()
        const nameTask = $('#addtask-nametask').val()
        const user_id = $('#addtask-userid').val()
        const start_date = $('#addtask-startdate').val()
        const end_date = $('#addtask-enddate').val()

        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/tasks/add`,
            data: {
                'addtask-projectname': projectName,
                'addtask-nametask': nameTask,
                'addtask-userid': user_id,
                'addtask-startdate': start_date,
                'addtask-enddate': end_date
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