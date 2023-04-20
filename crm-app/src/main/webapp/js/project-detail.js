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
        url: `http://localhost:8080/api/project-detail`,
    }).done(function (data) {
        if (data.data[0].data != null) {
            $('#undo-task').html(Math.floor(data.data[0].data[1] * 100 / data.data[0].data[0]) + '%')
            $('#doing-task').html(Math.floor(data.data[0].data[2] * 100 / data.data[0].data[0]) + '%')
            $('#done-task').html(Math.floor(data.data[0].data[3] * 100 / data.data[0].data[0]) + '%')

            $('#undo-rate').attr('style', 'width: ' + (data.data[0].data[1] * 100 / data.data[0].data[0]) + '%')
            $('#doing-rate').attr('style', 'width: ' + (data.data[0].data[2] * 100 / data.data[0].data[0]) + '%')
            $('#done-rate').attr('style', 'width: ' + (data.data[0].data[3] * 100 / data.data[0].data[0]) + '%')
        }

        if (data.data[1].data != null) {
            $('#project-name').html(data.data[1].data["name"])
        }

        console.log(data.data[2].data[0]["userModel"]["id"])
        let temp = data.data[2].data[0]["userModel"]["id"]
        let count = 1, count1 = 0, nameUser = [], avatarUser = [], nameTask = [], startDate = [], endDate = [], j = 0;
        nameUser[0] = data.data[2].data[0]["userModel"]["fullName"]
        avatarUser[0] = data.data[2].data[0]["userModel"]["avatar"]
        console.log(data.data[2].data)
        for (const i in data.data[2].data) {
            nameTask[i] = data.data[2].data[i]["name"]
            startDate[i] = data.data[2].data[i]["startDate"]
            endDate[i] = data.data[2].data[i]["endDate"]
            j++;
            if (data.data[2].data[i]["userModel"]["id"] > temp) {
                temp = data.data[2].data[i]["userModel"]["id"]
                if (data.data[2].data[i]["userModel"]["fullName"] != nameUser[count1]) {
                    nameUser[count1 + 1] = data.data[2].data[i]["userModel"]["fullName"]
                    avatarUser[count1 + 1] = data.data[2].data[i]["userModel"]["avatar"]
                    count1++
                }
                count++;
            }
        }
        console.log(nameUser)
        console.log(avatarUser)
        console.log(nameTask)
        console.log(startDate)
        console.log(endDate)

        const This = $('#list-project-user')
        for (let i = 0; i < count; i++) {
            const html = `
                    <div class="row">
                    <div class="col-xs-12">
                        <a href="#" class="group-title">
                            <img width="30" src="plugins/images/users/${avatarUser[i]}" class="img-circle" />
                            <span>${nameUser[i]}</span>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <div class="white-box">
                            <h3 class="box-title">Chưa thực hiện</h3>
                            <div class="message-center" id="undo-detail${i}">
                                <!--
                                    <div class="mail-contnet">
                                        <h5>Pavan kumar</h5> <span class="mail-desc">Just see the my admin!</span> <span
                                            class="time">9:30 AM</span>
                                    </div>
                                    -->
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="white-box">
                            <h3 class="box-title">Đang thực hiện</h3>
                            <div class="message-center" id="doing-detail${i}">
                            <!--
                                    <div class="mail-contnet">
                                        <h5>Pavan kumar</h5> <span class="mail-desc">Just see the my admin!</span> <span
                                            class="time">9:30 AM</span>
                                    </div>
                                    -->
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="white-box">
                            <h3 class="box-title">Đã hoàn thành</h3>
                            <div class="message-center" id="done-detail${i}">
                            <!--
                                    <div class="mail-contnet">
                                        <h5>Pavan kumar</h5> <span class="mail-desc">Just see the my admin!</span> <span
                                            class="time">9:30 AM</span>
                                    </div>
                                    -->
                            </div>
                        </div>
                    </div>
                </div>
                `
            This.append(html)

            for (let k = i; k < nameTask.length; k++) {
                // if (nameUser[k] !== "" || nameUser[k] !== null) {
                if (nameUser[i] === data.data[2].data[k]["userModel"]["fullName"]
                    && data.data[2].data[k]["statusModel"]["id"] === 1) {
                    console.log(data.data[2].data[k]["userModel"]["fullName"])
                    const html1 = `<div class="mail-contnet">
                                        <h5 style="margin-top: 10px">${nameTask[k]}</h5> <span class="mail-desc">Bắt đầu: ${startDate[k]}</span> <span
                                            class="mail-desc">Kết thúc: ${endDate[k]}</span>
                                    </div>`
                    This.find("div[id='undo-detail" + i + "']").append(html1)
                }
                if (nameUser[i] === data.data[2].data[k]["userModel"]["fullName"]
                    && data.data[2].data[k]["statusModel"]["id"] === 2) {
                    console.log(data.data[2].data[k]["userModel"]["fullName"])
                    const html1 = `<div class="mail-contnet">
                                        <h5 style="margin-top: 10px">${nameTask[k]}</h5> <span class="mail-desc">Bắt đầu: ${startDate[k]}</span> <span
                                            class="mail-desc">Kết thúc: ${endDate[k]}</span>
                                    </div>`
                    // $('#doing-detail').append(html1)
                    This.find("div[id='doing-detail" + i + "']").append(html1)
                }
                if (nameUser[i] === data.data[2].data[k]["userModel"]["fullName"]
                    && data.data[2].data[k]["statusModel"]["id"] === 3) {
                    console.log(data.data[2].data[k]["userModel"]["fullName"])
                    const html1 = `<div class="mail-contnet" style="margin-top: 10px">
                                        <h5 style="margin-top: 10px">${nameTask[k]}</h5> <span class="mail-desc">Bắt đầu: ${startDate[k]}</span> <span
                                            class="mail-desc">Kết thúc: ${endDate[k]}</span>
                                    </div>`
                    // $('#done-detail').append(html1)
                    This.find("div[id='done-detail" + i + "']").append(html1)
                }
                // }
            }
        }
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