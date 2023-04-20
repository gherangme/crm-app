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
        url: `http://localhost:8080/api/users`,
    }).done(function (data) {
        console.log(data[0].data)
        for (const i in data[0].data) {
            const getId = data[0].data[i]["id"],
                getFullName = data[0].data[i]["fullName"],
                getEmail = data[0].data[i]["email"],
                getRoleName = data[0].data[i]["roleModel"]["roleName"]
            let stt = Number(Number(i) + 1)
            const html = `<tr>
                                                <td>${stt}</td>
                                                <td>${getFullName}</td>
                                                <td>${getEmail}</td>
                                                <td>${getRoleName}</td>
                                                <td>${getId}</td>
                                                <td>
                                                    <a href="#" class="btn btn-sm btn-primary check-edit-user"
                                                       id=${getEmail}>Sửa</a>
                                                    <a href="#" class="btn btn-sm btn-danger btn-xoa-user"
                                                       id=${getId}>Xóa</a>
                                                    <a href="http://localhost:8080/user-details"
                                                       class="btn btn-sm btn-info btn-view"
                                                       id=${getEmail}>Xem</a>
                                                </td>
                                            </tr>`
            $('#user-list').append(html)
        }

        $('.btn-xoa-user').click(function (e) {
            e.preventDefault()
            const id = $(this).attr('id')
            const This = $(this)
            $.ajax({
                method: 'POST',
                url: `http://localhost:8080/api/users/delete`,
                data: {
                    'id': id
                }
            }).done(function (data) {
                if (data.data) {
                    alert(data.message)
                    This.closest('tr').remove()
                } else {
                    alert(data.message)
                }
            })
        })

        $('.check-edit-user').click(function (e) {
            e.preventDefault()
            const id = $(this).attr('id')

            $.ajax({
                method: 'GET',
                url: `http://localhost:8080/api/home`,
            }).done(function (data) {
                if (data[0].data["roleModel"]["id"] == 1) {
                    console.log(id)
                    $.ajax({
                        method: 'POST',
                        url: `http://localhost:8080/api/user-edit`,
                        data: {
                            'email': id
                        }
                    })

                    window.location.href = "/user-edit"
                } else {
                    alert('Bạn không có quyền này !')
                }
            })
        })
    })

    $('#check-add-user').click(function (e) {
        e.preventDefault()
        $.ajax({
            method: 'GET',
            url: `http://localhost:8080/api/home`,
        }).done(function (data) {
            if (data[0].data["roleModel"]["id"] == 1) {
                window.location.href = "/user-add"
            } else {
                alert('Bạn không có quyền này !')
            }
        })
    })

    $('#user-list').on('click', '.btn-view', function () {
        $.ajax({
            method: 'POST',
            url: `http://localhost:8080/api/user-detail`,
            data: {
                'function': 'viewUser',
                'emailUserTable': $(this).attr('id')
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