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
    //Khi toàn bộ nội dung được load xong thì chạy dòng này
    //.ten_class #ten_id
    $('.btn-xoa').click(function (e) {
        e.preventDefault()
        //logic code
        //${this} đại diện cho chính element mà mình đang click
        const id = $(this).attr('id')
        const This = $(this)

        //Chức năng chính
        $.ajax({
            method: 'GET',
            url: `http://localhost:8080/api/roles/delete?id=${id}`,
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

    $('.btn-edit').click(function (e) {
        e.preventDefault()
        const id = $(this).attr('id')
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/role-edit`,
            data: {
                'id': id
            }
        }).done(function (data) {
            window.location.href = "/role-edit"
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